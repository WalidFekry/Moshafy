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
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ayakor);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (this != null && !isFinishing()) {
                    SmartRate.Rate(Ayakor.this
                            , "تقييم التطبيق"
                            , "تقييمك للتطبيق يساعدنا علي التطوير المستمر وتقديم المزيد"
                            , "تقييم الان"
                            , "حسنا يمكنك تقيممنا الان علي جوجل بلاي"
                            , "اضغط هنا"
                            , "ليس الان"
                            , "Thanks "
                            , Color.parseColor("#c65164"), 2);


                }
            }
        }, 6000);

        setUpAds();
        getHandler().postDelayed(this::LoadAds, 4000);
    }

    public void korpic1(View view) {
        Intent i = new Intent(Ayakor.this, ayakorpic.class);
        startActivity(i);
        }


    public void kortext2(View view) {
        Intent i = new Intent(Ayakor.this, ayakortext.class);
        startActivity(i);
    }

    public void korlisten3(View view) {
        Intent i = new Intent(Ayakor.this, ayakorlisten.class);
        startActivity(i);
    }

    private  void setUpAds(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();

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
            mInterstitialAd.show(Ayakor.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }

}
