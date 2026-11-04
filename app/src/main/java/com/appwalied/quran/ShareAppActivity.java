package com.appwalied.quran;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityShareAppBinding;

public class ShareAppActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityShareAppBinding binding = ActivityShareAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> finish());
        binding.btnShareApp.setOnClickListener(v -> shareApp());
        binding.btnCopyText.setOnClickListener(v -> copyShareText());
    }

    private String getFormattedShareText() {
        return getString(R.string.about1) + "\n\n" +
                "لتحميل تطبيق مصحفي 👇👇:\n" +
                "https://play.google.com/store/apps/details?id=" + getPackageName();
    }

    private void shareApp() {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "تطبيق مصحفي - القرآن الكريم والأذكار");
        sendIntent.putExtra(Intent.EXTRA_TEXT, getFormattedShareText());
        startActivity(Intent.createChooser(sendIntent, "مشاركة تطبيق مصحفي:"));
    }

    private void copyShareText() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText("ShareAppText", getFormattedShareText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "تم نسخ النص ورابط التطبيق بنجاح 📋", Toast.LENGTH_SHORT).show();
        }
    }
}
