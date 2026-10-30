package com.appwalied.quran.quran.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteSurahDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addFavorite(FavoriteSurahEntity favorite);

    @Delete
    void removeFavorite(FavoriteSurahEntity favorite);

    @Query("DELETE FROM favorite_surahs WHERE suraId = :suraId")
    void removeFavoriteById(int suraId);

    @Query("SELECT * FROM favorite_surahs ORDER BY createdAt DESC")
    List<FavoriteSurahEntity> getAllFavorites();

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_surahs WHERE suraId = :suraId)")
    boolean isFavorite(int suraId);
}
