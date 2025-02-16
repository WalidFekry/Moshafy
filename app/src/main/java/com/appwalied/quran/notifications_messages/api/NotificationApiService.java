package com.appwalied.quran.notifications_messages.api;


import com.appwalied.quran.notifications_messages.model.NotificationResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NotificationApiService {
    @GET("auto_notification/auto_notification_apps/api_post_apps.php")
    Call<NotificationResponse> getAllNotificationsMessages();
}
