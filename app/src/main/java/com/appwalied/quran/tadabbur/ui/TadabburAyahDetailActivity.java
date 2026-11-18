package com.appwalied.quran.tadabbur.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityTadabburAyahDetailBinding;
import com.appwalied.quran.tadabbur.db.QuranV2Repository;
import com.appwalied.quran.tadabbur.db.room.TadabburBookmarkEntity;
import com.appwalied.quran.tadabbur.db.room.TadabburLocalRepository;
import com.appwalied.quran.tadabbur.model.QuranV2Ayah;
import com.google.android.material.tabs.TabLayout;

public class TadabburAyahDetailActivity extends BaseActivity {

    private ActivityTadabburAyahDetailBinding binding;
    private QuranV2Repository repository;
    private TadabburLocalRepository localRepository;

    private int sora = 1;
    private int ayaNo = 1;
    private QuranV2Ayah currentAyah;

    private int currentCategoryTab = 0; // 0: Tafseer, 1: Eerab, 2: Maany, 3: Reasons
    private int currentTafseerIndex = 0; // 0: Saadi, 1: Moysar, 2: Bughiu
    private float contentFontSizeSp = 16f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTadabburAyahDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = QuranV2Repository.getInstance(this);
        localRepository = TadabburLocalRepository.getInstance(this);

        parseIntent();
        setupToolbar();
        setupTabs();
        setupTafseerSelector();
        setupClickListeners();

