package com.appwalied.quran.quran.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_surahs")
public class FavoriteSurahEntity {

    @PrimaryKey
    private int suraId;

    private String suraName;
    private long createdAt;

    public FavoriteSurahEntity(int suraId, String suraName, long createdAt) {
        this.suraId = suraId;
        this.suraName = suraName;
        this.createdAt = createdAt;
    }

    public int getSuraId() {
        return suraId;
    }

    public void setSuraId(int suraId) {
        this.suraId = suraId;
    }

    public String getSuraName() {
        return suraName;
    }

    public void setSuraName(String suraName) {
        this.suraName = suraName;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
