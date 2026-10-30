package com.appwalied.quran.quran.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookmarks")
public class BookmarkEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private int suraId;
    private int ayahId;
    private int pageNumber;
    private int paraNumber;
    private String bookmarkName;
    private long createdAt;

    public BookmarkEntity(int suraId, int ayahId, int pageNumber, int paraNumber, String bookmarkName, long createdAt) {
        this.suraId = suraId;
        this.ayahId = ayahId;
        this.pageNumber = pageNumber;
        this.paraNumber = paraNumber;
        this.bookmarkName = bookmarkName;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getBookmarkName() {
        return bookmarkName;
    }

    public void setBookmarkName(String bookmarkName) {
        this.bookmarkName = bookmarkName;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
