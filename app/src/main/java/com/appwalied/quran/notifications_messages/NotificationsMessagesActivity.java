package com.appwalied.quran.notifications_messages;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.notifications_messages.adapter.NotificationAdapter;
import com.appwalied.quran.notifications_messages.model.NotificationModel;
import com.appwalied.quran.notifications_messages.model.NotificationResponse;
import com.appwalied.quran.notifications_messages.repository.NotificationRepository;
import com.appwalied.quran.notifications_messages.utility.SavedQuotesManager;
import com.appwalied.quran.utils.shared_helper.SharedPrefsConstants;

import java.util.ArrayList;
import java.util.List;

public class NotificationsMessagesActivity extends BaseActivity {
    private static final String TAG = "NotificationsMessages";
    private RecyclerView rec;
    private NotificationAdapter adapter;
    private LinearLayout noInternetLayout;
    private LinearLayout offlineBannerLayout;
    private AppCompatButton retryButton;
    private AppCompatButton btnTabAll;
    private AppCompatButton btnTabSaved;
    private FrameLayout adsContainer;

    private List<NotificationModel> currentOnlineList = new ArrayList<>();
    private boolean isShowingSavedTab = false;

    private final NotificationRepository repository = new NotificationRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_messages);

        checkAndShowDialog(SharedPrefsConstants.NOTIFICATIONS_MESSAGES_FIRST_TIME, "في هذا القسم، ستجد اقتباسات عشوائية تلهمك وتضيف لمسة من الحكمة إلى يومك 💙");

        initViews();
        setupRecyclerView();
        setupTabs();
        showLoading();
        fetchNotificationsMessages();

        showBanner(adsContainer);
        setUpAds();
    }

    private void initViews() {
        adsContainer = findViewById(R.id.adsContainer);
        AppCompatImageButton back = findViewById(R.id.back_button);
        rec = findViewById(R.id.rec);
        noInternetLayout = findViewById(R.id.no_internet_layout);
        offlineBannerLayout = findViewById(R.id.layout_offline_banner);
        retryButton = findViewById(R.id.retry_button);
        btnTabAll = findViewById(R.id.btn_tab_all);
        btnTabSaved = findViewById(R.id.btn_tab_saved);

        if (back != null) {
            back.setOnClickListener(v -> finish());
        }
        if (retryButton != null) {
            retryButton.setOnClickListener(v -> {
                showLoading();
                noInternetLayout.setVisibility(View.GONE);
                fetchNotificationsMessages();
            });
        }
    }

    private void setupTabs() {
        if (btnTabAll != null && btnTabSaved != null) {
            btnTabAll.setOnClickListener(v -> switchToAllTab());
            btnTabSaved.setOnClickListener(v -> switchToSavedTab());
        }
    }

    private void switchToAllTab() {
        isShowingSavedTab = false;
        btnTabAll.setBackgroundResource(R.drawable.buoo);
        btnTabSaved.setBackgroundResource(R.drawable.buoo1);

        if (!currentOnlineList.isEmpty()) {
            rec.setVisibility(View.VISIBLE);
            noInternetLayout.setVisibility(View.GONE);
            updateRecyclerView(currentOnlineList);
        } else {
            List<NotificationModel> cached = SavedQuotesManager.getCachedQuotes(this);
            if (!cached.isEmpty()) {
                currentOnlineList = cached;
                offlineBannerLayout.setVisibility(View.VISIBLE);
                rec.setVisibility(View.VISIBLE);
                noInternetLayout.setVisibility(View.GONE);
                updateRecyclerView(cached);
            } else {
                showNoInternet();
            }
        }
    }

    private void switchToSavedTab() {
        isShowingSavedTab = true;
        btnTabSaved.setBackgroundResource(R.drawable.buoo);
        btnTabAll.setBackgroundResource(R.drawable.buoo1);

        List<NotificationModel> savedList = SavedQuotesManager.getSavedQuotes(this);
        if (savedList.isEmpty()) {
            Toast.makeText(this, "لا توجد اقتباسات محفوظة بعد 🔖", Toast.LENGTH_SHORT).show();
        }
        rec.setVisibility(View.VISIBLE);
        noInternetLayout.setVisibility(View.GONE);
        updateRecyclerView(savedList);
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
                hideLoading();
                offlineBannerLayout.setVisibility(View.GONE);
                noInternetLayout.setVisibility(View.GONE);
                rec.setVisibility(View.VISIBLE);

                if (notifications != null && notifications.getPosts() != null) {
                    currentOnlineList = notifications.getPosts();
                    SavedQuotesManager.cacheQuotes(getApplicationContext(), currentOnlineList);
                    if (!isShowingSavedTab) {
                        updateRecyclerView(currentOnlineList);
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                hideLoading();
                Log.e(TAG, "Fetch failure: " + errorMessage);

                List<NotificationModel> cached = SavedQuotesManager.getCachedQuotes(getApplicationContext());
                if (!cached.isEmpty()) {
                    currentOnlineList = cached;
                    offlineBannerLayout.setVisibility(View.VISIBLE);
                    rec.setVisibility(View.VISIBLE);
                    noInternetLayout.setVisibility(View.GONE);
                    if (!isShowingSavedTab) {
                        updateRecyclerView(cached);
                    }
                } else {
                    List<NotificationModel> savedList = SavedQuotesManager.getSavedQuotes(getApplicationContext());
                    if (!savedList.isEmpty()) {
                        switchToSavedTab();
                    } else {
                        showNoInternet();
                    }
                }
            }
        });
    }

    private void showNoInternet() {
        rec.setVisibility(View.GONE);
        offlineBannerLayout.setVisibility(View.GONE);
        noInternetLayout.setVisibility(View.VISIBLE);
    }

    private void updateRecyclerView(List<NotificationModel> notifications) {
        adapter = new NotificationAdapter(notifications);
        adapter.setOnSavedStateChangedListener(() -> {
            if (isShowingSavedTab) {
                updateRecyclerView(SavedQuotesManager.getSavedQuotes(this));
            }
        });
        rec.setAdapter(adapter);
    }
}
