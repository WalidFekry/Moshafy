package com.appwalied.quran.monw3at;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.appwalied.quran.R;
import com.appwalied.quran.adabters.Adapterrec2;
import com.appwalied.quran.adabters.Item;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityMainAhdesBinding;

import java.util.ArrayList;
import java.util.List;

public class MainAhdes extends BaseActivity {

    private ActivityMainAhdesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainAhdesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> finish());

        binding.rec2.setLayoutManager(new LinearLayoutManager(this));

        List<Item> list = new ArrayList<>();
        list.add(new Item(getResources().getString(R.string.d1)));
        list.add(new Item(getResources().getString(R.string.d2)));
        list.add(new Item(getResources().getString(R.string.d3)));
        list.add(new Item(getResources().getString(R.string.d4)));
        list.add(new Item(getResources().getString(R.string.d5)));
        list.add(new Item(getResources().getString(R.string.d6)));
        list.add(new Item(getResources().getString(R.string.d7)));
        list.add(new Item(getResources().getString(R.string.d8)));
        list.add(new Item(getResources().getString(R.string.d9)));
        list.add(new Item(getResources().getString(R.string.d10)));
        list.add(new Item(getResources().getString(R.string.d11)));
        list.add(new Item(getResources().getString(R.string.d12)));
        list.add(new Item(getResources().getString(R.string.d13)));
        list.add(new Item(getResources().getString(R.string.d14)));
        list.add(new Item(getResources().getString(R.string.d15)));
        list.add(new Item(getResources().getString(R.string.d16)));
        list.add(new Item(getResources().getString(R.string.d17)));
        list.add(new Item(getResources().getString(R.string.d18)));
        list.add(new Item(getResources().getString(R.string.d19)));
        list.add(new Item(getResources().getString(R.string.d20)));

        Adapterrec2 adapterrec2 = new Adapterrec2(list, this);
        binding.rec2.setAdapter(adapterrec2);

        showBanner(binding.adsContainer);
    }
}
