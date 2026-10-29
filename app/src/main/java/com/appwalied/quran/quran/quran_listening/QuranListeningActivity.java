package com.appwalied.quran.quran.quran_listening;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.quran.quran_listening.listening.SongsManager;
import com.appwalied.quran.quran.quran_listening.listening.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class QuranListeningActivity extends BaseActivity implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "QuranListeningActivity";

    private MediaPlayer mp;
    private String RecitesName = "";
    private String RecitesAYA = "";
    private ImageButton btnPlay;
    private ImageButton btnForward;
    private ImageButton btnBackward;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private ImageButton btnRepeat;
    private ImageButton btnShuffle;
    private SeekBar songProgressBar;
    private TextView songTitleLabel;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;

    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private SongsManager songManager;
    private Utilities utils;
    private final int seekForwardTime = 5000;
    private final int seekBackwardTime = 5000;
    private int currentSongIndex = 0;
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private FrameLayout adsContainer;
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<>();

    private final Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (mp != null) {
                try {
                    if (mp.isPlaying()) {
                        long totalDuration = mp.getDuration();
                        long currentDuration = mp.getCurrentPosition();

                        if (songTotalDurationLabel != null) {
                            songTotalDurationLabel.setText(utils.milliSecondsToTimer(totalDuration));
                        }
                        if (songCurrentDurationLabel != null) {
                            songCurrentDurationLabel.setText(utils.milliSecondsToTimer(currentDuration));
                        }

                        int progress = utils.getProgressPercentage(currentDuration, totalDuration);
                        if (songProgressBar != null) {
                            songProgressBar.setProgress(progress);
                        }

                        mHandler.postDelayed(this, 100);
                    }
                } catch (Exception ex) {
                    Log.e(TAG, "Error in progress update runnable", ex);
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managerdb);

        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            RecitesName = b.getString("RecitesName", "");
            RecitesAYA = b.getString("RecitesAYA", "0");
        }

        btnPlay = findViewById(R.id.btnPlay);
        btnForward = findViewById(R.id.btnForward);
        btnBackward = findViewById(R.id.btnBackward);
        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnRepeat = findViewById(R.id.btnRepeat);
        btnShuffle = findViewById(R.id.btnShuffle);
        songProgressBar = findViewById(R.id.songProgressBar);
        songTitleLabel = findViewById(R.id.songTitle);
        songCurrentDurationLabel = findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = findViewById(R.id.songTotalDurationLabel);
        View back = findViewById(R.id.back_button);
        adsContainer = findViewById(R.id.adsContainer);

        if (back != null) {
            back.setOnClickListener(v -> {
                safeStopAndRelease();
                finish();
            });
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                safeStopAndRelease();
                finish();
            }
        });

        showBanner(adsContainer);

        songManager = new SongsManager();
        utils = new Utilities();

        if (songProgressBar != null) {
            songProgressBar.setOnSeekBarChangeListener(this);
        }

        songsList = songManager.getPlayList(RecitesName);

        try {
            currentSongIndex = Integer.parseInt(RecitesAYA);
        } catch (NumberFormatException e) {
            currentSongIndex = 0;
        }

        playSong(currentSongIndex);

        btnPlay.setOnClickListener(arg0 -> {
            if (mp != null) {
                try {
                    if (mp.isPlaying()) {
                        mp.pause();
                        btnPlay.setImageResource(R.drawable.btn_play);
                    } else {
                        mp.start();
                        btnPlay.setImageResource(R.drawable.btn_pause);
                        updateProgressBar();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error toggling play/pause", e);
                    playSong(currentSongIndex);
                }
            } else {
                playSong(currentSongIndex);
            }
        });

        btnForward.setOnClickListener(arg0 -> {
            if (mp != null) {
                try {
                    int currentPosition = mp.getCurrentPosition();
                    int duration = mp.getDuration();
                    if (currentPosition + seekForwardTime <= duration) {
                        mp.seekTo(currentPosition + seekForwardTime);
                    } else {
                        mp.seekTo(duration);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error seeking forward", e);
                }
            }
        });

        btnBackward.setOnClickListener(arg0 -> {
            if (mp != null) {
                try {
                    int currentPosition = mp.getCurrentPosition();
                    if (currentPosition - seekBackwardTime >= 0) {
                        mp.seekTo(currentPosition - seekBackwardTime);
                    } else {
                        mp.seekTo(0);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error seeking backward", e);
                }
            }
        });

        btnNext.setOnClickListener(arg0 -> {
            if (songsList != null && !songsList.isEmpty()) {
                if (currentSongIndex < (songsList.size() - 1)) {
                    playSong(currentSongIndex + 1);
                    currentSongIndex = currentSongIndex + 1;
                } else {
                    playSong(0);
                    currentSongIndex = 0;
                }
            }
        });

        btnPrevious.setOnClickListener(arg0 -> {
            if (songsList != null && !songsList.isEmpty()) {
                if (currentSongIndex > 0) {
                    playSong(currentSongIndex - 1);
                    currentSongIndex = currentSongIndex - 1;
                } else {
                    playSong(songsList.size() - 1);
                    currentSongIndex = songsList.size() - 1;
                }
            }
        });

        btnRepeat.setOnClickListener(arg0 -> {
            if (isRepeat) {
                isRepeat = false;
                Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                btnRepeat.setImageResource(R.drawable.btn_repeat);
            } else {
                isRepeat = true;
                Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                isShuffle = false;
                btnRepeat.setImageResource(R.drawable.btn_repeat_focused);
                btnShuffle.setImageResource(R.drawable.btn_shuffle);
            }
        });

        btnShuffle.setOnClickListener(arg0 -> {
            if (isShuffle) {
                isShuffle = false;
                Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                btnShuffle.setImageResource(R.drawable.btn_shuffle);
            } else {
                isShuffle = true;
                Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                isRepeat = false;
                btnShuffle.setImageResource(R.drawable.btn_shuffle_focused);
                btnRepeat.setImageResource(R.drawable.btn_repeat);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100 && data != null && data.getExtras() != null) {
            currentSongIndex = data.getExtras().getInt("songIndex", 0);
            playSong(currentSongIndex);
        }
    }

    public void playSong(int songIndex) {
        if (songsList == null || songsList.isEmpty() || songIndex < 0 || songIndex >= songsList.size()) {
            Log.e(TAG, "Invalid song index or empty songs list: " + songIndex);
            return;
        }

        mHandler.removeCallbacks(mUpdateTimeTask);

        if (mp != null) {
            try {
                if (mp.isPlaying()) {
                    mp.stop();
                }
            } catch (Exception ignored) {}
            try {
                mp.reset();
            } catch (Exception ignored) {}
        } else {
            mp = new MediaPlayer();
        }

        try {
            mp.setAudioAttributes(
                new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            );

            String songPath = songsList.get(songIndex).get("songPath");
            if (songPath == null || songPath.isEmpty()) {
                Toast.makeText(this, "رابط الصوت غير صالح", Toast.LENGTH_SHORT).show();
                return;
            }

            mp.setDataSource(songPath);
            mp.setOnCompletionListener(this);

            showLoading();

            mp.setOnPreparedListener(mpPrepared -> {
                hideLoading();
                if (isFinishing() || isDestroyed()) {
                    safeStopAndRelease();
                    return;
                }
                try {
                    mpPrepared.start();
                    if (songTitleLabel != null) {
                        String songTitle = songsList.get(songIndex).get("songTitle");
                        songTitleLabel.setText(songTitle != null ? songTitle : "");
                    }
                    if (btnPlay != null) {
                        btnPlay.setImageResource(R.drawable.btn_pause);
                    }
                    if (songProgressBar != null) {
                        songProgressBar.setProgress(0);
                        songProgressBar.setMax(100);
                    }
                    updateProgressBar();
                } catch (Exception e) {
                    Log.e(TAG, "Error starting player in onPrepared", e);
                }
            });

            mp.setOnErrorListener((mpError, what, extra) -> {
                hideLoading();
                Log.e(TAG, "MediaPlayer error: what=" + what + ", extra=" + extra);
                Toast.makeText(QuranListeningActivity.this, "حدث خطأ أثناء تشغيل الصوت", Toast.LENGTH_SHORT).show();
                if (btnPlay != null) {
                    btnPlay.setImageResource(R.drawable.btn_play);
                }
                return true;
            });

            mp.prepareAsync();

        } catch (Exception e) {
            hideLoading();
            Log.e(TAG, "Error preparing media player", e);
            Toast.makeText(this, "تعذر تشغيل هذا المقطع", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateProgressBar() {
        mHandler.removeCallbacks(mUpdateTimeTask);
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {}

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        if (mp != null) {
            try {
                int totalDuration = mp.getDuration();
                int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
                mp.seekTo(currentPosition);
            } catch (Exception e) {
                Log.e(TAG, "Error seeking onStopTrackingTouch", e);
            }
        }
        updateProgressBar();
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        if (isRepeat) {
            playSong(currentSongIndex);
        } else if (isShuffle && songsList != null && songsList.size() > 1) {
            Random rand = new Random();
            currentSongIndex = rand.nextInt(songsList.size());
            playSong(currentSongIndex);
        } else if (songsList != null && !songsList.isEmpty()) {
            if (currentSongIndex < (songsList.size() - 1)) {
                playSong(currentSongIndex + 1);
                currentSongIndex = currentSongIndex + 1;
            } else {
                playSong(0);
                currentSongIndex = 0;
            }
        }
    }

    private void safeStopAndRelease() {
        mHandler.removeCallbacks(mUpdateTimeTask);
        if (mp != null) {
            try {
                if (mp.isPlaying()) {
                    mp.stop();
                }
            } catch (Exception ignored) {}
            try {
                mp.release();
            } catch (Exception ignored) {}
            mp = null;
        }
    }

    @Override
    protected void onDestroy() {
        safeStopAndRelease();
        super.onDestroy();
    }
}
