package com.appwalied.quran.tadabbur.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;

import com.appwalied.quran.tadabbur.model.QuranV2Ayah;
import com.appwalied.quran.tadabbur.model.QuranV2Juz;
import com.appwalied.quran.tadabbur.model.QuranV2Page;
import com.appwalied.quran.tadabbur.model.QuranV2Surah;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuranV2Repository {

    private static volatile QuranV2Repository instance;
    private final QuranV2DatabaseHelper helper;
    private final ExecutorService executor;
    private final Handler mainHandler;

    public interface Callback<T> {
        void onResult(T result);
    }

    public enum SearchScope {
        ALL, AYAH_TEXT, MAANY, EARAB, TAFSEER
    }

    private QuranV2Repository(Context context) {
        helper = new QuranV2DatabaseHelper(context.getApplicationContext());
        executor = Executors.newFixedThreadPool(4);
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public static QuranV2Repository getInstance(Context context) {
        if (instance == null) {
            synchronized (QuranV2Repository.class) {
                if (instance == null) {
                    instance = new QuranV2Repository(context);
                }
            }
        }
        return instance;
    }

    public void getAllSurahs(Callback<List<QuranV2Surah>> callback) {
        executor.execute(() -> {
            List<QuranV2Surah> list = new ArrayList<>();
            SQLiteDatabase db = helper.getReadableDatabase();
            String sql = "SELECT sora, sora_name_ar, sora_name_en, COUNT(*), MIN(page), MAX(page), MIN(jozz), MAX(jozz) " +
                    "FROM quran GROUP BY sora ORDER BY sora ASC";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int sora = cursor.getInt(0);
                    String nameAr = cursor.getString(1);
                    String nameEn = cursor.getString(2);
                    int count = cursor.getInt(3);
                    int minPage = cursor.getInt(4);
                    int maxPage = cursor.getInt(5);
                    int minJozz = cursor.getInt(6);
                    int maxJozz = cursor.getInt(7);
                    list.add(new QuranV2Surah(sora, nameAr, nameEn, count, minPage, maxPage, minJozz, maxJozz));
                }
                cursor.close();
            }
            mainHandler.post(() -> callback.onResult(list));
        });
    }

    public void getSurahName(int soraNumber, Callback<String> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = helper.getReadableDatabase();
            String name = "";
            String sql = "SELECT sora_name_ar FROM quran WHERE sora = ? LIMIT 1";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(soraNumber)});
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

    public void getAyahsBySurah(int suraNumber, Callback<List<QuranV2Ayah>> callback) {
        executor.execute(() -> {
            List<QuranV2Ayah> list = new ArrayList<>();
            SQLiteDatabase db = helper.getReadableDatabase();
            String sql = "SELECT * FROM quran WHERE sora = ? ORDER BY aya_no ASC";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(suraNumber)});
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    list.add(mapAyah(cursor));
                }
                cursor.close();
            }
            mainHandler.post(() -> callback.onResult(list));
        });
    }

    public void getAyahsByPage(int pageNumber, Callback<List<QuranV2Ayah>> callback) {
        executor.execute(() -> {
            List<QuranV2Ayah> list = new ArrayList<>();
            SQLiteDatabase db = helper.getReadableDatabase();
            String sql = "SELECT * FROM quran WHERE page = ? ORDER BY id ASC";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(pageNumber)});
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    list.add(mapAyah(cursor));
                }
                cursor.close();
            }
            mainHandler.post(() -> callback.onResult(list));
        });
    }

    public void getAyahsByJuz(int juzNumber, Callback<List<QuranV2Ayah>> callback) {
        executor.execute(() -> {
            List<QuranV2Ayah> list = new ArrayList<>();
            SQLiteDatabase db = helper.getReadableDatabase();
            String sql = "SELECT * FROM quran WHERE jozz = ? ORDER BY id ASC";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(juzNumber)});
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    list.add(mapAyah(cursor));
                }
                cursor.close();
            }
            mainHandler.post(() -> callback.onResult(list));
        });
    }

    public void getAllJuzs(Callback<List<QuranV2Juz>> callback) {
        executor.execute(() -> {
            List<QuranV2Juz> list = new ArrayList<>();
            SQLiteDatabase db = helper.getReadableDatabase();
            String sql = "SELECT jozz, sora, sora_name_ar, aya_no, page, COUNT(*) FROM quran GROUP BY jozz ORDER BY jozz ASC";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int jNum = cursor.getInt(0);
                    int sora = cursor.getInt(1);
                    String soraNameAr = cursor.getString(2);
                    int ayaNo = cursor.getInt(3);
                    int page = cursor.getInt(4);
                    int total = cursor.getInt(5);
                    list.add(new QuranV2Juz(jNum, sora, soraNameAr, ayaNo, page, total));
                }
                cursor.close();
            }
            mainHandler.post(() -> callback.onResult(list));
        });
    }

    public void getAllPages(Callback<List<QuranV2Page>> callback) {
        executor.execute(() -> {
            List<QuranV2Page> list = new ArrayList<>();
            SQLiteDatabase db = helper.getReadableDatabase();
            String sql = "SELECT page, jozz, sora_name_ar, COUNT(*) FROM quran GROUP BY page ORDER BY page ASC";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int pageNum = cursor.getInt(0);
                    int jozz = cursor.getInt(1);
                    String surahName = cursor.getString(2);
                    int count = cursor.getInt(3);
                    list.add(new QuranV2Page(pageNum, jozz, surahName, count));
                }
                cursor.close();
            }
            mainHandler.post(() -> callback.onResult(list));
        });
    }

    public void getAyahById(int id, Callback<QuranV2Ayah> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = helper.getReadableDatabase();
            String sql = "SELECT * FROM quran WHERE id = ? LIMIT 1";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});
            QuranV2Ayah ayah = null;
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    ayah = mapAyah(cursor);
                }
                cursor.close();
            }
            QuranV2Ayah finalAyah = ayah;
            mainHandler.post(() -> callback.onResult(finalAyah));
        });
    }

    public void getAyahBySoraAndAya(int sora, int ayaNo, Callback<QuranV2Ayah> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = helper.getReadableDatabase();
            String sql = "SELECT * FROM quran WHERE sora = ? AND aya_no = ? LIMIT 1";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(sora), String.valueOf(ayaNo)});
            QuranV2Ayah ayah = null;
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    ayah = mapAyah(cursor);
                }
                cursor.close();
            }
            QuranV2Ayah finalAyah = ayah;
            mainHandler.post(() -> callback.onResult(finalAyah));
        });
    }

    public void search(String queryText, SearchScope scope, Callback<List<QuranV2Ayah>> callback) {
        executor.execute(() -> {
            List<QuranV2Ayah> results = new ArrayList<>();
            if (queryText == null || queryText.trim().isEmpty()) {
                mainHandler.post(() -> callback.onResult(results));
                return;
            }

            String query = queryText.trim();
            String normalizedQuery = normalizeArabic(query);
            SQLiteDatabase db = helper.getReadableDatabase();

            String sql = "SELECT * FROM quran ORDER BY id ASC";
            Cursor cursor = db.rawQuery(sql, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    QuranV2Ayah ayah = mapAyah(cursor);
                    boolean match = false;

                    switch (scope) {
                        case AYAH_TEXT:
                            match = matchesText(ayah.getAyaTextEmlaey(), normalizedQuery) ||
                                    matchesText(ayah.getAyaText(), query);
                            break;
                        case MAANY:
                            match = matchesText(ayah.getMaanyAya(), query);
                            break;
                        case EARAB:
                            match = matchesText(ayah.getEarabQuran(), query);
                            break;
                        case TAFSEER:
                            match = matchesText(ayah.getTafseerMoysar(), query) ||
                                    matchesText(ayah.getTafseerSaadi(), query) ||
                                    matchesText(ayah.getTafseerBughiu(), query);
                            break;
                        case ALL:
                        default:
                            match = matchesText(ayah.getAyaTextEmlaey(), normalizedQuery) ||
                                    matchesText(ayah.getAyaText(), query) ||
                                    matchesText(ayah.getSoraNameAr(), query) ||
                                    matchesText(ayah.getMaanyAya(), query) ||
                                    matchesText(ayah.getEarabQuran(), query) ||
                                    matchesText(ayah.getTafseerMoysar(), query) ||
                                    matchesText(ayah.getTafseerSaadi(), query) ||
                                    matchesText(ayah.getTafseerBughiu(), query);
                            break;
                    }

                    if (match) {
                        results.add(ayah);
                        if (results.size() >= 150) {
                            break;
                        }
                    }
                }
                cursor.close();
            }
            mainHandler.post(() -> callback.onResult(results));
        });
    }

    private boolean matchesText(String target, String query) {
        if (target == null || query == null || query.isEmpty()) return false;
        return target.toLowerCase().contains(query.toLowerCase()) ||
                normalizeArabic(target).contains(normalizeArabic(query));
    }

    private QuranV2Ayah mapAyah(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        int jozz = cursor.getInt(cursor.getColumnIndexOrThrow("jozz"));
        int sora = cursor.getInt(cursor.getColumnIndexOrThrow("sora"));
        String soraNameEn = cursor.getString(cursor.getColumnIndexOrThrow("sora_name_en"));
        String soraNameAr = cursor.getString(cursor.getColumnIndexOrThrow("sora_name_ar"));
        int page = cursor.getInt(cursor.getColumnIndexOrThrow("page"));
        int lineStart = cursor.getInt(cursor.getColumnIndexOrThrow("line_start"));
        int lineEnd = cursor.getInt(cursor.getColumnIndexOrThrow("line_end"));
        int ayaNo = cursor.getInt(cursor.getColumnIndexOrThrow("aya_no"));
        String ayaText = cursor.getString(cursor.getColumnIndexOrThrow("aya_text"));
        String ayaTextEmlaey = cursor.getString(cursor.getColumnIndexOrThrow("aya_text_emlaey"));
        String maanyAya = cursor.getString(cursor.getColumnIndexOrThrow("maany_aya"));
        String earabQuran = cursor.getString(cursor.getColumnIndexOrThrow("earab_quran"));
        String reasonsOfVerses = cursor.getString(cursor.getColumnIndexOrThrow("reasons_of_verses"));
        String tafseerSaadi = cursor.getString(cursor.getColumnIndexOrThrow("tafseer_saadi"));
        String tafseerMoysar = cursor.getString(cursor.getColumnIndexOrThrow("tafseer_moysar"));
        String tafseerBughiu = cursor.getString(cursor.getColumnIndexOrThrow("tafseer_bughiu"));
        String ayaTextTashkil = cursor.getString(cursor.getColumnIndexOrThrow("aya_text_tashkil"));

        return new QuranV2Ayah(id, jozz, sora, soraNameEn, soraNameAr, page, lineStart, lineEnd, ayaNo,
                ayaText, ayaTextEmlaey, maanyAya, earabQuran, reasonsOfVerses, tafseerSaadi,
                tafseerMoysar, tafseerBughiu, ayaTextTashkil);
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
