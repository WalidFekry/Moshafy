package com.appwalied.quran.image_quran.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "image_quran_bookmarks")
public class ImageQuranBookmarkEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String riwayaKey;
    private int pageNumber;
    private String surahName;
    private int juzNumber;
    private String title;
    private long createdAt;

    public ImageQuranBookmarkEntity(String riwayaKey, int pageNumber, String surahName, int juzNumber, String title, long createdAt) {
        this.riwayaKey = riwayaKey;
        this.pageNumber = pageNumber;
        this.surahName = surahName;
        this.juzNumber = juzNumber;
        this.title = title;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRiwayaKey() {
        return riwayaKey;
    }

    public void setRiwayaKey(String riwayaKey) {
        this.riwayaKey = riwayaKey;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getSurahName() {
        return surahName;
    }

    public void setSurahName(String surahName) {
        this.surahName = surahName;
    }

    public int getJuzNumber() {
        return juzNumber;
    }

    public void setJuzNumber(int juzNumber) {
        this.juzNumber = juzNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
