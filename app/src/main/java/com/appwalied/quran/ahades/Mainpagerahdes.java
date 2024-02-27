package com.appwalied.quran.ahades;

import android.app.MediaRouteButton;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.appwalied.quran.R;
import com.appwalied.quran.ayakor.Ayakor;
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
    ViewPager pagerahdes;
    ArrayList<item_ahdes> listahdes;
    private static final String TAG = "TAG";
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpagerahdes);
        Intent intent=getIntent();
        int ID=intent.getIntExtra("positon",0);
        pagerahdes=(ViewPager)findViewById(R.id.pagerahdes);
        ArrayList<item_ahdes>listahdes=new ArrayList<>();
        DatabaseAcsessahdes dp=DatabaseAcsessahdes.getInstance(this);
        dp.opean();
        listahdes= dp.getalldata();
        dp.close();
        AdapterpagerAhdes adapter =new AdapterpagerAhdes(listahdes,Mainpagerahdes.this);
        pagerahdes.setAdapter(adapter);
        pagerahdes.setCurrentItem(ID-1);

        setUpAds();
        getHandler().postDelayed(this::LoadAds, 4000);
    }

    private  void setUpAds(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = findViewById(R.id.M4);

        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);

        InterstitialAd.load(this, getString(R.string.Biny2), adRequest,
                new InterstitialAdLoadCallback() {
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
            mInterstitialAd.show(Mainpagerahdes.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }

    public void onBackClicked(View view) {
        onBackPressed();
    }
}
