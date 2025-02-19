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

import com.appwalied.quran.MainActivity;
import com.appwalied.quran.R;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final int DAILY_NOTIFICATION_CODE = 1525;
    private static final String channel_id = "Remote_notification_fire_base_new";
    private static final String RANDOM_KEY = "OpenName";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().get("title") != null && !remoteMessage.getData().get("title").isEmpty()) {
            return;
        }
        Class<?> c = null;
        String name = remoteMessage.getData().get(RANDOM_KEY);
        try {
            c = Class.forName(name);
        } catch (ClassNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
        if (c != null) {
            sendNotification(Objects.requireNonNull(remoteMessage.getNotification()).getTitle(), remoteMessage.getNotification().getBody(), c);
        } else {
            sendNotification(Objects.requireNonNull(remoteMessage.getNotification()).getTitle(), remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "Channel for notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }

    private PendingIntent getPendingIntent(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return PendingIntent.getActivity(getApplicationContext(), DAILY_NOTIFICATION_CODE, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            return PendingIntent.getActivity(getApplicationContext(), DAILY_NOTIFICATION_CODE, intent, PendingIntent.FLAG_ONE_SHOT);
        }
    }

    private void sendNotification(String title, String messageBody, Class<?> name) {
        Intent intent = new Intent(getApplicationContext(), name);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = getPendingIntent(intent);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), channel_id).setSmallIcon(R.mipmap.ico_app).setContentTitle(title).setContentText(messageBody).setAutoCancel(true).setSound(defaultSoundUri).setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(1101, notificationBuilder.build());
    }

    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = getPendingIntent(intent);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), channel_id).setSmallIcon(R.mipmap.ico_app).setContentTitle(title).setContentText(messageBody).setAutoCancel(true).setSound(defaultSoundUri).setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(1101, notificationBuilder.build());
    }
}