        currentTafseerIndex = localRepository.getLastTafseerIndex();
        loadAyahDetails();
    }

    private void parseIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            sora = intent.getIntExtra("SORA", 1);
            ayaNo = intent.getIntExtra("AYA_NO", 1);
        }
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupTabs() {
        binding.tabCategories.addTab(binding.tabCategories.newTab().setText("التفاسير"));
        binding.tabCategories.addTab(binding.tabCategories.newTab().setText("إعراب القرآن"));
        binding.tabCategories.addTab(binding.tabCategories.newTab().setText("معاني الكلمات"));
        binding.tabCategories.addTab(binding.tabCategories.newTab().setText("أسباب النزول"));

        binding.tabCategories.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentCategoryTab = tab.getPosition();
                if (currentCategoryTab == 0) {
                    binding.layoutTafseerSelector.setVisibility(View.VISIBLE);
                } else {
                    binding.layoutTafseerSelector.setVisibility(View.GONE);
                }
                updateContentDisplay();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupTafseerSelector() {
        updateTafseerButtonsStyle();

        binding.btnTafseerSaadi.setOnClickListener(v -> {
            currentTafseerIndex = 0;
            localRepository.saveLastTafseerIndex(0);
            updateTafseerButtonsStyle();
            updateContentDisplay();
        });

        binding.btnTafseerMoysar.setOnClickListener(v -> {
            currentTafseerIndex = 1;
            localRepository.saveLastTafseerIndex(1);
            updateTafseerButtonsStyle();
            updateContentDisplay();
        });

        binding.btnTafseerBughiu.setOnClickListener(v -> {
            currentTafseerIndex = 2;
            localRepository.saveLastTafseerIndex(2);
            updateTafseerButtonsStyle();
            updateContentDisplay();
        });
    }

    private void updateTafseerButtonsStyle() {
        int activeColor = getResources().getColor(R.color.colorPrimary, getTheme());
        int inactiveColor = getResources().getColor(R.color.text_secondary, getTheme());

        binding.btnTafseerSaadi.setTextColor(currentTafseerIndex == 0 ? activeColor : inactiveColor);
        binding.btnTafseerMoysar.setTextColor(currentTafseerIndex == 1 ? activeColor : inactiveColor);
        binding.btnTafseerBughiu.setTextColor(currentTafseerIndex == 2 ? activeColor : inactiveColor);
    }

    private void setupClickListeners() {
        binding.btnDetailCopy.setOnClickListener(v -> {
            if (currentAyah == null) return;
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                String copyText = currentAyah.getAyaText() + " [" + currentAyah.getSoraNameAr() + ": " + currentAyah.getAyaNo() + "]\n\n" +
                        binding.tvContentSectionTitle.getText() + ":\n" + binding.tvContentBody.getText();
                ClipData clip = ClipData.newPlainText("Ayah & Tafseer", copyText);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "تم نسخ النص والتفسير بنجاح 📋", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnDetailShare.setOnClickListener(v -> {
            if (currentAyah == null) return;
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String shareBody = "﴿ " + currentAyah.getAyaText() + " ﴾\n" +
                    "سورة " + currentAyah.getSoraNameAr() + " - الآية " + currentAyah.getAyaNo() + "\n\n" +
                    "■ " + binding.tvContentSectionTitle.getText() + ":\n" +
                    binding.tvContentBody.getText() + "\n\n" +
                    "مشارك من تطبيق مُصحفي 🌸";
            intent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(intent, "مشاركة الآية والتفسير"));
        });

        binding.btnDetailBookmark.setOnClickListener(v -> {
            if (currentAyah == null) return;
            TadabburBookmarkEntity entity = new TadabburBookmarkEntity(
                    currentAyah.getSora(),
                    currentAyah.getAyaNo(),
                    currentAyah.getPage(),
                    currentAyah.getJozz(),
                    currentAyah.getSoraNameAr(),
                    currentAyah.getAyaText(),
                    System.currentTimeMillis()
            );
            localRepository.toggleBookmark(entity, isBookmarked -> {
                if (isBookmarked) {
                    binding.btnDetailBookmark.setIconResource(R.drawable.ic_favorite);
                    Toast.makeText(this, "تمت إضافة الآية للمفضلة 🌸", Toast.LENGTH_SHORT).show();
                } else {
                    binding.btnDetailBookmark.setIconResource(R.drawable.ic_favorite_border);
                    Toast.makeText(this, "تمت إزالة الآية من المفضلة", Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.btnContentZoomIn.setOnClickListener(v -> {
            if (contentFontSizeSp < 30f) {
                contentFontSizeSp += 2f;
                binding.tvContentBody.setTextSize(TypedValue.COMPLEX_UNIT_SP, contentFontSizeSp);
            }
        });

        binding.btnContentZoomOut.setOnClickListener(v -> {
            if (contentFontSizeSp > 12f) {
                contentFontSizeSp -= 2f;
                binding.tvContentBody.setTextSize(TypedValue.COMPLEX_UNIT_SP, contentFontSizeSp);
            }
        });

        binding.btnPrevAyah.setOnClickListener(v -> {
            if (currentAyah != null && currentAyah.getId() > 1) {
                navigateToAyah(currentAyah.getId() - 1);
            }
        });

        binding.btnNextAyah.setOnClickListener(v -> {
            if (currentAyah != null && currentAyah.getId() < 6236) {
                navigateToAyah(currentAyah.getId() + 1);
            }
        });
    }

    private void navigateToAyah(int targetId) {
        if (targetId < 1 || targetId > 6236) return;
        showLoading();
        repository.getAyahById(targetId, ayah -> {
            hideLoading();
            if (ayah != null) {
                currentAyah = ayah;
                sora = ayah.getSora();
                ayaNo = ayah.getAyaNo();
                bindHeaderData();
                updateContentDisplay();
                checkBookmarkStatus();
                binding.scrollView.smoothScrollTo(0, 0);
            }
        });
    }

    private void loadAyahDetails() {
        showLoading();
        repository.getAyahBySoraAndAya(sora, ayaNo, ayah -> {
            hideLoading();
            if (ayah != null) {
                currentAyah = ayah;
                bindHeaderData();
                updateContentDisplay();
                checkBookmarkStatus();
            } else {
                Toast.makeText(this, "تعذر تحميل بيانات الآية", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void bindHeaderData() {
        binding.tvDetailSurahInfo.setText("سورة " + currentAyah.getSoraNameAr() + " - الآية " + currentAyah.getAyaNo());
        binding.tvDetailJuzPage.setText("الجزء " + currentAyah.getJozz() + " • الصفحة " + currentAyah.getPage());
        binding.tvDetailAyahText.setText(currentAyah.getAyaText());

        binding.tvAyahNavIndicator.setText("آية " + currentAyah.getAyaNo() + " (" + currentAyah.getSoraNameAr() + ")");
        binding.btnPrevAyah.setEnabled(currentAyah.getId() > 1);
        binding.btnNextAyah.setEnabled(currentAyah.getId() < 6236);
    }

    private void checkBookmarkStatus() {
        localRepository.isBookmarked(sora, ayaNo, isBookmarked -> {
            if (isBookmarked) {
                binding.btnDetailBookmark.setIconResource(R.drawable.ic_favorite);
            } else {
                binding.btnDetailBookmark.setIconResource(R.drawable.ic_favorite_border);
            }
        });
    }

    private void updateContentDisplay() {
        if (currentAyah == null) return;

        String title = "";
        String body = "";
        boolean isAvailable = false;

        switch (currentCategoryTab) {
            case 0: // Tafseer
                if (currentTafseerIndex == 0) {
                    title = "تفسير السعدي (تيسير الكريم الرحمن)";
                    body = currentAyah.getTafseerSaadi();
                    isAvailable = currentAyah.hasSaadi();
                } else if (currentTafseerIndex == 1) {
                    title = "التفسير الميسر";
                    body = currentAyah.getTafseerMoysar();
                    isAvailable = currentAyah.hasMoysar();
                } else {
                    title = "تفسير البغوي (معالم التنزيل)";
                    body = currentAyah.getTafseerBughiu();
                    isAvailable = currentAyah.hasBughiu();
                }
                break;
            case 1: // Eerab
                title = "إعراب القرآن الكريم";
                body = currentAyah.getEarabQuran();
                isAvailable = currentAyah.hasEarab();
                break;
            case 2: // Maany
                title = "معاني مفردات الآية";
                body = currentAyah.getMaanyAya();
                isAvailable = currentAyah.hasMaany();
                break;
            case 3: // Reasons of verses
                title = "أسباب النزول";
                body = currentAyah.getReasonsOfVerses();
                isAvailable = currentAyah.hasReasons();
                break;
        }

        binding.tvContentSectionTitle.setText(title);

        if (isAvailable && body != null && !body.trim().isEmpty()) {
            // Clean unescaped newlines if present
            String cleanedBody = body.replace("\\n", "\n").replace("\\r", "").trim();
            binding.tvContentBody.setText(cleanedBody);
            binding.tvContentBody.setVisibility(View.VISIBLE);
            binding.layoutContentEmpty.setVisibility(View.GONE);
        } else {
            binding.tvContentBody.setVisibility(View.GONE);
            binding.layoutContentEmpty.setVisibility(View.VISIBLE);
            binding.tvEmptyNotice.setText("لا تتوفر معلومات لـ (" + title + ") لهذه الآية الشريفة في قاعدة البيانات.");
        }
    }
}
