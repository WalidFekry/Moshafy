package com.appwalied.quran.tadabbur.model;

import java.io.Serializable;

public class QuranV2Surah implements Serializable {
    private int soraNumber;
    private String nameAr;
    private String nameEn;
    private int versesCount;
    private int startPage;
    private int endPage;
    private int startJuzz;
    private int endJuzz;

    public QuranV2Surah() {
    }

    public QuranV2Surah(int soraNumber, String nameAr, String nameEn, int versesCount,
                        int startPage, int endPage, int startJuzz, int endJuzz) {
        this.soraNumber = soraNumber;
        this.nameAr = nameAr;
        this.nameEn = nameEn;
        this.versesCount = versesCount;
        this.startPage = startPage;
        this.endPage = endPage;
        this.startJuzz = startJuzz;
        this.endJuzz = endJuzz;
    }

    public int getSoraNumber() {
        return soraNumber;
    }

    public void setSoraNumber(int soraNumber) {
        this.soraNumber = soraNumber;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public int getVersesCount() {
        return versesCount;
    }

    public void setVersesCount(int versesCount) {
        this.versesCount = versesCount;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public int getStartJuzz() {
        return startJuzz;
    }

    public void setStartJuzz(int startJuzz) {
        this.startJuzz = startJuzz;
    }

    public int getEndJuzz() {
        return endJuzz;
    }

    public void setEndJuzz(int endJuzz) {
        this.endJuzz = endJuzz;
    }
}
