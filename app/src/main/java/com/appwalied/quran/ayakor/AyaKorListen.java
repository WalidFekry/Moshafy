package com.appwalied.quran.ayakor;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.appcompat.widget.AppCompatImageButton;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;

public class AyaKorListen extends BaseActivity {

    private ImageButton playButton;
    private SeekBar ayakorseekbar;
    private MediaPlayer mediaPlayer;
    private AppCompatImageButton back;
    private final Handler progressHandler = new Handler(Looper.getMainLooper());
    private Runnable updateProgressTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayakorlisten);

        playButton = findViewById(R.id.btnPlay);
        ayakorseekbar = findViewById(R.id.ayakorseekbar);
        back = findViewById(R.id.back_button);

        if (back != null) {
            back.setOnClickListener(v -> finish());
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.kor);
        if (mediaPlayer != null) {
            int totalTime = mediaPlayer.getDuration();
            ayakorseekbar.setMax(totalTime);
        }

        ayakorseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
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
                    ayakorseekbar.setProgress(mediaPlayer.getCurrentPosition());
                    progressHandler.postDelayed(this, 500);
                }
            }
        };

        playButton.setOnClickListener(v -> {
            if (mediaPlayer == null) return;

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                playButton.setImageResource(R.drawable.btn_play);
                progressHandler.removeCallbacks(updateProgressTask);
            } else {
                mediaPlayer.start();
                playButton.setImageResource(R.drawable.btn_pause);
                progressHandler.post(updateProgressTask);
            }
        });

        mediaPlayer.setOnCompletionListener(mp -> {
            playButton.setImageResource(R.drawable.btn_play);
            ayakorseekbar.setProgress(0);
            progressHandler.removeCallbacks(updateProgressTask);
        });
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
