package com.appwalied.quran.quran.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.appwalied.quran.quran.db.models.Ayah;
import com.appwalied.quran.quran.room.QuranSettingsEntity;
import com.appwalied.quran.quran.room.ReadingProgressEntity;
import com.appwalied.quran.quran.room.UserLocalRepository;
import com.appwalied.quran.quran.ui.adapters.AyahAdapter;
import com.appwalied.quran.quran.ui.dialogs.AyahActionsBottomSheet;
import com.appwalied.quran.quran.ui.dialogs.QuranSettingsDialog;
import com.appwalied.quran.quran.ui.dialogs.ReciterChooserBottomSheet;

import java.util.ArrayList;
import java.util.List;

public class QuranReadingActivity extends BaseActivity implements QuranAudioPlayer.AudioPlayerListener {

    private int suraId = 1;
    private int pageNumber = -1;
    private int juzNumber = -1;
    private int targetAyahId = 1;

    private Toolbar toolbar;
    private TextView tvSurahInfo;
    private ProgressBar pbLoading;
    private RecyclerView rvVerses;
    private AyahAdapter adapter;
    private LinearLayoutManager layoutManager;

    private CardView cardMiniPlayer;
    private TextView tvMiniTitle, tvMiniReciter;
    private ImageButton btnMiniPlayPause, btnMiniNext, btnMiniPrev, btnMiniClose;
    private ImageView ivAudioIcon;

