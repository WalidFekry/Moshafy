package com.appwalied.quran.azkar;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.viewpager.widget.ViewPager;

import com.appwalied.quran.R;
import com.appwalied.quran.adabters.Adapterpager6;
import com.appwalied.quran.adabters.item_pager;
import com.appwalied.quran.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class Azcartitle6 extends BaseActivity {
    List<item_pager> listpager7;
    private FrameLayout adsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azcartitle6);
        adsContainer = findViewById(R.id.adsContainer);

        final ViewPager viewPager = findViewById(R.id.viewpager1);
        listpager7 = new ArrayList<>();
        listpager7.add(new item_pager(getString(R.string.manzel1), ""));
        listpager7.add(new item_pager(getString(R.string.manzel2), ""));
        listpager7.add(new item_pager(getString(R.string.masgeed), ""));
        Adapterpager6 adapterpager7 = new Adapterpager6(Azcartitle6.this, listpager7);
        viewPager.setAdapter(adapterpager7);

        showBanner(adsContainer);
    }
}
