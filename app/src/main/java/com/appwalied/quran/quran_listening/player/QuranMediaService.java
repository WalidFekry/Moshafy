package com.appwalied.quran.quran_listening.player;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.appwalied.quran.R;
import com.appwalied.quran.quran_listening.QuranListeningActivity;

public class QuranMediaService extends Service implements AudioPlayerManager.OnMediaServiceUpdateListener {

    public static final String CHANNEL_ID = "quran_listening_channel";
    public static final int NOTIFICATION_ID = 9001;

    public static final String ACTION_PLAY_PAUSE = "action_play_pause";
    public static final String ACTION_NEXT = "action_next";
    public static final String ACTION_PREV = "action_prev";
    public static final String ACTION_STOP = "action_stop";

    private static final String[] SURAH_NAMES_AR = {
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

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        AudioPlayerManager.getInstance(this).setServiceUpdateListener(this);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "تلاوات القرآن الكريم",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("إشعار تشغيل التلاوات القرآنية في الخلفية");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AudioPlayerManager playerManager = AudioPlayerManager.getInstance(this);

        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();

            if (ACTION_PLAY_PAUSE.equals(action)) {
                playerManager.togglePlayPause();
            } else if (ACTION_NEXT.equals(action)) {
                playerManager.playNext(SURAH_NAMES_AR);
            } else if (ACTION_PREV.equals(action)) {
                playerManager.playPrevious(SURAH_NAMES_AR);
            } else if (ACTION_STOP.equals(action)) {
                playerManager.stop();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    stopForeground(STOP_FOREGROUND_REMOVE);
                } else {
                    stopForeground(true);
                }
                stopSelf();
                return START_NOT_STICKY;
            }
        }

        updateNotification();
        return START_STICKY;
    }

    public void updateNotification() {
        AudioPlayerManager playerManager = AudioPlayerManager.getInstance(this);
        boolean isPlayWhenReady = playerManager.isPlayWhenReady();
        String title = "سورة " + playerManager.getCurrentSurahName();
        String reciter = playerManager.getCurrentReciterName();

        Intent openIntent = new Intent(this, QuranListeningActivity.class);
        PendingIntent pendingOpenIntent = PendingIntent.getActivity(
                this, 0, openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
        );

        Intent prevIntent = new Intent(this, QuranMediaService.class);
        prevIntent.setAction(ACTION_PREV);
        PendingIntent pendingPrev = PendingIntent.getService(
                this, 1, prevIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
        );

        Intent playPauseIntent = new Intent(this, QuranMediaService.class);
        playPauseIntent.setAction(ACTION_PLAY_PAUSE);
        PendingIntent pendingPlayPause = PendingIntent.getService(
                this, 2, playPauseIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
        );

        Intent nextIntent = new Intent(this, QuranMediaService.class);
        nextIntent.setAction(ACTION_NEXT);
        PendingIntent pendingNext = PendingIntent.getService(
                this, 3, nextIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
        );

        Intent stopIntent = new Intent(this, QuranMediaService.class);
        stopIntent.setAction(ACTION_STOP);
        PendingIntent pendingStop = PendingIntent.getService(
                this, 4, stopIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ico_app)
                .setContentTitle(title)
                .setContentText("القارئ: " + reciter)
                .setContentIntent(pendingOpenIntent)
                .setOngoing(isPlayWhenReady)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .addAction(R.drawable.ic_skip_previous, "السابقة", pendingPrev)
                .addAction(
                        isPlayWhenReady ? R.drawable.ic_pause : R.drawable.ic_play_arrow,
                        isPlayWhenReady ? "إيقاف مؤقت" : "تشغيل",
                        pendingPlayPause
                )
                .addAction(R.drawable.ic_skip_next, "التالية", pendingNext)
                .addAction(R.drawable.ic_close, "إغلاق", pendingStop);

        startForeground(NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onMediaStateUpdated() {
        updateNotification();
    }

    @Override
    public void onDestroy() {
        AudioPlayerManager.getInstance(this).setServiceUpdateListener(null);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
