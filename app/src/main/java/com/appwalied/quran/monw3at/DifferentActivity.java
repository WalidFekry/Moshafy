package com.appwalied.quran.monw3at;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.appwalied.quran.ahades.ActivityAhdes;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityDiffrentisBinding;
import com.appwalied.quran.monw3at.roqia.RoqiaNoIternet;
import com.appwalied.quran.utils.shared_helper.SharedPrefsConstants;

public class DifferentActivity extends BaseActivity {

    private ActivityDiffrentisBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiffrentisBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> finish());

        binding.cardAzkarSalah.setOnClickListener(v ->
                startActivity(new Intent(this, Title1.class)));

        binding.cardRoqia.setOnClickListener(v ->
                startActivity(new Intent(this, RoqiaNoIternet.class)));

        binding.cardWsaya.setOnClickListener(v ->
                startActivity(new Intent(this, Mainwsaya.class)));

        binding.cardHajj.setOnClickListener(v ->
                startActivity(new Intent(this, Mainhagandomra.class)));

        binding.cardRamadanBag.setOnClickListener(v -> openRamadanBagApp());

        checkAndShowDialog(SharedPrefsConstants.MONW3AT_FIRST_TIME, "في قسم منوعات إسلامية، ستجد الأذكار، الرقية الشرعية، الأحاديث، الوصايا، سنن الرسول ﷺ، وأحكام الحج والعمرة 💙");

        showBanner(binding.adsContainer);
    }

    private void openRamadanBagApp() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.walid.myapplication")));
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.walid.myapplication")));
        }
    }
}
