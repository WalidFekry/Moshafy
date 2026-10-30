package com.appwalied.quran.quran.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reading_progress")
public class ReadingProgressEntity {

    @PrimaryKey
    private int id = 1;

    private int suraId;
    private int ayahId;
    private int pageNumber;
    private int paraNumber;
    private long updatedAt;

    public ReadingProgressEntity(int id, int suraId, int ayahId, int pageNumber, int paraNumber, long updatedAt) {
        this.id = id;
        this.suraId = suraId;
        this.ayahId = ayahId;
        this.pageNumber = pageNumber;
        this.paraNumber = paraNumber;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSuraId() {
        return suraId;
    }

    public void setSuraId(int suraId) {
        this.suraId = suraId;
    }

    public int getAyahId() {
        return ayahId;
    }

    public void setAyahId(int ayahId) {
        this.ayahId = ayahId;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getParaNumber() {
        return paraNumber;
    }

    public void setParaNumber(int paraNumber) {
        this.paraNumber = paraNumber;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
