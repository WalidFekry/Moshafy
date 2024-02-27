package com.appwalied.quran.ahades;


import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class sqliteahdes extends SQLiteAssetHelper {
    public static final String DB_NAME= "ahdes.db";
    public static final int DB_VERGIN=1;
    public static final String ID="id";
    public static final String DB_table="ahdes";
    public static final String NAME="name";
    public static final String TITLE="title";
    public sqliteahdes(Context context) {
        super(context, DB_NAME, null, DB_VERGIN);
    }
}
