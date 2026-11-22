package com.appwalied.quran.quran_listening.download;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DownloadedSurahDao {

    @Query("SELECT * FROM downloaded_surahs ORDER BY downloadDate DESC")
    List<DownloadedSurahEntity> getAllDownloadedSurahs();

    @Query("SELECT * FROM downloaded_surahs WHERE downloadStatus = 4 ORDER BY reciterName ASC, surahId ASC")
    List<DownloadedSurahEntity> getCompletedSurahs();

    @Query("SELECT * FROM downloaded_surahs WHERE reciterId = :reciterId AND downloadStatus = 4 ORDER BY surahId ASC")
    List<DownloadedSurahEntity> getCompletedSurahsByReciter(int reciterId);

    @Query("SELECT * FROM downloaded_surahs WHERE downloadId = :downloadId LIMIT 1")
    DownloadedSurahEntity getDownloadedSurah(String downloadId);

    @Query("SELECT * FROM downloaded_surahs WHERE reciterId = :reciterId AND surahId = :surahId LIMIT 1")
    DownloadedSurahEntity getDownloadedSurah(int reciterId, int surahId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(DownloadedSurahEntity entity);

    @Query("DELETE FROM downloaded_surahs WHERE downloadId = :downloadId")
    void deleteByDownloadId(String downloadId);

    @Query("DELETE FROM downloaded_surahs WHERE reciterId = :reciterId")
    void deleteByReciterId(int reciterId);

    @Query("SELECT SUM(fileSize) FROM downloaded_surahs WHERE downloadStatus = 4")
    Long getTotalStorageSize();

    @Query("SELECT COUNT(*) FROM downloaded_surahs WHERE downloadStatus = 4")
    int getCompletedCount();
}
