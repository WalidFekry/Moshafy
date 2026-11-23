package com.appwalied.quran.utils;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * دالة مركزية موحدة لتطبيع النصوص العربية ومقارنتها دون التأثر بالتشكيل والهمزات.
 */
public class ArabicNormalizer {

    // أنماط التشكيل والعلامات الصوتية والرموز القرآنية والتطويل
    private static final Pattern DIACRITICS_PATTERN = Pattern.compile(
            "[\\u0610-\\u061A\\u064B-\\u065F\\u0670\\u06D6-\\u06DC\\u06DF-\\u06E8\\u06EA-\\u06ED\\u06E9\\u0640]"
    );

    private ArabicNormalizer() {
        // Private constructor for utility class
    }

    /**
     * تطبيع شامل للنص العربي: إزالة التشكيل، توحيد الألفات والياء والتاء المربوطة والمسافات.
     */
    public static String normalize(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        // 1. إزالة جميع الحركات والعلامات الصوتية والقرآنية
        String text = DIACRITICS_PATTERN.matcher(input).replaceAll("");

        // 2. توحيد الحروف المتقاربة
        StringBuilder sb = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
                case 'أ':
                case 'إ':
                case 'آ':
                case 'ٱ':
                    sb.append('ا');
                    break;
                case 'ى':
                    sb.append('ي');
                    break;
                case 'ة':
                    sb.append('ه');
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }

        // 3. تنظيف المسافات وتحويل الأحرف الإنجليزية للـ Lowercase إن وجدت
        return sb.toString().replaceAll("\\s+", " ").trim().toLowerCase(Locale.ROOT);
    }

    /**
     * إزالة التشكيل فقط دون التعديل على أشكال الحروف (الألف/الياء/التاء المربوطة).
     */
    public static String normalizeDiacriticsOnly(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        String text = DIACRITICS_PATTERN.matcher(input).replaceAll("");
        return text.replaceAll("\\s+", " ").trim().toLowerCase(Locale.ROOT);
    }

    /**
     * فحص مطابقة نص البحث للهدف بشكل ذكي وغير حساس للتشكيل.
     */
    public static boolean matches(String source, String query) {
        if (source == null || query == null) {
            return false;
        }

        String normQuery = normalize(query);
        if (normQuery.isEmpty()) {
            return true;
        }

        String normSource = normalize(source);
        if (normSource.contains(normQuery)) {
            return true;
        }

        // دعم البحث بالكلمات المتعددة المقسمة بمسافات
        String[] queryTokens = normQuery.split(" ");
        if (queryTokens.length > 1) {
            for (String token : queryTokens) {
                if (!token.isEmpty() && !normSource.contains(token)) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }
}
