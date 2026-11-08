package com.appwalied.quran.quran_listening.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ReciterModel {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("letter")
    private String letter;

    @SerializedName("moshaf")
    private List<MoshafModel> moshaf;

    public int getId() {
        return id;
    }

    public String getName() {
        return name != null ? name : "";
    }

    public String getLetter() {
        return letter != null ? letter : "";
    }

    public List<MoshafModel> getMoshaf() {
        return moshaf != null ? moshaf : new ArrayList<>();
    }

    public MoshafModel getMainMoshaf() {
        if (moshaf != null && !moshaf.isEmpty()) {
            return moshaf.get(0);
        }
        return null;
    }
}
