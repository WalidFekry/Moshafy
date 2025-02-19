package com.appwalied.quran.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.appwalied.quran.R;

import guy4444.smartrate.SmartRate;

public abstract class BaseActivity extends AppCompatActivity implements IBaseView {
    private static final String TAG = "BaseActivity";
    private static final int MAX_PROMPT_COUNT = 10;
    private static final long ONE_DAY_MILLIS = 24 * 60 * 60 * 1000;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable ratingRunnable;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        long lastPromptTime = prefs.getLong("last_rating_prompt", 0);
        int promptCount = prefs.getInt("rating_prompt_count", 0);
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
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("last_rating_prompt", System.currentTimeMillis());
        editor.putInt("rating_prompt_count", newCount);
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        if (ratingRunnable != null) {
            handler.removeCallbacks(ratingRunnable);
        }
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
