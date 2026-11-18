package com.appwalied.quran.tadabbur.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityTadabburMainBinding;
import com.appwalied.quran.tadabbur.db.QuranV2Repository;
import com.appwalied.quran.tadabbur.db.room.TadabburBookmarkEntity;
import com.appwalied.quran.tadabbur.db.room.TadabburLocalRepository;
import com.appwalied.quran.tadabbur.model.QuranV2Juz;
import com.appwalied.quran.tadabbur.model.QuranV2Page;
import com.appwalied.quran.tadabbur.model.QuranV2Surah;
import com.appwalied.quran.tadabbur.ui.adapters.TadabburBookmarkAdapter;
import com.appwalied.quran.tadabbur.ui.adapters.TadabburJuzAdapter;
import com.appwalied.quran.tadabbur.ui.adapters.TadabburPageAdapter;
import com.appwalied.quran.tadabbur.ui.adapters.TadabburSurahAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class TadabburMainActivity extends BaseActivity {

    private ActivityTadabburMainBinding binding;
    private QuranV2Repository repository;
    private TadabburLocalRepository localRepository;

    private TadabburSurahAdapter surahAdapter;
    private TadabburJuzAdapter juzAdapter;
    private TadabburPageAdapter pageAdapter;
    private TadabburBookmarkAdapter bookmarkAdapter;

    private final List<QuranV2Surah> allSurahs = new ArrayList<>();
    private final List<QuranV2Juz> allJuzs = new ArrayList<>();
    private final List<QuranV2Page> allPages = new ArrayList<>();
    private final List<TadabburBookmarkEntity> allBookmarks = new ArrayList<>();

    private int currentTabPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTadabburMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = QuranV2Repository.getInstance(this);
        localRepository = TadabburLocalRepository.getInstance(this);

        setupToolbar();
        setupTabs();
        setupRecyclerView();
        setupClickListeners();
        setupSearchFilter();

        loadAllData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateResumeBanner();
        if (currentTabPosition == 3) {
            loadBookmarks();
        }
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupTabs() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("السور"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("الأجزاء"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("الصفحات"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("المفضلة"));

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTabPosition = tab.getPosition();
                binding.etTabFilter.setText("");
                switchTabContent();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupRecyclerView() {
        binding.rvContent.setLayoutManager(new LinearLayoutManager(this));

        surahAdapter = new TadabburSurahAdapter(surah -> {
            Intent intent = new Intent(this, TadabburReadingActivity.class);
            intent.putExtra("TYPE", "SURAH");
            intent.putExtra("SURAH_NUM", surah.getSoraNumber());
            startActivity(intent);
        });

        juzAdapter = new TadabburJuzAdapter(juz -> {
            Intent intent = new Intent(this, TadabburReadingActivity.class);
            intent.putExtra("TYPE", "JUZ");
            intent.putExtra("JUZ_NUM", juz.getJuzNumber());
            startActivity(intent);
        });

        pageAdapter = new TadabburPageAdapter(page -> {
            Intent intent = new Intent(this, TadabburReadingActivity.class);
            intent.putExtra("TYPE", "PAGE");
            intent.putExtra("PAGE_NUM", page.getPageNumber());
            startActivity(intent);
        });

        bookmarkAdapter = new TadabburBookmarkAdapter(new TadabburBookmarkAdapter.OnBookmarkClickListener() {
            @Override
            public void onBookmarkClick(TadabburBookmarkEntity bookmark) {
                Intent intent = new Intent(TadabburMainActivity.this, TadabburAyahDetailActivity.class);
                intent.putExtra("SORA", bookmark.getSora());
                intent.putExtra("AYA_NO", bookmark.getAyaNo());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(TadabburBookmarkEntity bookmark) {
                localRepository.deleteBookmarkById(bookmark.getId(), () -> loadBookmarks());
            }
        });

        binding.rvContent.setAdapter(surahAdapter);
    }

    private void setupClickListeners() {
        binding.cardSearch.setOnClickListener(v -> startActivity(new Intent(this, TadabburSearchActivity.class)));

        binding.cardResumeReading.setOnClickListener(v -> {
            int sora = localRepository.getLastReadSora();
            int page = localRepository.getLastReadPage();
            Intent intent = new Intent(this, TadabburReadingActivity.class);
            intent.putExtra("TYPE", "SURAH");
            intent.putExtra("SURAH_NUM", sora);
            intent.putExtra("PAGE_NUM", page);
            startActivity(intent);
        });
    }

    private void setupSearchFilter() {
        binding.etTabFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCurrentTab(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void updateResumeBanner() {
        int sora = localRepository.getLastReadSora();
        int aya = localRepository.getLastReadAya();
        int page = localRepository.getLastReadPage();

        repository.getSurahName(sora, name -> {
            String text = "سورة " + name + " - الآية " + aya + " (صفحة " + page + ")";
            binding.tvLastReadInfo.setText(text);
        });
    }

    private void loadAllData() {
        showLoading();

        repository.getAllSurahs(surahs -> {
            allSurahs.clear();
            if (surahs != null) allSurahs.addAll(surahs);
            if (currentTabPosition == 0) {
                surahAdapter.setSurahs(allSurahs);
                checkEmptyState(allSurahs.isEmpty());
            }
            hideLoading();
        });

        repository.getAllJuzs(juzs -> {
            allJuzs.clear();
            if (juzs != null) allJuzs.addAll(juzs);
        });

        repository.getAllPages(pages -> {
            allPages.clear();
            if (pages != null) allPages.addAll(pages);
        });
    }

    private void loadBookmarks() {
        localRepository.getAllBookmarks(bookmarks -> {
            allBookmarks.clear();
            if (bookmarks != null) allBookmarks.addAll(bookmarks);
            if (currentTabPosition == 3) {
                bookmarkAdapter.setBookmarks(allBookmarks);
                checkEmptyState(allBookmarks.isEmpty());
            }
        });
    }

    private void switchTabContent() {
        switch (currentTabPosition) {
            case 0:
                binding.rvContent.setAdapter(surahAdapter);
                surahAdapter.setSurahs(allSurahs);
                checkEmptyState(allSurahs.isEmpty());
                break;
            case 1:
                binding.rvContent.setAdapter(juzAdapter);
                juzAdapter.setJuzs(allJuzs);
                checkEmptyState(allJuzs.isEmpty());
                break;
            case 2:
                binding.rvContent.setAdapter(pageAdapter);
                pageAdapter.setPages(allPages);
                checkEmptyState(allPages.isEmpty());
                break;
            case 3:
                binding.rvContent.setAdapter(bookmarkAdapter);
                loadBookmarks();
                break;
        }
    }

    private void filterCurrentTab(String query) {
        String q = query.trim();
        if (q.isEmpty()) {
            switchTabContent();
            return;
        }

        String normQuery = QuranV2Repository.normalizeArabic(q);

        switch (currentTabPosition) {
            case 0:
                List<QuranV2Surah> filteredSurahs = new ArrayList<>();
                for (QuranV2Surah s : allSurahs) {
                    String normSurahAr = QuranV2Repository.normalizeArabic(s.getNameAr());
                    if (normSurahAr.contains(normQuery) || s.getNameEn().toLowerCase().contains(q.toLowerCase()) || String.valueOf(s.getSoraNumber()).equals(q)) {
                        filteredSurahs.add(s);
                    }
                }
                surahAdapter.setSurahs(filteredSurahs);
                checkEmptyState(filteredSurahs.isEmpty());
                break;
            case 1:
                List<QuranV2Juz> filteredJuzs = new ArrayList<>();
                for (QuranV2Juz j : allJuzs) {
                    String normSoraName = QuranV2Repository.normalizeArabic(j.getStartSoraNameAr());
                    if (String.valueOf(j.getJuzNumber()).contains(q) || normSoraName.contains(normQuery)) {
                        filteredJuzs.add(j);
                    }
                }
                juzAdapter.setJuzs(filteredJuzs);
                checkEmptyState(filteredJuzs.isEmpty());
                break;
            case 2:
                List<QuranV2Page> filteredPages = new ArrayList<>();
                for (QuranV2Page p : allPages) {
                    String normSurahs = QuranV2Repository.normalizeArabic(p.getSurahsSummary());
                    if (String.valueOf(p.getPageNumber()).contains(q) || normSurahs.contains(normQuery)) {
                        filteredPages.add(p);
                    }
                }
                pageAdapter.setPages(filteredPages);
                checkEmptyState(filteredPages.isEmpty());
                break;
            case 3:
                List<TadabburBookmarkEntity> filteredBookmarks = new ArrayList<>();
                for (TadabburBookmarkEntity b : allBookmarks) {
                    String normSoraName = QuranV2Repository.normalizeArabic(b.getSoraNameAr());
                    String normAyaText = QuranV2Repository.normalizeArabic(b.getAyaText());
                    if (normSoraName.contains(normQuery) || normAyaText.contains(normQuery)) {
                        filteredBookmarks.add(b);
                    }
                }
                bookmarkAdapter.setBookmarks(filteredBookmarks);
                checkEmptyState(filteredBookmarks.isEmpty());
                break;
        }
    }

    private void checkEmptyState(boolean isEmpty) {
        if (isEmpty) {
            binding.rvContent.setVisibility(View.GONE);
            binding.layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.rvContent.setVisibility(View.VISIBLE);
            binding.layoutEmpty.setVisibility(View.GONE);
        }
    }
}
