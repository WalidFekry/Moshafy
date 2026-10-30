package com.appwalied.quran.quran.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.quran.audio.QuranAudioPlayer;
import com.appwalied.quran.quran.audio.Reciter;
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
import com.appwalied.quran.quran.ui.dialogs.ReciterChooserBottomSheet;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class QuranActivity extends BaseActivity implements QuranAudioPlayer.AudioPlayerListener {

    private Toolbar toolbar;
    private CardView cardContinueReading;
    private TextView tvLastReadingPos, tvEmptyList;
    private Button btnContinueReading;
    private EditText etFilter;
    private TabLayout tabLayout;
    private ProgressBar pbLoading;
    private RecyclerView rvList;

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

    private CardView cardMiniPlayer;
    private TextView tvMiniTitle, tvMiniReciter;
    private ImageButton btnMiniPlayPause, btnMiniNext, btnMiniPrev, btnMiniClose;

    private int activeTabPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran);

        initViews();
        setupToolbar();
        setupTabs();
        setupAudioPlayer();
        loadSettings();
        loadAllData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar_quran);
        cardContinueReading = findViewById(R.id.card_continue_reading);
        tvLastReadingPos = findViewById(R.id.tv_last_reading_pos);
        btnContinueReading = findViewById(R.id.btn_continue_reading);
        etFilter = findViewById(R.id.et_filter_list);
        tabLayout = findViewById(R.id.tab_layout_quran);
        pbLoading = findViewById(R.id.pb_quran_loading);
        tvEmptyList = findViewById(R.id.tv_quran_list_empty);
        rvList = findViewById(R.id.rv_quran_list);

        cardMiniPlayer = findViewById(R.id.card_mini_player);
        tvMiniTitle = findViewById(R.id.tv_mini_player_title);
        tvMiniReciter = findViewById(R.id.tv_mini_player_reciter);
        btnMiniPlayPause = findViewById(R.id.btn_mini_play_pause);
        btnMiniNext = findViewById(R.id.btn_mini_next);
        btnMiniPrev = findViewById(R.id.btn_mini_prev);
        btnMiniClose = findViewById(R.id.btn_mini_close);

        rvList.setLayoutManager(new LinearLayoutManager(this));

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

        rvList.setAdapter(surahAdapter);

        etFilter.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCurrentTab(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        btnContinueReading.setOnClickListener(v -> {
            if (lastProgress != null) {
                openReadingForSurah(lastProgress.getSuraId(), lastProgress.getAyahId());
            } else {
                openReadingForSurah(1, 1);
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("القرآن الكريم");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.show_notifications) {
            startActivity(new Intent(this, QuranSearchActivity.class));
            return true;
        } else if (id == R.id.theme_mode) {
            openSettingsDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("السور"));
        tabLayout.addTab(tabLayout.newTab().setText("الأجزاء"));
        tabLayout.addTab(tabLayout.newTab().setText("العلامات"));
        tabLayout.addTab(tabLayout.newTab().setText("المفضلة"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                activeTabPosition = tab.getPosition();
                etFilter.setText("");
                switch (activeTabPosition) {
                    case 0:
                        rvList.setAdapter(surahAdapter);
                        tvEmptyList.setVisibility(allSurahs.isEmpty() ? View.VISIBLE : View.GONE);
                        break;
                    case 1:
                        rvList.setAdapter(juzAdapter);
                        tvEmptyList.setVisibility(allJuzs.isEmpty() ? View.VISIBLE : View.GONE);
                        break;
                    case 2:
                        rvList.setAdapter(bookmarkAdapter);
                        tvEmptyList.setVisibility(allBookmarks.isEmpty() ? View.VISIBLE : View.GONE);
                        break;
                    case 3:
                        rvList.setAdapter(favoritesAdapter);
                        tvEmptyList.setVisibility(allFavorites.isEmpty() ? View.VISIBLE : View.GONE);
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

        btnMiniPlayPause.setOnClickListener(v -> {
            if (audioPlayer.isPlaying()) {
                audioPlayer.pause();
            } else {
                audioPlayer.resume();
            }
        });

        btnMiniNext.setOnClickListener(v -> audioPlayer.nextAyah());
        btnMiniPrev.setOnClickListener(v -> audioPlayer.prevAyah());
        btnMiniClose.setOnClickListener(v -> {
            audioPlayer.stop();
            cardMiniPlayer.setVisibility(View.GONE);
        });
    }

    private void loadSettings() {
        UserLocalRepository.getInstance(this).getSettings(settings -> {
            this.settings = settings;
        });
    }

    private void loadAllData() {
        pbLoading.setVisibility(View.VISIBLE);

        // Load Surahs
        QuranRepository.getInstance(this).getAllSurahs(surahs -> {
            pbLoading.setVisibility(View.GONE);
            allSurahs = surahs;
            surahAdapter.setSurahs(surahs);

            // Load Juzs
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
                tvEmptyList.setVisibility(favSurahs.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void loadBookmarks() {
        UserLocalRepository.getInstance(this).getAllBookmarks(bookmarks -> {
            allBookmarks = bookmarks;
            bookmarkAdapter.setBookmarks(bookmarks);
            if (activeTabPosition == 2) {
                tvEmptyList.setVisibility(bookmarks.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void loadReadingProgress() {
        UserLocalRepository.getInstance(this).getReadingProgress(progress -> {
            this.lastProgress = progress;
            if (progress != null) {
                cardContinueReading.setVisibility(View.VISIBLE);
                QuranRepository.getInstance(this).getSurahName(progress.getSuraId(), suraName -> {
                    String info = "سورة " + suraName + " - آية " + progress.getAyahId() + " (صفحة " + progress.getPageNumber() + ")";
                    tvLastReadingPos.setText(info);
                });
            } else {
                cardContinueReading.setVisibility(View.GONE);
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

        if (cardMiniPlayer != null) {
            if (audioPlayer != null && audioPlayer.isPlaying()) {
                cardMiniPlayer.setVisibility(View.VISIBLE);
                if (tvMiniTitle != null) tvMiniTitle.setText("سورة " + audioPlayer.getCurrentSuraName() + " - آية " + audioPlayer.getCurrentAyahId());
                if (tvMiniReciter != null) tvMiniReciter.setText(audioPlayer.getReciterName());
                if (btnMiniPlayPause != null) btnMiniPlayPause.setImageResource(R.drawable.ic_pause);
            } else {
                cardMiniPlayer.setVisibility(View.GONE);
            }
        }
    }

    // AudioPlayerListener
    @Override
    public void onAyahChanged(int suraId, int ayahId, String suraName, String reciterName) {
        if (cardMiniPlayer != null) {
            cardMiniPlayer.setVisibility(View.VISIBLE);
            if (tvMiniTitle != null) tvMiniTitle.setText("سورة " + suraName + " - آية " + ayahId);
            if (tvMiniReciter != null) tvMiniReciter.setText(reciterName);
        }
    }

    @Override
    public void onPlaybackStateChanged(boolean isPlaying) {
        if (btnMiniPlayPause != null) {
            if (isPlaying) {
                btnMiniPlayPause.setImageResource(R.drawable.ic_pause);
            } else {
                btnMiniPlayPause.setImageResource(R.drawable.ic_play_arrow);
            }
        }
    }

    @Override
    public void onError(String errorMessage, boolean isHttp404) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPlayCompleted() {
        if (cardMiniPlayer != null) {
            cardMiniPlayer.setVisibility(View.GONE);
        }
    }
}
