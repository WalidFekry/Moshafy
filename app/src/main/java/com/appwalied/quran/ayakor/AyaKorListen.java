package com.appwalied.quran.ayakor;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.SeekBar;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityAyakorlistenBinding;

import java.util.Locale;

public class AyaKorListen extends BaseActivity {

    private ActivityAyakorlistenBinding binding;
    private MediaPlayer mediaPlayer;
    private final Handler progressHandler = new Handler(Looper.getMainLooper());
    private Runnable updateProgressTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAyakorlistenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> finish());

        mediaPlayer = MediaPlayer.create(this, R.raw.kor);
        if (mediaPlayer != null) {
            int totalTime = mediaPlayer.getDuration();
            binding.ayakorseekbar.setMax(totalTime);
            binding.tvTotalTime.setText(formatTime(totalTime));
            binding.tvCurrentTime.setText(formatTime(0));
        }

        binding.ayakorseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                    binding.tvCurrentTime.setText(formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        updateProgressTask = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int currentPos = mediaPlayer.getCurrentPosition();
                    binding.ayakorseekbar.setProgress(currentPos);
                    binding.tvCurrentTime.setText(formatTime(currentPos));
                    progressHandler.postDelayed(this, 500);
                }
            }
        };

        binding.btnPlay.setOnClickListener(v -> {
            if (mediaPlayer == null) return;

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                binding.btnPlay.setImageResource(R.drawable.btn_play);
                progressHandler.removeCallbacks(updateProgressTask);
            } else {
                mediaPlayer.start();
                binding.btnPlay.setImageResource(R.drawable.btn_pause);
                progressHandler.post(updateProgressTask);
            }
        });

        mediaPlayer.setOnCompletionListener(mp -> {
            binding.btnPlay.setImageResource(R.drawable.btn_play);
            binding.ayakorseekbar.setProgress(0);
            binding.tvCurrentTime.setText(formatTime(0));
            progressHandler.removeCallbacks(updateProgressTask);
        });

        showBanner(binding.adsContainer);
    }

    private String formatTime(int milliseconds) {
        int seconds = (milliseconds / 1000) % 60;
        int minutes = (milliseconds / (1000 * 60)) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        progressHandler.removeCallbacksAndMessages(null);
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
