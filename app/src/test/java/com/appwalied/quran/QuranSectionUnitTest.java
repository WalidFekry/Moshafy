package com.appwalied.quran;

import com.appwalied.quran.quran.audio.EveryAyahUrlHelper;
import com.appwalied.quran.quran.audio.Reciter;
import com.appwalied.quran.quran.db.QuranRepository;
import com.appwalied.quran.quran.room.QuranSettingsEntity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class QuranSectionUnitTest {

    @Test
    public void testAudioUrlConstruction_FatihahVerse1() {
        String url = EveryAyahUrlHelper.buildAudioUrl("Alafasy_128kbps", 1, 1);
        assertEquals("https://everyayah.com/data/Alafasy_128kbps/001001.mp3", url);
    }

    @Test
    public void testAudioUrlConstruction_NasVerse6() {
        String url = EveryAyahUrlHelper.buildAudioUrl("Husary_128kbps", 114, 6);
        assertEquals("https://everyayah.com/data/Husary_128kbps/114006.mp3", url);
    }

    @Test
    public void testAudioUrlConstruction_NullFolderFallback() {
        String url = EveryAyahUrlHelper.buildAudioUrl(null, 2, 255);
        assertEquals("https://everyayah.com/data/Alafasy_128kbps/002255.mp3", url);
    }

    @Test
    public void testReciterModel() {
        Reciter reciter = new Reciter("15", "Alafasy", "مشاري راشد العفاسي", "Alafasy_128kbps", "128kbps", false);
        assertEquals("15", reciter.getId());
        assertEquals("Alafasy", reciter.getName());
        assertEquals("مشاري راشد العفاسي", reciter.getArabicName());
        assertEquals("Alafasy_128kbps", reciter.getSubfolder());
        assertEquals("128kbps", reciter.getBitrate());
    }

    @Test
    public void testQuranSettingsDefaults() {
        QuranSettingsEntity defaultSettings = QuranSettingsEntity.createDefault();
        assertNotNull(defaultSettings);
        assertEquals(24, defaultSettings.getFontSize());
        assertEquals("Alafasy_128kbps", defaultSettings.getSelectedReciterFolder());
        assertEquals("uthmanii", defaultSettings.getSelectedFont());
        assertTrue(defaultSettings.isAutoPlayNextAyah());
        assertTrue(defaultSettings.isKeepScreenOn());
    }

    @Test
    public void testArabicNormalization() {
        String normalizedWithTashkeel = QuranRepository.normalizeArabic("بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ");
        String normalizedPlain = QuranRepository.normalizeArabic("بسم الله الرحمن الرحيم");
        assertEquals(normalizedPlain, normalizedWithTashkeel);

        String normalizedAlif = QuranRepository.normalizeArabic("أحَمَد إبرَاهِيم آَمَنُوا");
        assertEquals("احمد ابراهيم امنوا", normalizedAlif);
    }
}
