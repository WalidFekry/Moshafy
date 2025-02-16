package com.appwalied.quran.quran.quran_reading.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.appwalied.quran.quran.quran_reading.model.Ayat;
import com.appwalied.quran.quran.quran_reading.model.Surah;

import java.util.ArrayList;

public class GetAllSurahList {

    public static final String SURAH_ID = "id";
    public static final String SURAH_NAME = "name_ar";

    private Cursor cursor;
    private DatabaseHelper databaseHelper;


    public GetAllSurahList(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }


    public ArrayList<Surah> AllSurahList() {
        ArrayList<Surah> surahArrayList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT id,name_ar FROM sura", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Surah surah = new Surah();
            surah.setId(cursor.getInt(cursor.getColumnIndex(SURAH_ID)));
            surah.setName(cursor.getString(cursor.getColumnIndex(SURAH_NAME)));


            surahArrayList.add(surah);

            cursor.moveToNext();

        }

        cursor.close();
        db.close();

        return surahArrayList;
    }


    public ArrayList<Ayat> GetAyatBySurah(int sura_id) {
        ArrayList<Ayat> ayatArrayList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM quran_verses WHERE sura_id =" + sura_id, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Ayat ayat = new Ayat();
            ayat.setId(cursor.getString(cursor.getColumnIndex("id")));
            ayat.setSura_id(cursor.getString(cursor.getColumnIndex("sura_id")));
            ayat.setVerse_id(cursor.getString(cursor.getColumnIndex("verse_id")));
            ayat.setArabic_s(cursor.getString(cursor.getColumnIndex("arabic_uthmanic")));
            ayat.setPages(cursor.getString(cursor.getColumnIndex("pages")));
            ayat.setPara(cursor.getString(cursor.getColumnIndex("para")));

            ayatArrayList.add(ayat);

            cursor.moveToNext();

        }

        cursor.close();
        db.close();

        return ayatArrayList;
    }

}
