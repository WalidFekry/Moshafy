package com.appwalied.quran.quran.db.models;

public class Surah {
    private int id;
    private String nameAr;
    private int startPage;
    private int endPage;
    private int versesCount;

    public Surah(int id, String nameAr, int startPage, int endPage, int versesCount) {
        this.id = id;
        this.nameAr = nameAr;
        this.startPage = startPage;
        this.endPage = endPage;
        this.versesCount = versesCount;
    }

    public int getId() {
        return id;
    }

    public String getNameAr() {
        return nameAr;
    }

    public int getStartPage() {
        return startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public int getVersesCount() {
        return versesCount;
    }
}
