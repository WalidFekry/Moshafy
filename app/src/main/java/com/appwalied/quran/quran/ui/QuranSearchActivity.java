package com.appwalied.quran.quran.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.quran.db.QuranRepository;
import com.appwalied.quran.quran.db.models.Ayah;
import com.appwalied.quran.quran.ui.adapters.SearchResultAdapter;

import java.util.List;

public class QuranSearchActivity extends BaseActivity {

    private EditText etSearch;
    private ProgressBar pbLoading;
    private TextView tvEmpty;
    private RecyclerView rvResults;
    private SearchResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran_search);

        Toolbar toolbar = findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("البحث في القرآن الكريم");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        etSearch = findViewById(R.id.et_quran_search);
        pbLoading = findViewById(R.id.pb_search_loading);
        tvEmpty = findViewById(R.id.tv_search_empty);
        rvResults = findViewById(R.id.rv_search_results);

        rvResults.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchResultAdapter(ayah -> {
            Intent intent = new Intent(QuranSearchActivity.this, QuranReadingActivity.class);
            intent.putExtra("suraId", ayah.getSuraId());
            intent.putExtra("targetAyahId", ayah.getVerseId());
            intent.putExtra("pageNumber", ayah.getPageNumber());
            startActivity(intent);
        });
        rvResults.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(etSearch.getText().toString());
                return true;
            }
            return false;
        });
    }

    private void performSearch(String query) {
        if (query == null || query.trim().length() < 2) {
            adapter.setResults(null);
            tvEmpty.setVisibility(View.GONE);
            pbLoading.setVisibility(View.GONE);
            return;
        }

        pbLoading.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);

        QuranRepository.getInstance(this).searchVerses(query, new QuranRepository.Callback<List<Ayah>>() {
            @Override
            public void onResult(List<Ayah> result) {
                pbLoading.setVisibility(View.GONE);
                adapter.setResults(result);
                if (result == null || result.isEmpty()) {
                    tvEmpty.setVisibility(View.VISIBLE);
                } else {
                    tvEmpty.setVisibility(View.GONE);
                }
            }
        });
    }
}
