package com.appwalied.quran.utils;

import android.content.Context;
import android.icu.util.IslamicCalendar;
import com.appwalied.quran.utils.shared_helper.SharedHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HijriCalendarHelper {

    public static final String KEY_HIJRI_OFFSET = "KEY_HIJRI_OFFSET";

    private static final String[] HIJRI_MONTHS = {
            "محرم", "صفر", "ربيع الأول", "ربيع الثاني",
            "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
            "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
    };

    private static final String[] DAY_NAMES = {
            "الأحد", "الإثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت"
    };

    public static int getOffset(Context context) {
        return SharedHelper.getInt(context, KEY_HIJRI_OFFSET, 0);
    }

    public static void setOffset(Context context, int offset) {
        SharedHelper.putInt(context, KEY_HIJRI_OFFSET, offset);
    }

    public static class HijriDateResult {
        public final String dayName;
        public final int day;
        public final int month; // 1-12
        public final String monthName;
        public final int year;

        public HijriDateResult(String dayName, int day, int month, String monthName, int year) {
            this.dayName = dayName;
            this.day = day;
            this.month = month;
            this.monthName = monthName;
            this.year = year;
        }

        public String getFormattedHijriDate() {
            return dayName + "، " + day + " " + monthName + " " + year + " هـ";
        }
    }

    public static HijriDateResult getHijriDate(Context context) {
        int offset = getOffset(context);
        return getHijriDateWithOffset(offset);
    }

    public static HijriDateResult getHijriDateWithOffset(int offset) {
        Calendar cal = Calendar.getInstance();
        if (offset != 0) {
            cal.add(Calendar.DAY_OF_MONTH, offset);
        }

        int dayOfWeekIndex = cal.get(Calendar.DAY_OF_WEEK) - 1; // Calendar.SUNDAY = 1 -> index 0
        if (dayOfWeekIndex < 0 || dayOfWeekIndex >= DAY_NAMES.length) {
            dayOfWeekIndex = 0;
        }
        String dayName = DAY_NAMES[dayOfWeekIndex];

        int day;
        int monthIndex; // 0..11
        int year;

        try {
            IslamicCalendar islamicCalendar = new IslamicCalendar();
            islamicCalendar.setTime(cal.getTime());

            year = islamicCalendar.get(IslamicCalendar.YEAR);
            monthIndex = islamicCalendar.get(IslamicCalendar.MONTH); // 0-based
            day = islamicCalendar.get(IslamicCalendar.DAY_OF_MONTH);

            if (monthIndex < 0 || monthIndex >= 12) {
                monthIndex = 0;
            }
        } catch (Exception e) {
            int[] fallback = calculateHijriFallback(cal.getTime());
            day = fallback[0];
            monthIndex = fallback[1] - 1;
            year = fallback[2];
        }

        String monthName = HIJRI_MONTHS[monthIndex];

        return new HijriDateResult(dayName, day, monthIndex + 1, monthName, year);
    }

    public static String getGregorianDateFormatted() {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy 'م'", Locale.forLanguageTag("ar"));
        return sdf.format(new Date());
    }

    public static String getOffsetSpannableText(int offset) {
        if (offset == 0) {
            return "التاريخ الافتراضي";
        } else if (offset > 0) {
            return "تقديم " + offset + (offset == 1 ? " يوم" : " أيام");
        } else {
            int positiveOffset = Math.abs(offset);
            return "تأخير " + positiveOffset + (positiveOffset == 1 ? " يوم" : " أيام");
        }
    }

    private static int[] calculateHijriFallback(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);

        int m = month;
        int y = year;
        if (m < 3) {
            y -= 1;
            m += 12;
        }

        int a = y / 100;
        int b = 2 - a + a / 4;
        long jd = (long) (365.25 * (y + 4716)) + (int) (30.6001 * (m + 1)) + day + b - 1524;

        long l = jd - 1948440 + 10632;
        long n = (l - 1) / 10631;
        l = l - 10631 * n + 354;
        long j = ((10985 - l) / 5316) * ((30 * l) / 17719) + ((l) / 5670) * ((43 * l) / 15238);
        l = l - ((30 - j) / 15) * ((17719 * j) / 30) - (j / 16) * ((15238 * j) / 43) + 29;

        int hijriMonth = (int) ((24 * l) / 709);
        int hijriDay = (int) (l - ((709 * hijriMonth) / 24));
        int hijriYear = (int) (30 * n + j - 30);

        return new int[]{hijriDay, hijriMonth, hijriYear};
    }
}
