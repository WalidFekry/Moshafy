package com.appwalied.quran.tadabbur.db.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tadabbur_bookmarks")
public class TadabburBookmarkEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private int sora;
    private int ayaNo;
    private int page;
    private int jozz;
    private String soraNameAr;
    private String ayaText;
    private long timestamp;

    public TadabburBookmarkEntity(int sora, int ayaNo, int page, int jozz, String soraNameAr, String ayaText, long timestamp) {
        this.sora = sora;
        this.ayaNo = ayaNo;
        this.page = page;
        this.jozz = jozz;
        this.soraNameAr = soraNameAr;
        this.ayaText = ayaText;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSora() {
        return sora;
    }

    public void setSora(int sora) {
        this.sora = sora;
    }

    public int getAyaNo() {
        return ayaNo;
    }

    public void setAyaNo(int ayaNo) {
        this.ayaNo = ayaNo;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getJozz() {
        return jozz;
    }

    public void setJozz(int jozz) {
        this.jozz = jozz;
    }

    public String getSoraNameAr() {
        return soraNameAr;
    }

    public void setSoraNameAr(String soraNameAr) {
        this.soraNameAr = soraNameAr;
    }

    public String getAyaText() {
        return ayaText;
    }

    public void setAyaText(String ayaText) {
        this.ayaText = ayaText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
