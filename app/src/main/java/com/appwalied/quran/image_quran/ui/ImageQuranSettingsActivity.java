package com.appwalied.quran.image_quran.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityImageQuranSettingsBinding;
import com.appwalied.quran.image_quran.data.ImageQuranRepository;

public class ImageQuranSettingsActivity extends BaseActivity {

    private ActivityImageQuranSettingsBinding binding;
    private ImageQuranRepository repository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageQuranSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = ImageQuranRepository.getInstance(this);

        initSwitches();
        setupClickListeners();
    }

    private void initSwitches() {
        binding.switchKeepScreenOn.setChecked(repository.isKeepScreenOnEnabled());
        binding.switchAutoPrefetch.setChecked(repository.isAutoPrefetchEnabled());
    }

    private void setupClickListeners() {
        binding.btnBackSettings.setOnClickListener(v -> finish());

        binding.switchKeepScreenOn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            repository.setKeepScreenOnEnabled(isChecked);
        });

        binding.switchAutoPrefetch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            repository.setAutoPrefetchEnabled(isChecked);
        });
    }
}
