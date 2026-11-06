package com.appwalied.quran.sahaba;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityMainStoryBinding;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainStory extends BaseActivity {

    private ActivityMainStoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainStoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> finish());

        binding.recstory.setLayoutManager(new LinearLayoutManager(this));

        showLoading();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            DatabaseAsstes4 dp = DatabaseAsstes4.getInstance(getApplicationContext());
            ArrayList<item_story> data = dp.getalldata();
            runOnUiThread(() -> {
                if (!isFinishing() && !isDestroyed()) {
                    Adapterstory adapter = new Adapterstory(MainStory.this, data);
                    binding.recstory.setAdapter(adapter);
                    hideLoading();
                }
            });
        });
        executor.shutdown();

        showBanner(binding.adsContainer);
        promptUserForRating();
    }
}
