package com.appwalied.quran.quran.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quran_settings")
public class QuranSettingsEntity {

    @PrimaryKey
    private int id = 1;

    private int fontSize;
    private String selectedTheme;
    private String selectedReciter;
    private String selectedReciterFolder;
    private boolean autoPlayNextAyah;
    private boolean keepScreenOn;
    private int repeatCount;
    private int lastSelectedSura;
    private String selectedFont;

    public QuranSettingsEntity(int id, int fontSize, String selectedTheme, String selectedReciter, String selectedReciterFolder, boolean autoPlayNextAyah, boolean keepScreenOn, int repeatCount, int lastSelectedSura, String selectedFont) {
        this.id = id;
        this.fontSize = fontSize;
        this.selectedTheme = selectedTheme;
        this.selectedReciter = selectedReciter;
        this.selectedReciterFolder = selectedReciterFolder;
        this.autoPlayNextAyah = autoPlayNextAyah;
        this.keepScreenOn = keepScreenOn;
        this.repeatCount = repeatCount;
        this.lastSelectedSura = lastSelectedSura;
        this.selectedFont = selectedFont != null ? selectedFont : "uthmanii";
    }

    public static QuranSettingsEntity createDefault() {
        return new QuranSettingsEntity(
                1,
                24,
                "SYSTEM",
                "مشاري العفاسي",
                "Alafasy_128kbps",
                true,
                true,
                1,
                1,
                "uthmanii"
        );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getSelectedTheme() {
        return selectedTheme;
    }

    public void setSelectedTheme(String selectedTheme) {
        this.selectedTheme = selectedTheme;
    }

    public String getSelectedReciter() {
        return selectedReciter;
    }

    public void setSelectedReciter(String selectedReciter) {
        this.selectedReciter = selectedReciter;
    }

    public String getSelectedReciterFolder() {
        return selectedReciterFolder;
    }

    public void setSelectedReciterFolder(String selectedReciterFolder) {
        this.selectedReciterFolder = selectedReciterFolder;
    }

    public boolean isAutoPlayNextAyah() {
        return autoPlayNextAyah;
    }

    public void setAutoPlayNextAyah(boolean autoPlayNextAyah) {
        this.autoPlayNextAyah = autoPlayNextAyah;
    }

    public boolean isKeepScreenOn() {
        return keepScreenOn;
    }

    public void setKeepScreenOn(boolean keepScreenOn) {
        this.keepScreenOn = keepScreenOn;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public int getLastSelectedSura() {
        return lastSelectedSura;
    }

    public void setLastSelectedSura(int lastSelectedSura) {
        this.lastSelectedSura = lastSelectedSura;
    }

    public String getSelectedFont() {
        return selectedFont != null ? selectedFont : "uthmanii";
    }

    public void setSelectedFont(String selectedFont) {
        this.selectedFont = selectedFont;
    }
}
