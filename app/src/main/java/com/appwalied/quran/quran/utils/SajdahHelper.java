package com.appwalied.quran.quran.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * كلاس مساعد يتضمن بيانات وفحوصات مواضع سجود التلاوة الـ 15 المعتمدة في المصحف الشريف.
 */
public class SajdahHelper {

    public static class SajdahLocation {
        private final int surahId;
        private final int verseId;
        private final String surahName;
        private final boolean isAgreed;

        public SajdahLocation(int surahId, int verseId, String surahName, boolean isAgreed) {
            this.surahId = surahId;
            this.verseId = verseId;
            this.surahName = surahName;
            this.isAgreed = isAgreed;
        }

        public int getSurahId() {
            return surahId;
        }

        public int getVerseId() {
            return verseId;
        }

        public String getSurahName() {
            return surahName;
        }

        public boolean isAgreed() {
            return isAgreed;
        }
    }

    private static final Map<String, SajdahLocation> SAJDAH_MAP = new HashMap<>();
    private static final List<SajdahLocation> SAJDAH_LIST = new ArrayList<>();

    static {
        addSajdah(7, 206, "الأعراف", true);
        addSajdah(13, 15, "الرعد", true);
        addSajdah(16, 50, "النحل", true);
        addSajdah(17, 109, "الإسراء", true);
        addSajdah(19, 58, "مريم", true);
        addSajdah(22, 18, "الحج", true);
        addSajdah(22, 77, "الحج", false);
        addSajdah(25, 60, "الفرقان", true);
        addSajdah(27, 26, "النمل", true);
        addSajdah(32, 15, "السجدة", true);
        addSajdah(38, 24, "ص", false);
        addSajdah(41, 38, "فصلت", true);
        addSajdah(53, 62, "النجم", true);
        addSajdah(84, 21, "الانشقاق", true);
        addSajdah(96, 19, "العلق", true);
    }

    private static void addSajdah(int surahId, int verseId, String surahName, boolean isAgreed) {
        SajdahLocation loc = new SajdahLocation(surahId, verseId, surahName, isAgreed);
        SAJDAH_MAP.put(getKey(surahId, verseId), loc);
        SAJDAH_LIST.add(loc);
    }

    private static String getKey(int surahId, int verseId) {
        return surahId + "_" + verseId;
    }

    /**
     * فحص ما إذا كانت الآية المحددة آية سجدة تلاوة.
     */
    public static boolean isSajdahAyah(int surahId, int verseId) {
        return SAJDAH_MAP.containsKey(getKey(surahId, verseId));
    }

    /**
     * جلب معلومات السجدة للآية المحددة إن وجدت.
     */
    public static SajdahLocation getSajdahLocation(int surahId, int verseId) {
        return SAJDAH_MAP.get(getKey(surahId, verseId));
    }

    /**
     * الحصول على جميع مواضع السجود بالكامل.
     */
    public static List<SajdahLocation> getAllSajdahs() {
        return new ArrayList<>(SAJDAH_LIST);
    }
}
