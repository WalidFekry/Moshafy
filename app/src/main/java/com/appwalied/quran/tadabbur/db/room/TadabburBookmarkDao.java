package com.appwalied.quran.tadabbur.db.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TadabburBookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertBookmark(TadabburBookmarkEntity bookmark);

    @Query("DELETE FROM tadabbur_bookmarks WHERE sora = :sora AND ayaNo = :ayaNo")
    void deleteBookmark(int sora, int ayaNo);

    @Query("DELETE FROM tadabbur_bookmarks WHERE id = :id")
    void deleteBookmarkById(long id);

    @Query("SELECT * FROM tadabbur_bookmarks ORDER BY timestamp DESC")
    List<TadabburBookmarkEntity> getAllBookmarks();

    @Query("SELECT * FROM tadabbur_bookmarks WHERE sora = :sora AND ayaNo = :ayaNo LIMIT 1")
    TadabburBookmarkEntity getBookmark(int sora, int ayaNo);
}
