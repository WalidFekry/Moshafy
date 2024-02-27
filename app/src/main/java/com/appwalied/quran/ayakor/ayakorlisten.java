package com.appwalied.quran.ayakor;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.appwalied.quran.R;

public class ayakorlisten extends AppCompatActivity {

    ImageButton PlayButton;
    SeekBar ayakorseekbar;
    MediaPlayer mediaPlayer;
    int totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ayakorlisten);

        PlayButton = (ImageButton) findViewById(R.id.btnPlay);
        ayakorseekbar = (SeekBar) findViewById(R.id.ayakorseekbar);

        mediaPlayer = MediaPlayer.create(ayakorlisten.this, R.raw.kor);
        mediaPlayer.seekTo(0);
        totalTime = mediaPlayer.getDuration();

        ayakorseekbar.setMax(totalTime);
        ayakorseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                {
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
            public void run()
            {
                while (mediaPlayer != null)
                {
                    try
                    {
                        if (mediaPlayer.isPlaying()) {
                            Message message = new Message();
                            message.what = mediaPlayer.getCurrentPosition();
                            handler.sendMessage(message);
                            Thread.sleep(1000);
                        }
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        PlayButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check for already playing
                if(mediaPlayer.isPlaying()){
                    if(mediaPlayer!=null){
                        mediaPlayer.pause();
                        // Changing button image to play button
                        PlayButton.setImageResource( R.drawable.btn_play);
                    }
                }else{
                    // Resume song
                    if(mediaPlayer!=null){
                        mediaPlayer.start();
                        // Changing button image to pause button
                        PlayButton.setImageResource( R.drawable.btn_pause);
                    }
                }

            }
        });

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message)
        {
            ayakorseekbar.setProgress(message.what);
        }
    };

    public String createTimeLable(int time)
    {
        String timeLable = "";
        int min = time/1000/60;
        int sec = time/1000%60;
        timeLable = min + ":";
        if (sec<10)
        timeLable += "0";
        timeLable += sec;

        return timeLable;
    }

    @Override
    public void onBackPressed() {
        mediaPlayer.pause();
        finish();
    }
}
