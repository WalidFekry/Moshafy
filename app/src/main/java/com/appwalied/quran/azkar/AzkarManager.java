package com.appwalied.quran.azkar;

import android.content.Context;
import android.content.SharedPreferences;

public class AzkarManager {

    private static final String PREF_NAME = "azkar_preferences";
    private static final String KEY_COUNT_PREFIX = "azkar_count_";
    private static final String KEY_TEXT_SIZE = "azkar_text_size";

    private static SharedPreferences getPrefs(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Counter Progress
    public static int getSavedCount(Context context, String zikrId) {
        if (zikrId == null || zikrId.isEmpty()) return 0;
        return getPrefs(context).getInt(KEY_COUNT_PREFIX + zikrId, 0);
    }

    public static void saveCount(Context context, String zikrId, int count) {
        if (zikrId == null || zikrId.isEmpty()) return;
        getPrefs(context).edit().putInt(KEY_COUNT_PREFIX + zikrId, count).apply();
    }

    public static void resetCount(Context context, String zikrId) {
        if (zikrId == null || zikrId.isEmpty()) return;
        getPrefs(context).edit().remove(KEY_COUNT_PREFIX + zikrId).apply();
    }

    // Custom Text Size preference
    public static int getTextSize(Context context, int defaultSizeSp) {
        return getPrefs(context).getInt(KEY_TEXT_SIZE, defaultSizeSp);
    }

    public static void saveTextSize(Context context, int sizeSp) {
        getPrefs(context).edit().putInt(KEY_TEXT_SIZE, sizeSp).apply();
    }
}
