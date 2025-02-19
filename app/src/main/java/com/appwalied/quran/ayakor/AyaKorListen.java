package com.appwalied.quran.ayakor;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.appwalied.quran.R;

public class AyaKorListen extends AppCompatActivity {

    ImageButton PlayButton;
    SeekBar ayakorseekbar;
    MediaPlayer mediaPlayer;
    int totalTime;
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            ayakorseekbar.setProgress(message.what);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayakorlisten);

        PlayButton = findViewById(R.id.btnPlay);
        ayakorseekbar = findViewById(R.id.ayakorseekbar);

        mediaPlayer = MediaPlayer.create(AyaKorListen.this, R.raw.kor);
        mediaPlayer.seekTo(0);
        totalTime = mediaPlayer.getDuration();

        ayakorseekbar.setMax(totalTime);
        ayakorseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    ayakorseekbar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null) {
                    try {
                        if (mediaPlayer.isPlaying()) {
                            Message message = new Message();
                            message.what = mediaPlayer.getCurrentPosition();
                            handler.sendMessage(message);
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        PlayButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check for already playing
                if (mediaPlayer.isPlaying()) {
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                        // Changing button image to play button
                        PlayButton.setImageResource(R.drawable.btn_play);
                    }
                } else {
                    // Resume song
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                        // Changing button image to pause button
                        PlayButton.setImageResource(R.drawable.btn_pause);
                    }
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        mediaPlayer.pause();
        finish();
    }
}
