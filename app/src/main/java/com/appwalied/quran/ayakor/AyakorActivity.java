package com.appwalied.quran.ayakor;

import android.content.Intent;
import android.os.Bundle;

import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityAyakorBinding;

public class AyakorActivity extends BaseActivity {

    private ActivityAyakorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAyakorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> finish());

        binding.cardText.setOnClickListener(v ->
                startActivity(new Intent(this, AyaKorText.class)));

        binding.cardListen.setOnClickListener(v ->
                startActivity(new Intent(this, AyaKorListen.class)));

        binding.cardPic.setOnClickListener(v ->
                startActivity(new Intent(this, AyaKorPic.class)));

        setUpAds();
        promptUserForRating();
    }
}
