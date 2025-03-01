package com.appwalied.quran.notifications_messages;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.appwalied.quran.R;
import com.appwalied.quran.Splash;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.notifications_messages.adapter.NotificationAdapter;
import com.appwalied.quran.notifications_messages.model.NotificationModel;
import com.appwalied.quran.notifications_messages.model.NotificationResponse;
import com.appwalied.quran.notifications_messages.repository.NotificationRepository;
import com.appwalied.quran.utils.shared_helper.SharedHelper;
import com.appwalied.quran.utils.shared_helper.SharedPrefsConstants;
import com.appwalied.quran.utils.shared_helper.views.CustomDialogClass;

import java.util.List;

public class NotificationsMessagesActivity extends BaseActivity {
    private static final String TAG = "NotificationsMessages";
    private RecyclerView rec;
    private NotificationAdapter adapter;
    private Dialog loadingDialog;
    private LinearLayout noInternetLayout;
    private AppCompatButton retryButton;
    private FrameLayout adsContainer;

    private final NotificationRepository repository = new NotificationRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_messages);

        checkAndShowDialog(SharedPrefsConstants.NOTIFICATIONS_MESSAGES_FIRST_TIME,"في هذا القسم، ستجد اقتباسات عشوائية تلهمك وتضيف لمسة من الحكمة إلى يومك 💙");

        initViews();
        setupRecyclerView();
        showLoading();
        fetchNotificationsMessages();

        showBanner(adsContainer);
        setUpAds();
        getHandler().postDelayed(this::loadAds, 4000);
    }

    private void initViews() {
        adsContainer = findViewById(R.id.adsContainer);
        AppCompatImageButton back = findViewById(R.id.back_button);
        rec = findViewById(R.id.rec);
        noInternetLayout = findViewById(R.id.no_internet_layout);
        retryButton = findViewById(R.id.retry_button);
        back.setOnClickListener(v -> finish());
        retryButton.setOnClickListener(v -> {
            startActivity(new Intent(this, Splash.class));
            finish();
        });
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        rec.setLayoutManager(layoutManager);
        rec.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation));
    }

    private void fetchNotificationsMessages() {
        repository.fetchNotifications(new NotificationRepository.OnNotificationsFetchedListener() {
            @Override
            public void onSuccess(NotificationResponse notifications) {
                updateRecyclerView(notifications.getPosts());
                Log.d(TAG, "Fetched " + notifications.getPosts().size() + " notifications");
            }

            @Override
            public void onFailure(String errorMessage) {
                hideLoading();
                Log.e(TAG, "Error: " + errorMessage);
                showNoInternet();
            }
        });
    }

    private void showNoInternet() {
       rec.setVisibility(View.GONE);
       noInternetLayout.setVisibility(View.VISIBLE);
    }

    private void updateRecyclerView(List<NotificationModel> notifications) {
        if (adapter == null) {
            adapter = new NotificationAdapter(notifications);
            rec.setAdapter(adapter);
        }
      hideLoading();
    }
}
