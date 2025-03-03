package com.appwalied.quran.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.window.layout.WindowMetrics;
import androidx.window.layout.WindowMetricsCalculator;

import com.appwalied.quran.R;
import com.appwalied.quran.utils.shared_helper.SharedHelper;
import com.appwalied.quran.utils.shared_helper.views.CustomDialogClass;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.Random;

import guy4444.smartrate.SmartRate;

public abstract class BaseActivity extends AppCompatActivity implements IBaseView {
    private static final String TAG = "BaseActivity";
    private static final int MAX_PROMPT_COUNT = 10;
    private static final long ONE_DAY_MILLIS = 24 * 60 * 60 * 1000;
    private static final long AD_INTERVAL = 30 * 60 * 1000; // 30 دقيقة بالميللي ثانية
    private static final String PREFS_NAME = "app_prefs";
    private static final String LAST_AD_TIME_KEY = "LastAdTime";
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable ratingRunnable;
    private Dialog loadingDialog;
    private InterstitialAd mInterstitialAd;
    private SharedPreferences sharedPreferences;
    private AdView adView;
    private boolean initialLayoutComplete = false;
    private long currentTime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // تهيئة SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }

    public void checkAndShowDialog(String preferenceKey, String message) {
        boolean hasSeenDialog = SharedHelper.getBoolean(this, preferenceKey);
        if (!hasSeenDialog) {
            SharedHelper.putBoolean(this, preferenceKey, true);
            CustomDialogClass.Options options = new CustomDialogClass.Options();
            options.title = "ملاحظة هامة";
            options.message = message;
            CustomDialogClass customDialogClass = new CustomDialogClass(this, options);
            customDialogClass.show();
        } else {
            Log.d("DEBUG", "Dialog will not be shown as it has been seen before.");
        }
    }

    public Handler getHandler() {
        return handler;
    }


    @Override
    public void showLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            return;
        }

        if (isFinishing() || isDestroyed()) {
            return;
        }

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    @Override
    public void showMessage(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            runOnUiThread(() -> { // يضمن التنفيذ على UI Thread
                try {
                    Context context = loadingDialog.getContext();
                    if (context instanceof Activity activity) {
                        if (!activity.isFinishing() && !activity.isDestroyed()) {
                            loadingDialog.dismiss();
                        }
                    } else {
                        loadingDialog.dismiss();
                    }
                    loadingDialog = null;
                } catch (Exception e) {
                    Log.e(TAG, "خطأ أثناء إغلاق الـ Dialog", e);
                }
            });
        }
    }

    @Override
    public void promptUserForRating() {
        long lastPromptTime = sharedPreferences.getLong("last_rating_prompt", 0);
        int promptCount = sharedPreferences.getInt("rating_prompt_count", 0);
        long currentTime = System.currentTimeMillis();

        // Ensure the prompt count does not exceed the limit and at least 24 hours have passed
        if (promptCount < MAX_PROMPT_COUNT && (currentTime - lastPromptTime >= ONE_DAY_MILLIS)) {
            ratingRunnable = () -> {
                if (!isFinishing()) {
                    SmartRate.Rate(BaseActivity.this, "قيم تجربتك معنا!", "نحن نسعى لجعل تطبيق مُصحفي أفضل كل يوم، ويساعدنا تقييمك في تقديم تجربة مميزة لك!", "قيم الآن", "دعمك لنا يحفزنا! اترك لنا تقييماً رائعاً على جوجل بلاي", "اضغط هنا للتقييم", "ليس الآن", "شكراً لدعمك!", Color.parseColor("#1898AE"), 2);

                    // Update rating prompt data (last prompt time + prompt count)
                    saveLastPromptData(promptCount + 1);
                }
            };

            handler.postDelayed(ratingRunnable, 5000); // Execute after 5 seconds
        }
    }

    @Override
    public void saveLastPromptData(int newCount) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("last_rating_prompt", System.currentTimeMillis());
        editor.putInt("rating_prompt_count", newCount);
        editor.apply();
    }

    @Override
    public void setUpAds() {
        long lastAdTime = sharedPreferences.getLong(LAST_AD_TIME_KEY, 0);
        currentTime = System.currentTimeMillis();

        // التحقق مما إذا كان قد مر 30 دقيقة منذ آخر إعلان
        if ((currentTime - lastAdTime) < AD_INTERVAL) {
            Log.d("TAG", "Not enough time has passed since the last ad. Skipping ad load.");
            return;
        }

        String[] adIds = {getString(R.string.Biny1), getString(R.string.Biny2), getString(R.string.Biny3)};
        String selectedAdId = adIds[new Random().nextInt(adIds.length)];

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, selectedAdId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                getHandler().postDelayed(BaseActivity.this::loadAds, 4000);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.i("TAG", "Ad Failed: " + selectedAdId + " - " + loadAdError.getMessage());
                mInterstitialAd = null;
                sharedPreferences.edit().putLong(LAST_AD_TIME_KEY, currentTime).apply();
            }
        });
    }


    private void loadAds() {
        sharedPreferences.edit().putLong(LAST_AD_TIME_KEY, currentTime).apply();
        if (mInterstitialAd != null) {
            mInterstitialAd.show(BaseActivity.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }

    @Override
    public void showBanner(FrameLayout adsContainer) {
        if (adsContainer == null) return;
        adView = new AdView(this);
        adsContainer.addView(adView);
        adsContainer.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (!initialLayoutComplete) {
                initialLayoutComplete = true;
                String[] adIds = {getString(R.string.Banner1), getString(R.string.Banner2), getString(R.string.Banner3)};
                String selectedAdId = adIds[new Random().nextInt(adIds.length)];
                adView.setAdUnitId(selectedAdId);
                AdSize adSize = getAdSize(adsContainer);
                adView.setAdSize(adSize);
                AdRequest adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest);
            }
            });
    }

    private AdSize getAdSize(FrameLayout adsContainer) {
        WindowMetrics windowMetrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this);
        Rect bounds = windowMetrics.getBounds();

        float adWidthPixels = adsContainer.getWidth();

        if (adWidthPixels == 0f) {
            adWidthPixels = bounds.width();
        }

        float density = getResources().getDisplayMetrics().density;
        int adWidth = (int) (adWidthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    @Override
    protected void onDestroy() {
        if (ratingRunnable != null) {
            handler.removeCallbacks(ratingRunnable);
        }
        handler.removeCallbacksAndMessages(null);
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (adView != null) {
            adView.resume();
        }
        super.onResume();
    }
}
