package com.appwalied.quran.sonan;

import android.content.Intent;
import android.os.Bundle;

import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityMaintitlepagerAyaBinding;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MaintitlepagerAya extends BaseActivity {

    private ActivityMaintitlepagerAyaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMaintitlepagerAyaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> finish());

        Intent intent = getIntent();
        int positionId = intent.getIntExtra("positon", 0);

        showLoading();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            DatabaseAcsessAya dp = DatabaseAcsessAya.getInstance(getApplicationContext());
            dp.opean();
            ArrayList<AyaItem> list2 = dp.getalldata();
            dp.close();

            runOnUiThread(() -> {
                if (!isFinishing() && !isDestroyed()) {
                    pagerAya pager = new pagerAya(list2, MaintitlepagerAya.this);
                    binding.pagerAya.setAdapter(pager);
                    if (positionId > 0 && positionId <= list2.size()) {
                        binding.pagerAya.setCurrentItem(positionId - 1);
                    }
                    hideLoading();
                }
            });
        });
        executor.shutdown();

        showBanner(binding.adsContainer);
        setUpAds();
    }
}
