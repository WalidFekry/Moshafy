package com.appwalied.quran.ahades;

import android.content.Intent;
import android.os.Bundle;

import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityMainpagerahdesBinding;

import java.util.ArrayList;
import java.util.concurrent.Executors;

public class Mainpagerahdes extends BaseActivity {

    private ActivityMainpagerahdesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainpagerahdesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> finish());

        Intent intent = getIntent();
        int positionId = intent.getIntExtra("positon", 0);

        showLoading();
        Executors.newSingleThreadExecutor().execute(() -> {
            DatabaseAcsessahdes dp = DatabaseAcsessahdes.getInstance(getApplicationContext());
            dp.opean();
            ArrayList<item_ahdes> listahdes = dp.getalldata();
            dp.close();

            runOnUiThread(() -> {
                if (!isFinishing() && !isDestroyed()) {
                    AdapterpagerAhdes adapter = new AdapterpagerAhdes(listahdes, Mainpagerahdes.this);
                    binding.pagerahdes.setAdapter(adapter);
                    if (positionId > 0 && positionId <= listahdes.size()) {
                        binding.pagerahdes.setCurrentItem(positionId - 1);
                    }
                    hideLoading();
                }
            });
        });

        showBanner(binding.adsContainer);
        setUpAds();
    }
}
