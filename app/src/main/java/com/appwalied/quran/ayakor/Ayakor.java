package com.appwalied.quran.ayakor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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

import guy4444.smartrate.SmartRate;

public class Ayakor extends BaseActivity {

    private static final String TAG = "TAG";
    private AppCompatImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayakor);
        back = findViewById(R.id.back_button);
        back.setOnClickListener(v -> finish());
        setUpAds();

        promptUserForRating();
    }

    public void korpic1(View view) {
        Intent i = new Intent(Ayakor.this, AyaKorPic.class);
        startActivity(i);
        }


    public void kortext2(View view) {
        Intent i = new Intent(Ayakor.this, AyaKorText.class);
        startActivity(i);
    }

    public void korlisten3(View view) {
        Intent i = new Intent(Ayakor.this, AyaKorListen.class);
        startActivity(i);
    }
}
