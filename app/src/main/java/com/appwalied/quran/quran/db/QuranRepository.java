package com.appwalied.quran.quran.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;

import com.appwalied.quran.quran.db.models.Ayah;
import com.appwalied.quran.quran.db.models.Juz;
import com.appwalied.quran.quran.db.models.Surah;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuranRepository {

    private static volatile QuranRepository instance;
    private final QuranDatabaseHelper helper;
    private final ExecutorService executor;
    private final Handler mainHandler;

    public interface Callback<T> {
        void onResult(T result);
    }

    private QuranRepository(Context context) {
        helper = new QuranDatabaseHelper(context.getApplicationContext());
        executor = Executors.newFixedThreadPool(4);
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public static QuranRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (QuranRepository.class) {
                if (instance == null) {
                    instance = new QuranRepository(context);
                }
            }
        }
        return instance;
    }

    public void getAllSurahs(Callback<List<Surah>> callback) {
        executor.execute(() -> {
            List<Surah> surahs = new ArrayList<>();
            SQLiteDatabase db = helper.getReadableDatabase();
            String sql = "SELECT s.id, s.name_ar, MIN(qv.pages) AS startPage, MAX(qv.pages) AS endPage, COUNT(qv.id) AS versesCount " +
                    "FROM sura s " +
                    "JOIN quran_verses qv ON s.id = qv.sura_id " +
                    "GROUP BY s.id, s.name_ar " +
                    "ORDER BY s.id ASC";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String nameAr = cursor.getString(1);
                    int startPage = cursor.getInt(2);
                    int endPage = cursor.getInt(3);
                    int count = cursor.getInt(4);
                    surahs.add(new Surah(id, nameAr, startPage, endPage, count));
                }
                cursor.close();
            }
            mainHandler.post(() -> callback.onResult(surahs));
        });
    }

    public void getVersesBySurah(int suraId, Callback<List<Ayah>> callback) {
        executor.execute(() -> {
            List<Ayah> verses = new ArrayList<>();
            SQLiteDatabase db = helper.getReadableDatabase();
            String sql = "SELECT qv.id, qv.sura_id, qv.verse_id, qv.arabic_uthmanic, qv.pages, qv.para, s.name_ar " +
                    "FROM quran_verses qv " +
                    "JOIN sura s ON qv.sura_id = s.id " +
                    "WHERE qv.sura_id = ? " +
                    "ORDER BY qv.verse_id ASC";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(suraId)});
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    verses.add(mapAyah(cursor));
                }
                cursor.close();
            }
            mainHandler.post(() -> callback.onResult(verses));
        });
    }

    public void getVersesByPage(int pageNumber, Callback<List<Ayah>> callback) {
        executor.execute(() -> {
            List<Ayah> verses = new ArrayList<>();
            SQLiteDatabase db = helper.getReadableDatabase();
            String sql = "SELECT qv.id, qv.sura_id, qv.verse_id, qv.arabic_uthmanic, qv.pages, qv.para, s.name_ar " +
                    "FROM quran_verses qv " +
                    "JOIN sura s ON qv.sura_id = s.id " +
                    "WHERE qv.pages = ? " +
                    "ORDER BY qv.id ASC";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(pageNumber)});
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    verses.add(mapAyah(cursor));
                }
                cursor.close();
            }
            mainHandler.post(() -> callback.onResult(verses));
        });
    }

    public void getVersesByJuz(int juzNumber, Callback<List<Ayah>> callback) {
        executor.execute(() -> {
            List<Ayah> verses = new ArrayList<>();
            SQLiteDatabase db = helper.getReadableDatabase();
            String sql = "SELECT qv.id, qv.sura_id, qv.verse_id, qv.arabic_uthmanic, qv.pages, qv.para, s.name_ar " +
                    "FROM quran_verses qv " +
                    "JOIN sura s ON qv.sura_id = s.id " +
                    "WHERE qv.para = ? " +
                    "ORDER BY qv.id ASC";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(juzNumber)});
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    verses.add(mapAyah(cursor));
                }
                cursor.close();
            }
            mainHandler.post(() -> callback.onResult(verses));
        });
    }

    public void getAllJuzs(Callback<List<Juz>> callback) {
        executor.execute(() -> {
            List<Juz> juzList = new ArrayList<>();
            SQLiteDatabase db = helper.getReadableDatabase();
            String sql = "SELECT qv.para AS juzNumber, qv.sura_id AS startSuraId, s.name_ar AS startSuraName, qv.verse_id AS startAyahId, qv.pages AS startPage " +
                    "FROM quran_verses qv " +
                    "JOIN sura s ON qv.sura_id = s.id " +
                    "GROUP BY qv.para " +
                    "ORDER BY qv.para ASC";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int jNum = cursor.getInt(0);
                    int sId = cursor.getInt(1);
                    String sName = cursor.getString(2);
                    int aId = cursor.getInt(3);
                    int pNum = cursor.getInt(4);
                    juzList.add(new Juz(jNum, sId, sName, aId, pNum));
                }
                cursor.close();
            }
            mainHandler.post(() -> callback.onResult(juzList));
        });
    }

    public void searchVerses(String queryText, Callback<List<Ayah>> callback) {
        executor.execute(() -> {
            List<Ayah> results = new ArrayList<>();
            if (queryText == null || queryText.trim().isEmpty()) {
                mainHandler.post(() -> callback.onResult(results));
                return;
            }

            String normalizedQuery = normalizeArabic(queryText);
            if (normalizedQuery.isEmpty()) {
                mainHandler.post(() -> callback.onResult(results));
                return;
            }

            SQLiteDatabase db = helper.getReadableDatabase();
            String sql = "SELECT qv.id, qv.sura_id, qv.verse_id, qv.arabic_uthmanic, qv.pages, qv.para, s.name_ar " +
                    "FROM quran_verses qv " +
                    "JOIN sura s ON qv.sura_id = s.id " +
                    "ORDER BY qv.id ASC";

            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Ayah ayah = mapAyah(cursor);
                    String normalizedVerse = normalizeArabic(ayah.getArabicUthmanic());
                    if (normalizedVerse.contains(normalizedQuery)) {
                        results.add(ayah);
                        if (results.size() >= 100) {
                            break;
                        }
                    }
                }
                cursor.close();
            }
            mainHandler.post(() -> callback.onResult(results));
        });
    }

    public void getAyah(int suraId, int verseId, Callback<Ayah> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = helper.getReadableDatabase();
            String sql = "SELECT qv.id, qv.sura_id, qv.verse_id, qv.arabic_uthmanic, qv.pages, qv.para, s.name_ar " +
                    "FROM quran_verses qv " +
                    "JOIN sura s ON qv.sura_id = s.id " +
                    "WHERE qv.sura_id = ? AND qv.verse_id = ? " +
                    "LIMIT 1";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(suraId), String.valueOf(verseId)});
            Ayah ayah = null;
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    ayah = mapAyah(cursor);
                }
                cursor.close();
            }
            Ayah finalAyah = ayah;
            mainHandler.post(() -> callback.onResult(finalAyah));
        });
    }

    public void getSurahName(int suraId, Callback<String> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = helper.getReadableDatabase();
            String name = "";
            Cursor cursor = db.rawQuery("SELECT name_ar FROM sura WHERE id = ?", new String[]{String.valueOf(suraId)});
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    name = cursor.getString(0);
                }
                cursor.close();
            }
            String finalName = name;
            mainHandler.post(() -> callback.onResult(finalName));
        });
    }

    private Ayah mapAyah(Cursor cursor) {
        int id = cursor.getInt(0);
        int suraId = cursor.getInt(1);
        int verseId = cursor.getInt(2);
        String text = cursor.getString(3);
        int page = cursor.getInt(4);
        int para = cursor.getInt(5);
        String suraName = cursor.getString(6);
        return new Ayah(id, suraId, verseId, text, page, para, suraName);
    }

    public static String normalizeArabic(String input) {
        if (input == null) return "";
        String text = input.replaceAll("[\\u0610-\\u061A\\u064B-\\u065F\\u0670\\u06D6-\\u06DC\\u06DF-\\u06E8\\u06EA-\\u06ED]", "");
        text = text.replace('أ', 'ا')
                .replace('إ', 'ا')
                .replace('آ', 'ا')
                .replace('ٱ', 'ا')
                .replace('ى', 'ي')
                .replace('ة', 'ه');
        return text.trim().toLowerCase();
    }
}
