package com.appwalied.quran.azkar;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.appwalied.quran.R;
import com.appwalied.quran.adabters.Adapterpager5;
import com.appwalied.quran.adabters.item_pager;
import com.appwalied.quran.ayakor.Ayakor;
import com.appwalied.quran.base.BaseActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.ArrayList;
import java.util.List;

public class Azcartitle5 extends BaseActivity {
    List<item_pager> listpager5;
    private static final String TAG = "TAG";
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azcartitle5);

        final ViewPager viewPager=(ViewPager)findViewById(R.id.viewpager1);
        listpager5=new ArrayList<>();
        listpager5.add(new item_pager(getString(R.string.stkazmnelnom),""));
        Adapterpager5 adapterpager6=new Adapterpager5(Azcartitle5.this,listpager5);
        viewPager.setAdapter(adapterpager6);
        setUpAds();
        getHandler().postDelayed(this::loadAds, 4000);
    }
}
