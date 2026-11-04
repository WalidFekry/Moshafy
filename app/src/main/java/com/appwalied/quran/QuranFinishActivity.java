package com.appwalied.quran;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.appwalied.quran.azkar.AzkarManager;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityQuranFnishBinding;

public class QuranFinishActivity extends BaseActivity {

    private ActivityQuranFnishBinding binding;
    private int currentTextSizeSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuranFnishBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentTextSizeSp = AzkarManager.getTextSize(this, 19);
        binding.tvDuaText.setTextSize(currentTextSizeSp);

        binding.backButton.setOnClickListener(v -> finish());

        binding.btnFontMinus.setOnClickListener(v -> {
            if (currentTextSizeSp > 14) {
                currentTextSizeSp -= 2;
                binding.tvDuaText.setTextSize(currentTextSizeSp);
                AzkarManager.saveTextSize(this, currentTextSizeSp);
            } else {
                Toast.makeText(this, "هذا أصغر حجم للخط", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnFontPlus.setOnClickListener(v -> {
            if (currentTextSizeSp < 36) {
                currentTextSizeSp += 2;
                binding.tvDuaText.setTextSize(currentTextSizeSp);
                AzkarManager.saveTextSize(this, currentTextSizeSp);
            } else {
                Toast.makeText(this, "هذا أكبر حجم للخط", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnCopy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                ClipData clip = ClipData.newPlainText("QuranFinishDua", getString(R.string.quran_snish));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "تم نسخ دعاء ختم القرآن الكريم بنجاح 📋", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnShare.setOnClickListener(v -> {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            String shareBody = getString(R.string.quran_snish) + "\n\n" +
                    "مشاركة من تطبيق مصحفي القرآن الكريم:\nhttps://play.google.com/store/apps/details?id=" + getPackageName();
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sendIntent, "مشاركة دعاء ختم القرآن:"));
        });

        showBanner(binding.adsContainer);
    }
}
