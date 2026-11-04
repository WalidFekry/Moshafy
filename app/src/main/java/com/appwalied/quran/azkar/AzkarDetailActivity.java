package com.appwalied.quran.azkar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.viewpager2.widget.ViewPager2;

import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityAzkarDetailBinding;

import java.util.List;
import java.util.Locale;

public class AzkarDetailActivity extends BaseActivity {

    public static final String EXTRA_CATEGORY = "extra_azkar_category";

    private ActivityAzkarDetailBinding binding;
    private int categoryType = AzkarRepository.CATEGORY_MORNING;
    private List<AzkarModel> azkarList;
    private AzkarAdapter adapter;

    public static Intent getIntent(Context context, int categoryType) {
        Intent intent = new Intent(context, AzkarDetailActivity.class);
        intent.putExtra(EXTRA_CATEGORY, categoryType);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAzkarDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent() != null && getIntent().hasExtra(EXTRA_CATEGORY)) {
            categoryType = getIntent().getIntExtra(EXTRA_CATEGORY, AzkarRepository.CATEGORY_MORNING);
        }

        setupToolbar();
        loadAzkarData();
        setupViewPager();
        setupNavigationButtons();

        showBanner(binding.adsContainer);
    }

    private void setupToolbar() {
        binding.backButton.setOnClickListener(v -> finish());
        String title = AzkarRepository.getCategoryTitle(categoryType);
        binding.tvTitle.setText(title);

        binding.btnTopResetAll.setOnClickListener(v -> {
            if (azkarList != null) {
                for (AzkarModel model : azkarList) {
                    model.resetCount();
                    AzkarManager.resetCount(this, model.getId());
                }
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
                Toast.makeText(this, "تمت إعادة ضبط جميع عدادات " + title, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAzkarData() {
        azkarList = AzkarRepository.getAzkarByCategory(this, categoryType);
    }

    private void setupViewPager() {
        if (azkarList == null || azkarList.isEmpty()) return;

        adapter = new AzkarAdapter(this, azkarList);
        binding.viewPager.setAdapter(adapter);

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updatePageIndicator(position);
            }
        });

        updatePageIndicator(0);
    }

    private void setupNavigationButtons() {
        binding.btnPrev.setOnClickListener(v -> {
            int current = binding.viewPager.getCurrentItem();
            if (current > 0) {
                binding.viewPager.setCurrentItem(current - 1, true);
            }
        });

        binding.btnNextItem.setOnClickListener(v -> {
            int current = binding.viewPager.getCurrentItem();
            if (azkarList != null && current < azkarList.size() - 1) {
                binding.viewPager.setCurrentItem(current + 1, true);
            }
        });
    }

    private void updatePageIndicator(int position) {
        if (azkarList == null || azkarList.isEmpty()) return;

        int total = azkarList.size();
        int currentDisplay = position + 1;

        String subtitle = String.format(Locale.getDefault(), "الذكر %d من %d", currentDisplay, total);
        binding.tvSubtitle.setText(subtitle);

        String pageIndicator = String.format(Locale.getDefault(), "%d / %d", currentDisplay, total);
        binding.tvPageIndicator.setText(pageIndicator);

        binding.btnPrev.setEnabled(position > 0);
        binding.btnPrev.setAlpha(position > 0 ? 1.0f : 0.4f);

        boolean hasNext = position < total - 1;
        binding.btnNextItem.setEnabled(hasNext);
        binding.btnNextItem.setAlpha(hasNext ? 1.0f : 0.4f);
    }
}
