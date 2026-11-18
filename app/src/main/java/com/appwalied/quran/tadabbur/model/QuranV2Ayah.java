package com.appwalied.quran.tadabbur.model;

import java.io.Serializable;

public class QuranV2Ayah implements Serializable {
    private int id;
    private int jozz;
    private int sora;
    private String soraNameEn;
    private String soraNameAr;
    private int page;
    private int lineStart;
    private int lineEnd;
    private int ayaNo;
    private String ayaText;
    private String ayaTextEmlaey;
    private String maanyAya;
    private String earabQuran;
    private String reasonsOfVerses;
    private String tafseerSaadi;
    private String tafseerMoysar;
    private String tafseerBughiu;
    private String ayaTextTashkil;

    public QuranV2Ayah() {
    }

    public QuranV2Ayah(int id, int jozz, int sora, String soraNameEn, String soraNameAr,
                       int page, int lineStart, int lineEnd, int ayaNo,
                       String ayaText, String ayaTextEmlaey, String maanyAya,
                       String earabQuran, String reasonsOfVerses, String tafseerSaadi,
                       String tafseerMoysar, String tafseerBughiu, String ayaTextTashkil) {
        this.id = id;
        this.jozz = jozz;
        this.sora = sora;
        this.soraNameEn = soraNameEn;
        this.soraNameAr = soraNameAr;
        this.page = page;
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
        this.ayaNo = ayaNo;
        this.ayaText = ayaText;
        this.ayaTextEmlaey = ayaTextEmlaey;
        this.maanyAya = maanyAya;
        this.earabQuran = earabQuran;
        this.reasonsOfVerses = reasonsOfVerses;
        this.tafseerSaadi = tafseerSaadi;
        this.tafseerMoysar = tafseerMoysar;
        this.tafseerBughiu = tafseerBughiu;
        this.ayaTextTashkil = ayaTextTashkil;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJozz() {
        return jozz;
    }

    public void setJozz(int jozz) {
        this.jozz = jozz;
    }

    public int getSora() {
        return sora;
    }

    public void setSora(int sora) {
        this.sora = sora;
    }

    public String getSoraNameEn() {
        return soraNameEn;
    }

    public void setSoraNameEn(String soraNameEn) {
        this.soraNameEn = soraNameEn;
    }

    public String getSoraNameAr() {
        return soraNameAr;
    }

    public void setSoraNameAr(String soraNameAr) {
        this.soraNameAr = soraNameAr;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLineStart() {
        return lineStart;
    }

    public void setLineStart(int lineStart) {
        this.lineStart = lineStart;
    }

    public int getLineEnd() {
        return lineEnd;
    }

    public void setLineEnd(int lineEnd) {
        this.lineEnd = lineEnd;
    }

    public int getAyaNo() {
        return ayaNo;
    }

    public void setAyaNo(int ayaNo) {
        this.ayaNo = ayaNo;
    }

    public String getAyaText() {
        return ayaText;
    }

    public void setAyaText(String ayaText) {
        this.ayaText = ayaText;
    }

    public String getAyaTextEmlaey() {
        return ayaTextEmlaey;
    }

    public void setAyaTextEmlaey(String ayaTextEmlaey) {
        this.ayaTextEmlaey = ayaTextEmlaey;
    }

    public String getMaanyAya() {
        return maanyAya;
    }

    public void setMaanyAya(String maanyAya) {
        this.maanyAya = maanyAya;
    }

    public String getEarabQuran() {
        return earabQuran;
    }

    public void setEarabQuran(String earabQuran) {
        this.earabQuran = earabQuran;
    }

    public String getReasonsOfVerses() {
        return reasonsOfVerses;
    }

    public void setReasonsOfVerses(String reasonsOfVerses) {
        this.reasonsOfVerses = reasonsOfVerses;
    }

    public String getTafseerSaadi() {
        return tafseerSaadi;
    }

    public void setTafseerSaadi(String tafseerSaadi) {
        this.tafseerSaadi = tafseerSaadi;
    }

    public String getTafseerMoysar() {
        return tafseerMoysar;
    }

    public void setTafseerMoysar(String tafseerMoysar) {
        this.tafseerMoysar = tafseerMoysar;
    }

    public String getTafseerBughiu() {
        return tafseerBughiu;
    }

    public void setTafseerBughiu(String tafseerBughiu) {
        this.tafseerBughiu = tafseerBughiu;
    }

    public String getAyaTextTashkil() {
        return ayaTextTashkil;
    }

    public void setAyaTextTashkil(String ayaTextTashkil) {
        this.ayaTextTashkil = ayaTextTashkil;
    }

    public boolean hasMaany() {
        return maanyAya != null && !maanyAya.trim().isEmpty();
    }

    public boolean hasEarab() {
        return earabQuran != null && !earabQuran.trim().isEmpty();
    }

    public boolean hasReasons() {
        return reasonsOfVerses != null && !reasonsOfVerses.trim().isEmpty();
    }

    public boolean hasSaadi() {
        return tafseerSaadi != null && !tafseerSaadi.trim().isEmpty();
    }

    public boolean hasMoysar() {
        return tafseerMoysar != null && !tafseerMoysar.trim().isEmpty();
    }

    public boolean hasBughiu() {
        return tafseerBughiu != null && !tafseerBughiu.trim().isEmpty();
    }
}
