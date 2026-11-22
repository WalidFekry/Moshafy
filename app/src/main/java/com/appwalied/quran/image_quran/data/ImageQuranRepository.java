package com.appwalied.quran.image_quran.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import com.appwalied.quran.quran.room.AppDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageQuranRepository {

    private static final String PREF_NAME = "image_quran_prefs";
    private static volatile ImageQuranRepository instance;

    private final SharedPreferences prefs;
    private final AppDatabase db;
    private final ExecutorService executor;
    private final Handler mainHandler;

    public interface Callback<T> {
        void onResult(T result);
    }

    private ImageQuranRepository(Context context) {
        Context appContext = context.getApplicationContext();
        prefs = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        db = AppDatabase.getInstance(appContext);
        executor = Executors.newFixedThreadPool(4);
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public static ImageQuranRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (ImageQuranRepository.class) {
                if (instance == null) {
                    instance = new ImageQuranRepository(context);
                }
            }
        }
        return instance;
    }

    // Riwaya & Progress
    public Riwaya getSelectedRiwaya() {
        String key = prefs.getString(ImageQuranConfig.PREF_KEY_SELECTED_RIWAYA, Riwaya.HAFS.getKey());
        return Riwaya.fromKey(key);
    }

    public void setSelectedRiwaya(Riwaya riwaya) {
        prefs.edit().putString(ImageQuranConfig.PREF_KEY_SELECTED_RIWAYA, riwaya.getKey()).apply();
    }

    public int getLastPage(Riwaya riwaya) {
        String key = (riwaya == Riwaya.WARSH) ? ImageQuranConfig.PREF_KEY_LAST_PAGE_WARSH : ImageQuranConfig.PREF_KEY_LAST_PAGE_HAFS;
        return prefs.getInt(key, 1);
    }

    public void saveLastPage(Riwaya riwaya, int page) {
        String key = (riwaya == Riwaya.WARSH) ? ImageQuranConfig.PREF_KEY_LAST_PAGE_WARSH : ImageQuranConfig.PREF_KEY_LAST_PAGE_HAFS;
        prefs.edit().putInt(key, page).apply();
    }

    // Bookmarks
    public void addBookmark(ImageQuranBookmarkEntity bookmark, Callback<Long> callback) {
        executor.execute(() -> {
            long id = db.imageQuranBookmarkDao().insertBookmark(bookmark);
            if (callback != null) {
                mainHandler.post(() -> callback.onResult(id));
            }
        });
    }

    public void deleteBookmark(long bookmarkId, Runnable onComplete) {
        executor.execute(() -> {
            db.imageQuranBookmarkDao().deleteBookmarkById(bookmarkId);
            if (onComplete != null) {
                mainHandler.post(onComplete);
            }
        });
    }

    public void deleteAllBookmarks(Runnable onComplete) {
        executor.execute(() -> {
            db.imageQuranBookmarkDao().deleteAllBookmarks();
            if (onComplete != null) {
                mainHandler.post(onComplete);
            }
        });
    }

    public void getAllBookmarks(Callback<List<ImageQuranBookmarkEntity>> callback) {
        executor.execute(() -> {
            List<ImageQuranBookmarkEntity> list = db.imageQuranBookmarkDao().getAllBookmarks();
            if (callback != null) {
                mainHandler.post(() -> callback.onResult(list));
            }
        });
    }

    public void getBookmarkForPage(Riwaya riwaya, int page, Callback<ImageQuranBookmarkEntity> callback) {
        executor.execute(() -> {
            ImageQuranBookmarkEntity bookmark = db.imageQuranBookmarkDao().getBookmarkForPage(riwaya.getKey(), page);
            if (callback != null) {
                mainHandler.post(() -> callback.onResult(bookmark));
            }
        });
    }

    // Preferences
    public boolean isAutoPrefetchEnabled() {
        return prefs.getBoolean(ImageQuranConfig.PREF_KEY_AUTO_PREFETCH, true);
    }

    public void setAutoPrefetchEnabled(boolean enabled) {
        prefs.edit().putBoolean(ImageQuranConfig.PREF_KEY_AUTO_PREFETCH, enabled).apply();
    }

    public boolean isKeepScreenOnEnabled() {
        return prefs.getBoolean(ImageQuranConfig.PREF_KEY_KEEP_SCREEN_ON, true);
    }

    public void setKeepScreenOnEnabled(boolean enabled) {
        prefs.edit().putBoolean(ImageQuranConfig.PREF_KEY_KEEP_SCREEN_ON, enabled).apply();
    }
}
