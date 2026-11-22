package com.appwalied.quran.quran_listening.download;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SurahDownloadWorker extends Worker {

    public static final String KEY_RECITER_ID = "reciter_id";
    public static final String KEY_RECITER_NAME = "reciter_name";
    public static final String KEY_SURAH_ID = "surah_id";
    public static final String KEY_SURAH_NAME = "surah_name";
    public static final String KEY_SERVER_URL = "server_url";

    public SurahDownloadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public static void enqueueDownload(Context context, int reciterId, String reciterName, int surahId, String surahName, String serverUrl) {
        Data inputData = new Data.Builder()
                .putInt(KEY_RECITER_ID, reciterId)
                .putString(KEY_RECITER_NAME, reciterName)
                .putInt(KEY_SURAH_ID, surahId)
                .putString(KEY_SURAH_NAME, surahName)
                .putString(KEY_SERVER_URL, serverUrl)
                .build();

        String workTag = "download_" + reciterId + "_" + surahId;

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SurahDownloadWorker.class)
                .setInputData(inputData)
                .addTag(workTag)
                .build();

        WorkManager.getInstance(context).enqueue(workRequest);
    }

    @NonNull
    @Override
    public Result doWork() {
        int reciterId = getInputData().getInt(KEY_RECITER_ID, -1);
        String reciterName = getInputData().getString(KEY_RECITER_NAME);
        int surahId = getInputData().getInt(KEY_SURAH_ID, -1);
        String surahName = getInputData().getString(KEY_SURAH_NAME);
        String serverUrl = getInputData().getString(KEY_SERVER_URL);

        if (reciterId <= 0 || surahId <= 0 || serverUrl == null) {
            return Result.failure();
        }

        SurahDownloadManager downloadManager = SurahDownloadManager.getInstance(getApplicationContext());
        final boolean[] isDone = {false};
        final boolean[] isFailed = {false};

        SurahDownloadManager.SurahDownloadListener listener = new SurahDownloadManager.SurahDownloadListener() {
            @Override
            public void onDownloadStatusChanged(DownloadedSurahEntity surah) {
            }

            @Override
            public void onDownloadProgress(DownloadedSurahEntity surah, int percent, long downloadedBytes, long totalBytes) {
            }

            @Override
            public void onDownloadCompleted(DownloadedSurahEntity surah) {
                if (surah.getReciterId() == reciterId && surah.getSurahId() == surahId) {
                    isDone[0] = true;
                }
            }

            @Override
            public void onDownloadFailed(DownloadedSurahEntity surah, String errorMessage) {
                if (surah.getReciterId() == reciterId && surah.getSurahId() == surahId) {
                    isFailed[0] = true;
                    isDone[0] = true;
                }
            }

            @Override
            public void onDownloadCancelled(DownloadedSurahEntity surah) {
                if (surah.getReciterId() == reciterId && surah.getSurahId() == surahId) {
                    isDone[0] = true;
                }
            }
        };

        downloadManager.addListener(listener);
        downloadManager.startDownload(reciterId, reciterName, surahId, surahName, serverUrl);

        while (!isDone[0]) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                downloadManager.removeListener(listener);
                return Result.failure();
            }
        }

        downloadManager.removeListener(listener);
        return isFailed[0] ? Result.retry() : Result.success();
    }
}
