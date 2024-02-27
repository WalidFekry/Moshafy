package com.appwalied.quran.islamicstory;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class sqlitenewstory extends SQLiteAssetHelper {
    public static final String DB_NAME= "storynew.db";
    public static final int DB_VERGIN=1;
    public static final String DB_table="storynew";
    public static final String HEADER="header";
    public static final String TITLE="title";
    public sqlitenewstory(Context context) {
        super(context, DB_NAME, null, DB_VERGIN);
    }
}
