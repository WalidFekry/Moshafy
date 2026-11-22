package com.appwalied.quran.image_quran.download;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.appwalied.quran.image_quran.data.Riwaya;

public class ImageQuranDownloadWorker extends Worker {

    public static final String KEY_RIWAYA = "riwaya_key";
    public static final String KEY_MODE = "download_mode";

    public ImageQuranDownloadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String riwayaKey = getInputData().getString(KEY_RIWAYA);
        String modeStr = getInputData().getString(KEY_MODE);

        Riwaya riwaya = Riwaya.fromKey(riwayaKey);
        ImageQuranDownloadManager.DownloadMode mode = ImageQuranDownloadManager.DownloadMode.IMAGES_AND_AUDIO;
        if (modeStr != null) {
            try {
                mode = ImageQuranDownloadManager.DownloadMode.valueOf(modeStr);
            } catch (Exception ignored) {
            }
        }

        ImageQuranDownloadManager downloadManager = ImageQuranDownloadManager.getInstance(getApplicationContext());
        final boolean[] isDone = {false};
        final boolean[] isFailed = {false};

        downloadManager.startDownload(riwaya, mode, new ImageQuranDownloadManager.DownloadListener() {
            @Override
            public void onProgress(int downloadedCount, int totalCount, int percent, String statusText) {
            }

            @Override
            public void onCompleted(Riwaya riwaya, ImageQuranDownloadManager.DownloadMode mode) {
                isDone[0] = true;
            }

            @Override
            public void onError(String message) {
                isFailed[0] = true;
                isDone[0] = true;
            }

            @Override
            public void onCancelled() {
                isDone[0] = true;
            }
        });

        while (!isDone[0]) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return Result.failure();
            }
        }

        return isFailed[0] ? Result.retry() : Result.success();
    }
}
