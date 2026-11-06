package com.appwalied.quran.quran.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityQuranBinding;
import com.appwalied.quran.quran.audio.QuranAudioPlayer;
import com.appwalied.quran.quran.db.QuranRepository;
import com.appwalied.quran.quran.db.models.Juz;
import com.appwalied.quran.quran.db.models.Surah;
import com.appwalied.quran.quran.room.BookmarkEntity;
import com.appwalied.quran.quran.room.FavoriteSurahEntity;
import com.appwalied.quran.quran.room.QuranSettingsEntity;
import com.appwalied.quran.quran.room.ReadingProgressEntity;
import com.appwalied.quran.quran.room.UserLocalRepository;
import com.appwalied.quran.quran.ui.adapters.BookmarkAdapter;
import com.appwalied.quran.quran.ui.adapters.JuzAdapter;
import com.appwalied.quran.quran.ui.adapters.SurahAdapter;
import com.appwalied.quran.quran.ui.dialogs.QuranSettingsDialog;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class QuranActivity extends BaseActivity implements QuranAudioPlayer.AudioPlayerListener {

    private ActivityQuranBinding binding;

    private SurahAdapter surahAdapter;
    private JuzAdapter juzAdapter;
    private BookmarkAdapter bookmarkAdapter;
    private SurahAdapter favoritesAdapter;

    private List<Surah> allSurahs = new ArrayList<>();
    private List<Juz> allJuzs = new ArrayList<>();
    private List<BookmarkEntity> allBookmarks = new ArrayList<>();
    private List<FavoriteSurahEntity> allFavorites = new ArrayList<>();

    private ReadingProgressEntity lastProgress;
    private QuranSettingsEntity settings;
    private QuranAudioPlayer audioPlayer;

    private int activeTabPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuranBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
        setupTabs();
        setupAudioPlayer();
        loadSettings();
        loadAllData();
    }

    private void initViews() {
        binding.backButton.setOnClickListener(v -> finish());
        binding.btnActionSearch.setOnClickListener(v -> startActivity(new Intent(this, QuranSearchActivity.class)));
        binding.btnActionSettings.setOnClickListener(v -> openSettingsDialog());

        binding.rvQuranList.setLayoutManager(new LinearLayoutManager(this));

        // Adapters
        surahAdapter = new SurahAdapter(surah -> openReadingForSurah(surah.getId(), 1), (surah, isFavorite) -> {
            UserLocalRepository.getInstance(this).toggleFavoriteSurah(surah.getId(), surah.getNameAr(), favState -> {
                loadFavorites();
            });
        });

        juzAdapter = new JuzAdapter(juz -> {
            Intent intent = new Intent(QuranActivity.this, QuranReadingActivity.class);
            intent.putExtra("juzNumber", juz.getJuzNumber());
            intent.putExtra("suraId", juz.getStartSuraId());
            intent.putExtra("targetAyahId", juz.getStartAyahId());
            intent.putExtra("pageNumber", juz.getStartPage());
            startActivity(intent);
        });

        bookmarkAdapter = new BookmarkAdapter(bookmark -> {
            Intent intent = new Intent(QuranActivity.this, QuranReadingActivity.class);
            intent.putExtra("suraId", bookmark.getSuraId());
            intent.putExtra("targetAyahId", bookmark.getAyahId());
            intent.putExtra("pageNumber", bookmark.getPageNumber());
            startActivity(intent);
        }, bookmark -> {
            UserLocalRepository.getInstance(this).deleteBookmark(bookmark.getId(), () -> {
                Toast.makeText(this, "تم حذف العلامة المرجعية", Toast.LENGTH_SHORT).show();
                loadBookmarks();
            });
        });

        favoritesAdapter = new SurahAdapter(surah -> openReadingForSurah(surah.getId(), 1), (surah, isFavorite) -> {
            UserLocalRepository.getInstance(this).toggleFavoriteSurah(surah.getId(), surah.getNameAr(), favState -> {
                loadFavorites();
            });
        });

        binding.rvQuranList.setAdapter(surahAdapter);

        binding.etFilterList.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCurrentTab(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        binding.btnContinueReading.setOnClickListener(v -> {
            if (lastProgress != null) {
                openReadingForSurah(lastProgress.getSuraId(), lastProgress.getAyahId());
            } else {
                openReadingForSurah(1, 1);
            }
        });
    }

    private void setupTabs() {
        binding.tabLayoutQuran.addTab(binding.tabLayoutQuran.newTab().setText("السور"));
        binding.tabLayoutQuran.addTab(binding.tabLayoutQuran.newTab().setText("الأجزاء"));
        binding.tabLayoutQuran.addTab(binding.tabLayoutQuran.newTab().setText("العلامات"));
        binding.tabLayoutQuran.addTab(binding.tabLayoutQuran.newTab().setText("المفضلة"));

        binding.tabLayoutQuran.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                activeTabPosition = tab.getPosition();
                binding.etFilterList.setText("");
                switch (activeTabPosition) {
                    case 0:
                        binding.rvQuranList.setAdapter(surahAdapter);
                        binding.tvQuranListEmpty.setVisibility(allSurahs.isEmpty() ? View.VISIBLE : View.GONE);
                        break;
                    case 1:
                        binding.rvQuranList.setAdapter(juzAdapter);
                        binding.tvQuranListEmpty.setVisibility(allJuzs.isEmpty() ? View.VISIBLE : View.GONE);
                        break;
                    case 2:
                        binding.rvQuranList.setAdapter(bookmarkAdapter);
                        binding.tvQuranListEmpty.setVisibility(allBookmarks.isEmpty() ? View.VISIBLE : View.GONE);
                        break;
                    case 3:
                        binding.rvQuranList.setAdapter(favoritesAdapter);
                        binding.tvQuranListEmpty.setVisibility(allFavorites.isEmpty() ? View.VISIBLE : View.GONE);
                        break;
                }
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupAudioPlayer() {
        audioPlayer = QuranAudioPlayer.getInstance(this);
        audioPlayer.setListener(this);

        binding.miniPlayerLayout.btnMiniPlayPause.setOnClickListener(v -> {
            if (audioPlayer.isPlaying()) {
                audioPlayer.pause();
            } else {
                audioPlayer.resume();
            }
        });

        binding.miniPlayerLayout.btnMiniNext.setOnClickListener(v -> audioPlayer.nextAyah());
        binding.miniPlayerLayout.btnMiniPrev.setOnClickListener(v -> audioPlayer.prevAyah());
        binding.miniPlayerLayout.btnMiniClose.setOnClickListener(v -> {
            audioPlayer.stop();
            binding.miniPlayerLayout.getRoot().setVisibility(View.GONE);
        });
    }

    private void loadSettings() {
        UserLocalRepository.getInstance(this).getSettings(settings -> {
            this.settings = settings;
        });
    }

    private void loadAllData() {
        binding.pbQuranLoading.setVisibility(View.VISIBLE);

        QuranRepository.getInstance(this).getAllSurahs(surahs -> {
            binding.pbQuranLoading.setVisibility(View.GONE);
            allSurahs = surahs;
            surahAdapter.setSurahs(surahs);

            QuranRepository.getInstance(this).getAllJuzs(juzs -> {
                allJuzs = juzs;
                juzAdapter.setJuzList(juzs);
            });

            loadFavorites();
            loadBookmarks();
            loadReadingProgress();
        });
    }

    private void loadFavorites() {
        UserLocalRepository.getInstance(this).getAllFavorites(favs -> {
            allFavorites = favs;
            List<Integer> favIds = new ArrayList<>();
            List<Surah> favSurahs = new ArrayList<>();

            for (FavoriteSurahEntity f : favs) {
                favIds.add(f.getSuraId());
                for (Surah s : allSurahs) {
                    if (s.getId() == f.getSuraId()) {
                        favSurahs.add(s);
                        break;
                    }
                }
            }

            surahAdapter.setFavorites(favIds);
            favoritesAdapter.setSurahs(favSurahs);

            if (activeTabPosition == 3) {
                binding.tvQuranListEmpty.setVisibility(favSurahs.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void loadBookmarks() {
        UserLocalRepository.getInstance(this).getAllBookmarks(bookmarks -> {
            allBookmarks = bookmarks;
            bookmarkAdapter.setBookmarks(bookmarks);
            if (activeTabPosition == 2) {
                binding.tvQuranListEmpty.setVisibility(bookmarks.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void loadReadingProgress() {
        UserLocalRepository.getInstance(this).getReadingProgress(progress -> {
            this.lastProgress = progress;
            if (progress != null) {
                binding.cardContinueReading.setVisibility(View.VISIBLE);
                QuranRepository.getInstance(this).getSurahName(progress.getSuraId(), suraName -> {
                    String info = "سورة " + suraName + " - آية " + progress.getAyahId() + " (صفحة " + progress.getPageNumber() + ")";
                    binding.tvLastReadingPos.setText(info);
                });
            } else {
                binding.cardContinueReading.setVisibility(View.GONE);
            }
        });
    }

    private void filterCurrentTab(String query) {
        if (activeTabPosition == 0) {
            surahAdapter.getFilter().filter(query);
        } else if (activeTabPosition == 1) {
            juzAdapter.getFilter().filter(query);
        } else if (activeTabPosition == 3) {
            favoritesAdapter.getFilter().filter(query);
        }
    }

    private void openReadingForSurah(int suraId, int ayahId) {
        Intent intent = new Intent(QuranActivity.this, QuranReadingActivity.class);
        intent.putExtra("suraId", suraId);
        intent.putExtra("targetAyahId", ayahId);
        startActivity(intent);
    }

    private void openSettingsDialog() {
        QuranSettingsDialog dialog = new QuranSettingsDialog(this, updatedSettings -> {
            this.settings = updatedSettings;
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReadingProgress();
        loadBookmarks();
        loadFavorites();

        if (audioPlayer != null && audioPlayer.isPlaying()) {
            binding.miniPlayerLayout.getRoot().setVisibility(View.VISIBLE);
            binding.miniPlayerLayout.tvMiniPlayerTitle.setText("سورة " + audioPlayer.getCurrentSuraName() + " - آية " + audioPlayer.getCurrentAyahId());
            binding.miniPlayerLayout.tvMiniPlayerReciter.setText(audioPlayer.getReciterName());
            binding.miniPlayerLayout.btnMiniPlayPause.setImageResource(R.drawable.ic_pause);
        } else {
            binding.miniPlayerLayout.getRoot().setVisibility(View.GONE);
        }
    }

    // AudioPlayerListener
    @Override
    public void onAyahChanged(int suraId, int ayahId, String suraName, String reciterName) {
        binding.miniPlayerLayout.getRoot().setVisibility(View.VISIBLE);
        binding.miniPlayerLayout.tvMiniPlayerTitle.setText("سورة " + suraName + " - آية " + ayahId);
        binding.miniPlayerLayout.tvMiniPlayerReciter.setText(reciterName);
    }

    @Override
    public void onPlaybackStateChanged(boolean isPlaying) {
        binding.miniPlayerLayout.btnMiniPlayPause.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play_arrow);
    }

    @Override
    public void onError(String errorMessage, boolean isHttp404) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPlayCompleted() {
        binding.miniPlayerLayout.getRoot().setVisibility(View.GONE);
    }
}
