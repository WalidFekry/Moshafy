package com.appwalied.quran.quran.quran_reading.model;

public class Ayat {

    private String id;
    private String sura_id;
    private String verse_id;
    private String arabic_indopak;
    private String arabic_s;
    private String trans;
    private String bn_muhi;
    private String pages;
    private String para;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSura_id() {
        return sura_id;
    }

    public void setSura_id(String sura_id) {
        this.sura_id = sura_id;
    }

    public String getVerse_id() {
        return verse_id;
    }

    public void setVerse_id(String verse_id) {
        this.verse_id = verse_id;
    }


    public String getArabic_s() {
        return arabic_s;
    }

    public void setArabic_s(String arabic_s) {
        this.arabic_s = arabic_s;
    }


    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }
}
