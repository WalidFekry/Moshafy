package com.appwalied.quran.tadabbur.model;

import java.io.Serializable;

public class QuranV2Page implements Serializable {
    private int pageNumber;
    private int juzNumber;
    private String surahsSummary;
    private int versesCount;

    public QuranV2Page() {
    }

    public QuranV2Page(int pageNumber, int juzNumber, String surahsSummary, int versesCount) {
        this.pageNumber = pageNumber;
        this.juzNumber = juzNumber;
        this.surahsSummary = surahsSummary;
        this.versesCount = versesCount;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getJuzNumber() {
        return juzNumber;
    }

    public void setJuzNumber(int juzNumber) {
        this.juzNumber = juzNumber;
    }

    public String getSurahsSummary() {
        return surahsSummary;
    }

    public void setSurahsSummary(String surahsSummary) {
        this.surahsSummary = surahsSummary;
    }

    public int getVersesCount() {
        return versesCount;
    }

    public void setVersesCount(int versesCount) {
        this.versesCount = versesCount;
    }
}
