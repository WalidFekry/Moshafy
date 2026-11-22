package com.appwalied.quran.image_quran.data;

import android.content.Context;
import java.io.File;
import java.util.Locale;

public class ImageQuranConfig {

    public static final int TOTAL_PAGES = 604;
    public static final String AUDIO_BASE_URL = "https://post.walid-fekry.com/quran/mp3/";

    public static final String PREF_KEY_SELECTED_RIWAYA = "image_quran_selected_riwaya";
    public static final String PREF_KEY_LAST_PAGE_HAFS = "image_quran_last_page_hafs";
    public static final String PREF_KEY_LAST_PAGE_WARSH = "image_quran_last_page_warsh";
    public static final String PREF_KEY_AUTO_PREFETCH = "image_quran_auto_prefetch";
    public static final String PREF_KEY_KEEP_SCREEN_ON = "image_quran_keep_screen_on";

    public static int pageToPosition(int page) {
        if (page < 1) page = 1;
        if (page > TOTAL_PAGES) page = TOTAL_PAGES;
        return TOTAL_PAGES - page;
    }

    public static int positionToPage(int position) {
        int page = TOTAL_PAGES - position;
        if (page < 1) page = 1;
        if (page > TOTAL_PAGES) page = TOTAL_PAGES;
        return page;
    }

    public static String getImageUrl(Riwaya riwaya, int page) {
        return riwaya.getBaseUrl() + page + ".jpg";
    }

    public static String getAudioUrl(int page) {
        return AUDIO_BASE_URL + String.format(Locale.US, "%03d.mp3", page);
    }

    public static File getImagesDirectory(Context context, Riwaya riwaya) {
        File baseDir = context.getExternalFilesDir(null);
        if (baseDir == null) {
            baseDir = context.getFilesDir();
        }
        File dir = new File(baseDir, "quran_images/" + riwaya.getKey());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File getThumbnailsDirectory(Context context, Riwaya riwaya) {
        File baseDir = context.getExternalFilesDir(null);
        if (baseDir == null) {
            baseDir = context.getFilesDir();
        }
        File dir = new File(baseDir, "quran_thumbnails/" + riwaya.getKey());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File getAudioDirectory(Context context) {
        File baseDir = context.getExternalFilesDir(null);
        if (baseDir == null) {
            baseDir = context.getFilesDir();
        }
        File dir = new File(baseDir, "quran_audio");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File getLocalImageFile(Context context, Riwaya riwaya, int page) {
        return new File(getImagesDirectory(context, riwaya), page + ".jpg");
    }

    public static File getLocalThumbnailFile(Context context, Riwaya riwaya, int page) {
        return new File(getThumbnailsDirectory(context, riwaya), page + ".jpg");
    }

    public static File getLocalAudioFile(Context context, int page) {
        return new File(getAudioDirectory(context), String.format(Locale.US, "%03d.mp3", page));
    }

    public static boolean isImageDownloaded(Context context, Riwaya riwaya, int page) {
        File file = getLocalImageFile(context, riwaya, page);
        if (file.exists()) {
            if (file.length() > 2048) {
                return true;
            } else {
                file.delete();
            }
        }
        return false;
    }

    public static boolean isAudioDownloaded(Context context, int page) {
        File file = getLocalAudioFile(context, page);
        if (file.exists()) {
            if (file.length() > 4096) {
                return true;
            } else {
                file.delete();
            }
        }
        return false;
    }
}
