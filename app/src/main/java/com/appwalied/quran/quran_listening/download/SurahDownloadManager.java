package com.appwalied.quran.quran_listening.download;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.appwalied.quran.quran.room.AppDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SurahDownloadManager {

    private static final String TAG = "SurahDownloadManager";
    private static volatile SurahDownloadManager instance;

    private final Context context;
    private final AppDatabase db;
    private final OkHttpClient httpClient;
    private final ExecutorService executorService;
    private final Handler mainHandler;

    private final Map<String, DownloadTask> activeTasks = new ConcurrentHashMap<>();
    private final List<SurahDownloadListener> listeners = Collections.synchronizedList(new ArrayList<>());

    public interface SurahDownloadListener {
        void onDownloadStatusChanged(DownloadedSurahEntity surah);
        void onDownloadProgress(DownloadedSurahEntity surah, int percent, long downloadedBytes, long totalBytes);
        void onDownloadCompleted(DownloadedSurahEntity surah);
        void onDownloadFailed(DownloadedSurahEntity surah, String errorMessage);
        void onDownloadCancelled(DownloadedSurahEntity surah);
    }

    private static class DownloadTask {
        final DownloadedSurahEntity entity;
        volatile boolean isPaused = false;
        volatile boolean isCancelled = false;

        DownloadTask(DownloadedSurahEntity entity) {
            this.entity = entity;
        }
    }

    private SurahDownloadManager(Context context) {
        this.context = context.getApplicationContext();
        this.db = AppDatabase.getInstance(this.context);
        this.httpClient = new OkHttpClient.Builder().build();
        this.executorService = Executors.newFixedThreadPool(2);
        this.mainHandler = new Handler(Looper.getMainLooper());

        reconcileDatabaseWithDisk();
    }

    public static SurahDownloadManager getInstance(Context context) {
        if (instance == null) {
            synchronized (SurahDownloadManager.class) {
                if (instance == null) {
                    instance = new SurahDownloadManager(context);
                }
            }
        }
        return instance;
    }

    public void addListener(SurahDownloadListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(SurahDownloadListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    private void reconcileDatabaseWithDisk() {
        executorService.execute(() -> {
            try {
                List<DownloadedSurahEntity> list = db.downloadedSurahDao().getAllDownloadedSurahs();
                for (DownloadedSurahEntity entity : list) {
                    File localFile = new File(entity.getLocalFilePath());
                    if (entity.getDownloadStatus() == DownloadStatus.COMPLETED.getValue()) {
                        if (!localFile.exists() || localFile.length() < 4096) {
                            entity.setStatusEnum(DownloadStatus.NOT_DOWNLOADED);
                            entity.setDownloadedBytes(0);
                            db.downloadedSurahDao().insertOrUpdate(entity);
                        }
                    } else if (entity.getDownloadStatus() == DownloadStatus.DOWNLOADING.getValue()
                            || entity.getDownloadStatus() == DownloadStatus.QUEUED.getValue()
                            || entity.getDownloadStatus() == DownloadStatus.VERIFYING.getValue()) {
                        entity.setStatusEnum(DownloadStatus.PAUSED);
                        db.downloadedSurahDao().insertOrUpdate(entity);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error reconciling DB", e);
            }
        });
    }

    public File getAudioDirectory(int reciterId) {
        File baseDir = context.getExternalFilesDir("quran_audio");
        if (baseDir == null) {
            baseDir = new File(context.getFilesDir(), "quran_audio");
        }
        File reciterDir = new File(baseDir, "reciter_" + reciterId);
        if (!reciterDir.exists()) {
            reciterDir.mkdirs();
        }
        return reciterDir;
    }

    public File getLocalFile(int reciterId, int surahId) {
        File dir = getAudioDirectory(reciterId);
        return new File(dir, String.format(Locale.US, "%03d.mp3", surahId));
    }

    public File getTempFile(int reciterId, int surahId) {
        File dir = getAudioDirectory(reciterId);
        return new File(dir, String.format(Locale.US, "%03d.tmp", surahId));
    }

    public boolean isSurahDownloaded(int reciterId, int surahId) {
        File file = getLocalFile(reciterId, surahId);
        return file.exists() && file.length() >= 4096;
    }

    public void startDownload(int reciterId, String reciterName, int surahId, String surahName, String serverUrl) {
        String downloadId = reciterId + "_" + surahId;

        if (activeTasks.containsKey(downloadId)) {
            DownloadTask task = activeTasks.get(downloadId);
            if (task != null && task.isPaused) {
                resumeDownload(reciterId, surahId);
            }
            return;
        }

        String remoteUrl = (serverUrl.endsWith("/") ? serverUrl : serverUrl + "/") + String.format(Locale.US, "%03d.mp3", surahId);
        File destFile = getLocalFile(reciterId, surahId);

        if (destFile.exists() && destFile.length() >= 4096) {
            DownloadedSurahEntity entity = new DownloadedSurahEntity(reciterId, reciterName, surahId, surahName, remoteUrl, destFile.getAbsolutePath());
            entity.setFileSize(destFile.length());
            entity.setDownloadedBytes(destFile.length());
            entity.setStatusEnum(DownloadStatus.COMPLETED);
            executorService.execute(() -> db.downloadedSurahDao().insertOrUpdate(entity));
            notifyStatusChanged(entity);
            return;
        }

        DownloadedSurahEntity entity = new DownloadedSurahEntity(reciterId, reciterName, surahId, surahName, remoteUrl, destFile.getAbsolutePath());
        entity.setStatusEnum(DownloadStatus.QUEUED);

        DownloadTask task = new DownloadTask(entity);
        activeTasks.put(downloadId, task);

        executorService.execute(() -> {
            db.downloadedSurahDao().insertOrUpdate(entity);
            notifyStatusChanged(entity);
            executeDownload(task);
        });
    }

    public void pauseDownload(int reciterId, int surahId) {
        String downloadId = reciterId + "_" + surahId;
        DownloadTask task = activeTasks.get(downloadId);
        if (task != null) {
            task.isPaused = true;
            task.entity.setStatusEnum(DownloadStatus.PAUSED);
            executorService.execute(() -> db.downloadedSurahDao().insertOrUpdate(task.entity));
            notifyStatusChanged(task.entity);
        }
    }

    public void resumeDownload(int reciterId, int surahId) {
        String downloadId = reciterId + "_" + surahId;
        DownloadTask task = activeTasks.get(downloadId);
        if (task != null) {
            task.isPaused = false;
            task.entity.setStatusEnum(DownloadStatus.DOWNLOADING);
            executorService.execute(() -> db.downloadedSurahDao().insertOrUpdate(task.entity));
            notifyStatusChanged(task.entity);
        } else {
            executorService.execute(() -> {
                DownloadedSurahEntity entity = db.downloadedSurahDao().getDownloadedSurah(reciterId, surahId);
                if (entity != null) {
                    startDownload(reciterId, entity.getReciterName(), surahId, entity.getSurahName(), entity.getRemoteUrl());
                }
            });
        }
    }

    public void cancelDownload(int reciterId, int surahId) {
        String downloadId = reciterId + "_" + surahId;
        DownloadTask task = activeTasks.remove(downloadId);
        if (task != null) {
            task.isCancelled = true;
            task.entity.setStatusEnum(DownloadStatus.CANCELLED);
        }

        executorService.execute(() -> {
            File tmpFile = getTempFile(reciterId, surahId);
            if (tmpFile.exists()) {
                tmpFile.delete();
            }
            db.downloadedSurahDao().deleteByDownloadId(downloadId);

            DownloadedSurahEntity entity = new DownloadedSurahEntity(reciterId, "", surahId, "", "", "");
            entity.setDownloadId(downloadId);
            entity.setStatusEnum(DownloadStatus.CANCELLED);
            notifyCancelled(entity);
        });
    }

    public void deleteDownloadedSurah(int reciterId, int surahId, Runnable onComplete) {
        String downloadId = reciterId + "_" + surahId;
        cancelDownload(reciterId, surahId);

        executorService.execute(() -> {
            File file = getLocalFile(reciterId, surahId);
            if (file.exists()) {
                file.delete();
            }
            File tmp = getTempFile(reciterId, surahId);
            if (tmp.exists()) {
                tmp.delete();
            }
            db.downloadedSurahDao().deleteByDownloadId(downloadId);

            DownloadedSurahEntity entity = new DownloadedSurahEntity(reciterId, "", surahId, "", "", "");
            entity.setDownloadId(downloadId);
            entity.setStatusEnum(DownloadStatus.NOT_DOWNLOADED);

            mainHandler.post(() -> {
                notifyStatusChanged(entity);
                if (onComplete != null) {
                    onComplete.run();
                }
            });
        });
    }

    public void deleteReciterSurahs(int reciterId, Runnable onComplete) {
        executorService.execute(() -> {
            File reciterDir = getAudioDirectory(reciterId);
            if (reciterDir.exists()) {
                File[] files = reciterDir.listFiles();
                if (files != null) {
                    for (File f : files) {
                        f.delete();
                    }
                }
                reciterDir.delete();
            }
            db.downloadedSurahDao().deleteByReciterId(reciterId);

            mainHandler.post(() -> {
                if (onComplete != null) {
                    onComplete.run();
                }
            });
        });
    }

    private void executeDownload(DownloadTask task) {
        DownloadedSurahEntity entity = task.entity;
        int reciterId = entity.getReciterId();
        int surahId = entity.getSurahId();
        String downloadId = entity.getDownloadId();

        if (!isNetworkAvailable()) {
            entity.setStatusEnum(DownloadStatus.FAILED);
            entity.setErrorMessage("لا يوجد اتصال بالإنترنت");
            db.downloadedSurahDao().insertOrUpdate(entity);
            activeTasks.remove(downloadId);
            notifyFailed(entity, "لا يوجد اتصال بالإنترنت");
            return;
        }

        File tempFile = getTempFile(reciterId, surahId);
        long existingBytes = tempFile.exists() ? tempFile.length() : 0;

        entity.setStatusEnum(DownloadStatus.DOWNLOADING);
        entity.setDownloadedBytes(existingBytes);
        db.downloadedSurahDao().insertOrUpdate(entity);
        notifyStatusChanged(entity);

        try {
            Request.Builder requestBuilder = new Request.Builder().url(entity.getRemoteUrl());
            if (existingBytes > 0) {
                requestBuilder.header("Range", "bytes=" + existingBytes + "-");
            }

            Request request = requestBuilder.build();
            Response response = httpClient.newCall(request).execute();

            if (!response.isSuccessful()) {
                if (response.code() == 416) { // Range not satisfiable
                    tempFile.delete();
                    existingBytes = 0;
                    response.close();
                    request = new Request.Builder().url(entity.getRemoteUrl()).build();
                    response = httpClient.newCall(request).execute();
                }

                if (!response.isSuccessful()) {
                    String err = "خطأ في السيرفر (" + response.code() + ")";
                    response.close();
                    handleDownloadFailure(task, err);
                    return;
                }
            }

            ResponseBody body = response.body();
            if (body == null) {
                response.close();
                handleDownloadFailure(task, "استجابة السيرفر فارغة");
                return;
            }

            long contentLength = body.contentLength();
            long totalBytes = (response.code() == 206) ? (existingBytes + contentLength) : contentLength;

            if (totalBytes > 0) {
                entity.setFileSize(totalBytes);
            }

            // Check Available Free Storage
            File storageDir = getAudioDirectory(reciterId);
            long freeSpace = storageDir.getFreeSpace();
            if (totalBytes > 0 && freeSpace < (totalBytes - existingBytes + 10485760)) { // 10MB safety buffer
                body.close();
                response.close();
                handleDownloadFailure(task, "مساحة التخزين غير كافية على الجهاز");
                return;
            }

            boolean append = (response.code() == 206 && existingBytes > 0);
            if (!append && existingBytes > 0) {
                existingBytes = 0;
            }

            InputStream inputStream = body.byteStream();
            RandomAccessFile outputStream = new RandomAccessFile(tempFile, "rw");
            if (append) {
                outputStream.seek(existingBytes);
            } else {
                outputStream.setLength(0);
            }

            byte[] buffer = new byte[8192];
            long currentDownloaded = existingBytes;
            int bytesRead;
            long lastProgressTime = System.currentTimeMillis();

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                if (task.isCancelled) {
                    outputStream.close();
                    inputStream.close();
                    response.close();
                    tempFile.delete();
                    activeTasks.remove(downloadId);
                    notifyCancelled(entity);
                    return;
                }

                if (task.isPaused) {
                    outputStream.close();
                    inputStream.close();
                    response.close();
                    entity.setDownloadedBytes(currentDownloaded);
                    entity.setStatusEnum(DownloadStatus.PAUSED);
                    db.downloadedSurahDao().insertOrUpdate(entity);
                    notifyStatusChanged(entity);
                    return;
                }

                outputStream.write(buffer, 0, bytesRead);
                currentDownloaded += bytesRead;

                long now = System.currentTimeMillis();
                if (now - lastProgressTime > 300) {
                    lastProgressTime = now;
                    entity.setDownloadedBytes(currentDownloaded);
                    int pct = totalBytes > 0 ? (int) ((currentDownloaded * 100) / totalBytes) : 0;
                    notifyProgress(entity, pct, currentDownloaded, totalBytes);
                }
            }

            outputStream.close();
            inputStream.close();
            response.close();

            // Verify File Integrity
            entity.setStatusEnum(DownloadStatus.VERIFYING);
            notifyStatusChanged(entity);

            File destFile = getLocalFile(reciterId, surahId);
            if (tempFile.length() < 4096) {
                tempFile.delete();
                handleDownloadFailure(task, "الملف المحمل غير صالح أو تالف");
                return;
            }

            if (destFile.exists()) {
                destFile.delete();
            }

            boolean renamed = tempFile.renameTo(destFile);
            if (!renamed) {
                handleDownloadFailure(task, "تعذر حفظ الملف على التخزين المحلي");
                return;
            }

            entity.setLocalFilePath(destFile.getAbsolutePath());
            entity.setFileSize(destFile.length());
            entity.setDownloadedBytes(destFile.length());
            entity.setDownloadDate(System.currentTimeMillis());
            entity.setStatusEnum(DownloadStatus.COMPLETED);
            entity.setErrorMessage(null);

            db.downloadedSurahDao().insertOrUpdate(entity);
            activeTasks.remove(downloadId);

            notifyCompleted(entity);

        } catch (Exception e) {
            Log.e(TAG, "Download exception for " + downloadId, e);
            handleDownloadFailure(task, "حدث خطأ أثناء التحميل: " + (e.getMessage() != null ? e.getMessage() : "خطأ غير معروف"));
        }
    }

    private void handleDownloadFailure(DownloadTask task, String errorMessage) {
        DownloadedSurahEntity entity = task.entity;
        entity.setStatusEnum(DownloadStatus.FAILED);
        entity.setErrorMessage(errorMessage);
        db.downloadedSurahDao().insertOrUpdate(entity);
        activeTasks.remove(entity.getDownloadId());
        notifyFailed(entity, errorMessage);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    // Listener Notifications on Main Thread
    private void notifyStatusChanged(DownloadedSurahEntity entity) {
        mainHandler.post(() -> {
            synchronized (listeners) {
                for (SurahDownloadListener l : listeners) {
                    l.onDownloadStatusChanged(entity);
                }
            }
        });
    }

    private void notifyProgress(DownloadedSurahEntity entity, int percent, long downloadedBytes, long totalBytes) {
        mainHandler.post(() -> {
            synchronized (listeners) {
                for (SurahDownloadListener l : listeners) {
                    l.onDownloadProgress(entity, percent, downloadedBytes, totalBytes);
                }
            }
        });
    }

    private void notifyCompleted(DownloadedSurahEntity entity) {
        mainHandler.post(() -> {
            synchronized (listeners) {
                for (SurahDownloadListener l : listeners) {
                    l.onDownloadCompleted(entity);
                }
            }
        });
    }

    private void notifyFailed(DownloadedSurahEntity entity, String errorMessage) {
        mainHandler.post(() -> {
            synchronized (listeners) {
                for (SurahDownloadListener l : listeners) {
                    l.onDownloadFailed(entity, errorMessage);
                }
            }
        });
    }

    private void notifyCancelled(DownloadedSurahEntity entity) {
        mainHandler.post(() -> {
            synchronized (listeners) {
                for (SurahDownloadListener l : listeners) {
                    l.onDownloadCancelled(entity);
                }
            }
        });
    }
}
