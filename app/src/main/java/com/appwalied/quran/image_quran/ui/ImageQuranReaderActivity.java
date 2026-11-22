package com.appwalied.quran.image_quran.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityImageQuranReaderBinding;
import com.appwalied.quran.image_quran.data.ImageQuranBookmarkEntity;
import com.appwalied.quran.image_quran.data.ImageQuranConfig;
import com.appwalied.quran.image_quran.data.ImageQuranRepository;
import com.appwalied.quran.image_quran.data.Riwaya;
import com.appwalied.quran.image_quran.player.ImageQuranAudioPlayerManager;
import com.appwalied.quran.quran.db.QuranRepository;
import com.appwalied.quran.quran.db.models.Ayah;

import java.util.List;
import java.util.Locale;

public class ImageQuranReaderActivity extends BaseActivity {

    public static final String EXTRA_RIWAYA_KEY = "extra_riwaya_key";
    public static final String EXTRA_TARGET_PAGE = "extra_target_page";

    private ActivityImageQuranReaderBinding binding;
    private ImageQuranRepository repository;
    private QuranRepository quranDbRepository;
    private ImageQuranAudioPlayerManager audioPlayerManager;

    private ImageQuranPagerAdapter pagerAdapter;
    private Riwaya currentRiwaya = Riwaya.HAFS;
    private int currentPage = 1;
    private String currentSurahName = "الفاتحة";
    private int currentJuzNumber = 1;

    private boolean isOverlayVisible = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageQuranReaderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = ImageQuranRepository.getInstance(this);
        quranDbRepository = QuranRepository.getInstance(this);
        audioPlayerManager = ImageQuranAudioPlayerManager.getInstance(this);

        parseIntent();
        setupViewPager();
        setupClickListeners();
        setupAudioPlayer();
        setupBackDispatcher();

