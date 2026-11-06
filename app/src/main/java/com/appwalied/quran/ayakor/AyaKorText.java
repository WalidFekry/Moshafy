package com.appwalied.quran.ayakor;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.appwalied.quran.R;
import com.appwalied.quran.azkar.AzkarManager;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityAyakortextBinding;

public class AyaKorText extends BaseActivity {

    private ActivityAyakortextBinding binding;
    private int currentTextSizeSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAyakortextBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentTextSizeSp = AzkarManager.getTextSize(this, 21);
        binding.tvAyakorText.setTextSize(currentTextSizeSp);

        binding.backButton.setOnClickListener(v -> finish());

        binding.btnFontMinus.setOnClickListener(v -> {
            if (currentTextSizeSp > 15) {
                currentTextSizeSp -= 2;
                binding.tvAyakorText.setTextSize(currentTextSizeSp);
                AzkarManager.saveTextSize(this, currentTextSizeSp);
            } else {
                Toast.makeText(this, "هذا أصغر حجم للخط", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnFontPlus.setOnClickListener(v -> {
            if (currentTextSizeSp < 38) {
                currentTextSizeSp += 2;
                binding.tvAyakorText.setTextSize(currentTextSizeSp);
                AzkarManager.saveTextSize(this, currentTextSizeSp);
            } else {
                Toast.makeText(this, "هذا أكبر حجم للخط", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnCopy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                ClipData clip = ClipData.newPlainText("AyatAlKursi", getString(R.string.ayakortxt));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "تم نسخ آية الكرسي بنجاح 📋", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnShare.setOnClickListener(v -> {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            String shareBody = "﴿" + getString(R.string.ayakortxt) + "﴾\n\n" +
                    "مشاركة من تطبيق مصحفي القرآن الكريم:\nhttps://play.google.com/store/apps/details?id=" + getPackageName();
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sendIntent, "مشاركة آية الكرسي:"));
        });

        showBanner(binding.adsContainer);
    }
}
