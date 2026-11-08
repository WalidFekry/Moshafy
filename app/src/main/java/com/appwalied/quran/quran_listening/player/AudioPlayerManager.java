package com.appwalied.quran.quran_listening.player;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AudioPlayerManager implements Player.Listener {

    private static volatile AudioPlayerManager instance;
    private final Context context;
    private ExoPlayer player;
    private OnPlayerStateChangeListener listener;
    private OnMediaServiceUpdateListener serviceUpdateListener;

    private int currentSurahId = 1;
    private String currentSurahName = "الفاتحة";
    private int currentReciterId = 1;
    private String currentReciterName = "أبو بكر الشاطري";
    private String currentServerUrl = "https://server11.mp3quran.net/shatri/";
    private List<Integer> availableSurahIds = new ArrayList<>();

    private int repeatMode = 0; // 0 = Off, 1 = Repeat One, 2 = Repeat All
    private float playbackSpeed = 1.0f;

    private final Handler progressHandler = new Handler(Looper.getMainLooper());
    private final Runnable updateProgressTask;

    public interface OnPlayerStateChangeListener {
        void onSurahChanged(int surahId, String surahName, String reciterName);
        void onPlaybackStateChanged(boolean isPlaying, boolean isBuffering);
        void onProgressUpdate(long currentPositionMs, long durationMs);
        void onPlaybackCompleted();
        void onError(String errorMessage);
    }

    public interface OnMediaServiceUpdateListener {
        void onMediaStateUpdated();
    }

    private AudioPlayerManager(Context context) {
        this.context = context.getApplicationContext();
        initPlayer();

        updateProgressTask = new Runnable() {
            @Override
            public void run() {
                if (player != null) {
                    long current = Math.max(0, player.getCurrentPosition());
                    long duration = Math.max(0, player.getDuration());
                    if (listener != null) {
                        listener.onProgressUpdate(current, duration);
                    }
                    if (player.isPlaying()) {
                        progressHandler.postDelayed(this, 500);
                    }
                }
            }
        };
    }

    public static AudioPlayerManager getInstance(Context context) {
        if (instance == null) {
            synchronized (AudioPlayerManager.class) {
                if (instance == null) {
                    instance = new AudioPlayerManager(context);
                }
            }
        }
        return instance;
    }

    @OptIn(markerClass = UnstableApi.class)
    private void initPlayer() {
        if (player == null) {
            player = new ExoPlayer.Builder(context).build();
            player.addListener(this);
            player.setPlaybackParameters(new PlaybackParameters(playbackSpeed));
        }
    }

    public void setListener(OnPlayerStateChangeListener listener) {
        this.listener = listener;
    }

    public void setServiceUpdateListener(OnMediaServiceUpdateListener serviceUpdateListener) {
        this.serviceUpdateListener = serviceUpdateListener;
    }

    public void playSurah(int surahId, String surahName, int reciterId, String reciterName, String serverUrl, List<Integer> availableSurahIds) {
        initPlayer();

        this.currentSurahId = surahId;
        this.currentSurahName = surahName;
        this.currentReciterId = reciterId;
        this.currentReciterName = reciterName;

        if (serverUrl != null && !serverUrl.trim().isEmpty()) {
            this.currentServerUrl = serverUrl.endsWith("/") ? serverUrl : serverUrl + "/";
        }
        if (availableSurahIds != null) {
            this.availableSurahIds = availableSurahIds;
        }

        String audioUrl = this.currentServerUrl + String.format(Locale.US, "%03d.mp3", surahId);

        MediaItem mediaItem = MediaItem.fromUri(audioUrl);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true);

        if (listener != null) {
            listener.onSurahChanged(surahId, surahName, reciterName);
            listener.onPlaybackStateChanged(true, true);
        }

        if (serviceUpdateListener != null) {
            serviceUpdateListener.onMediaStateUpdated();
        }

        startProgressUpdates();
    }

    public void togglePlayPause() {
        if (player == null) return;

        if (player.isPlaying()) {
            pause();
        } else {
            resume();
        }
    }

    public void resume() {
        if (player != null) {
            player.play();
            startProgressUpdates();
            if (listener != null) {
                listener.onPlaybackStateChanged(true, false);
            }
            if (serviceUpdateListener != null) {
                serviceUpdateListener.onMediaStateUpdated();
            }
        }
    }

    public void pause() {
        if (player != null) {
            player.pause();
            stopProgressUpdates();
            if (listener != null) {
                listener.onPlaybackStateChanged(false, false);
            }
            if (serviceUpdateListener != null) {
                serviceUpdateListener.onMediaStateUpdated();
            }
        }
    }

    public void stop() {
        if (player != null) {
            player.stop();
            stopProgressUpdates();
            if (listener != null) {
                listener.onPlaybackStateChanged(false, false);
            }
            if (serviceUpdateListener != null) {
                serviceUpdateListener.onMediaStateUpdated();
            }
        }
    }

    public void seekTo(long positionMs) {
        if (player != null) {
            player.seekTo(positionMs);
        }
    }

    public void seekForward(long deltaMs) {
        if (player != null) {
            long duration = Math.max(0, player.getDuration());
            long newPos = duration > 0 ? Math.min(duration, player.getCurrentPosition() + deltaMs) : player.getCurrentPosition() + deltaMs;
            player.seekTo(newPos);
        }
    }

    public void seekRewind(long deltaMs) {
        if (player != null) {
            long newPos = Math.max(0, player.getCurrentPosition() - deltaMs);
            player.seekTo(newPos);
        }
    }

    public void playNext(String[] surahNames) {
        if (availableSurahIds == null || availableSurahIds.isEmpty()) {
            if (currentSurahId < 114) {
                int nextId = currentSurahId + 1;
                String nextName = (surahNames != null && nextId <= surahNames.length) ? surahNames[nextId - 1] : "سورة " + nextId;
                playSurah(nextId, nextName, currentReciterId, currentReciterName, currentServerUrl, availableSurahIds);
            }
            return;
        }

        int currentIndexInList = availableSurahIds.indexOf(currentSurahId);
        if (currentIndexInList != -1 && currentIndexInList < availableSurahIds.size() - 1) {
            int nextId = availableSurahIds.get(currentIndexInList + 1);
            String nextName = (surahNames != null && nextId <= surahNames.length) ? surahNames[nextId - 1] : "سورة " + nextId;
            playSurah(nextId, nextName, currentReciterId, currentReciterName, currentServerUrl, availableSurahIds);
        } else if (repeatMode == 2 && !availableSurahIds.isEmpty()) {
            int firstId = availableSurahIds.get(0);
            String firstName = (surahNames != null && firstId <= surahNames.length) ? surahNames[firstId - 1] : "سورة " + firstId;
            playSurah(firstId, firstName, currentReciterId, currentReciterName, currentServerUrl, availableSurahIds);
        }
    }

    public void playPrevious(String[] surahNames) {
        if (availableSurahIds == null || availableSurahIds.isEmpty()) {
            if (currentSurahId > 1) {
                int prevId = currentSurahId - 1;
                String prevName = (surahNames != null && prevId <= surahNames.length) ? surahNames[prevId - 1] : "سورة " + prevId;
                playSurah(prevId, prevName, currentReciterId, currentReciterName, currentServerUrl, availableSurahIds);
            }
            return;
        }

        int currentIndexInList = availableSurahIds.indexOf(currentSurahId);
        if (currentIndexInList > 0) {
            int prevId = availableSurahIds.get(currentIndexInList - 1);
            String prevName = (surahNames != null && prevId <= surahNames.length) ? surahNames[prevId - 1] : "سورة " + prevId;
            playSurah(prevId, prevName, currentReciterId, currentReciterName, currentServerUrl, availableSurahIds);
        }
    }

    public void setRepeatMode(int mode) {
        this.repeatMode = mode;
    }

    public int getRepeatMode() {
        return repeatMode;
    }

    public void setPlaybackSpeed(float speed) {
        this.playbackSpeed = speed;
        if (player != null) {
            player.setPlaybackParameters(new PlaybackParameters(speed));
        }
    }

    public float getPlaybackSpeed() {
        return playbackSpeed;
    }

    public boolean isPlaying() {
        return player != null && player.isPlaying();
    }

    public boolean isPlayWhenReady() {
        return player != null && player.getPlayWhenReady();
    }

    public boolean hasMediaItem() {
        return player != null && player.getMediaItemCount() > 0;
    }

    public long getCurrentPositionMs() {
        return player != null ? player.getCurrentPosition() : 0;
    }

    public long getDurationMs() {
        return player != null ? player.getDuration() : 0;
    }

    public int getCurrentSurahId() {
        return currentSurahId;
    }

    public String getCurrentSurahName() {
        return currentSurahName;
    }

    public int getCurrentReciterId() {
        return currentReciterId;
    }

    public String getCurrentReciterName() {
        return currentReciterName;
    }

    public String getCurrentServerUrl() {
        return currentServerUrl;
    }

    public List<Integer> getAvailableSurahIds() {
        return availableSurahIds;
    }

    public void startProgressUpdates() {
        progressHandler.removeCallbacks(updateProgressTask);
        progressHandler.post(updateProgressTask);
    }

    public void stopProgressUpdates() {
        progressHandler.removeCallbacks(updateProgressTask);
    }

    // Player.Listener Callbacks
    @Override
    public void onIsPlayingChanged(boolean isPlaying) {
        boolean isBuffering = (player != null && player.getPlaybackState() == Player.STATE_BUFFERING);
        if (listener != null) {
            listener.onPlaybackStateChanged(isPlaying, isBuffering);
        }
        if (serviceUpdateListener != null) {
            serviceUpdateListener.onMediaStateUpdated();
        }
    }

    @Override
    public void onPlaybackStateChanged(int playbackState) {
        boolean isBuffering = (playbackState == Player.STATE_BUFFERING);
        boolean isPlaying = (playbackState == Player.STATE_READY && player.getPlayWhenReady());

        if (listener != null) {
            listener.onPlaybackStateChanged(isPlaying, isBuffering);
        }

        if (serviceUpdateListener != null) {
            serviceUpdateListener.onMediaStateUpdated();
        }

        if (isPlaying) {
            startProgressUpdates();
        }

        if (playbackState == Player.STATE_ENDED) {
            stopProgressUpdates();
            if (listener != null) {
                listener.onPlaybackCompleted();
            }
            if (repeatMode == 1) { // Repeat current Surah
                playSurah(currentSurahId, currentSurahName, currentReciterId, currentReciterName, currentServerUrl, availableSurahIds);
            }
        }
    }

    @Override
    public void onPlayerError(@NonNull PlaybackException error) {
        stopProgressUpdates();
        String errorMsg = "حدث خطأ أثناء تشغيل التلاوة، يرجى التأكد من الاتصال بالإنترنت";
        if (listener != null) {
            listener.onError(errorMsg);
        }
        if (serviceUpdateListener != null) {
            serviceUpdateListener.onMediaStateUpdated();
        }
    }

    public void release() {
        stopProgressUpdates();
        if (player != null) {
            player.removeListener(this);
            player.release();
            player = null;
        }
    }
}
