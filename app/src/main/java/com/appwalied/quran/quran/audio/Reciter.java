package com.appwalied.quran.quran.audio;

public class Reciter {
    private String id;
    private String name;
    private String arabicName;
    private String subfolder;
    private String bitrate;
    private boolean isFavorite;

    public Reciter(String id, String name, String arabicName, String subfolder, String bitrate, boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.arabicName = arabicName;
        this.subfolder = subfolder;
        this.bitrate = bitrate;
        this.isFavorite = isFavorite;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArabicName() {
        if (arabicName != null && !arabicName.trim().isEmpty()) {
            return arabicName;
        }
        return name;
    }

    public String getSubfolder() {
        return subfolder;
    }

    public String getBitrate() {
        return bitrate;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
