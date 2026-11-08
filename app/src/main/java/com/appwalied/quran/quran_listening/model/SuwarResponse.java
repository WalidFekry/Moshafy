package com.appwalied.quran.quran_listening.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SuwarResponse {

    @SerializedName("suwar")
    private List<SurahModel> suwar;

    public List<SurahModel> getSuwar() {
        return suwar != null ? suwar : new ArrayList<>();
    }
}
