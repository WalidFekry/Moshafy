package com.appwalied.quran.image_quran.download;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.appwalied.quran.image_quran.data.ImageQuranConfig;
import com.appwalied.quran.image_quran.data.Riwaya;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ImageQuranDownloadManager {

    public enum DownloadMode {
        IMAGES_ONLY,
        AUDIO_ONLY,
        IMAGES_AND_AUDIO
    }

    public interface DownloadListener {
        void onProgress(int downloadedCount, int totalCount, int percent, String statusText);
        void onCompleted(Riwaya riwaya, DownloadMode mode);
        void onError(String message);
        void onCancelled();
    }

    private static volatile ImageQuranDownloadManager instance;
    private final Context context;
    private final OkHttpClient httpClient;
    private final ExecutorService executor;
    private final Handler mainHandler;

    private final AtomicBoolean isDownloading = new AtomicBoolean(false);
    private final AtomicBoolean isCancelled = new AtomicBoolean(false);
    private final AtomicBoolean isPaused = new AtomicBoolean(false);

    private DownloadListener currentListener;

    private ImageQuranDownloadManager(Context context) {
        this.context = context.getApplicationContext();
        this.httpClient = new OkHttpClient.Builder().build();
        this.executor = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public static ImageQuranDownloadManager getInstance(Context context) {
        if (instance == null) {
            synchronized (ImageQuranDownloadManager.class) {
                if (instance == null) {
                    instance = new ImageQuranDownloadManager(context);
                }
            }
        }
        return instance;
    }

    public boolean isDownloading() {
        return isDownloading.get();
    }

    public boolean isPaused() {
        return isPaused.get();
    }

    public void pauseDownload() {
        if (isDownloading.get()) {
            isPaused.set(true);
        }
    }

    public void resumeDownload() {
        if (isDownloading.get()) {
            isPaused.set(false);
        }
    }

    public void cancelDownload() {
        if (isDownloading.get()) {
            isCancelled.set(true);
            isPaused.set(false);
        }
    }

    public void startDownload(Riwaya riwaya, DownloadMode mode, DownloadListener listener) {
        if (isDownloading.get()) {
            if (listener != null) {
                listener.onError("هناك عملية تنزيل جارية بالفعل.");
            }
            return;
        }

        this.currentListener = listener;
        isDownloading.set(true);
        isCancelled.set(false);
        isPaused.set(false);

        executor.execute(() -> runBatchDownload(riwaya, mode));
    }

    private void runBatchDownload(Riwaya riwaya, DownloadMode mode) {
        int totalItems = 0;
        if (mode == DownloadMode.IMAGES_ONLY || mode == DownloadMode.IMAGES_AND_AUDIO) {
            totalItems += ImageQuranConfig.TOTAL_PAGES;
        }
        if (mode == DownloadMode.AUDIO_ONLY || mode == DownloadMode.IMAGES_AND_AUDIO) {
            totalItems += ImageQuranConfig.TOTAL_PAGES;
        }

        int currentProgressCount = 0;

        // 1. Download Images
        if (mode == DownloadMode.IMAGES_ONLY || mode == DownloadMode.IMAGES_AND_AUDIO) {
            for (int page = 1; page <= ImageQuranConfig.TOTAL_PAGES; page++) {
                if (checkCancelled()) return;
                waitIfPaused();

                File destFile = ImageQuranConfig.getLocalImageFile(context, riwaya, page);
                if (!destFile.exists() || destFile.length() == 0) {
                    String url = ImageQuranConfig.getImageUrl(riwaya, page);
                    boolean success = downloadSingleFile(url, destFile);
                    if (!success) {
                        // retry once
                        success = downloadSingleFile(url, destFile);
                    }
                }

                currentProgressCount++;
                publishProgress(currentProgressCount, totalItems, "جاري تنزيل صور " + riwaya.getDisplayName() + " (" + page + "/" + ImageQuranConfig.TOTAL_PAGES + ")");
            }
        }

        // 2. Download Audio
        if (mode == DownloadMode.AUDIO_ONLY || mode == DownloadMode.IMAGES_AND_AUDIO) {
            for (int page = 1; page <= ImageQuranConfig.TOTAL_PAGES; page++) {
                if (checkCancelled()) return;
                waitIfPaused();

                File destFile = ImageQuranConfig.getLocalAudioFile(context, page);
                if (!destFile.exists() || destFile.length() == 0) {
                    String url = ImageQuranConfig.getAudioUrl(page);
                    boolean success = downloadSingleFile(url, destFile);
                    if (!success) {
                        success = downloadSingleFile(url, destFile);
                    }
                }

                currentProgressCount++;
                publishProgress(currentProgressCount, totalItems, "جاري تنزيل تلاوات الصفحات (" + page + "/" + ImageQuranConfig.TOTAL_PAGES + ")");
            }
        }

        isDownloading.set(false);
        int finalTotalItems = totalItems;
        mainHandler.post(() -> {
            if (currentListener != null) {
                currentListener.onProgress(finalTotalItems, finalTotalItems, 100, "اكتمل التنزيل بنجاح! 🎉");
                currentListener.onCompleted(riwaya, mode);
            }
        });
    }

    public boolean downloadSingleFile(String url, File destFile) {
        File parentDir = destFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created && !parentDir.exists()) {
                return false;
            }
        }
        File tmpFile = new File(parentDir, destFile.getName() + ".tmp");
        try {
            Request request = new Request.Builder().url(url).build();
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful() || response.body() == null) {
                    return false;
                }

                ResponseBody body = response.body();
                try (InputStream inputStream = body.byteStream();
                     FileOutputStream outputStream = new FileOutputStream(tmpFile)) {

                    byte[] buffer = new byte[8192];
                    int read;
                    while ((read = inputStream.read(buffer)) != -1) {
                        if (isCancelled.get()) {
                            tmpFile.delete();
                            return false;
                        }
                        outputStream.write(buffer, 0, read);
                    }
                    outputStream.flush();
                }

                long minSize = destFile.getName().endsWith(".mp3") ? 4096 : 2048;
                if (tmpFile.length() >= minSize) {
                    if (destFile.exists()) {
                        destFile.delete();
                    }
                    return tmpFile.renameTo(destFile);
                } else {
                    tmpFile.delete();
                }
            }
        } catch (Exception e) {
            if (tmpFile.exists()) {
                tmpFile.delete();
            }
        }
        return false;
    }

    private boolean checkCancelled() {
        if (isCancelled.get()) {
            isDownloading.set(false);
            mainHandler.post(() -> {
                if (currentListener != null) {
                    currentListener.onCancelled();
                }
            });
            return true;
        }
        return false;
    }

    private void waitIfPaused() {
        while (isPaused.get() && !isCancelled.get()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private void publishProgress(int count, int total, String statusText) {
        int percent = (int) (((float) count / total) * 100);
        mainHandler.post(() -> {
            if (currentListener != null) {
                currentListener.onProgress(count, total, percent, statusText);
            }
        });
    }

    // Storage Status Helpers
    public int getDownloadedImagesCount(Riwaya riwaya) {
        int count = 0;
        for (int p = 1; p <= ImageQuranConfig.TOTAL_PAGES; p++) {
            if (ImageQuranConfig.isImageDownloaded(context, riwaya, p)) {
                count++;
            }
        }
        return count;
    }

    public long getDownloadedImagesSizeBytes(Riwaya riwaya) {
        long size = 0;
        File dir = ImageQuranConfig.getImagesDirectory(context, riwaya);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isFile() && f.length() > 2048) {
                    size += f.length();
                }
            }
        }
        return size;
    }

    public int getDownloadedAudioCount() {
        int count = 0;
        for (int p = 1; p <= ImageQuranConfig.TOTAL_PAGES; p++) {
            if (ImageQuranConfig.isAudioDownloaded(context, p)) {
                count++;
            }
        }
        return count;
    }

    public long getDownloadedAudioSizeBytes() {
        long size = 0;
        File dir = ImageQuranConfig.getAudioDirectory(context);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isFile() && f.length() > 4096) {
                    size += f.length();
                }
            }
        }
        return size;
    }

    public void deleteDownloadedImages(Riwaya riwaya) {
        File dir = ImageQuranConfig.getImagesDirectory(context, riwaya);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
    }

    public void deleteDownloadedAudio() {
        File dir = ImageQuranConfig.getAudioDirectory(context);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
    }
}
