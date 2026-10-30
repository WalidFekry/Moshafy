package com.appwalied.quran.quran.db.models;

public class Ayah {
    private int id;
    private int suraId;
    private int verseId;
    private String arabicUthmanic;
    private int pageNumber;
    private int paraNumber;
    private String suraName;

    public Ayah(int id, int suraId, int verseId, String arabicUthmanic, int pageNumber, int paraNumber, String suraName) {
        this.id = id;
        this.suraId = suraId;
        this.verseId = verseId;
        this.arabicUthmanic = arabicUthmanic;
        this.pageNumber = pageNumber;
        this.paraNumber = paraNumber;
        this.suraName = suraName;
    }

    public int getId() {
        return id;
    }

    public int getSuraId() {
        return suraId;
    }

    public int getVerseId() {
        return verseId;
    }

    public String getArabicUthmanic() {
        return arabicUthmanic;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getParaNumber() {
        return paraNumber;
    }

    public String getSuraName() {
        return suraName;
    }

    public void setSuraName(String suraName) {
        this.suraName = suraName;
    }
}
