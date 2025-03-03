package com.appwalied.quran.azkar;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.viewpager.widget.ViewPager;

import com.appwalied.quran.R;
import com.appwalied.quran.adabters.Adapterpager5;
import com.appwalied.quran.adabters.item_pager;
import com.appwalied.quran.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class Azcartitle5 extends BaseActivity {
    private static final String TAG = "TAG";
    List<item_pager> listpager5;
    private FrameLayout adsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azcartitle5);
        adsContainer = findViewById(R.id.adsContainer);

        final ViewPager viewPager = findViewById(R.id.viewpager1);
        listpager5 = new ArrayList<>();
        listpager5.add(new item_pager(getString(R.string.stkazmnelnom), ""));
        Adapterpager5 adapterpager6 = new Adapterpager5(Azcartitle5.this, listpager5);
        viewPager.setAdapter(adapterpager6);
        setUpAds();
        showBanner(adsContainer);
    }
}
