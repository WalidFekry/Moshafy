package com.appwalied.quran.quran_listening.model;

import com.google.gson.annotations.SerializedName;

public class SurahModel {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private int type; // 0 = Meccan, 1 = Medinan

    @SerializedName("start_page")
    private int startPage;

    @SerializedName("end_page")
    private int endPage;

    @SerializedName("makkia")
    private int makkia; // 1 = Meccan, 0 = Medinan

    public SurahModel() {}

    public SurahModel(int id, String name, int makkia) {
        this.id = id;
        this.name = name;
        this.makkia = makkia;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name != null ? name : "";
    }

    public int getType() {
        return type;
    }

    public int getStartPage() {
        return startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public boolean isMakkia() {
        return makkia == 1 || type == 0;
    }
}
