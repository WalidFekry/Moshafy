package com.appwalied.quran.sonan;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseAcsessAya {
    private SQLiteDatabase database;
    private final SQLiteOpenHelper openHelper;
    private static DatabaseAcsessAya instance;

    private DatabaseAcsessAya(Context context) {
        this.openHelper = new sqliteAya(context.getApplicationContext());
    }

    public static synchronized DatabaseAcsessAya getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAcsessAya(context);
        }
        return instance;
    }

    public void opean() {
        this.database = this.openHelper.getWritableDatabase();
    }

    public void close() {
        if (this.database != null && this.database.isOpen()) {
            this.database.close();
        }
    }



    public ArrayList<AyaItem> getalldata() {
        ArrayList<AyaItem> liststory = new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("SELECT * FROM " + sqliteAya.DB_table, null);
            if (cursor != null && cursor.moveToFirst()) {
                int idCol = cursor.getColumnIndex(sqliteAya.ID);
                int nameCol = cursor.getColumnIndex(sqliteAya.NAME);
                int titleCol = cursor.getColumnIndex(sqliteAya.TITLE);
                int numCol = cursor.getColumnIndex("num");

                do {
                    int id = idCol != -1 ? cursor.getInt(idCol) : 0;
                    String name = nameCol != -1 ? cursor.getString(nameCol) : "";
                    String title = titleCol != -1 ? cursor.getString(titleCol) : "";
                    int num = numCol != -1 ? cursor.getInt(numCol) : 0;
                    AyaItem data = new AyaItem(name, title, id, num);
                    liststory.add(data);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
            close();
        }
        return liststory;
    }
}
