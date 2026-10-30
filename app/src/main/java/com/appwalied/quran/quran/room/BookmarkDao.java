package com.appwalied.quran.quran.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertBookmark(BookmarkEntity bookmark);

    @Update
    void updateBookmark(BookmarkEntity bookmark);

    @Delete
    void deleteBookmark(BookmarkEntity bookmark);

    @Query("DELETE FROM bookmarks WHERE id = :id")
    void deleteBookmarkById(long id);

    @Query("SELECT * FROM bookmarks ORDER BY createdAt DESC")
    List<BookmarkEntity> getAllBookmarks();

    @Query("SELECT * FROM bookmarks WHERE suraId = :suraId AND ayahId = :ayahId LIMIT 1")
    BookmarkEntity getBookmarkForAyah(int suraId, int ayahId);
}
