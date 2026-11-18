package com.appwalied.quran.quran.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.appwalied.quran.tadabbur.db.room.TadabburBookmarkDao;
import com.appwalied.quran.tadabbur.db.room.TadabburBookmarkEntity;

@Database(
    entities = {
        BookmarkEntity.class,
        ReadingProgressEntity.class,
        QuranSettingsEntity.class,
        FavoriteSurahEntity.class,
        TadabburBookmarkEntity.class
    },
    version = 2,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "app_database.db";
    private static volatile AppDatabase instance;

    public abstract BookmarkDao bookmarkDao();
    public abstract ReadingProgressDao readingProgressDao();
    public abstract QuranSettingsDao quranSettingsDao();
    public abstract FavoriteSurahDao favoriteSurahDao();
    public abstract TadabburBookmarkDao tadabburBookmarkDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `tadabbur_bookmarks` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`sora` INTEGER NOT NULL, " +
                    "`ayaNo` INTEGER NOT NULL, " +
                    "`page` INTEGER NOT NULL, " +
                    "`jozz` INTEGER NOT NULL, " +
                    "`soraNameAr` TEXT, " +
                    "`ayaText` TEXT, " +
                    "`timestamp` INTEGER NOT NULL)");
        }
    };

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME
                    )
                    .addMigrations(MIGRATION_1_2)
                    .build();
                }
            }
        }
        return instance;
    }
}
