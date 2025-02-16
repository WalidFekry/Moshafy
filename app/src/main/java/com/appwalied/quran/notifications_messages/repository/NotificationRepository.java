package com.appwalied.quran.notifications_messages.repository;

import androidx.annotation.NonNull;


import com.appwalied.quran.notifications_messages.api.NotificationApiService;
import com.appwalied.quran.notifications_messages.model.NotificationResponse;
import com.appwalied.quran.notifications_messages.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NotificationRepository {

    private final NotificationApiService apiService;

    public NotificationRepository() {
        Retrofit retrofit = RetrofitClient.getClient();
        apiService = retrofit.create(NotificationApiService.class);
    }

    public void fetchNotifications(final OnNotificationsFetchedListener listener) {
        Call<NotificationResponse> call = apiService.getAllNotificationsMessages();
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(@NonNull Call<NotificationResponse> call, @NonNull Response<NotificationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure("Response is empty or unsuccessful");
                }
            }

            @Override
            public void onFailure(@NonNull Call<NotificationResponse> call, @NonNull Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    public interface OnNotificationsFetchedListener {
        void onSuccess(NotificationResponse notifications);
        void onFailure(String errorMessage);
    }
}
