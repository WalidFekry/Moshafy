package com.appwalied.quran.sonan;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityMainAyaandabraBinding;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainAyaandabra extends BaseActivity {

    private ActivityMainAyaandabraBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainAyaandabraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> finish());

        binding.recaya.setLayoutManager(new LinearLayoutManager(this));

        showLoading();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            DatabaseAcsessAya dp = DatabaseAcsessAya.getInstance(getApplicationContext());
            ArrayList<AyaItem> data = dp.getalldata();
            runOnUiThread(() -> {
                if (!isFinishing() && !isDestroyed()) {
                    AdapterRecyclerAya adapter = new AdapterRecyclerAya(MainAyaandabra.this, data);
                    binding.recaya.setAdapter(adapter);
                    hideLoading();
                }
            });
        });
        executor.shutdown();

        showBanner(binding.adsContainer);
        promptUserForRating();
    }
}
