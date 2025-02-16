package com.appwalied.quran.quran.quran_reading.utilities;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Utils {
    public static String getBnNumber(String paramString) {
        return paramString.replace("0", "0").replace("1", "1").replace("2", "2").replace("3", "3").replace("4", "4").replace("5", "5").replace("6", "6").replace("7", "7").replace("8", "8").replace("9", "9");
    }
}
