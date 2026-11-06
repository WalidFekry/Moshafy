package com.appwalied.quran.quran.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityQuranSearchBinding;
import com.appwalied.quran.quran.db.QuranRepository;
import com.appwalied.quran.quran.db.models.Ayah;
import com.appwalied.quran.quran.ui.adapters.SearchResultAdapter;

import java.util.List;

public class QuranSearchActivity extends BaseActivity {

    private ActivityQuranSearchBinding binding;
    private SearchResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuranSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> finish());

        binding.rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchResultAdapter(ayah -> {
            Intent intent = new Intent(QuranSearchActivity.this, QuranReadingActivity.class);
            intent.putExtra("suraId", ayah.getSuraId());
            intent.putExtra("targetAyahId", ayah.getVerseId());
            intent.putExtra("pageNumber", ayah.getPageNumber());
            startActivity(intent);
        });
        binding.rvSearchResults.setAdapter(adapter);

        binding.etQuranSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        binding.etQuranSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(binding.etQuranSearch.getText().toString());
                return true;
            }
            return false;
        });
    }

    private void performSearch(String query) {
        if (query == null || query.trim().length() < 2) {
            adapter.setResults(null);
            binding.tvSearchEmpty.setVisibility(View.GONE);
            binding.pbSearchLoading.setVisibility(View.GONE);
            return;
        }

        binding.pbSearchLoading.setVisibility(View.VISIBLE);
        binding.tvSearchEmpty.setVisibility(View.GONE);

        QuranRepository.getInstance(this).searchVerses(query, new QuranRepository.Callback<List<Ayah>>() {
            @Override
            public void onResult(List<Ayah> result) {
                binding.pbSearchLoading.setVisibility(View.GONE);
                adapter.setResults(result);
                if (result == null || result.isEmpty()) {
                    binding.tvSearchEmpty.setVisibility(View.VISIBLE);
                } else {
                    binding.tvSearchEmpty.setVisibility(View.GONE);
                }
            }
        });
    }
}
