package com.appwalied.quran.quran_listening;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityQuranListeningBinding;
import com.appwalied.quran.quran_listening.adapter.ListeningReciterAdapter;
import com.appwalied.quran.quran_listening.adapter.ListeningSurahAdapter;
import com.appwalied.quran.quran_listening.helper.ListeningPreferences;
import com.appwalied.quran.quran_listening.model.MoshafModel;
import com.appwalied.quran.quran_listening.model.ReciterModel;
import com.appwalied.quran.quran_listening.model.SurahModel;
import com.appwalied.quran.quran_listening.player.AudioPlayerManager;
import com.appwalied.quran.quran_listening.player.QuranMediaService;
import com.appwalied.quran.quran_listening.repository.QuranListeningRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QuranListeningActivity extends BaseActivity implements AudioPlayerManager.OnPlayerStateChangeListener {

    private ActivityQuranListeningBinding binding;
    private QuranListeningRepository repository;
    private ListeningPreferences preferences;
    private AudioPlayerManager playerManager;

    private ListeningSurahAdapter surahAdapter;
    private ListeningReciterAdapter reciterAdapter;

    private List<SurahModel> allSuwar = new ArrayList<>();
    private List<ReciterModel> allReciters = new ArrayList<>();

    private ReciterModel activeReciter;
    private SurahModel activeSurah;

    private int activeTab = 0; // 0 = Surahs, 1 = Reciters
    private boolean isTrackingSeekBar = false;
    private boolean isProgrammaticSearchChange = false;

    private static final String[] SURAH_NAMES_AR = {
            "الفاتحة", "البقرة", "آل عمران", "النساء", "المائدة", "الأنعام", "الأعراف", "الأنفال", "التوبة", "يونس",
            "هود", "يوسف", "الرعد", "إبراهيم", "الحجر", "النحل", "الإسراء", "الكهف", "مريم", "طه",
            "الأنبياء", "الحج", "المؤمنون", "النور", "الفرقان", "الشعراء", "النمل", "القصص", "العنكبوت", "الروم",
            "لقمان", "السجدة", "الأحزاب", "سبأ", "فاطر", "يس", "الصافات", "ص", "الزمر", "غافر",
            "فصلت", "الشورى", "الزخرف", "الدخان", "الجاثية", "الأحقاف", "محمد", "الفتح", "الحجرات", "ق",
            "الذاريات", "الطور", "النجم", "القمر", "الرحمن", "الواقعة", "الحديد", "المجادلة", "الحشر", "الممتحنة",
            "الصف", "الجمعة", "المنافقون", "التغابن", "الطلاق", "التحريم", "الملك", "القلم", "الحاقة", "المعارج",
            "نوح", "الجن", "المزمل", "المدثر", "القيامة", "الإنسان", "المرسلات", "النبأ", "النازعات", "عبس",
            "التكوير", "الانفطار", "المطففين", "الانشقاق", "البروج", "الطارق", "الأعلى", "الغاشية", "الفجر", "البلد",
            "الشمس", "الليل", "الضحى", "الشرح", "التين", "العلق", "القدر", "البينة", "الزلزلة", "العاديات",
            "القارعة", "التكاثر", "العصر", "الهمزة", "الفيل", "قريش", "الماعون", "الكوثر", "الكافرون", "النصر",
            "المسد", "الإخلاص", "الفلق", "الناس"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuranListeningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new QuranListeningRepository(this);
        preferences = new ListeningPreferences(this);
        playerManager = AudioPlayerManager.getInstance(this);

        initViews();
        setupTabs();
        setupPlayerControls();
        loadInitialData();

        showBanner(binding.adsContainer);
    }

    private void initViews() {
        binding.backButton.setOnClickListener(v -> finish());
        binding.rvListeningContent.setLayoutManager(new LinearLayoutManager(this));

        surahAdapter = new ListeningSurahAdapter(surah -> playSurah(surah));
        reciterAdapter = new ListeningReciterAdapter(reciter -> changeReciter(reciter));

        binding.rvListeningContent.setAdapter(surahAdapter);

        binding.etSearchFilter.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isProgrammaticSearchChange) {
                    filterCurrentTab(s.toString());
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void setupTabs() {
        binding.btnTabSurahs.setOnClickListener(v -> switchToTab(0));
        binding.btnTabReciters.setOnClickListener(v -> switchToTab(1));
    }

    private void switchToTab(int tabIndex) {
        activeTab = tabIndex;

        isProgrammaticSearchChange = true;
        binding.etSearchFilter.setText("");
        isProgrammaticSearchChange = false;

        binding.btnTabSurahs.setBackgroundResource(tabIndex == 0 ? R.drawable.buoo : R.drawable.buoo1);
        binding.btnTabReciters.setBackgroundResource(tabIndex == 1 ? R.drawable.buoo : R.drawable.buoo1);

        switch (tabIndex) {
            case 0:
                binding.rvListeningContent.setAdapter(surahAdapter);
                updateSurahsView();
                break;
            case 1:
                binding.rvListeningContent.setAdapter(reciterAdapter);
                reciterAdapter.setReciters(allReciters, activeReciter != null ? activeReciter.getId() : -1);
                binding.tvListeningEmpty.setVisibility(reciterAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                break;
        }
    }

    private void setupPlayerControls() {
        playerManager.setListener(this);

        updateRepeatButtonText(playerManager.getRepeatMode());
        updateSpeedButtonText(playerManager.getPlaybackSpeed());

        binding.btnPlayerPlayPause.setOnClickListener(v -> {
            if (playerManager.isPlaying()) {
                playerManager.pause();
            } else {
                startMediaService();
                if (playerManager.hasMediaItem() && activeSurah != null && activeReciter != null
                        && playerManager.getCurrentSurahId() == activeSurah.getId()
                        && playerManager.getCurrentReciterId() == activeReciter.getId()) {
                    playerManager.resume();
                } else if (activeSurah != null && activeReciter != null) {
                    MoshafModel mainMoshaf = activeReciter.getMainMoshaf();
                    String serverUrl = mainMoshaf != null ? mainMoshaf.getServer() : preferences.getLastServerUrl();
                    List<Integer> availableSurahs = mainMoshaf != null ? mainMoshaf.getAvailableSurahIds() : new ArrayList<>();
                    playerManager.playSurah(activeSurah.getId(), activeSurah.getName(), activeReciter.getId(), activeReciter.getName(), serverUrl, availableSurahs);
                } else {
                    Toast.makeText(this, "يرجى اختيار سورة للتغيير والتشغيل", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnPlayerNext.setOnClickListener(v -> playerManager.playNext(SURAH_NAMES_AR));
        binding.btnPlayerPrev.setOnClickListener(v -> playerManager.playPrevious(SURAH_NAMES_AR));

        binding.btnPlayerForward.setOnClickListener(v -> playerManager.seekForward(10000));
        binding.btnPlayerRewind.setOnClickListener(v -> playerManager.seekRewind(10000));

        binding.btnRepeatMode.setOnClickListener(v -> {
            int newMode = (playerManager.getRepeatMode() + 1) % 3;
            playerManager.setRepeatMode(newMode);
            preferences.saveRepeatMode(newMode);
            updateRepeatButtonText(newMode);

            String modeName = newMode == 0 ? "بدون تكرار" : (newMode == 1 ? "تكرار السورة الحالية 🔂" : "تكرار القائمة 🔁");
            Toast.makeText(this, modeName, Toast.LENGTH_SHORT).show();
        });

        binding.btnPlayerSpeed.setOnClickListener(v -> showSpeedDialog());

        binding.sbPlayerProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    binding.tvCurrentTime.setText(formatTimeMs(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTrackingSeekBar = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTrackingSeekBar = false;
                playerManager.seekTo(seekBar.getProgress());
            }
        });
    }

    private void updateRepeatButtonText(int mode) {
        String modeName = mode == 0 ? "بدون تكرار" : (mode == 1 ? "تكرار السورة الحالية 🔂" : "تكرار القائمة 🔁");
        binding.btnRepeatMode.setContentDescription(modeName);
    }

    private void updateSpeedButtonText(float speed) {
        binding.btnPlayerSpeed.setContentDescription(String.format(Locale.getDefault(), "السرعة: %.1fx", speed));
    }

    private void loadInitialData() {
        binding.pbListeningLoading.setVisibility(View.VISIBLE);

        repository.fetchSuwar(new QuranListeningRepository.OnSuwarFetchedListener() {
            @Override
            public void onSuccess(List<SurahModel> suwar, boolean isFromCache) {
                allSuwar = suwar;

                repository.fetchReciters(new QuranListeningRepository.OnRecitersFetchedListener() {
                    @Override
                    public void onSuccess(List<ReciterModel> reciters, boolean isFromCacheReciters) {
                        binding.pbListeningLoading.setVisibility(View.GONE);
                        allReciters = reciters;

                        int lastReciterId = preferences.getLastReciterId();
                        for (ReciterModel r : reciters) {
                            if (r.getId() == lastReciterId) {
                                activeReciter = r;
                                break;
                            }
                        }
                        if (activeReciter == null && !reciters.isEmpty()) {
                            activeReciter = reciters.get(0);
                        }

                        int lastSurahId = preferences.getLastSurahId();
                        for (SurahModel s : suwar) {
                            if (s.getId() == lastSurahId) {
                                activeSurah = s;
                                break;
                            }
                        }
                        if (activeSurah == null && !suwar.isEmpty()) {
                            activeSurah = suwar.get(0);
                        }

                        updateHeroCardUI();
                        reciterAdapter.setReciters(reciters, activeReciter != null ? activeReciter.getId() : -1);
                        updateSurahsView();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        binding.pbListeningLoading.setVisibility(View.GONE);
                        Toast.makeText(QuranListeningActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                binding.pbListeningLoading.setVisibility(View.GONE);
                Toast.makeText(QuranListeningActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSurahsView() {
        if (activeReciter != null && activeReciter.getMainMoshaf() != null) {
            List<Integer> availableSurahIds = activeReciter.getMainMoshaf().getAvailableSurahIds();
            surahAdapter.setSurahs(allSuwar, availableSurahIds, playerManager.getCurrentSurahId());
        } else {
            surahAdapter.setSurahs(allSuwar, null, playerManager.getCurrentSurahId());
        }
        binding.tvListeningEmpty.setVisibility(surahAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    private void updateHeroCardUI() {
        if (activeReciter != null) {
            binding.tvActiveReciterName.setText(activeReciter.getName());
        } else {
            binding.tvActiveReciterName.setText(preferences.getLastReciterName());
        }

        if (activeSurah != null) {
            binding.tvActiveSurahName.setText("سورة " + activeSurah.getName());
        } else {
            binding.tvActiveSurahName.setText("سورة " + preferences.getLastSurahName());
        }
    }

    private void changeReciter(ReciterModel reciter) {
        this.activeReciter = reciter;
        MoshafModel mainMoshaf = reciter.getMainMoshaf();
        String serverUrl = mainMoshaf != null ? mainMoshaf.getServer() : "";
        String surahList = mainMoshaf != null ? mainMoshaf.getSurahList() : "";

        preferences.saveLastReciter(reciter.getId(), reciter.getName(), serverUrl, surahList);
        updateHeroCardUI();
        updateSurahsView();

        Toast.makeText(this, "تم اختيار القارئ: " + reciter.getName(), Toast.LENGTH_SHORT).show();
        switchToTab(0);
    }

    private void playSurah(SurahModel surah) {
        this.activeSurah = surah;
        preferences.saveLastSurah(surah.getId(), surah.getName());
        updateHeroCardUI();

        MoshafModel mainMoshaf = activeReciter != null ? activeReciter.getMainMoshaf() : null;
        String serverUrl = mainMoshaf != null ? mainMoshaf.getServer() : preferences.getLastServerUrl();
        List<Integer> availableSurahs = mainMoshaf != null ? mainMoshaf.getAvailableSurahIds() : new ArrayList<>();

        startMediaService();
        playerManager.playSurah(surah.getId(), surah.getName(), activeReciter != null ? activeReciter.getId() : 1, activeReciter != null ? activeReciter.getName() : "", serverUrl, availableSurahs);
    }

    private void showSpeedDialog() {
        String[] speeds = {"0.75x", "1.0x (طبيعي)", "1.25x", "1.5x", "2.0x"};
        float[] speedValues = {0.75f, 1.0f, 1.25f, 1.5f, 2.0f};

        int currentIdx = 1;
        float currentSpeed = playerManager.getPlaybackSpeed();
        for (int i = 0; i < speedValues.length; i++) {
            if (Math.abs(speedValues[i] - currentSpeed) < 0.05f) {
                currentIdx = i;
                break;
            }
        }

        new AlertDialog.Builder(this)
                .setTitle("سرعة التشغيل")
                .setSingleChoiceItems(speeds, currentIdx, (dialog, which) -> {
                    float speed = speedValues[which];
                    playerManager.setPlaybackSpeed(speed);
                    preferences.savePlaybackSpeed(speed);
                    updateSpeedButtonText(speed);
                    Toast.makeText(this, "تم تغيير سرعة التشغيل إلى " + speeds[which], Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .setNegativeButton("إلغاء", null)
                .show();
    }

    private void filterCurrentTab(String query) {
        if (activeTab == 0) {
            surahAdapter.getFilter().filter(query);
        } else if (activeTab == 1) {
            reciterAdapter.getFilter().filter(query);
        }
    }

    private void startMediaService() {
        Intent serviceIntent = new Intent(this, QuranMediaService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    private void stopMediaService() {
        Intent serviceIntent = new Intent(this, QuranMediaService.class);
        stopService(serviceIntent);
    }

    private String formatTimeMs(long milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    // Player State Callbacks
    @Override
    public void onSurahChanged(int surahId, String surahName, String reciterName) {
        binding.tvActiveSurahName.setText("سورة " + surahName);
        binding.tvActiveReciterName.setText(reciterName);
        surahAdapter.setPlayingSurahId(surahId);
    }

    @Override
    public void onPlaybackStateChanged(boolean isPlaying, boolean isBuffering) {
        binding.pbPlayerBuffering.setVisibility(isBuffering ? View.VISIBLE : View.GONE);
        binding.btnPlayerPlayPause.setVisibility(isBuffering ? View.GONE : View.VISIBLE);
        binding.btnPlayerPlayPause.setImageResource(isPlaying ? R.drawable.btn_pause : R.drawable.btn_play);
    }

    @Override
    public void onProgressUpdate(long currentPositionMs, long durationMs) {
        if (!isTrackingSeekBar) {
            if (durationMs > 0) {
                binding.sbPlayerProgress.setMax((int) durationMs);
                binding.tvTotalTime.setText(formatTimeMs(durationMs));
            }
            binding.sbPlayerProgress.setProgress((int) currentPositionMs);
            binding.tvCurrentTime.setText(formatTimeMs(currentPositionMs));
        }
    }

    @Override
    public void onPlaybackCompleted() {
        binding.btnPlayerPlayPause.setImageResource(R.drawable.btn_play);
        binding.sbPlayerProgress.setProgress(0);
        binding.tvCurrentTime.setText("00:00");
    }

    @Override
    public void onError(String errorMessage) {
        binding.pbPlayerBuffering.setVisibility(View.GONE);
        binding.btnPlayerPlayPause.setVisibility(View.VISIBLE);
        binding.btnPlayerPlayPause.setImageResource(R.drawable.btn_play);
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
