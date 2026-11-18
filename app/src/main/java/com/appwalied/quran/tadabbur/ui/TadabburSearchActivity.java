package com.appwalied.quran.tadabbur.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityTadabburSearchBinding;
import com.appwalied.quran.tadabbur.db.QuranV2Repository;
import com.appwalied.quran.tadabbur.db.QuranV2Repository.SearchScope;
import com.appwalied.quran.tadabbur.model.QuranV2Ayah;
import com.appwalied.quran.tadabbur.ui.adapters.TadabburSearchResultAdapter;

import java.util.ArrayList;
import java.util.List;

public class TadabburSearchActivity extends BaseActivity {

    private ActivityTadabburSearchBinding binding;
    private QuranV2Repository repository;
    private TadabburSearchResultAdapter adapter;

    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    private SearchScope currentScope = SearchScope.ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTadabburSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = QuranV2Repository.getInstance(this);

        setupToolbar();
        setupRecyclerView();
        setupSearchInput();
        setupChipGroup();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        binding.rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TadabburSearchResultAdapter(ayah -> {
            Intent intent = new Intent(TadabburSearchActivity.this, TadabburAyahDetailActivity.class);
            intent.putExtra("SORA", ayah.getSora());
            intent.putExtra("AYA_NO", ayah.getAyaNo());
            startActivity(intent);
        });
        binding.rvSearchResults.setAdapter(adapter);
    }

    private void setupSearchInput() {
        binding.etSearchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString().trim();
                binding.btnClearQuery.setVisibility(text.isEmpty() ? View.GONE : View.VISIBLE);
                scheduleSearch(text);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.btnClearQuery.setOnClickListener(v -> {
            binding.etSearchQuery.setText("");
            adapter.setResults(new ArrayList<>());
            updateSearchUI(0, false);
        });
    }

    private void setupChipGroup() {
        binding.chipGroupScope.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == binding.chipScopeAyah.getId()) {
                currentScope = SearchScope.AYAH_TEXT;
            } else if (checkedId == binding.chipScopeTafseer.getId()) {
                currentScope = SearchScope.TAFSEER;
            } else if (checkedId == binding.chipScopeEarab.getId()) {
                currentScope = SearchScope.EARAB;
            } else if (checkedId == binding.chipScopeMaany.getId()) {
                currentScope = SearchScope.MAANY;
            } else {
                currentScope = SearchScope.ALL;
            }
            performSearch(binding.etSearchQuery.getText().toString().trim());
        });
    }

    private void scheduleSearch(String query) {
        if (searchRunnable != null) {
            searchHandler.removeCallbacks(searchRunnable);
        }
        searchRunnable = () -> performSearch(query);
        searchHandler.postDelayed(searchRunnable, 350); // 350ms debounce
    }

    private void performSearch(String query) {
        if (query.isEmpty()) {
            adapter.setResults(new ArrayList<>());
            updateSearchUI(0, false);
            return;
        }

        binding.progressSearch.setVisibility(View.VISIBLE);
        repository.search(query, currentScope, results -> {
            binding.progressSearch.setVisibility(View.GONE);
            List<QuranV2Ayah> safeResults = (results != null) ? results : new ArrayList<>();
            adapter.setResults(safeResults);
            updateSearchUI(safeResults.size(), true);
        });
    }

    private void updateSearchUI(int count, boolean hasSearched) {
        if (!hasSearched) {
            binding.tvSearchResultsCount.setVisibility(View.GONE);
            binding.rvSearchResults.setVisibility(View.GONE);
            binding.layoutSearchEmpty.setVisibility(View.VISIBLE);
            binding.tvSearchEmptyMsg.setText("ادخل كلمة أو نصًا في صندوق البحث أعلاه للبدء");
        } else if (count == 0) {
            binding.tvSearchResultsCount.setVisibility(View.GONE);
            binding.rvSearchResults.setVisibility(View.GONE);
            binding.layoutSearchEmpty.setVisibility(View.VISIBLE);
            binding.tvSearchEmptyMsg.setText("لم يتم العثور على نتائج تطابق البحث.");
        } else {
            binding.tvSearchResultsCount.setVisibility(View.VISIBLE);
            binding.tvSearchResultsCount.setText("نتائج البحث (" + count + " نتيجة)");
            binding.rvSearchResults.setVisibility(View.VISIBLE);
            binding.layoutSearchEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        if (searchRunnable != null) {
            searchHandler.removeCallbacks(searchRunnable);
        }
        super.onDestroy();
    }
}
