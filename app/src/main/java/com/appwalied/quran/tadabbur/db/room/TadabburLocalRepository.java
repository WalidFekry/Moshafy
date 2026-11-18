package com.appwalied.quran.tadabbur.db.room;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import com.appwalied.quran.quran.room.AppDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TadabburLocalRepository {

    private static final String PREF_NAME = "tadabbur_prefs";
    private static final String KEY_LAST_TAFSEER_INDEX = "key_last_tafseer_index";
    private static final String KEY_FONT_SIZE = "key_font_size";
    private static final String KEY_LAST_SORA = "key_last_sora";
    private static final String KEY_LAST_AYA = "key_last_aya";
    private static final String KEY_LAST_PAGE = "key_last_page";

    private static volatile TadabburLocalRepository instance;
    private final AppDatabase db;
    private final SharedPreferences prefs;
    private final ExecutorService executor;
    private final Handler mainHandler;

    public interface Callback<T> {
        void onResult(T result);
    }

    private TadabburLocalRepository(Context context) {
        db = AppDatabase.getInstance(context);
        prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        executor = Executors.newFixedThreadPool(4);
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public static TadabburLocalRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (TadabburLocalRepository.class) {
                if (instance == null) {
                    instance = new TadabburLocalRepository(context);
                }
            }
        }
        return instance;
    }

    public void toggleBookmark(TadabburBookmarkEntity bookmark, Callback<Boolean> callback) {
        executor.execute(() -> {
            TadabburBookmarkEntity existing = db.tadabburBookmarkDao().getBookmark(bookmark.getSora(), bookmark.getAyaNo());
            boolean isBookmarked;
            if (existing != null) {
                db.tadabburBookmarkDao().deleteBookmark(bookmark.getSora(), bookmark.getAyaNo());
                isBookmarked = false;
            } else {
                db.tadabburBookmarkDao().insertBookmark(bookmark);
                isBookmarked = true;
            }
            if (callback != null) {
                mainHandler.post(() -> callback.onResult(isBookmarked));
            }
        });
    }

    public void isBookmarked(int sora, int ayaNo, Callback<Boolean> callback) {
        executor.execute(() -> {
            TadabburBookmarkEntity existing = db.tadabburBookmarkDao().getBookmark(sora, ayaNo);
            boolean isBookmarked = (existing != null);
            if (callback != null) {
                mainHandler.post(() -> callback.onResult(isBookmarked));
            }
        });
    }

    public void getAllBookmarks(Callback<List<TadabburBookmarkEntity>> callback) {
        executor.execute(() -> {
            List<TadabburBookmarkEntity> list = db.tadabburBookmarkDao().getAllBookmarks();
            if (callback != null) {
                mainHandler.post(() -> callback.onResult(list));
            }
        });
    }

    public void deleteBookmarkById(long id, Runnable onComplete) {
        executor.execute(() -> {
            db.tadabburBookmarkDao().deleteBookmarkById(id);
            if (onComplete != null) {
                mainHandler.post(onComplete);
            }
        });
    }

    // Preferences
    public int getLastTafseerIndex() {
        return prefs.getInt(KEY_LAST_TAFSEER_INDEX, 0); // 0: Saadi, 1: Moysar, 2: Bughiu
    }

    public void saveLastTafseerIndex(int index) {
        prefs.edit().putInt(KEY_LAST_TAFSEER_INDEX, index).apply();
    }

    public int getFontSize() {
        return prefs.getInt(KEY_FONT_SIZE, 22); // Default font size
    }

    public void saveFontSize(int size) {
        prefs.edit().putInt(KEY_FONT_SIZE, size).apply();
    }

    public void saveLastReadPosition(int sora, int ayaNo, int page) {
        prefs.edit()
                .putInt(KEY_LAST_SORA, sora)
                .putInt(KEY_LAST_AYA, ayaNo)
                .putInt(KEY_LAST_PAGE, page)
                .apply();
    }

    public int getLastReadSora() {
        return prefs.getInt(KEY_LAST_SORA, 1);
    }

    public int getLastReadAya() {
        return prefs.getInt(KEY_LAST_AYA, 1);
    }

    public int getLastReadPage() {
        return prefs.getInt(KEY_LAST_PAGE, 1);
    }
}
