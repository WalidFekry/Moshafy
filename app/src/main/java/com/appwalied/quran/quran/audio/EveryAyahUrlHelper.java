package com.appwalied.quran.quran.audio;

import java.util.Locale;

public class EveryAyahUrlHelper {

    private static final String BASE_URL = "https://everyayah.com/data/";

    public static String buildAudioUrl(String reciterFolder, int suraId, int ayahId) {
        if (reciterFolder == null || reciterFolder.trim().isEmpty()) {
            reciterFolder = "Alafasy_128kbps";
        }
        String suraFormatted = String.format(Locale.US, "%03d", suraId);
        String ayahFormatted = String.format(Locale.US, "%03d", ayahId);
        return BASE_URL + reciterFolder.trim() + "/" + suraFormatted + ayahFormatted + ".mp3";
    }
}
