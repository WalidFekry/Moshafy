package com.appwalied.quran.quran.audio;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

public class QuranAudioPlayer {

    private static volatile QuranAudioPlayer instance;
    private final Context context;
    private ExoPlayer exoPlayer;
    private AudioPlayerListener listener;

    private int currentSuraId = 1;
    private int currentAyahId = 1;
    private int totalAyahsInSurah = 7;
    private String currentSuraName = "الفاتحة";
    private String reciterFolder = "Alafasy_128kbps";
    private String reciterName = "مشاري راشد العفاسي";

    private boolean autoPlayNext = true;
    private int maxRepeatCount = 1;
    private int currentRepeatCount = 0;

    public interface AudioPlayerListener {
        void onAyahChanged(int suraId, int ayahId, String suraName, String reciterName);
        void onPlaybackStateChanged(boolean isPlaying);
        void onError(String errorMessage, boolean isHttp404);
        void onPlayCompleted();
    }

    private QuranAudioPlayer(Context context) {
        this.context = context.getApplicationContext();
        initPlayer();
    }

    public static QuranAudioPlayer getInstance(Context context) {
        if (instance == null) {
            synchronized (QuranAudioPlayer.class) {
                if (instance == null) {
                    instance = new QuranAudioPlayer(context);
                }
            }
        }
        return instance;
    }

    private void initPlayer() {
        if (exoPlayer != null) {
            exoPlayer.release();
        }
        exoPlayer = new ExoPlayer.Builder(context).build();
        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (listener != null) {
                    listener.onPlaybackStateChanged(isPlaying);
                }
            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_ENDED) {
                    handleAyahCompletion();
                }
            }

            @Override
            public void onPlayerError(PlaybackException error) {
                boolean is404 = error.getCause() != null && error.getCause().getMessage() != null &&
                        (error.getCause().getMessage().contains("404") || error.getCause().getMessage().contains("Response code: 404"));

                String msg = is404 ? "التلاوة غير متاحة لهذه الآية بصوت القارئ المختار"
                        : "حدث خطأ أثناء تشغيل الصوت. يرجى التحقق من الاتصال بالإنترنت.";

                if (listener != null) {
                    listener.onError(msg, is404);
                }

                if (autoPlayNext && currentAyahId < totalAyahsInSurah) {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> nextAyah(), 1500);
                }
            }
        });
    }

    public void setListener(AudioPlayerListener listener) {
        this.listener = listener;
    }

    public void setReciter(String reciterName, String reciterFolder) {
        this.reciterName = reciterName;
        this.reciterFolder = reciterFolder;
    }

    public void setAutoPlayNext(boolean autoPlayNext) {
        this.autoPlayNext = autoPlayNext;
    }

    public void setMaxRepeatCount(int maxRepeatCount) {
        this.maxRepeatCount = Math.max(1, maxRepeatCount);
    }

    public void playAyah(int suraId, int ayahId, int totalAyahs, String suraName, String reciterName, String reciterFolder) {
        this.currentSuraId = suraId;
        this.currentAyahId = ayahId;
        this.totalAyahsInSurah = totalAyahs;
        this.currentSuraName = suraName;
        if (reciterName != null) this.reciterName = reciterName;
        if (reciterFolder != null) this.reciterFolder = reciterFolder;
        this.currentRepeatCount = 0;

        startPlaybackForCurrentAyah();
    }

    private void startPlaybackForCurrentAyah() {
        if (exoPlayer == null) {
            initPlayer();
        }
        String audioUrl = EveryAyahUrlHelper.buildAudioUrl(reciterFolder, currentSuraId, currentAyahId);
        MediaItem mediaItem = MediaItem.fromUri(audioUrl);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();

        if (listener != null) {
            listener.onAyahChanged(currentSuraId, currentAyahId, currentSuraName, reciterName);
        }
    }

    private void handleAyahCompletion() {
        currentRepeatCount++;
        if (currentRepeatCount < maxRepeatCount) {
            startPlaybackForCurrentAyah();
            return;
        }

        currentRepeatCount = 0;

        if (autoPlayNext && currentAyahId < totalAyahsInSurah) {
            currentAyahId++;
            startPlaybackForCurrentAyah();
        } else {
            if (listener != null) {
                listener.onPlayCompleted();
                listener.onPlaybackStateChanged(false);
            }
        }
    }

    public void nextAyah() {
        if (currentAyahId < totalAyahsInSurah) {
            currentAyahId++;
            currentRepeatCount = 0;
            startPlaybackForCurrentAyah();
        }
    }

    public void prevAyah() {
        if (currentAyahId > 1) {
            currentAyahId--;
            currentRepeatCount = 0;
            startPlaybackForCurrentAyah();
        }
    }

    public void pause() {
        if (exoPlayer != null && exoPlayer.isPlaying()) {
            exoPlayer.pause();
        }
    }

    public void resume() {
        if (exoPlayer != null) {
            exoPlayer.play();
        }
    }

    public void stop() {
        if (exoPlayer != null) {
            exoPlayer.stop();
        }
    }

    public boolean isPlaying() {
        return exoPlayer != null && exoPlayer.isPlaying();
    }

    public int getCurrentSuraId() {
        return currentSuraId;
    }

    public int getCurrentAyahId() {
        return currentAyahId;
    }

    public String getCurrentSuraName() {
        return currentSuraName;
    }

    public String getReciterName() {
        return reciterName;
    }

    public String getReciterFolder() {
        return reciterFolder;
    }

    public void release() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }
}
