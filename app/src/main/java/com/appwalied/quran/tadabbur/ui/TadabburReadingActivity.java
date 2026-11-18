package com.appwalied.quran.tadabbur.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityTadabburReadingBinding;
import com.appwalied.quran.tadabbur.db.QuranV2Repository;
import com.appwalied.quran.tadabbur.db.room.TadabburBookmarkEntity;
import com.appwalied.quran.tadabbur.db.room.TadabburLocalRepository;
import com.appwalied.quran.tadabbur.model.QuranV2Ayah;
import com.appwalied.quran.tadabbur.ui.adapters.TadabburAyahAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TadabburReadingActivity extends BaseActivity {

    private ActivityTadabburReadingBinding binding;
    private QuranV2Repository repository;
    private TadabburLocalRepository localRepository;

    private TadabburAyahAdapter adapter;
    private final List<QuranV2Ayah> currentAyahs = new ArrayList<>();

    private String type = "SURAH"; // SURAH, JUZ, PAGE
    private int currentSurahNum = 1;
    private int currentJuzNum = 1;
    private int currentPageNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTadabburReadingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = QuranV2Repository.getInstance(this);
        localRepository = TadabburLocalRepository.getInstance(this);

        parseIntent();
        setupToolbar();
        setupRecyclerView();
        setupClickListeners();

        loadAyahs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBookmarks();
    }

    private void parseIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("TYPE")) {
                type = intent.getStringExtra("TYPE");
            }
            currentSurahNum = intent.getIntExtra("SURAH_NUM", 1);
            currentJuzNum = intent.getIntExtra("JUZ_NUM", 1);
            currentPageNum = intent.getIntExtra("PAGE_NUM", 1);
        }
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        binding.rvAyahs.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TadabburAyahAdapter(ayah -> {
            Intent intent = new Intent(TadabburReadingActivity.this, TadabburAyahDetailActivity.class);
            intent.putExtra("SORA", ayah.getSora());
            intent.putExtra("AYA_NO", ayah.getAyaNo());
            startActivity(intent);
        });

        int savedFontSize = localRepository.getFontSize();
        adapter.setTextSizeSp(savedFontSize);
        binding.rvAyahs.setAdapter(adapter);
    }

    private void setupClickListeners() {
        binding.btnFontSize.setOnClickListener(v -> showFontSizeDialog());

        binding.btnSearchInSurah.setOnClickListener(v -> {
            Intent intent = new Intent(this, TadabburSearchActivity.class);
            startActivity(intent);
        });

        binding.btnPrev.setOnClickListener(v -> navigatePrevious());
        binding.btnNext.setOnClickListener(v -> navigateNext());
    }

    private void loadAyahs() {
        showLoading();
        QuranV2Repository.Callback<List<QuranV2Ayah>> callback = ayahs -> {
            currentAyahs.clear();
            if (ayahs != null && !ayahs.isEmpty()) {
                currentAyahs.addAll(ayahs);
                adapter.setAyahs(currentAyahs);

                // Save reading position
                QuranV2Ayah first = ayahs.get(0);
                currentSurahNum = first.getSora();
                currentPageNum = first.getPage();
                currentJuzNum = first.getJozz();
                localRepository.saveLastReadPosition(first.getSora(), first.getAyaNo(), first.getPage());

                updateHeaderInfo();
            }
            hideLoading();
            loadBookmarks();
        };

        if ("JUZ".equals(type)) {
            repository.getAyahsByJuz(currentJuzNum, callback);
        } else if ("PAGE".equals(type)) {
            repository.getAyahsByPage(currentPageNum, callback);
        } else {
            repository.getAyahsBySurah(currentSurahNum, callback);
        }
    }

    private void loadBookmarks() {
        localRepository.getAllBookmarks(bookmarks -> {
            Set<String> bookmarkedKeys = new HashSet<>();
            if (bookmarks != null) {
                for (TadabburBookmarkEntity b : bookmarks) {
                    bookmarkedKeys.add(b.getSora() + "_" + b.getAyaNo());
                }
            }
            adapter.setBookmarkedAyahs(bookmarkedKeys);
        });
    }

    private void updateHeaderInfo() {
        if (currentAyahs.isEmpty()) return;

        QuranV2Ayah first = currentAyahs.get(0);
        String headerTitle;

        if ("PAGE".equals(type)) {
            headerTitle = "صفحة " + currentPageNum + " • سورة " + first.getSoraNameAr() + " • الجزء " + first.getJozz();
            binding.tvPageIndicator.setText("صفحة " + currentPageNum + " من 604");
        } else if ("JUZ".equals(type)) {
            headerTitle = "الجزء " + currentJuzNum + " • سورة " + first.getSoraNameAr() + " • ص " + first.getPage();
            binding.tvPageIndicator.setText("الجزء " + currentJuzNum + " من 30");
        } else {
            headerTitle = "سورة " + first.getSoraNameAr() + " • الجزء " + first.getJozz() + " • ص " + first.getPage();
            binding.tvPageIndicator.setText("سورة " + currentSurahNum + " من 114");
        }

        binding.tvHeaderTitle.setText(headerTitle);

        // Update nav buttons visibility
        if ("PAGE".equals(type)) {
            binding.btnPrev.setEnabled(currentPageNum > 1);
            binding.btnNext.setEnabled(currentPageNum < 604);
        } else if ("JUZ".equals(type)) {
            binding.btnPrev.setEnabled(currentJuzNum > 1);
            binding.btnNext.setEnabled(currentJuzNum < 30);
        } else {
            binding.btnPrev.setEnabled(currentSurahNum > 1);
            binding.btnNext.setEnabled(currentSurahNum < 114);
        }
    }

    private void navigatePrevious() {
        if ("PAGE".equals(type) && currentPageNum > 1) {
            currentPageNum--;
            loadAyahs();
        } else if ("JUZ".equals(type) && currentJuzNum > 1) {
            currentJuzNum--;
            loadAyahs();
        } else if ("SURAH".equals(type) && currentSurahNum > 1) {
            currentSurahNum--;
            loadAyahs();
        }
    }

    private void navigateNext() {
        if ("PAGE".equals(type) && currentPageNum < 604) {
            currentPageNum++;
            loadAyahs();
        } else if ("JUZ".equals(type) && currentJuzNum < 30) {
            currentJuzNum++;
            loadAyahs();
        } else if ("SURAH".equals(type) && currentSurahNum < 114) {
            currentSurahNum++;
            loadAyahs();
        }
    }

    private void showFontSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("حجم خط الآيات 🔍");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_font_size, null);
        SeekBar seekBar = dialogView.findViewById(R.id.seekbar_font);
        TextView tvPreview = dialogView.findViewById(R.id.tv_preview_font);

        int currentSize = localRepository.getFontSize();
        seekBar.setProgress(currentSize - 16);
        tvPreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentSize);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int newSize = progress + 16;
                tvPreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, newSize);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        builder.setView(dialogView);
        builder.setPositiveButton("حفظ", (dialog, which) -> {
            int newSize = seekBar.getProgress() + 16;
            localRepository.saveFontSize(newSize);
            adapter.setTextSizeSp(newSize);
        });

        builder.setNegativeButton("إلغاء", null);
        builder.show();
    }
}
