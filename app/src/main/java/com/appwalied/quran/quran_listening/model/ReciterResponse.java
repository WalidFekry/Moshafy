package com.appwalied.quran.quran_listening.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ReciterResponse {

    @SerializedName("reciters")
    private List<ReciterModel> reciters;

    public List<ReciterModel> getReciters() {
        return reciters != null ? reciters : new ArrayList<>();
    }
}
