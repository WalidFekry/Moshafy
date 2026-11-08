package com.appwalied.quran.quran_listening.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class ListeningPreferences {

    private static final String PREF_NAME = "quran_listening_prefs";
    private static final String KEY_LAST_RECITER_ID = "last_reciter_id";
    private static final String KEY_LAST_RECITER_NAME = "last_reciter_name";
    private static final String KEY_LAST_SERVER_URL = "last_server_url";
    private static final String KEY_LAST_SURAH_LIST = "last_surah_list";

    private static final String KEY_LAST_SURAH_ID = "last_surah_id";
    private static final String KEY_LAST_SURAH_NAME = "last_surah_name";

    private static final String KEY_REPEAT_MODE = "repeat_mode";
    private static final String KEY_PLAYBACK_SPEED = "playback_speed";

    private static final String KEY_FAV_RECITERS = "fav_reciters";
    private static final String KEY_FAV_SURAHS = "fav_surahs";

    private static final String KEY_CACHED_RECITERS_JSON = "cached_reciters_json";
    private static final String KEY_CACHED_SUWAR_JSON = "cached_suwar_json";

    private final SharedPreferences prefs;

    public ListeningPreferences(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveLastReciter(int reciterId, String reciterName, String serverUrl, String surahList) {
        prefs.edit()
                .putInt(KEY_LAST_RECITER_ID, reciterId)
                .putString(KEY_LAST_RECITER_NAME, reciterName)
                .putString(KEY_LAST_SERVER_URL, serverUrl)
                .putString(KEY_LAST_SURAH_LIST, surahList)
                .apply();
    }

    public int getLastReciterId() {
        return prefs.getInt(KEY_LAST_RECITER_ID, 1);
    }

    public String getLastReciterName() {
        return prefs.getString(KEY_LAST_RECITER_NAME, "أبو بكر الشاطري");
    }

    public String getLastServerUrl() {
        return prefs.getString(KEY_LAST_SERVER_URL, "https://server11.mp3quran.net/shatri/");
    }

    public String getLastSurahList() {
        return prefs.getString(KEY_LAST_SURAH_LIST, "");
    }

    public void saveLastSurah(int surahId, String surahName) {
        prefs.edit()
                .putInt(KEY_LAST_SURAH_ID, surahId)
                .putString(KEY_LAST_SURAH_NAME, surahName)
                .apply();
    }

    public int getLastSurahId() {
        return prefs.getInt(KEY_LAST_SURAH_ID, 1);
    }

    public String getLastSurahName() {
        return prefs.getString(KEY_LAST_SURAH_NAME, "الفاتحة");
    }

    public void saveRepeatMode(int mode) {
        prefs.edit().putInt(KEY_REPEAT_MODE, mode).apply();
    }

    public int getRepeatMode() {
        return prefs.getInt(KEY_REPEAT_MODE, 0); // 0 = No Repeat, 1 = Repeat One, 2 = Repeat All
    }

    public void savePlaybackSpeed(float speed) {
        prefs.edit().putFloat(KEY_PLAYBACK_SPEED, speed).apply();
    }

    public float getPlaybackSpeed() {
        return prefs.getFloat(KEY_PLAYBACK_SPEED, 1.0f);
    }

    public boolean isReciterFavorite(int reciterId) {
        Set<String> set = prefs.getStringSet(KEY_FAV_RECITERS, new HashSet<>());
        return set.contains(String.valueOf(reciterId));
    }

    public boolean toggleReciterFavorite(int reciterId) {
        Set<String> set = new HashSet<>(prefs.getStringSet(KEY_FAV_RECITERS, new HashSet<>()));
        String key = String.valueOf(reciterId);
        boolean isFav;
        if (set.contains(key)) {
            set.remove(key);
            isFav = false;
        } else {
            set.add(key);
            isFav = true;
        }
        prefs.edit().putStringSet(KEY_FAV_RECITERS, set).apply();
        return isFav;
    }

    public boolean isSurahFavorite(int surahId) {
        Set<String> set = prefs.getStringSet(KEY_FAV_SURAHS, new HashSet<>());
        return set.contains(String.valueOf(surahId));
    }

    public boolean toggleSurahFavorite(int surahId) {
        Set<String> set = new HashSet<>(prefs.getStringSet(KEY_FAV_SURAHS, new HashSet<>()));
        String key = String.valueOf(surahId);
        boolean isFav;
        if (set.contains(key)) {
            set.remove(key);
            isFav = false;
        } else {
            set.add(key);
            isFav = true;
        }
        prefs.edit().putStringSet(KEY_FAV_SURAHS, set).apply();
        return isFav;
    }

    public void cacheRecitersJson(String json) {
        prefs.edit().putString(KEY_CACHED_RECITERS_JSON, json).apply();
    }

    public String getCachedRecitersJson() {
        return prefs.getString(KEY_CACHED_RECITERS_JSON, null);
    }

    public void cacheSuwarJson(String json) {
        prefs.edit().putString(KEY_CACHED_SUWAR_JSON, json).apply();
    }

    public String getCachedSuwarJson() {
        return prefs.getString(KEY_CACHED_SUWAR_JSON, null);
    }
}
