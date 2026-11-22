package com.appwalied.quran.quran.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.appwalied.quran.image_quran.data.ImageQuranBookmarkDao;
import com.appwalied.quran.image_quran.data.ImageQuranBookmarkEntity;
import com.appwalied.quran.quran_listening.download.DownloadedSurahDao;
import com.appwalied.quran.quran_listening.download.DownloadedSurahEntity;
import com.appwalied.quran.tadabbur.db.room.TadabburBookmarkDao;
import com.appwalied.quran.tadabbur.db.room.TadabburBookmarkEntity;

@Database(
    entities = {
        BookmarkEntity.class,
        ReadingProgressEntity.class,
        QuranSettingsEntity.class,
        FavoriteSurahEntity.class,
        TadabburBookmarkEntity.class,
        ImageQuranBookmarkEntity.class,
        DownloadedSurahEntity.class
    },
    version = 4,
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
    public abstract ImageQuranBookmarkDao imageQuranBookmarkDao();
    public abstract DownloadedSurahDao downloadedSurahDao();

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

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `image_quran_bookmarks` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`riwayaKey` TEXT, " +
                    "`pageNumber` INTEGER NOT NULL, " +
                    "`surahName` TEXT, " +
                    "`juzNumber` INTEGER NOT NULL, " +
                    "`title` TEXT, " +
                    "`createdAt` INTEGER NOT NULL)");
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `downloaded_surahs` (" +
                    "`downloadId` TEXT NOT NULL PRIMARY KEY, " +
                    "`reciterId` INTEGER NOT NULL, " +
                    "`reciterName` TEXT, " +
                    "`surahId` INTEGER NOT NULL, " +
                    "`surahName` TEXT, " +
                    "`remoteUrl` TEXT, " +
                    "`localFilePath` TEXT, " +
                    "`fileSize` INTEGER NOT NULL, " +
                    "`downloadedBytes` INTEGER NOT NULL, " +
                    "`downloadStatus` INTEGER NOT NULL, " +
                    "`downloadDate` INTEGER NOT NULL, " +
                    "`lastModified` INTEGER NOT NULL, " +
                    "`errorMessage` TEXT)");
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
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .fallbackToDestructiveMigration(true)
                    .build();
                }
            }
        }
        return instance;
    }
}
