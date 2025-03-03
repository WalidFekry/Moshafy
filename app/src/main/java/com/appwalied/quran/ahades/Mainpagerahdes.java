package com.appwalied.quran.ahades;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.viewpager.widget.ViewPager;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.ArrayList;

public class Mainpagerahdes extends BaseActivity {
    private static final String TAG = "TAG";
    ViewPager pagerahdes;
    private AppCompatImageButton back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpagerahdes);
        back = findViewById(R.id.back_button);
        back.setOnClickListener(v -> finish());
        Intent intent = getIntent();
        int ID = intent.getIntExtra("positon", 0);
        pagerahdes = findViewById(R.id.pagerahdes);
        ArrayList<item_ahdes> listahdes = new ArrayList<>();
        DatabaseAcsessahdes dp = DatabaseAcsessahdes.getInstance(this);
        dp.opean();
        listahdes = dp.getalldata();
        dp.close();
        AdapterpagerAhdes adapter = new AdapterpagerAhdes(listahdes, Mainpagerahdes.this);
        pagerahdes.setAdapter(adapter);
        pagerahdes.setCurrentItem(ID - 1);

        setUpAds();
    }
    public void onBackClicked(View view) {
        onBackPressed();
    }
}
