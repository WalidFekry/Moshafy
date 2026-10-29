package com.appwalied.quran.ahades;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseAcsessahdes {
    private SQLiteDatabase database;
    private final SQLiteOpenHelper openHelper;
    private static DatabaseAcsessahdes instance;

    private DatabaseAcsessahdes(Context context) {
        this.openHelper = new sqliteahdes(context.getApplicationContext());
    }

    public static synchronized DatabaseAcsessahdes getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAcsessahdes(context);
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

    public boolean insertdata(azkarmodel azkarmodel) {
        ContentValues values = new ContentValues();
        values.put(sqliteahdes.NAME, azkarmodel.getName());
        values.put(sqliteahdes.TITLE, azkarmodel.getTitle());
        opean();
        long result = database.insert(sqliteahdes.DB_table, null, values);
        return result != -1;
    }

    public boolean updatedata(azkarmodel azkarmodel) {
        ContentValues values = new ContentValues();
        values.put(sqliteahdes.NAME, azkarmodel.getName());
        values.put(sqliteahdes.TITLE, azkarmodel.getTitle());
        String[] args = {String.valueOf(azkarmodel.getId())};
        opean();
        int result = database.update(sqliteahdes.DB_table, values, "id=?", args);
        return result > 0;
    }

    public ArrayList<item_ahdes> getalldata() {
        ArrayList<item_ahdes> liststory = new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("SELECT * FROM " + sqliteahdes.DB_table, null);
            if (cursor != null && cursor.moveToFirst()) {
                int idCol = cursor.getColumnIndex(sqliteahdes.ID);
                int nameCol = cursor.getColumnIndex(sqliteahdes.NAME);
                int titleCol = cursor.getColumnIndex(sqliteahdes.TITLE);

                do {
                    int id = idCol != -1 ? cursor.getInt(idCol) : 0;
                    String name = nameCol != -1 ? cursor.getString(nameCol) : "";
                    String title = titleCol != -1 ? cursor.getString(titleCol) : "";
                    item_ahdes data = new item_ahdes(id, name, title);
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
