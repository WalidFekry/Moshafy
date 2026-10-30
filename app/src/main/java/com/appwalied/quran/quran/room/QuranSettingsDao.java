package com.appwalied.quran.quran.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface QuranSettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveSettings(QuranSettingsEntity settings);

    @Update
    void updateSettings(QuranSettingsEntity settings);

    @Query("SELECT * FROM quran_settings WHERE id = 1 LIMIT 1")
    QuranSettingsEntity getSettings();
}
