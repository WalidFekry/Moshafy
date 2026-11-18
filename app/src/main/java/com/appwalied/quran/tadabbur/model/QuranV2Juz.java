package com.appwalied.quran.tadabbur.model;

import java.io.Serializable;

public class QuranV2Juz implements Serializable {
    private int juzNumber;
    private int startSora;
    private String startSoraNameAr;
    private int startAyaNo;
    private int startPage;
    private int totalVerses;

    public QuranV2Juz() {
    }

    public QuranV2Juz(int juzNumber, int startSora, String startSoraNameAr, int startAyaNo, int startPage, int totalVerses) {
        this.juzNumber = juzNumber;
        this.startSora = startSora;
        this.startSoraNameAr = startSoraNameAr;
        this.startAyaNo = startAyaNo;
        this.startPage = startPage;
        this.totalVerses = totalVerses;
    }

    public int getJuzNumber() {
        return juzNumber;
    }

    public void setJuzNumber(int juzNumber) {
        this.juzNumber = juzNumber;
    }

    public int getStartSora() {
        return startSora;
    }

    public void setStartSora(int startSora) {
        this.startSora = startSora;
    }

    public String getStartSoraNameAr() {
        return startSoraNameAr;
    }

    public void setStartSoraNameAr(String startSoraNameAr) {
        this.startSoraNameAr = startSoraNameAr;
    }

    public int getStartAyaNo() {
        return startAyaNo;
    }

    public void setStartAyaNo(int startAyaNo) {
        this.startAyaNo = startAyaNo;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getTotalVerses() {
        return totalVerses;
    }

    public void setTotalVerses(int totalVerses) {
        this.totalVerses = totalVerses;
    }
}
