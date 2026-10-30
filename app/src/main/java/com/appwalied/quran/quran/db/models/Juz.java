package com.appwalied.quran.quran.db.models;

public class Juz {
    private int juzNumber;
    private int startSuraId;
    private String startSuraName;
    private int startAyahId;
    private int startPage;

    public Juz(int juzNumber, int startSuraId, String startSuraName, int startAyahId, int startPage) {
        this.juzNumber = juzNumber;
        this.startSuraId = startSuraId;
        this.startSuraName = startSuraName;
        this.startAyahId = startAyahId;
        this.startPage = startPage;
    }

    public int getJuzNumber() {
        return juzNumber;
    }

    public int getStartSuraId() {
        return startSuraId;
    }

    public String getStartSuraName() {
        return startSuraName;
    }

    public int getStartAyahId() {
        return startAyahId;
    }

    public int getStartPage() {
        return startPage;
    }
}
