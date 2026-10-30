package com.appwalied.quran.quran.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(
    entities = {
        BookmarkEntity.class,
        ReadingProgressEntity.class,
        QuranSettingsEntity.class,
        FavoriteSurahEntity.class
    },
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "app_database.db";
    private static volatile AppDatabase instance;

    public abstract BookmarkDao bookmarkDao();
    public abstract ReadingProgressDao readingProgressDao();
    public abstract QuranSettingsDao quranSettingsDao();
    public abstract FavoriteSurahDao favoriteSurahDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME
                    )
                    .build();
                }
            }
        }
        return instance;
    }
}