    private QuranAudioPlayer audioPlayer;
    private QuranSettingsEntity settings;
    private List<Ayah> currentAyahsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran_reading);

        parseIntentData();
        initViews();
        setupToolbar();
        setupAudioPlayer();
        loadSettingsAndData();
    }

    private void parseIntentData() {
        if (getIntent() != null) {
            suraId = getIntent().getIntExtra("suraId", 1);
            pageNumber = getIntent().getIntExtra("pageNumber", -1);
            juzNumber = getIntent().getIntExtra("juzNumber", -1);
            targetAyahId = getIntent().getIntExtra("targetAyahId", 1);
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar_reading);
        tvSurahInfo = findViewById(R.id.tv_reading_surah_info);
        pbLoading = findViewById(R.id.pb_verses_loading);
        rvVerses = findViewById(R.id.rv_verses);

        ImageButton btnReciter = findViewById(R.id.btn_reading_reciter);
        ImageButton btnSettings = findViewById(R.id.btn_reading_settings);

        btnReciter.setOnClickListener(v -> openReciterChooser());
        btnSettings.setOnClickListener(v -> openSettingsDialog());

        // Mini player views
        cardMiniPlayer = findViewById(R.id.card_mini_player);
        tvMiniTitle = findViewById(R.id.tv_mini_player_title);
        tvMiniReciter = findViewById(R.id.tv_mini_player_reciter);
        btnMiniPlayPause = findViewById(R.id.btn_mini_play_pause);
        btnMiniNext = findViewById(R.id.btn_mini_next);
        btnMiniPrev = findViewById(R.id.btn_mini_prev);
        btnMiniClose = findViewById(R.id.btn_mini_close);
        ivAudioIcon = findViewById(R.id.iv_audio_icon);

        layoutManager = new LinearLayoutManager(this);
        rvVerses.setLayoutManager(layoutManager);

        adapter = new AyahAdapter(ayah -> {
            AyahActionsBottomSheet sheet = AyahActionsBottomSheet.newInstance(ayah, new AyahActionsBottomSheet.OnAyahActionListener() {
                @Override
                public void onPlayAyah(Ayah selectedAyah) {
                    playAyahAudio(selectedAyah);
                }

                @Override
                public void onBookmarkAdded(Ayah ayah) {
                    // Refresh bookmark status if needed
                }
            });
            sheet.show(getSupportFragmentManager(), "ayah_actions");
        });
        rvVerses.setAdapter(adapter);

        setupMiniPlayerControls();
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

    private void setupAudioPlayer() {
        audioPlayer = QuranAudioPlayer.getInstance(this);
        audioPlayer.setListener(this);
    }

    private void setupMiniPlayerControls() {
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
            adapter.setPlayingAyahId(-1);
        });
    }

    private void loadSettingsAndData() {
        UserLocalRepository.getInstance(this).getSettings(settings -> {
            this.settings = settings;

            if (settings.isKeepScreenOn()) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }

            adapter.setFontSizeSp(settings.getFontSize());
            adapter.setFontKey(settings.getSelectedFont());
            audioPlayer.setReciter(settings.getSelectedReciter(), settings.getSelectedReciterFolder());
            audioPlayer.setAutoPlayNext(settings.isAutoPlayNextAyah());
            audioPlayer.setMaxRepeatCount(settings.getRepeatCount());

            loadVerses();
        });
    }

    private void loadVerses() {
        pbLoading.setVisibility(View.VISIBLE);

        if (pageNumber > 0) {
            QuranRepository.getInstance(this).getVersesByPage(pageNumber, this::displayVerses);
        } else if (juzNumber > 0) {
            QuranRepository.getInstance(this).getVersesByJuz(juzNumber, this::displayVerses);
        } else {
            QuranRepository.getInstance(this).getVersesBySurah(suraId, this::displayVerses);
        }
    }

    private void displayVerses(List<Ayah> ayahs) {
        pbLoading.setVisibility(View.GONE);
        if (ayahs == null || ayahs.isEmpty()) {
            Toast.makeText(this, "تعذر تحميل الآيات", Toast.LENGTH_SHORT).show();
            return;
        }

        this.currentAyahsList = ayahs;
        adapter.setAyahs(ayahs);

        Ayah firstAyah = ayahs.get(0);
        String title = "سورة " + firstAyah.getSuraName() + " • الجزء " + firstAyah.getParaNumber() + " • صفحة " + firstAyah.getPageNumber();
        tvSurahInfo.setText(title);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("سورة " + firstAyah.getSuraName());
        }

        // Scroll to target ayah or saved position
        int targetPosition = 0;
        if (targetAyahId > 1) {
            for (int i = 0; i < ayahs.size(); i++) {
                if (ayahs.get(i).getVerseId() == targetAyahId) {
                    targetPosition = i;
                    break;
                }
            }
        }
        final int pos = targetPosition;
        rvVerses.post(() -> rvVerses.scrollToPosition(pos));

        // Save reading progress
        saveProgress(ayahs.get(pos));
    }

    private void playAyahAudio(Ayah ayah) {
        if (settings != null) {
            audioPlayer.playAyah(
                    ayah.getSuraId(),
                    ayah.getVerseId(),
                    currentAyahsList.size(),
                    ayah.getSuraName(),
                    settings.getSelectedReciter(),
                    settings.getSelectedReciterFolder()
            );
        } else {
            audioPlayer.playAyah(
                    ayah.getSuraId(),
                    ayah.getVerseId(),
                    currentAyahsList.size(),
                    ayah.getSuraName(),
                    "مشاري راشد العفاسي",
                    "Alafasy_128kbps"
            );
        }

        cardMiniPlayer.setVisibility(View.VISIBLE);
        saveProgress(ayah);
    }

    private void saveProgress(Ayah ayah) {
        ReadingProgressEntity progress = new ReadingProgressEntity(
                1,
                ayah.getSuraId(),
                ayah.getVerseId(),
                ayah.getPageNumber(),
                ayah.getParaNumber(),
                System.currentTimeMillis()
        );
        UserLocalRepository.getInstance(this).saveReadingProgress(progress);
    }

    private void openReciterChooser() {
        String activeFolder = settings != null ? settings.getSelectedReciterFolder() : "Alafasy_128kbps";
        ReciterChooserBottomSheet dialog = ReciterChooserBottomSheet.newInstance(activeFolder, reciter -> {
            if (settings != null) {
                settings.setSelectedReciter(reciter.getArabicName());
                settings.setSelectedReciterFolder(reciter.getSubfolder());
                UserLocalRepository.getInstance(this).updateSettings(settings);
            }
            audioPlayer.setReciter(reciter.getArabicName(), reciter.getSubfolder());
            tvMiniReciter.setText(reciter.getArabicName());
            Toast.makeText(this, "تم اختيار القارئ: " + reciter.getArabicName(), Toast.LENGTH_SHORT).show();
        });
        dialog.show(getSupportFragmentManager(), "reciter_chooser");
    }

    private void openSettingsDialog() {
        QuranSettingsDialog dialog = new QuranSettingsDialog(this, updatedSettings -> {
            this.settings = updatedSettings;
            adapter.setFontSizeSp(updatedSettings.getFontSize());
            adapter.setFontKey(updatedSettings.getSelectedFont());

            if (updatedSettings.isKeepScreenOn()) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }

            audioPlayer.setAutoPlayNext(updatedSettings.isAutoPlayNextAyah());
            audioPlayer.setMaxRepeatCount(updatedSettings.getRepeatCount());
        });
        dialog.show();
    }

    // AudioPlayerListener Callbacks
    @Override
    public void onAyahChanged(int suraId, int ayahId, String suraName, String reciterName) {
        adapter.setPlayingAyahId(ayahId);
        if (cardMiniPlayer != null) {
            cardMiniPlayer.setVisibility(View.VISIBLE);
            if (tvMiniTitle != null) tvMiniTitle.setText("سورة " + suraName + " - آية " + ayahId);
            if (tvMiniReciter != null) tvMiniReciter.setText(reciterName);
        }

        // Scroll to verse
        for (int i = 0; i < currentAyahsList.size(); i++) {
            if (currentAyahsList.get(i).getVerseId() == ayahId) {
                int pos = i;
                rvVerses.post(() -> rvVerses.smoothScrollToPosition(pos));
                saveProgress(currentAyahsList.get(i));
                break;
            }
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
        adapter.setPlayingAyahId(-1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }
}
