package com.appwalied.quran;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;

import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityAboutBinding;

public class AboutActivity extends BaseActivity {

    private ActivityAboutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> finish());

        // Dynamic App Version
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            binding.tvAppVersion.setText("الإصدار " + pInfo.versionName);
        } catch (Exception e) {
            binding.tvAppVersion.setText("الإصدار 6.0");
        }

        // Action Listeners
        binding.btnRateApp.setOnClickListener(v -> openPlayStore());
        binding.btnShareApp.setOnClickListener(v -> openShareActivity());
        binding.btnContactDev.setOnClickListener(v -> sendEmail("prowalidfekry@gmail.com", "رسالة إلى مطور تطبيق مصحفي"));
        binding.btnSendFeedback.setOnClickListener(v -> sendEmail("prowalidfekry@gmail.com", "اقتراح أو استفسار حول تطبيق مصحفي"));
        binding.btnMoreApps.setOnClickListener(v -> openDeveloperPage());
    }

    private void openPlayStore() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    private void openShareActivity() {
        startActivity(new Intent(this, ShareAppActivity.class));
    }

    private void sendEmail(String email, String subject) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        startActivity(Intent.createChooser(emailIntent, "إرسال بريد إلكتروني..."));
    }

    private void openDeveloperPage() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=6257553101128037563")));
    }


}
