package com.appwalied.quran.monw3at.roqia;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.appwalied.quran.R;
import com.appwalied.quran.azkar.AzkarManager;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityRoqiaNoIternetBinding;

public class RoqiaNoIternet extends BaseActivity {

    private ActivityRoqiaNoIternetBinding binding;
    private int currentTextSizeSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoqiaNoIternetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentTextSizeSp = AzkarManager.getTextSize(this, 19);
        binding.tvRoqiaContent.setTextSize(currentTextSizeSp);

        String fullRoqiaText = "أعوذ بالله السميع العليم من الشيطان الرجيم\n\n" +
                getString(R.string.roqia_txt_1) + "\n\n" +
                getString(R.string.roqiz_txt2) + "\n\n" +
                getString(R.string.roqia_txt_3) + "\n\n" +
                getString(R.string.roqia_txt_4) + "\n\n" +
                getString(R.string.roqia_txt_5) + "\n\n" +
                getString(R.string.roqia_txt_6) + "\n\n" +
                getString(R.string.roqia_txt_7) + "\n\n" +
                getString(R.string.roqia_txt_8);

        binding.tvRoqiaContent.setText(fullRoqiaText);

        binding.backButton.setOnClickListener(v -> finish());

        binding.btnFontMinus.setOnClickListener(v -> {
            if (currentTextSizeSp > 14) {
                currentTextSizeSp -= 2;
                binding.tvRoqiaContent.setTextSize(currentTextSizeSp);
                AzkarManager.saveTextSize(this, currentTextSizeSp);
            } else {
                Toast.makeText(this, "هذا أصغر حجم للخط", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnFontPlus.setOnClickListener(v -> {
            if (currentTextSizeSp < 36) {
                currentTextSizeSp += 2;
                binding.tvRoqiaContent.setTextSize(currentTextSizeSp);
                AzkarManager.saveTextSize(this, currentTextSizeSp);
            } else {
                Toast.makeText(this, "هذا أكبر حجم للخط", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnCopy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                ClipData clip = ClipData.newPlainText("RoqiaText", fullRoqiaText);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "تم نسخ الرقية الشرعية بنجاح 📋", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnShare.setOnClickListener(v -> {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            String shareBody = fullRoqiaText + "\n\n" +
                    "مشاركة من تطبيق مصحفي القرآن الكريم:\nhttps://play.google.com/store/apps/details?id=" + getPackageName();
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sendIntent, "مشاركة الرقية الشرعية:"));
        });

        showBanner(binding.adsContainer);
        setUpAds();
    }
}
