package com.appwalied.quran.quran.qouran_learning.models;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Keep
public class Ayah implements Serializable {

    private static final long serialVersionUID = 4563856514646903511L;
    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("audio")
    @Expose
    private String audio;
    @SerializedName("audioSecondary")
    @Expose
    private List<String> audioSecondary;
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
    private Object sajda; // قد يكون Boolean أو Object (Map)
    private Boolean sajdaBoolean;
    private SajdaDetails sajdaObject;

    public Ayah() {
        // Constructor فارغ
    }

    // 🟢 Getter & Setter Methods
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

    public Object getSajda() {
        return sajda;
    }

    public void setSajda(Object sajda) {
        this.sajda = sajda;

        if (sajda instanceof Boolean) {
            this.sajdaBoolean = (Boolean) sajda;
            this.sajdaObject = null; // تأكد من عدم وجود قيمة قديمة
        } else if (sajda instanceof Map) {
            try {
                @SuppressWarnings("unchecked") // لتجنب التحذير
                Map<String, Object> sajdaMap = (Map<String, Object>) sajda;
                this.sajdaObject = new SajdaDetails(sajdaMap);
                this.sajdaBoolean = null; // تأكد من عدم وجود قيمة قديمة
            } catch (ClassCastException e) {
                this.sajdaObject = null; // في حالة خطأ التحويل
            }
        } else {
            this.sajdaBoolean = null;
            this.sajdaObject = null;
        }
    }

    public Boolean isSajdaBoolean() {
        return sajdaBoolean;
    }

    public SajdaDetails getSajdaObject() {
        return sajdaObject;
    }
}
