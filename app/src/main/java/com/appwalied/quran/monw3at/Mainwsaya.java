package com.appwalied.quran.monw3at;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.adabters.Adapterrec3;
import com.appwalied.quran.adabters.itemrex3;
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
import java.util.List;

public class Mainwsaya extends BaseActivity {
    private static final String TAG = "TAG";
    RecyclerView rec3;
    List<itemrex3> list;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainwsaya);

        AdView adView = (AdView) findViewById(R.id.mainwsaya);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
        rec3 = (RecyclerView) findViewById(R.id.rec3);
        rec3.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        list.add(new itemrex3(getResources().getString(R.string.wsya2), getResources().getString(R.string.wsya1)));
        list.add(new itemrex3(getResources().getString(R.string.wsya4), getResources().getString(R.string.wsya3)));
        list.add(new itemrex3(getResources().getString(R.string.wsya6), getResources().getString(R.string.wsya5)));
        list.add(new itemrex3(getResources().getString(R.string.wsya8), getResources().getString(R.string.wsya7)));
        Adapterrec3 adapterrec3 = new Adapterrec3(list, Mainwsaya.this);
        rec3.setAdapter(adapterrec3);

        setUpAds();
        getHandler().postDelayed(this::LoadAds, 4000);
    }

    private void setUpAds() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, getString(R.string.Biny1), adRequest,
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
            mInterstitialAd.show(Mainwsaya.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }
}
