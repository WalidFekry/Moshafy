package com.appwalied.quran.ads;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.window.layout.WindowMetrics;
import androidx.window.layout.WindowMetricsCalculator;

import com.appwalied.quran.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AdsManager {

    private static final String TAG = "AdsManager";
    private static volatile AdsManager instance;

    private InterstitialAd mInterstitialAd;
    private boolean isInterstitialLoading = false;
    private long lastInterstitialShowTime = 0;

    private final Map<Integer, AdView> activeAdViews = new HashMap<>();

    public interface OnAdClosedListener {
        void onAdClosed();
    }

    private AdsManager() {}

    public static AdsManager getInstance() {
        if (instance == null) {
            synchronized (AdsManager.class) {
                if (instance == null) {
                    instance = new AdsManager();
                }
            }
        }
        return instance;
    }

    // ==========================================
    // ADAPTIVE BANNER ADS MANAGEMENT
    // ==========================================

    public void loadAdaptiveBanner(@NonNull Activity activity, @Nullable FrameLayout container, @Nullable String adUnitId) {
        if (container == null || activity.isFinishing() || activity.isDestroyed()) {
            return;
        }

        int containerKey = activity.hashCode();

        // If an AdView already exists in this activity container, reuse it
        AdView existingAdView = activeAdViews.get(containerKey);
        if (existingAdView != null && existingAdView.getParent() == container) {
            return;
        }

        // Determine Ad Unit ID
        String finalAdUnitId = adUnitId;
        if (finalAdUnitId == null || finalAdUnitId.trim().isEmpty()) {
            String[] defaultBannerIds = {
                    activity.getString(R.string.Banner1),
                    activity.getString(R.string.Banner2),
                    activity.getString(R.string.Banner3)
            };
            finalAdUnitId = defaultBannerIds[new Random().nextInt(defaultBannerIds.length)];
        }

        String targetAdUnitId = finalAdUnitId;

        container.post(() -> {
            if (activity.isFinishing() || activity.isDestroyed()) return;

            AdView adView = new AdView(activity);
            adView.setAdUnitId(targetAdUnitId);

            AdSize adSize = getAdaptiveAdSize(activity, container);
            adView.setAdSize(adSize);

            container.removeAllViews();
            container.addView(adView);

            activeAdViews.put(containerKey, adView);

            AdRequest adRequest = new AdRequest.Builder().build();
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    Log.d(TAG, "Adaptive Banner loaded successfully for " + activity.getClass().getSimpleName());
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    Log.e(TAG, "Adaptive Banner failed to load: " + loadAdError.getMessage());
                }
            });

            adView.loadAd(adRequest);
        });
    }

    private AdSize getAdaptiveAdSize(@NonNull Activity activity, @NonNull ViewGroup container) {
        WindowMetrics windowMetrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(activity);
        Rect bounds = windowMetrics.getBounds();

        float adWidthPixels = container.getWidth();
        if (adWidthPixels == 0f) {
            adWidthPixels = bounds.width();
        }

        float density = activity.getResources().getDisplayMetrics().density;
        int adWidth = (int) (adWidthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

    public void onPauseActivity(@NonNull Activity activity) {
        AdView adView = activeAdViews.get(activity.hashCode());
        if (adView != null) {
            adView.pause();
        }
    }

    public void onResumeActivity(@NonNull Activity activity) {
        AdView adView = activeAdViews.get(activity.hashCode());
        if (adView != null) {
            adView.resume();
        }
    }

    public void onDestroyActivity(@NonNull Activity activity) {
        int key = activity.hashCode();
        AdView adView = activeAdViews.remove(key);
        if (adView != null) {
            adView.destroy();
        }
    }

    // ==========================================
    // INTERSTITIAL ADS MANAGEMENT
    // ==========================================

    public void preloadInterstitialAd(@NonNull Context context, @Nullable String adUnitId) {
        if (mInterstitialAd != null || isInterstitialLoading) {
            return;
        }

        String finalAdUnitId = adUnitId;
        if (finalAdUnitId == null || finalAdUnitId.trim().isEmpty()) {
            String[] defaultBinyIds = {
                    context.getString(R.string.Biny1),
                    context.getString(R.string.Biny2),
                    context.getString(R.string.Biny3)
            };
            finalAdUnitId = defaultBinyIds[new Random().nextInt(defaultBinyIds.length)];
        }

        isInterstitialLoading = true;
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(context.getApplicationContext(), finalAdUnitId, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        isInterstitialLoading = false;
                        mInterstitialAd.show((Activity) context);
                        Log.d(TAG, "Interstitial Ad loaded successfully.");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                        isInterstitialLoading = false;
                        Log.e(TAG, "Interstitial Ad failed to load: " + loadAdError.getMessage());
                    }
                });
    }

    public void showInterstitialAdIfReady(@NonNull Activity activity, @Nullable OnAdClosedListener listener) {
        if (mInterstitialAd != null) {
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    mInterstitialAd = null;
                    preloadInterstitialAd(activity, null);
                    if (listener != null) listener.onAdClosed();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    mInterstitialAd = null;
                    preloadInterstitialAd(activity, null);
                    if (listener != null) listener.onAdClosed();
                }
            });
            mInterstitialAd.show(activity);
        } else {
            preloadInterstitialAd(activity, null);
            if (listener != null) listener.onAdClosed();
        }
    }
}
