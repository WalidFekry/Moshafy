package com.appwalied.quran.quran.db;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class QuranDatabaseHelper extends SQLiteAssetHelper {

    public static final String DB_NAME = "quran.db";
    public static final int DB_VERSION = 1;

    public QuranDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        setForcedUpgrade();
    }
}
