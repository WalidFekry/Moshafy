package com.appwalied.quran.image_quran.player;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.OptIn;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;

import com.appwalied.quran.image_quran.data.ImageQuranConfig;

import java.io.File;

public class ImageQuranAudioPlayerManager {

    public interface PlayerListener {
        void onPageAudioChanged(int page, boolean isPlaying);
        void onPlaybackStateChanged(boolean isPlaying);
        void onProgressUpdate(long currentMs, long durationMs);
        void onPlaybackEnded(int page);
        void onError(String message);
    }

    private static volatile ImageQuranAudioPlayerManager instance;
    private final Context context;
    private ExoPlayer player;
    private PlayerListener listener;

    private int currentPage = -1;
    private boolean isLooping = false;
    private boolean isAutoAdvance = true;

    private final Handler progressHandler = new Handler(Looper.getMainLooper());
    private final Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            if (player != null && player.isPlaying()) {
                if (listener != null) {
                    listener.onProgressUpdate(player.getCurrentPosition(), player.getDuration());
                }
                progressHandler.postDelayed(this, 500);
            }
        }
    };

    private ImageQuranAudioPlayerManager(Context context) {
        this.context = context.getApplicationContext();
        initPlayer();
    }

    public static ImageQuranAudioPlayerManager getInstance(Context context) {
        if (instance == null) {
            synchronized (ImageQuranAudioPlayerManager.class) {
                if (instance == null) {
                    instance = new ImageQuranAudioPlayerManager(context);
                }
            }
        }
        return instance;
    }

    @OptIn(markerClass = UnstableApi.class)
    private void initPlayer() {
        if (player != null) return;
        player = new ExoPlayer.Builder(context).build();
        player.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (listener != null) {
                    listener.onPlaybackStateChanged(isPlaying);
                    listener.onPageAudioChanged(currentPage, isPlaying);
                }
                if (isPlaying) {
                    progressHandler.post(progressRunnable);
                } else {
                    progressHandler.removeCallbacks(progressRunnable);
                }
            }

            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_ENDED) {
                    progressHandler.removeCallbacks(progressRunnable);
                    if (isLooping) {
                        playPage(currentPage);
                    } else if (listener != null) {
                        listener.onPlaybackEnded(currentPage);
                        if (isAutoAdvance && currentPage < ImageQuranConfig.TOTAL_PAGES) {
                            playPage(currentPage + 1);
                        }
                    }
                }
            }

            @Override
            public void onPlayerError(PlaybackException error) {
                progressHandler.removeCallbacks(progressRunnable);
                if (listener != null) {
                    listener.onError("تعذر تشغيل الصوتية: " + error.getLocalizedMessage());
                }
            }
        });
    }

    public void setListener(PlayerListener listener) {
        this.listener = listener;
    }

    public void playPage(int page) {
        if (page < 1 || page > ImageQuranConfig.TOTAL_PAGES) return;
        initPlayer();
        this.currentPage = page;

        Uri uri;
        File localFile = ImageQuranConfig.getLocalAudioFile(context, page);
        if (localFile.exists() && localFile.length() > 0) {
            uri = Uri.fromFile(localFile);
        } else {
            uri = Uri.parse(ImageQuranConfig.getAudioUrl(page));
        }

        MediaItem mediaItem = MediaItem.fromUri(uri);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();

        if (listener != null) {
            listener.onPageAudioChanged(page, true);
        }
    }

    public void togglePlayPause() {
        if (player == null) return;
        if (player.isPlaying()) {
            player.pause();
        } else {
            if (player.getPlaybackState() == Player.STATE_ENDED) {
                player.seekTo(0);
            }
            player.play();
        }
    }

    public void pause() {
        if (player != null && player.isPlaying()) {
            player.pause();
        }
    }

    public void seekTo(long positionMs) {
        if (player != null) {
            player.seekTo(positionMs);
        }
    }

    public boolean isPlaying() {
        return player != null && player.isPlaying();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public boolean isLooping() {
        return isLooping;
    }

    public void setLooping(boolean looping) {
        isLooping = looping;
    }

    public boolean isAutoAdvance() {
        return isAutoAdvance;
    }

    public void setAutoAdvance(boolean autoAdvance) {
        isAutoAdvance = autoAdvance;
    }

    public long getCurrentPosition() {
        return player != null ? player.getCurrentPosition() : 0;
    }

    public long getDuration() {
        return player != null ? player.getDuration() : 0;
    }

    public void release() {
        progressHandler.removeCallbacks(progressRunnable);
        if (player != null) {
            player.release();
            player = null;
        }
        currentPage = -1;
    }
}
