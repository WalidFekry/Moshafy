package com.appwalied.quran;

import android.graphics.Color;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import guy4444.smartrate.SmartRate;

public class Quran_fnish extends AppCompatActivity {

    private AppCompatImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran_fnish);
        back = findViewById(R.id.back_button);
        back.setOnClickListener(v -> finish());
        AdView adView = (AdView) findViewById(R.id.quranfnish);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
    }
}
