package com.appwalied.quran.sonan;


import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class sqliteAya extends SQLiteAssetHelper {
    public static final String DB_NAME= "snan.db";
    public static final int DB_VERGIN=1;
    public static final String ID="id";
    public static final int NUM=1;
    public static final String DB_table="snan";
    public static final String NAME="name";
    public static final String TITLE="title";
    public sqliteAya(Context context) {
        super(context, DB_NAME, null, DB_VERGIN);
    }
}
