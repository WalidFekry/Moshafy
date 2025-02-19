package com.appwalied.quran.sonan;

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

public class MaintitlepagerAya extends BaseActivity {
    private static final String TAG = "TAG";
    ViewPager pagerAya;
    private InterstitialAd mInterstitialAd;
    private AppCompatImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintitlepager_aya);
        back = findViewById(R.id.back_button);
        back.setOnClickListener(v -> finish());
        Intent intent = getIntent();
        int ID = intent.getIntExtra("positon", 0);
        pagerAya = findViewById(R.id.pagerAya);
        ArrayList<item_Aya> list2 = new ArrayList<>();

        DatabaseAcsessAya dp = DatabaseAcsessAya.getInstance(this);
        dp.opean();
        list2 = dp.getalldata();
        dp.close();
        pagerAya pager = new pagerAya(list2, MaintitlepagerAya.this);
        pagerAya.setAdapter(pager);
        pagerAya.setCurrentItem(ID - 1);

        setUpAds();
        getHandler().postDelayed(this::LoadAds, 4000);
    }

    private void setUpAds() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = findViewById(R.id.M5);

        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);

        InterstitialAd.load(this, getString(R.string.Biny1), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Log.i(TAG, "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i(TAG, loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });
    }

    private void LoadAds() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(MaintitlepagerAya.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }
}
