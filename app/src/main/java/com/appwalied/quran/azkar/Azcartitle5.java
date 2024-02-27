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
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_azcartitle5);

        final ViewPager viewPager=(ViewPager)findViewById(R.id.viewpager6);
        listpager5=new ArrayList<>();
        listpager5.add(new item_pager(getString(R.string.stkazmnelnom),""));
        Adapterpager5 adapterpager6=new Adapterpager5(Azcartitle5.this,listpager5);
        viewPager.setAdapter(adapterpager6);
        setUpAds();
        getHandler().postDelayed(this::LoadAds, 4000);
    }

    private  void setUpAds(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, getString(R.string.Biny3), adRequest,
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
            mInterstitialAd.show(Azcartitle5.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }
}
