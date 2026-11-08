package com.appwalied.quran.quran_listening.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MoshafModel {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("server")
    private String server;

    @SerializedName("surah_total")
    private int surahTotal;

    @SerializedName("surah_list")
    private String surahList;

    public int getId() {
        return id;
    }

    public String getName() {
        return name != null ? name : "";
    }

    public String getServer() {
        if (server == null) return "";
        return server.endsWith("/") ? server : server + "/";
    }

    public int getSurahTotal() {
        return surahTotal;
    }

    public String getSurahList() {
        return surahList != null ? surahList : "";
    }

    public List<Integer> getAvailableSurahIds() {
        List<Integer> list = new ArrayList<>();
        if (surahList == null || surahList.trim().isEmpty()) {
            return list;
        }
        String[] parts = surahList.split(",");
        for (String p : parts) {
            try {
                list.add(Integer.parseInt(p.trim()));
            } catch (NumberFormatException ignored) {}
        }
        return list;
    }
}
