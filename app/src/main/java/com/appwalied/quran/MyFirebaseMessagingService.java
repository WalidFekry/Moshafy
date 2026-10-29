package com.appwalied.quran;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final int DAILY_NOTIFICATION_CODE = 1525;
    private static final String CHANNEL_ID = "Remote_notification_fire_base_new";
    private static final String RANDOM_KEY = "OpenName";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String title = null;
        String body = null;

        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();
        } else if (remoteMessage.getData() != null && !remoteMessage.getData().isEmpty()) {
            title = remoteMessage.getData().get("title");
            body = remoteMessage.getData().get("body");
        }

        if (title == null || title.trim().isEmpty()) {
            title = "مُصحفي";
        }
        if (body == null) {
            body = "";
        }

        Class<?> targetActivity = null;
        if (remoteMessage.getData() != null && remoteMessage.getData().containsKey(RANDOM_KEY)) {
            String className = remoteMessage.getData().get(RANDOM_KEY);
            if (className != null && !className.trim().isEmpty()) {
                try {
                    targetActivity = Class.forName(className);
                } catch (ClassNotFoundException ignored) {
                    targetActivity = null;
                }
            }
        }

        if (targetActivity != null) {
            sendNotification(title, body, targetActivity);
        } else {
            sendNotification(title, body);
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "إشعارات مصحفي";
            String description = "القناة الخاصة بالرسائل التقديرية والاقتباسات اليومية";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private PendingIntent getPendingIntent(Intent intent) {
        int flags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
        return PendingIntent.getActivity(getApplicationContext(), DAILY_NOTIFICATION_CODE, intent, flags);
    }

    private void sendNotification(String title, String messageBody, Class<?> name) {
        Intent intent = new Intent(getApplicationContext(), name);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = getPendingIntent(intent);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.ico_app)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
        }
    }

    private void sendNotification(String title, String messageBody) {
        sendNotification(title, messageBody, MainActivity.class);
    }
}
