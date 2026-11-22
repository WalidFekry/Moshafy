package com.appwalied.quran.image_quran.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityRiwayaSelectionBinding;
import com.appwalied.quran.image_quran.data.ImageQuranRepository;
import com.appwalied.quran.image_quran.data.Riwaya;

public class RiwayaSelectionActivity extends BaseActivity {

    private ActivityRiwayaSelectionBinding binding;
    private ImageQuranRepository repository;
    private Riwaya selectedRiwaya = Riwaya.HAFS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRiwayaSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = ImageQuranRepository.getInstance(this);
        selectedRiwaya = repository.getSelectedRiwaya();

        updateSelectionUI();
        updateLastPageInfo();
        setupClickListeners();
    }

    private void updateSelectionUI() {
        binding.radioHafs.setChecked(selectedRiwaya == Riwaya.HAFS);
        binding.radioWarsh.setChecked(selectedRiwaya == Riwaya.WARSH);
    }

    private void updateLastPageInfo() {
        int hafsPage = repository.getLastPage(Riwaya.HAFS);
        int warshPage = repository.getLastPage(Riwaya.WARSH);

        binding.tvHafsLastPage.setText("آخر موضع قراءة: صفحة " + hafsPage);
        binding.tvWarshLastPage.setText("آخر موضع قراءة: صفحة " + warshPage);
    }

    private void setupClickListeners() {
        binding.btnBackSelection.setOnClickListener(v -> finish());

        binding.cardRiwayaHafs.setOnClickListener(v -> {
            selectedRiwaya = Riwaya.HAFS;
            updateSelectionUI();
        });

        binding.cardRiwayaWarsh.setOnClickListener(v -> {
            selectedRiwaya = Riwaya.WARSH;
            updateSelectionUI();
        });

        binding.btnOpenQuran.setOnClickListener(v -> {
            repository.setSelectedRiwaya(selectedRiwaya);
            Intent intent = new Intent(RiwayaSelectionActivity.this, ImageQuranReaderActivity.class);
            intent.putExtra(ImageQuranReaderActivity.EXTRA_RIWAYA_KEY, selectedRiwaya.getKey());
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLastPageInfo();
    }
}
