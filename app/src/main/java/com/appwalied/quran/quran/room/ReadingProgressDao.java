package com.appwalied.quran.quran.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ReadingProgressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveProgress(ReadingProgressEntity progress);

    @Query("SELECT * FROM reading_progress WHERE id = 1 LIMIT 1")
    ReadingProgressEntity getProgress();
}
