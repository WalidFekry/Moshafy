package com.appwalied.quran.sahaba;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.appwalied.quran.azkar.AzkarManager;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityMainstorytitleBinding;

public class Mainstorytitle extends BaseActivity {

    private ActivityMainstorytitleBinding binding;
    private int currentTextSizeSp;
    private String title;
    private String headerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainstorytitleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> finish());

        Intent intent = getIntent();
        title = intent.getStringExtra("storytitle");
        headerText = intent.getStringExtra("headert");

        binding.header.setText(headerText);
        binding.titlestory.setText(title);

        currentTextSizeSp = AzkarManager.getTextSize(this, 18);
        binding.titlestory.setTextSize(currentTextSizeSp);

        binding.btnFontMinus.setOnClickListener(v -> {
            if (currentTextSizeSp > 14) {
                currentTextSizeSp -= 2;
                binding.titlestory.setTextSize(currentTextSizeSp);
                AzkarManager.saveTextSize(this, currentTextSizeSp);
            } else {
                Toast.makeText(this, "هذا أصغر حجم للخط", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnFontPlus.setOnClickListener(v -> {
            if (currentTextSizeSp < 36) {
                currentTextSizeSp += 2;
                binding.titlestory.setTextSize(currentTextSizeSp);
                AzkarManager.saveTextSize(this, currentTextSizeSp);
            } else {
                Toast.makeText(this, "هذا أكبر حجم للخط", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnCopy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                String copyText = (headerText != null ? headerText + "\n\n" : "") + title;
                ClipData clip = ClipData.newPlainText("SahabaStoryText", copyText);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "تم نسخ القصة بنجاح 📋", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnShare.setOnClickListener(v -> {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            String shareBody = (headerText != null ? headerText + "\n\n" : "") +
                    title + "\n\n" +
                    "مشاركة من تطبيق مصحفي القرآن الكريم:\nhttps://play.google.com/store/apps/details?id=" + getPackageName();
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sendIntent, "مشاركة قصة الصحابي:"));
        });

        showBanner(binding.adsContainer);
    }
}
