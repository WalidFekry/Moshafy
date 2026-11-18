package com.appwalied.quran.tadabbur.db;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class QuranV2DatabaseHelper extends SQLiteAssetHelper {

    public static final String DB_NAME = "quran_v2.db";
    public static final int DB_VERSION = 1;

    public QuranV2DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        setForcedUpgrade();
    }
}
