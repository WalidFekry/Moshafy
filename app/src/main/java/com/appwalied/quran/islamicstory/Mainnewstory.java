package com.appwalied.quran.islamicstory;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityMainnewstoryBinding;
import com.appwalied.quran.sahaba.item_story;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Mainnewstory extends BaseActivity {

    private ActivityMainnewstoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainnewstoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> finish());

        binding.recnewstory.setLayoutManager(new LinearLayoutManager(this));

        showLoading();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            DatabaseAsstes5 dp = DatabaseAsstes5.getInstance(getApplicationContext());
            ArrayList<item_story> data = dp.getalldata();
            runOnUiThread(() -> {
                if (!isFinishing() && !isDestroyed()) {
                    Adapternewstory adapter = new Adapternewstory(Mainnewstory.this, data);
                    binding.recnewstory.setAdapter(adapter);
                    hideLoading();
                }
            });
        });
        executor.shutdown();

        showBanner(binding.adsContainer);
        promptUserForRating();
    }
}
