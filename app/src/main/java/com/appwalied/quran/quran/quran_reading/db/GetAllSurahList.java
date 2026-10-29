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

    private final DatabaseHelper databaseHelper;

    public GetAllSurahList(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public ArrayList<Surah> AllSurahList() {
        ArrayList<Surah> surahArrayList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT id, name_ar FROM sura", null);
            if (cursor != null && cursor.moveToFirst()) {
                int idCol = cursor.getColumnIndex(SURAH_ID);
                int nameCol = cursor.getColumnIndex(SURAH_NAME);
                do {
                    Surah surah = new Surah();
                    surah.setId(idCol != -1 ? cursor.getInt(idCol) : 0);
                    surah.setName(nameCol != -1 ? cursor.getString(nameCol) : "");
                    surahArrayList.add(surah);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
            if (db != null && db.isOpen()) db.close();
        }
        return surahArrayList;
    }

    public ArrayList<Ayat> GetAyatBySurah(int sura_id) {
        ArrayList<Ayat> ayatArrayList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT id, sura_id, verse_id, arabic_uthmanic, pages, para FROM quran_verses WHERE sura_id = ?", new String[]{String.valueOf(sura_id)});
            if (cursor != null && cursor.moveToFirst()) {
                int idCol = cursor.getColumnIndex("id");
                int suraIdCol = cursor.getColumnIndex("sura_id");
                int verseIdCol = cursor.getColumnIndex("verse_id");
                int arabicCol = cursor.getColumnIndex("arabic_uthmanic");
                int pagesCol = cursor.getColumnIndex("pages");
                int paraCol = cursor.getColumnIndex("para");

                do {
                    Ayat ayat = new Ayat();
                    ayat.setId(idCol != -1 ? cursor.getString(idCol) : "");
                    ayat.setSura_id(suraIdCol != -1 ? cursor.getString(suraIdCol) : "");
                    ayat.setVerse_id(verseIdCol != -1 ? cursor.getString(verseIdCol) : "");
                    ayat.setArabic_s(arabicCol != -1 ? cursor.getString(arabicCol) : "");
                    ayat.setPages(pagesCol != -1 ? cursor.getString(pagesCol) : "");
                    ayat.setPara(paraCol != -1 ? cursor.getString(paraCol) : "");
                    ayatArrayList.add(ayat);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
            if (db != null && db.isOpen()) db.close();
        }
        return ayatArrayList;
    }
}
