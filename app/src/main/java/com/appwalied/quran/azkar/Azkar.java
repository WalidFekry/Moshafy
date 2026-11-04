package com.appwalied.quran.azkar;

import android.os.Bundle;

import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityAzkarBinding;
import com.appwalied.quran.utils.shared_helper.SharedPrefsConstants;

public class Azkar extends BaseActivity {

    private ActivityAzkarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAzkarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> finish());

        // Card click listeners
        binding.cardMorning.setOnClickListener(v ->
                startActivity(AzkarDetailActivity.getIntent(this, AzkarRepository.CATEGORY_MORNING)));

        binding.cardEvening.setOnClickListener(v ->
                startActivity(AzkarDetailActivity.getIntent(this, AzkarRepository.CATEGORY_EVENING)));

        binding.cardRamadan.setOnClickListener(v ->
                startActivity(AzkarDetailActivity.getIntent(this, AzkarRepository.CATEGORY_RAMADAN)));

        binding.cardSleep.setOnClickListener(v ->
                startActivity(AzkarDetailActivity.getIntent(this, AzkarRepository.CATEGORY_SLEEP)));

        binding.cardWakeup.setOnClickListener(v ->
                startActivity(AzkarDetailActivity.getIntent(this, AzkarRepository.CATEGORY_WAKEUP)));

        binding.cardGeneral.setOnClickListener(v ->
                startActivity(AzkarDetailActivity.getIntent(this, AzkarRepository.CATEGORY_GENERAL)));

        checkAndShowDialog(
                SharedPrefsConstants.AZKAR_FIRST_TIME,
                "يحتوي هذا القسم على أذكار المسلم كاملة مع عداد تفاعلي ومفضلة بدون إنترنت ^_^"
        );

        promptUserForRating();
    }
}
