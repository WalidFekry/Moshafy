package com.appwalied.quran.quran.room;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserLocalRepository {

    private static volatile UserLocalRepository instance;
    private final AppDatabase db;
    private final ExecutorService executor;
    private final Handler mainHandler;

    public interface Callback<T> {
        void onResult(T result);
    }

    private UserLocalRepository(Context context) {
        db = AppDatabase.getInstance(context);
        executor = Executors.newFixedThreadPool(4);
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public static UserLocalRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (UserLocalRepository.class) {
                if (instance == null) {
                    instance = new UserLocalRepository(context);
                }
            }
        }
        return instance;
    }

    // Bookmarks
    public void addBookmark(BookmarkEntity bookmark, Callback<Long> callback) {
        executor.execute(() -> {
            long id = db.bookmarkDao().insertBookmark(bookmark);
            if (callback != null) {
                mainHandler.post(() -> callback.onResult(id));
            }
        });
    }

    public void deleteBookmark(long bookmarkId, Runnable onComplete) {
        executor.execute(() -> {
            db.bookmarkDao().deleteBookmarkById(bookmarkId);
            if (onComplete != null) {
                mainHandler.post(onComplete);
            }
        });
    }

    public void getAllBookmarks(Callback<List<BookmarkEntity>> callback) {
        executor.execute(() -> {
            List<BookmarkEntity> list = db.bookmarkDao().getAllBookmarks();
            if (callback != null) {
                mainHandler.post(() -> callback.onResult(list));
            }
        });
    }

    public void getBookmarkForAyah(int suraId, int ayahId, Callback<BookmarkEntity> callback) {
        executor.execute(() -> {
            BookmarkEntity bookmark = db.bookmarkDao().getBookmarkForAyah(suraId, ayahId);
            if (callback != null) {
                mainHandler.post(() -> callback.onResult(bookmark));
            }
        });
    }

    // Reading Progress
    public void saveReadingProgress(ReadingProgressEntity progress) {
        executor.execute(() -> db.readingProgressDao().saveProgress(progress));
    }

    public void getReadingProgress(Callback<ReadingProgressEntity> callback) {
        executor.execute(() -> {
            ReadingProgressEntity progress = db.readingProgressDao().getProgress();
            if (callback != null) {
                mainHandler.post(() -> callback.onResult(progress));
            }
        });
    }

    // Settings
    public void getSettings(Callback<QuranSettingsEntity> callback) {
        executor.execute(() -> {
            QuranSettingsEntity settings = db.quranSettingsDao().getSettings();
            if (settings == null) {
                settings = QuranSettingsEntity.createDefault();
                db.quranSettingsDao().saveSettings(settings);
            }
            QuranSettingsEntity finalSettings = settings;
            if (callback != null) {
                mainHandler.post(() -> callback.onResult(finalSettings));
            }
        });
    }

    public void updateSettings(QuranSettingsEntity settings) {
        executor.execute(() -> db.quranSettingsDao().saveSettings(settings));
    }

    // Favorites
    public void toggleFavoriteSurah(int suraId, String suraName, Callback<Boolean> callback) {
        executor.execute(() -> {
            boolean isFav = db.favoriteSurahDao().isFavorite(suraId);
            if (isFav) {
                db.favoriteSurahDao().removeFavoriteById(suraId);
            } else {
                db.favoriteSurahDao().addFavorite(new FavoriteSurahEntity(suraId, suraName, System.currentTimeMillis()));
            }
            boolean newFavState = !isFav;
            if (callback != null) {
                mainHandler.post(() -> callback.onResult(newFavState));
            }
        });
    }

    public void isFavoriteSurah(int suraId, Callback<Boolean> callback) {
        executor.execute(() -> {
            boolean isFav = db.favoriteSurahDao().isFavorite(suraId);
            if (callback != null) {
                mainHandler.post(() -> callback.onResult(isFav));
            }
        });
    }

    public void getAllFavorites(Callback<List<FavoriteSurahEntity>> callback) {
        executor.execute(() -> {
            List<FavoriteSurahEntity> list = db.favoriteSurahDao().getAllFavorites();
            if (callback != null) {
                mainHandler.post(() -> callback.onResult(list));
            }
        });
    }
}
