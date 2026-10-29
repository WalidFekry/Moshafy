package com.appwalied.quran.notifications_messages.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.appwalied.quran.notifications_messages.model.NotificationModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SavedQuotesManager {

    private static final String PREF_NAME = "quotes_prefs";
    private static final String KEY_SAVED_QUOTES = "saved_quotes";
    private static final String KEY_CACHED_QUOTES = "cached_quotes";
    private static final Gson gson = new Gson();

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static List<NotificationModel> getSavedQuotes(Context context) {
        String json = getPrefs(context).getString(KEY_SAVED_QUOTES, null);
        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<ArrayList<NotificationModel>>() {}.getType();
        List<NotificationModel> list = gson.fromJson(json, type);
        return list != null ? list : new ArrayList<>();
    }

    public static boolean isQuoteSaved(Context context, int id) {
        List<NotificationModel> list = getSavedQuotes(context);
        for (NotificationModel item : list) {
            if (item.getId() == id) return true;
        }
        return false;
    }

    public static boolean toggleSaveQuote(Context context, NotificationModel quote) {
        List<NotificationModel> list = getSavedQuotes(context);
        boolean saved = false;
        int targetIndex = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == quote.getId()) {
                targetIndex = i;
                break;
            }
        }
        if (targetIndex != -1) {
            list.remove(targetIndex);
            saved = false;
        } else {
            list.add(quote);
            saved = true;
        }
        getPrefs(context).edit().putString(KEY_SAVED_QUOTES, gson.toJson(list)).apply();
        return saved;
    }

    public static void cacheQuotes(Context context, List<NotificationModel> quotes) {
        if (quotes != null && !quotes.isEmpty()) {
            getPrefs(context).edit().putString(KEY_CACHED_QUOTES, gson.toJson(quotes)).apply();
        }
    }

    public static List<NotificationModel> getCachedQuotes(Context context) {
        String json = getPrefs(context).getString(KEY_CACHED_QUOTES, null);
        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<ArrayList<NotificationModel>>() {}.getType();
        List<NotificationModel> list = gson.fromJson(json, type);
        return list != null ? list : new ArrayList<>();
    }
}
