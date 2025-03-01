package com.appwalied.quran.monw3at.roqia;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;


public class RoqiaNoIternet extends BaseActivity {

    private static final String TAG = "TAG";
    private InterstitialAd mInterstitialAd;

    private AppCompatImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roqia_no_iternet);
        back = findViewById(R.id.back_button);
        back.setOnClickListener(v -> finish());
        setUpAds();
        getHandler().postDelayed(this::loadAds, 4000);
    }
}