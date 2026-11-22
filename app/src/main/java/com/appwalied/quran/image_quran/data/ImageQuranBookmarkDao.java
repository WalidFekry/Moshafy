package com.appwalied.quran.image_quran.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ImageQuranBookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertBookmark(ImageQuranBookmarkEntity bookmark);

    @Update
    void updateBookmark(ImageQuranBookmarkEntity bookmark);

    @Query("DELETE FROM image_quran_bookmarks WHERE id = :id")
    void deleteBookmarkById(long id);

    @Query("DELETE FROM image_quran_bookmarks")
    void deleteAllBookmarks();

    @Query("SELECT * FROM image_quran_bookmarks ORDER BY createdAt DESC")
    List<ImageQuranBookmarkEntity> getAllBookmarks();

    @Query("SELECT * FROM image_quran_bookmarks WHERE riwayaKey = :riwayaKey AND pageNumber = :pageNumber LIMIT 1")
    ImageQuranBookmarkEntity getBookmarkForPage(String riwayaKey, int pageNumber);
}
