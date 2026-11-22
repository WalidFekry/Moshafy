package com.appwalied.quran.image_quran.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityImageQuranDownloadBinding;
import com.appwalied.quran.image_quran.data.ImageQuranConfig;
import com.appwalied.quran.image_quran.data.Riwaya;
import com.appwalied.quran.image_quran.download.ImageQuranDownloadManager;

import java.util.Locale;

public class ImageQuranDownloadActivity extends BaseActivity {

    public static final String EXTRA_RIWAYA_KEY = "extra_riwaya_key";

    private ActivityImageQuranDownloadBinding binding;
    private ImageQuranDownloadManager downloadManager;
    private Riwaya targetRiwaya = Riwaya.HAFS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageQuranDownloadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        downloadManager = ImageQuranDownloadManager.getInstance(this);

        if (getIntent() != null && getIntent().getStringExtra(EXTRA_RIWAYA_KEY) != null) {
            targetRiwaya = Riwaya.fromKey(getIntent().getStringExtra(EXTRA_RIWAYA_KEY));
        }

        updateStorageInfo();
        setupClickListeners();
    }

    private void updateStorageInfo() {
        int hafsCount = downloadManager.getDownloadedImagesCount(Riwaya.HAFS);
        long hafsSizeMb = downloadManager.getDownloadedImagesSizeBytes(Riwaya.HAFS) / (1024 * 1024);
        binding.tvHafsStorageInfo.setText(String.format(Locale.getDefault(), "الصور المحملة: %d من 604 (%d ميجابايت)", hafsCount, hafsSizeMb));

        int warshCount = downloadManager.getDownloadedImagesCount(Riwaya.WARSH);
        long warshSizeMb = downloadManager.getDownloadedImagesSizeBytes(Riwaya.WARSH) / (1024 * 1024);
        binding.tvWarshStorageInfo.setText(String.format(Locale.getDefault(), "الصور المحملة: %d من 604 (%d ميجابايت)", warshCount, warshSizeMb));

        int audioCount = downloadManager.getDownloadedAudioCount();
        long audioSizeMb = downloadManager.getDownloadedAudioSizeBytes() / (1024 * 1024);
        binding.tvAudioStorageInfo.setText(String.format(Locale.getDefault(), "الملفات المحملة: %d من 604 (%d ميجابايت)", audioCount, audioSizeMb));
    }

    private void setupClickListeners() {
        binding.btnBackDownload.setOnClickListener(v -> finish());

        binding.btnDownloadHafs.setOnClickListener(v -> startDownload(Riwaya.HAFS, ImageQuranDownloadManager.DownloadMode.IMAGES_ONLY));
        binding.btnDownloadWarsh.setOnClickListener(v -> startDownload(Riwaya.WARSH, ImageQuranDownloadManager.DownloadMode.IMAGES_ONLY));
        binding.btnDownloadAudio.setOnClickListener(v -> startDownload(targetRiwaya, ImageQuranDownloadManager.DownloadMode.AUDIO_ONLY));

        binding.btnDeleteHafs.setOnClickListener(v -> confirmDeleteImages(Riwaya.HAFS));
        binding.btnDeleteWarsh.setOnClickListener(v -> confirmDeleteImages(Riwaya.WARSH));
        binding.btnDeleteAudio.setOnClickListener(v -> confirmDeleteAudio());

        binding.btnPauseResume.setOnClickListener(v -> {
            if (downloadManager.isPaused()) {
                downloadManager.resumeDownload();
                binding.btnPauseResume.setText("إيقاف مؤقت");
            } else {
                downloadManager.pauseDownload();
                binding.btnPauseResume.setText("استكمال");
            }
        });

        binding.btnCancelDownload.setOnClickListener(v -> downloadManager.cancelDownload());
    }

    private void startDownload(Riwaya riwaya, ImageQuranDownloadManager.DownloadMode mode) {
        binding.cardDownloadProgress.setVisibility(View.VISIBLE);
        binding.btnPauseResume.setText("إيقاف مؤقت");

        downloadManager.startDownload(riwaya, mode, new ImageQuranDownloadManager.DownloadListener() {
            @Override
            public void onProgress(int downloadedCount, int totalCount, int percent, String statusText) {
                binding.tvDownloadStatus.setText(statusText);
                binding.progressDownload.setProgress(percent);
                binding.tvDownloadPercent.setText(percent + "%");
            }

            @Override
            public void onCompleted(Riwaya riwayaCompleted, ImageQuranDownloadManager.DownloadMode modeCompleted) {
                binding.cardDownloadProgress.setVisibility(View.GONE);
                updateStorageInfo();
                Toast.makeText(ImageQuranDownloadActivity.this, "اكتمل التحميل بنجاح! 🎉", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                binding.cardDownloadProgress.setVisibility(View.GONE);
                Toast.makeText(ImageQuranDownloadActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled() {
                binding.cardDownloadProgress.setVisibility(View.GONE);
                Toast.makeText(ImageQuranDownloadActivity.this, "تم إلغاء التنزيل", Toast.LENGTH_SHORT).show();
                updateStorageInfo();
            }
        });
    }

    private void confirmDeleteImages(Riwaya riwaya) {
        new AlertDialog.Builder(this)
                .setTitle("حذف الصور")
                .setMessage("هل أنت تأكد من رغبتك في حذف جميع الصور المحملة محلياً لـ " + riwaya.getDisplayName() + "؟")
                .setPositiveButton("حذف", (dialog, which) -> {
                    downloadManager.deleteDownloadedImages(riwaya);
                    updateStorageInfo();
                    Toast.makeText(this, "تم حذف صور " + riwaya.getDisplayName(), Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("إلغاء", null)
                .show();
    }

    private void confirmDeleteAudio() {
        new AlertDialog.Builder(this)
                .setTitle("حذف التلاوات")
                .setMessage("هل أنت تأكد من رغبتك في حذف جميع التلاوات الصوتية المحملة محلياً؟")
                .setPositiveButton("حذف", (dialog, which) -> {
                    downloadManager.deleteDownloadedAudio();
                    updateStorageInfo();
                    Toast.makeText(this, "تم حذف جميع التلاوات", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("إلغاء", null)
                .show();
    }
}
