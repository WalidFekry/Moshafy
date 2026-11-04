package com.appwalied.quran.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.appwalied.quran.R;
import com.appwalied.quran.ads.AdsManager;
import com.appwalied.quran.utils.shared_helper.SharedHelper;
import com.appwalied.quran.utils.shared_helper.SharedPrefsConstants;
import com.appwalied.quran.utils.shared_helper.views.CustomDialogClass;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import guy4444.smartrate.SmartRate;

public abstract class BaseActivity extends AppCompatActivity implements IBaseView {
    private static final String TAG = "BaseActivity";
    private static final int MAX_PROMPT_COUNT = 10;
    private static final long ONE_DAY_MILLIS = 24 * 60 * 60 * 1000;
    private static final String PREFS_NAME = "app_prefs";
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable ratingRunnable;
    private Dialog loadingDialog;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        applyAppTheme();
        super.onCreate(savedInstanceState);
        // تهيئة SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }

    private void applyAppTheme() {
        int mode = SharedHelper.getInt(this, SharedPrefsConstants.APP_THEME_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    public void showThemeChooserDialog() {
        String[] options = {"تلقائي (حسب إعدادات النظام)", "الوضع النهاري ☀️", "الوضع الليلي 🌙"};
        int currentMode = SharedHelper.getInt(this, SharedPrefsConstants.APP_THEME_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        int checkedItem = 0;
        if (currentMode == AppCompatDelegate.MODE_NIGHT_NO) {
            checkedItem = 1;
        } else if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
            checkedItem = 2;
        }

        new AlertDialog.Builder(this).setTitle("اختر المظهر 🎨").setSingleChoiceItems(options, checkedItem, (dialog, which) -> {
            int selectedMode;
            if (which == 1) {
                selectedMode = AppCompatDelegate.MODE_NIGHT_NO;
            } else if (which == 2) {
                selectedMode = AppCompatDelegate.MODE_NIGHT_YES;
            } else {
                selectedMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
            }
            SharedHelper.putInt(this, SharedPrefsConstants.APP_THEME_MODE, selectedMode);
            AppCompatDelegate.setDefaultNightMode(selectedMode);
            dialog.dismiss();
        }).setNegativeButton("إلغاء", null).show();
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
        AdsManager.getInstance().showInterstitialAdIfReady(this, null);
    }

    @Override
    public void showBanner(FrameLayout adsContainer) {
        AdsManager.getInstance().loadAdaptiveBanner(this, adsContainer, null);
    }

    @Override
    protected void onDestroy() {
        if (ratingRunnable != null) {
            handler.removeCallbacks(ratingRunnable);
        }
        handler.removeCallbacksAndMessages(null);
        AdsManager.getInstance().onDestroyActivity(this);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        AdsManager.getInstance().onPauseActivity(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        AdsManager.getInstance().onResumeActivity(this);
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle viewHierarchyState = outState.getBundle("android:viewHierarchyState");
        if (viewHierarchyState != null) {
            Parcel parcel = Parcel.obtain();
            try {
                viewHierarchyState.writeToParcel(parcel, 0);
                int bytes = parcel.dataSize();
                if (bytes > 100000) {
                    Log.w(TAG, "viewHierarchyState is dangerously large (" + bytes + " bytes). Removing to prevent TransactionTooLargeException.");
                    outState.remove("android:viewHierarchyState");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error measuring viewHierarchyState size", e);
                outState.remove("android:viewHierarchyState");
            } finally {
                parcel.recycle();
            }
        }
    }
}
