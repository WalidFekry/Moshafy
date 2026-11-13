package com.appwalied.quran.quran.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import androidx.core.content.res.ResourcesCompat;

import com.appwalied.quran.R;

public class QuranFontHelper {

    public static final String FONT_UTHMANII = "uthmanii";
    public static final String FONT_QURAN_MSG = "quran_font";

    public static Typeface getFont(Context context, String fontKey) {
        if (FONT_QURAN_MSG.equals(fontKey)) {
            try {
                return ResourcesCompat.getFont(context, R.font.quran_font);
            } catch (Exception e) {
                return Typeface.DEFAULT;
            }
        }
        try {
            return ResourcesCompat.getFont(context, R.font.uthmanii);
        } catch (Exception e) {
            return Typeface.DEFAULT;
        }
    }

    public static String getFontDisplayName(String fontKey) {
        if (FONT_QURAN_MSG.equals(fontKey)) {
            return "خط القرآن العادي";
        }
        return "الرسم العثماني الأصلي";
    }

    public static class CustomTypefaceSpan extends MetricAffectingSpan {
        private final Typeface typeface;

        public CustomTypefaceSpan(Typeface typeface) {
            this.typeface = typeface;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            apply(ds);
        }

        @Override
        public void updateMeasureState(TextPaint paint) {
            apply(paint);
        }

        private void apply(TextPaint paint) {
            if (typeface != null) {
                paint.setTypeface(typeface);
            }
        }
    }
}
