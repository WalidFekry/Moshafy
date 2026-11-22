package com.appwalied.quran.quran_listening.player;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.appwalied.quran.quran_listening.download.DownloadedSurahEntity;
import com.appwalied.quran.quran_listening.download.SurahDownloadManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AudioPlayerManager implements Player.Listener {

    public static final String[] SURAH_NAMES_AR = {
            "الفاتحة", "البقرة", "آل عمران", "النساء", "المائدة", "الأنعام", "الأعراف", "الأنفال", "التوبة", "يونس",
            "هود", "يوسف", "الرعد", "إبراهيم", "الحجر", "النحل", "الإسراء", "الكهف", "مريم", "طه",
            "الأنبياء", "الحج", "المؤمنون", "النور", "الفرقان", "الشعراء", "النمل", "القصص", "العنكبوت", "الروم",
            "لقمان", "السجدة", "الأحزاب", "سبأ", "فاطر", "يس", "الصافات", "ص", "الزمر", "غافر",
            "فصلت", "الشورى", "الزخرف", "الدخان", "الجاثية", "الأحقاف", "محمد", "الفتح", "الحجرات", "ق",
            "الذاريات", "الطور", "النجم", "القمر", "الرحمن", "الواقعة", "الحديد", "المجادلة", "الحشر", "الممتحنة",
            "الصف", "الجمعة", "المنافقون", "التغابن", "الطلاق", "التحريم", "الملك", "القلم", "الحاقة", "المعارج",
            "نوح", "الجن", "المزمل", "المدثر", "القيامة", "الإنسان", "المرسلات", "النبأ", "النازعات", "عبس",
            "التكوير", "الانفطار", "المطففين", "الانشقاق", "البروج", "الطارق", "الأعلى", "الغاشية", "الفجر", "البلد",
            "الشمس", "الليل", "الضحى", "الشرح", "التين", "العلق", "القدر", "البينة", "الزلزلة", "العاديات",
            "القارعة", "التكاثر", "العصر", "الهمزة", "الفيل", "قريش", "الماعون", "الكوثر", "الكافرون", "النصر",
            "المسد", "الإخلاص", "الفلق", "الناس"
    };

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
    private List<DownloadedSurahEntity> downloadedPlaylist = null;
    private int currentDownloadedIndex = -1;
    private boolean isCurrentSourceOffline = false;

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

        this.downloadedPlaylist = null;
        this.currentDownloadedIndex = -1;

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

        SurahDownloadManager downloadManager = SurahDownloadManager.getInstance(context);
        File localFile = downloadManager.getLocalFile(reciterId, surahId);
        boolean isLocalValid = localFile != null && localFile.exists() && localFile.length() >= 4096;

        String audioSource;
        if (isLocalValid) {
            audioSource = localFile.getAbsolutePath();
            this.isCurrentSourceOffline = true;
        } else {
            this.isCurrentSourceOffline = false;
            if (!isNetworkAvailable()) {
                stopProgressUpdates();
                String errMsg = "هذه السورة غير محملة وتتطلب اتصالاً بالإنترنت للتشغيل";
                if (listener != null) {
                    listener.onError(errMsg);
                }
                return;
            }
            audioSource = this.currentServerUrl + String.format(Locale.US, "%03d.mp3", surahId);
        }

        MediaItem mediaItem = MediaItem.fromUri(audioSource);
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

    public void playDownloadedPlaylist(List<DownloadedSurahEntity> playlist, int startIndex) {
        if (playlist == null || playlist.isEmpty()) return;
        this.downloadedPlaylist = new ArrayList<>(playlist);
        if (startIndex >= 0 && startIndex < playlist.size()) {
            this.currentDownloadedIndex = startIndex;
        } else {
            this.currentDownloadedIndex = 0;
        }
        playDownloadedSurahEntity(this.downloadedPlaylist.get(this.currentDownloadedIndex));
    }

    public void playDownloadedSurahEntity(DownloadedSurahEntity entity) {
        if (entity == null) return;
        initPlayer();

        this.currentSurahId = entity.getSurahId();
        this.currentSurahName = entity.getSurahName();
        this.currentReciterId = entity.getReciterId();
        this.currentReciterName = entity.getReciterName();
        if (entity.getRemoteUrl() != null && !entity.getRemoteUrl().trim().isEmpty()) {
            this.currentServerUrl = entity.getRemoteUrl().endsWith("/") ? entity.getRemoteUrl() : entity.getRemoteUrl() + "/";
        }

        SurahDownloadManager downloadManager = SurahDownloadManager.getInstance(context);
        File localFile = downloadManager.getLocalFile(entity.getReciterId(), entity.getSurahId());
        boolean isLocalValid = localFile != null && localFile.exists() && localFile.length() >= 4096;

        String audioSource;
        if (isLocalValid) {
            audioSource = localFile.getAbsolutePath();
            this.isCurrentSourceOffline = true;
        } else if (entity.getLocalFilePath() != null && !entity.getLocalFilePath().isEmpty() && new File(entity.getLocalFilePath()).exists()) {
            audioSource = entity.getLocalFilePath();
            this.isCurrentSourceOffline = true;
        } else {
            this.isCurrentSourceOffline = false;
            if (!isNetworkAvailable()) {
                stopProgressUpdates();
                String errMsg = "هذه السورة غير محملة وتتطلب اتصالاً بالإنترنت للتشغيل";
                if (listener != null) {
                    listener.onError(errMsg);
                }
                return;
            }
            audioSource = entity.getRemoteUrl();
        }

        MediaItem mediaItem = MediaItem.fromUri(audioSource);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true);

        if (listener != null) {
            listener.onSurahChanged(entity.getSurahId(), entity.getSurahName(), entity.getReciterName());
            listener.onPlaybackStateChanged(true, true);
        }

        if (serviceUpdateListener != null) {
            serviceUpdateListener.onMediaStateUpdated();
        }

        startProgressUpdates();
    }

    public boolean isCurrentSourceOffline() {
        return isCurrentSourceOffline;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
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

    public boolean isSurahDownloadedLocally(int reciterId, int surahId) {
        SurahDownloadManager downloadManager = SurahDownloadManager.getInstance(context);
        File localFile = downloadManager.getLocalFile(reciterId, surahId);
        return localFile != null && localFile.exists() && localFile.length() >= 4096;
    }

    public void playNext(String[] surahNames) {
        if (downloadedPlaylist != null && !downloadedPlaylist.isEmpty()) {
            if (currentDownloadedIndex >= 0 && currentDownloadedIndex < downloadedPlaylist.size() - 1) {
                currentDownloadedIndex++;
                playDownloadedSurahEntity(downloadedPlaylist.get(currentDownloadedIndex));
                return;
            } else if (repeatMode == 2 && !downloadedPlaylist.isEmpty()) {
                currentDownloadedIndex = 0;
                playDownloadedSurahEntity(downloadedPlaylist.get(currentDownloadedIndex));
                return;
            }
            return;
        }

        String[] names = surahNames != null ? surahNames : SURAH_NAMES_AR;
        if (availableSurahIds == null || availableSurahIds.isEmpty()) {
            if (currentSurahId < 114) {
                int nextId = currentSurahId + 1;
                String nextName = (nextId <= names.length) ? names[nextId - 1] : "سورة " + nextId;
                playSurah(nextId, nextName, currentReciterId, currentReciterName, currentServerUrl, availableSurahIds);
            }
            return;
        }

        boolean requireOfflineOnly = !isNetworkAvailable();
        int currentIndexInList = availableSurahIds.indexOf(currentSurahId);
        int targetIndex = -1;

        if (requireOfflineOnly) {
            int startIndex = currentIndexInList != -1 ? currentIndexInList + 1 : 0;
            for (int i = startIndex; i < availableSurahIds.size(); i++) {
                int id = availableSurahIds.get(i);
                if (isSurahDownloadedLocally(currentReciterId, id)) {
                    targetIndex = i;
                    break;
                }
            }
            if (targetIndex == -1 && (repeatMode == 2 || currentIndexInList != -1)) {
                int limit = currentIndexInList != -1 ? currentIndexInList : availableSurahIds.size();
                for (int i = 0; i < limit; i++) {
                    int id = availableSurahIds.get(i);
                    if (isSurahDownloadedLocally(currentReciterId, id)) {
                        targetIndex = i;
                        break;
                    }
                }
            }
        } else {
            if (currentIndexInList != -1 && currentIndexInList < availableSurahIds.size() - 1) {
                targetIndex = currentIndexInList + 1;
            } else if (repeatMode == 2 && !availableSurahIds.isEmpty()) {
                targetIndex = 0;
            }
        }

        if (targetIndex != -1 && targetIndex < availableSurahIds.size()) {
            int nextId = availableSurahIds.get(targetIndex);
            String nextName = (nextId <= names.length) ? names[nextId - 1] : "سورة " + nextId;
            playSurah(nextId, nextName, currentReciterId, currentReciterName, currentServerUrl, availableSurahIds);
        } else if (!requireOfflineOnly && currentSurahId < 114) {
            int nextId = currentSurahId + 1;
            String nextName = (nextId <= names.length) ? names[nextId - 1] : "سورة " + nextId;
            playSurah(nextId, nextName, currentReciterId, currentReciterName, currentServerUrl, availableSurahIds);
        }
    }

    public void playPrevious(String[] surahNames) {
        if (downloadedPlaylist != null && !downloadedPlaylist.isEmpty()) {
            if (currentDownloadedIndex > 0 && currentDownloadedIndex < downloadedPlaylist.size()) {
                currentDownloadedIndex--;
                playDownloadedSurahEntity(downloadedPlaylist.get(currentDownloadedIndex));
                return;
            } else if (repeatMode == 2 && !downloadedPlaylist.isEmpty()) {
                currentDownloadedIndex = downloadedPlaylist.size() - 1;
                playDownloadedSurahEntity(downloadedPlaylist.get(currentDownloadedIndex));
                return;
            }
            return;
        }

        String[] names = surahNames != null ? surahNames : SURAH_NAMES_AR;
        if (availableSurahIds == null || availableSurahIds.isEmpty()) {
            if (currentSurahId > 1) {
                int prevId = currentSurahId - 1;
                String prevName = (prevId <= names.length) ? names[prevId - 1] : "سورة " + prevId;
                playSurah(prevId, prevName, currentReciterId, currentReciterName, currentServerUrl, availableSurahIds);
            }
            return;
        }

        boolean requireOfflineOnly = !isNetworkAvailable();
        int currentIndexInList = availableSurahIds.indexOf(currentSurahId);
        int targetIndex = -1;

        if (requireOfflineOnly) {
            int startIndex = currentIndexInList > 0 ? currentIndexInList - 1 : availableSurahIds.size() - 1;
            for (int i = startIndex; i >= 0; i--) {
                int id = availableSurahIds.get(i);
                if (isSurahDownloadedLocally(currentReciterId, id)) {
                    targetIndex = i;
                    break;
                }
            }
            if (targetIndex == -1 && repeatMode == 2) {
                for (int i = availableSurahIds.size() - 1; i > currentIndexInList; i--) {
                    int id = availableSurahIds.get(i);
                    if (isSurahDownloadedLocally(currentReciterId, id)) {
                        targetIndex = i;
                        break;
                    }
                }
            }
        } else {
            if (currentIndexInList > 0) {
                targetIndex = currentIndexInList - 1;
            } else if (repeatMode == 2 && !availableSurahIds.isEmpty()) {
                targetIndex = availableSurahIds.size() - 1;
            }
        }

        if (targetIndex != -1 && targetIndex < availableSurahIds.size()) {
            int prevId = availableSurahIds.get(targetIndex);
            String prevName = (prevId <= names.length) ? names[prevId - 1] : "سورة " + prevId;
            playSurah(prevId, prevName, currentReciterId, currentReciterName, currentServerUrl, availableSurahIds);
        } else if (!requireOfflineOnly && currentSurahId > 1) {
            int prevId = currentSurahId - 1;
            String prevName = (prevId <= names.length) ? names[prevId - 1] : "سورة " + prevId;
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
                if (downloadedPlaylist != null && currentDownloadedIndex >= 0 && currentDownloadedIndex < downloadedPlaylist.size()) {
                    playDownloadedSurahEntity(downloadedPlaylist.get(currentDownloadedIndex));
                } else {
                    playSurah(currentSurahId, currentSurahName, currentReciterId, currentReciterName, currentServerUrl, availableSurahIds);
                }
            } else { // Auto-play next Surah in playlist (repeatMode 0 or 2)
                playNext(SURAH_NAMES_AR);
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
