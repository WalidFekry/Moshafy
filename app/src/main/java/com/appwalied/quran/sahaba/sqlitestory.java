package com.appwalied.quran.sahaba;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class sqlitestory extends SQLiteAssetHelper {
    public static final String DB_NAME= "story.db";
    public static final int DB_VERGIN=1;
    public static final String DB_table="story";
    public static final String HEADER="header";
    public static final String TITLE="title";
    public sqlitestory(Context context) {
        super(context, DB_NAME, null, DB_VERGIN);
    }
}