        updateScreenKeepOn();
    }

    private void parseIntent() {
        if (getIntent() != null) {
            String riwayaKey = getIntent().getStringExtra(EXTRA_RIWAYA_KEY);
            if (riwayaKey != null) {
                currentRiwaya = Riwaya.fromKey(riwayaKey);
            } else {
                currentRiwaya = repository.getSelectedRiwaya();
            }

            int targetPage = getIntent().getIntExtra(EXTRA_TARGET_PAGE, -1);
            if (targetPage >= 1 && targetPage <= ImageQuranConfig.TOTAL_PAGES) {
                currentPage = targetPage;
            } else {
                currentPage = repository.getLastPage(currentRiwaya);
            }
        } else {
            currentRiwaya = repository.getSelectedRiwaya();
            currentPage = repository.getLastPage(currentRiwaya);
        }
    }

    private void setupViewPager() {
        pagerAdapter = new ImageQuranPagerAdapter(this, currentRiwaya);
        pagerAdapter.setOnPageClickListener(page -> toggleOverlay());

        // Force LTR layout direction on ViewPager2 and its internal RecyclerView.
        // In ViewPager2, LTR mode enforces:
        // Swiping finger Right -> Left advances to Next Page (+1)
        // Swiping finger Left -> Right goes back to Previous Page (-1)
        // This holds true regardless of system locale/language.
        binding.viewPagerQuran.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        View childView = binding.viewPagerQuran.getChildAt(0);
        if (childView != null) {
            childView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        binding.viewPagerQuran.setAdapter(pagerAdapter);
        binding.viewPagerQuran.setOffscreenPageLimit(2);
        binding.viewPagerQuran.setCurrentItem(ImageQuranConfig.pageToPosition(currentPage), false);

        binding.viewPagerQuran.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentPage = ImageQuranConfig.positionToPage(position);
                repository.saveLastPage(currentRiwaya, currentPage);
                updatePageMetadata();
            }
        });

        updatePageMetadata();
    }

    private void updatePageMetadata() {
        quranDbRepository.getVersesByPage(currentPage, new QuranRepository.Callback<List<Ayah>>() {
            @Override
            public void onResult(List<Ayah> verses) {
                if (verses != null && !verses.isEmpty()) {
                    Ayah firstAyah = verses.get(0);
                    currentSurahName = firstAyah.getSuraName();
                    currentJuzNumber = firstAyah.getParaNumber();
                }
                updateUiText();
                checkBookmarkStatus();
            }
        });
    }

    private void updateUiText() {
        binding.tvReaderSurahName.setText("سورة " + currentSurahName);
        binding.tvReaderRiwayaName.setText(currentRiwaya.getDisplayName());
        binding.tvPageInfo.setText(String.format(Locale.getDefault(), "صفحة %d من 604 - الجزء %d", currentPage, currentJuzNumber));
    }

    private void checkBookmarkStatus() {
        repository.getBookmarkForPage(currentRiwaya, currentPage, bookmark -> {
            if (bookmark != null) {
                binding.btnAddBookmark.setImageResource(R.drawable.ic_bookmark);
                binding.btnAddBookmark.setColorFilter(getColor(R.color.gold_accent));
            } else {
                binding.btnAddBookmark.setImageResource(R.drawable.ic_bookmark);
                binding.btnAddBookmark.setColorFilter(getColor(R.color.white));
            }
        });
    }

    private void setupClickListeners() {
        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnAddBookmark.setOnClickListener(v -> toggleBookmark());

        binding.btnDownloadOffline.setOnClickListener(v -> {
            Intent intent = new Intent(ImageQuranReaderActivity.this, ImageQuranDownloadActivity.class);
            intent.putExtra(ImageQuranDownloadActivity.EXTRA_RIWAYA_KEY, currentRiwaya.getKey());
            startActivity(intent);
        });

        binding.btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(ImageQuranReaderActivity.this, ImageQuranSettingsActivity.class);
            startActivity(intent);
        });

        binding.btnPrevPage.setOnClickListener(v -> {
            if (currentPage > 1) {
                binding.viewPagerQuran.setCurrentItem(ImageQuranConfig.pageToPosition(currentPage - 1), true);
            }
        });

        binding.btnNextPage.setOnClickListener(v -> {
            if (currentPage < ImageQuranConfig.TOTAL_PAGES) {
                binding.viewPagerQuran.setCurrentItem(ImageQuranConfig.pageToPosition(currentPage + 1), true);
            }
        });

        binding.btnVisualIndex.setOnClickListener(v -> openVisualPageIndex());
        binding.btnSurahIndex.setOnClickListener(v -> openSurahIndex());
        binding.btnJuzIndex.setOnClickListener(v -> openJuzIndex());
        binding.btnBookmarks.setOnClickListener(v -> openBookmarks());
        binding.btnQuickNav.setOnClickListener(v -> openQuickNavigation());
    }

    private void toggleBookmark() {
        repository.getBookmarkForPage(currentRiwaya, currentPage, existingBookmark -> {
            if (existingBookmark != null) {
                repository.deleteBookmark(existingBookmark.getId(), () -> {
                    Toast.makeText(ImageQuranReaderActivity.this, "تم إزالة العلامة المرجعية", Toast.LENGTH_SHORT).show();
                    checkBookmarkStatus();
                });
            } else {
                ImageQuranBookmarkEntity bookmark = new ImageQuranBookmarkEntity(
                        currentRiwaya.getKey(),
                        currentPage,
                        currentSurahName,
                        currentJuzNumber,
                        "صفحة " + currentPage + " (" + currentSurahName + ")",
                        System.currentTimeMillis()
                );
                repository.addBookmark(bookmark, id -> {
                    Toast.makeText(ImageQuranReaderActivity.this, "تم حفظ العلامة المرجعية 🌸", Toast.LENGTH_SHORT).show();
                    checkBookmarkStatus();
                });
            }
        });
    }

    private void setupAudioPlayer() {
        binding.btnAudioPlayPause.setOnClickListener(v -> {
            if (audioPlayerManager.getCurrentPage() != currentPage) {
                audioPlayerManager.playPage(currentPage);
            } else {
                audioPlayerManager.togglePlayPause();
            }
        });

        binding.btnAudioRepeat.setOnClickListener(v -> {
            boolean newLooping = !audioPlayerManager.isLooping();
            audioPlayerManager.setLooping(newLooping);
            binding.btnAudioRepeat.setColorFilter(getColor(newLooping ? R.color.gold_accent : R.color.white));
            Toast.makeText(this, newLooping ? "تم تفعيل تكرار الصفحة 🔄" : "تم إلغاء تكرار الصفحة", Toast.LENGTH_SHORT).show();
        });

        binding.seekbarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    audioPlayerManager.seekTo(progress);
                }
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        audioPlayerManager.setListener(new ImageQuranAudioPlayerManager.PlayerListener() {
            @Override
            public void onPageAudioChanged(int page, boolean isPlaying) {
                binding.btnAudioPlayPause.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
                if (page != currentPage && isPlaying && page >= 1 && page <= ImageQuranConfig.TOTAL_PAGES) {
                    binding.viewPagerQuran.setCurrentItem(ImageQuranConfig.pageToPosition(page), true);
                }
            }

            @Override
            public void onPlaybackStateChanged(boolean isPlaying) {
                binding.btnAudioPlayPause.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
            }

            @Override
            public void onProgressUpdate(long currentMs, long durationMs) {
                if (durationMs > 0) {
                    binding.seekbarAudio.setMax((int) durationMs);
                    binding.seekbarAudio.setProgress((int) currentMs);
                    binding.tvAudioTime.setText(formatTime(currentMs) + " / " + formatTime(durationMs));
                }
            }

            @Override
            public void onPlaybackEnded(int page) {
                binding.btnAudioPlayPause.setImageResource(R.drawable.ic_play);
                binding.seekbarAudio.setProgress(0);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(ImageQuranReaderActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String formatTime(long ms) {
        long seconds = (ms / 1000) % 60;
        long minutes = (ms / (1000 * 60)) % 60;
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }

    private void toggleOverlay() {
        if (isOverlayVisible) {
            binding.layoutTopBar.animate().alpha(0.0f).setDuration(250).withEndAction(() -> binding.layoutTopBar.setVisibility(View.GONE));
            binding.layoutBottomBar.animate().alpha(0.0f).setDuration(250).withEndAction(() -> binding.layoutBottomBar.setVisibility(View.GONE));
            isOverlayVisible = false;
        } else {
            binding.layoutTopBar.setVisibility(View.VISIBLE);
            binding.layoutBottomBar.setVisibility(View.VISIBLE);
            binding.layoutTopBar.animate().alpha(1.0f).setDuration(250);
            binding.layoutBottomBar.animate().alpha(1.0f).setDuration(250);
            isOverlayVisible = true;
        }
    }

    private void openVisualPageIndex() {
        VisualPageIndexBottomSheet sheet = VisualPageIndexBottomSheet.newInstance(currentRiwaya, currentPage);
        sheet.setOnPageSelectedListener(page -> binding.viewPagerQuran.setCurrentItem(ImageQuranConfig.pageToPosition(page), false));
        sheet.show(getSupportFragmentManager(), "VisualPageIndexBottomSheet");
    }

    private void openSurahIndex() {
        SurahIndexBottomSheet sheet = SurahIndexBottomSheet.newInstance();
        sheet.setOnSurahSelectedListener(startPage -> binding.viewPagerQuran.setCurrentItem(ImageQuranConfig.pageToPosition(startPage), false));
        sheet.show(getSupportFragmentManager(), "SurahIndexBottomSheet");
    }

    private void openJuzIndex() {
        JuzIndexBottomSheet sheet = JuzIndexBottomSheet.newInstance();
        sheet.setOnJuzSelectedListener(startPage -> binding.viewPagerQuran.setCurrentItem(ImageQuranConfig.pageToPosition(startPage), false));
        sheet.show(getSupportFragmentManager(), "JuzIndexBottomSheet");
    }

    private void openBookmarks() {
        BookmarksBottomSheet sheet = BookmarksBottomSheet.newInstance();
        sheet.setOnBookmarkSelectedListener((riwayaKey, page) -> {
            Riwaya targetRiwaya = Riwaya.fromKey(riwayaKey);
            if (targetRiwaya != currentRiwaya) {
                switchRiwaya(targetRiwaya, page);
            } else {
                binding.viewPagerQuran.setCurrentItem(ImageQuranConfig.pageToPosition(page), false);
            }
        });
        sheet.show(getSupportFragmentManager(), "BookmarksBottomSheet");
    }

    private void openQuickNavigation() {
        QuickNavigationBottomSheet sheet = QuickNavigationBottomSheet.newInstance(currentRiwaya, currentPage);
        sheet.setOnNavigationListener(new QuickNavigationBottomSheet.NavigationListener() {
            @Override
            public void onJumpToPage(int page) {
                binding.viewPagerQuran.setCurrentItem(ImageQuranConfig.pageToPosition(page), false);
            }

            @Override
            public void onSwitchRiwaya(Riwaya riwaya) {
                if (riwaya != currentRiwaya) {
                    switchRiwaya(riwaya, -1);
                }
            }

            @Override
            public void onOpenVisualIndex() {
                openVisualPageIndex();
            }

            @Override
            public void onOpenSurahIndex() {
                openSurahIndex();
            }

            @Override
            public void onOpenJuzIndex() {
                openJuzIndex();
            }

            @Override
            public void onOpenBookmarks() {
                openBookmarks();
            }
        });
        sheet.show(getSupportFragmentManager(), "QuickNavigationBottomSheet");
    }

    public void switchRiwaya(Riwaya newRiwaya, int targetPage) {
        currentRiwaya = newRiwaya;
        repository.setSelectedRiwaya(newRiwaya);
        pagerAdapter.setRiwaya(newRiwaya);

        int pageToOpen = (targetPage >= 1 && targetPage <= ImageQuranConfig.TOTAL_PAGES) ? targetPage : repository.getLastPage(newRiwaya);
        currentPage = pageToOpen;
        binding.viewPagerQuran.setCurrentItem(ImageQuranConfig.pageToPosition(currentPage), false);

        updatePageMetadata();
        Toast.makeText(this, "تم التبديل إلى " + newRiwaya.getDisplayName(), Toast.LENGTH_SHORT).show();
    }

    private void updateScreenKeepOn() {
        boolean keepOn = repository.isKeepScreenOnEnabled();
        binding.getRoot().setKeepScreenOn(keepOn);
    }

    private void setupBackDispatcher() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                audioPlayerManager.pause();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateScreenKeepOn();
    }

    @Override
    protected void onDestroy() {
        audioPlayerManager.pause();
        super.onDestroy();
    }
}
