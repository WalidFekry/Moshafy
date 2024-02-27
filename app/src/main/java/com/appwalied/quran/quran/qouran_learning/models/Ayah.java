package com.appwalied.quran.quran.qouran_learning.models;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Keep
public class Ayah implements Serializable
{

    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("audio")
    @Expose
    private String audio;
    @SerializedName("audioSecondary")
    @Expose
    private List<String> audioSecondary = null;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("numberInSurah")
    @Expose
    private Integer numberInSurah;
    @SerializedName("juz")
    @Expose
    private Integer juz;
    @SerializedName("manzil")
    @Expose
    private Integer manzil;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("ruku")
    @Expose
    private Integer ruku;
    @SerializedName("hizbQuarter")
    @Expose
    private Integer hizbQuarter;
    @SerializedName("sajda")
    @Expose
    private Boolean sajda;
    private final static long serialVersionUID = 4563856514646903511L;

    public Ayah() {
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public List<String> getAudioSecondary() {
        return audioSecondary;
    }

    public void setAudioSecondary(List<String> audioSecondary) {
        this.audioSecondary = audioSecondary;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getNumberInSurah() {
        return numberInSurah;
    }

    public void setNumberInSurah(Integer numberInSurah) {
        this.numberInSurah = numberInSurah;
    }

    public Integer getJuz() {
        return juz;
    }

    public void setJuz(Integer juz) {
        this.juz = juz;
    }

    public Integer getManzil() {
        return manzil;
    }

    public void setManzil(Integer manzil) {
        this.manzil = manzil;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRuku() {
        return ruku;
    }

    public void setRuku(Integer ruku) {
        this.ruku = ruku;
    }

    public Integer getHizbQuarter() {
        return hizbQuarter;
    }

    public void setHizbQuarter(Integer hizbQuarter) {
        this.hizbQuarter = hizbQuarter;
    }

    public Boolean getSajda() {
        return sajda;
    }

    public void setSajda(Boolean sajda) {
        this.sajda = sajda;
    }

}