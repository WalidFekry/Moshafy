package com.appwalied.quran.quran.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityQuranReadingBinding;
import com.appwalied.quran.quran.audio.QuranAudioPlayer;
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

    private ActivityQuranReadingBinding binding;

    private int suraId = 1;
    private int pageNumber = -1;
    private int juzNumber = -1;
    private int targetAyahId = 1;

    private AyahAdapter adapter;
    private LinearLayoutManager layoutManager;

    private QuranAudioPlayer audioPlayer;
    private QuranSettingsEntity settings;
    private List<Ayah> currentAyahsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuranReadingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        parseIntentData();
        initViews();
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
        binding.backButton.setOnClickListener(v -> finish());
        binding.btnReadingReciter.setOnClickListener(v -> openReciterChooser());
        binding.btnReadingSettings.setOnClickListener(v -> openSettingsDialog());

        layoutManager = new LinearLayoutManager(this);
        binding.rvVerses.setLayoutManager(layoutManager);

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
        binding.rvVerses.setAdapter(adapter);

        setupMiniPlayerControls();
    }

    private void setupAudioPlayer() {
        audioPlayer = QuranAudioPlayer.getInstance(this);
        audioPlayer.setListener(this);
    }

    private void setupMiniPlayerControls() {
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
        binding.pbVersesLoading.setVisibility(View.VISIBLE);

        if (pageNumber > 0) {
            QuranRepository.getInstance(this).getVersesByPage(pageNumber, this::displayVerses);
        } else if (juzNumber > 0) {
            QuranRepository.getInstance(this).getVersesByJuz(juzNumber, this::displayVerses);
        } else {
            QuranRepository.getInstance(this).getVersesBySurah(suraId, this::displayVerses);
        }
    }

    private void displayVerses(List<Ayah> ayahs) {
        binding.pbVersesLoading.setVisibility(View.GONE);
        if (ayahs == null || ayahs.isEmpty()) {
            Toast.makeText(this, "تعذر تحميل الآيات", Toast.LENGTH_SHORT).show();
            return;
        }

        this.currentAyahsList = ayahs;
        adapter.setAyahs(ayahs);

        Ayah firstAyah = ayahs.get(0);
        String title = "سورة " + firstAyah.getSuraName() + " • الجزء " + firstAyah.getParaNumber() + " • صفحة " + firstAyah.getPageNumber();
        binding.tvReadingSurahInfo.setText(title);

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
        binding.rvVerses.post(() -> binding.rvVerses.scrollToPosition(pos));

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

        binding.miniPlayerLayout.getRoot().setVisibility(View.VISIBLE);
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

            binding.miniPlayerLayout.tvMiniPlayerReciter.setText(reciter.getArabicName());
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
        binding.miniPlayerLayout.getRoot().setVisibility(View.VISIBLE);
        binding.miniPlayerLayout.tvMiniPlayerTitle.setText("سورة " + suraName + " - آية " + ayahId);
        binding.miniPlayerLayout.tvMiniPlayerReciter.setText(reciterName);

        for (int i = 0; i < currentAyahsList.size(); i++) {
            if (currentAyahsList.get(i).getVerseId() == ayahId) {
                int pos = i;
                binding.rvVerses.post(() -> binding.rvVerses.smoothScrollToPosition(pos));
                saveProgress(currentAyahsList.get(i));
                break;
            }
        }
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
