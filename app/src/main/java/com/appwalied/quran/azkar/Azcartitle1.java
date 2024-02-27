package com.appwalied.quran.azkar;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.appwalied.quran.R;
import com.appwalied.quran.adabters.Adapterpager1;
import com.appwalied.quran.adabters.item_pager;
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

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import java.util.List;

public class Azcartitle1 extends BaseActivity {
    List<item_pager> listpager1;
    MaterialIconView plus;
    int size=18;
    private static final String TAG = "TAG";
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_azcartitle1);

        AdView adView = (AdView) findViewById(R.id.azkartitle1);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);

        final ViewPager viewPager=(ViewPager)findViewById(R.id.viewpager1);
        listpager1=new ArrayList<>();
        listpager1.add(new item_pager(getString(R.string.shhh1),""));
        listpager1.add(new item_pager(getString(R.string.shhh2),""));
        listpager1.add(new item_pager(getString(R.string.shhh3),""));
        listpager1.add(new item_pager(getString(R.string.sbh1),getString(R.string.sbh2)));
        listpager1.add(new item_pager(getString(R.string.sbh3),getString(R.string.sbh4)));
        listpager1.add(new item_pager(getString(R.string.sbh5),getString(R.string.sbh6)));
        listpager1.add(new item_pager(getString(R.string.sbh7),getString(R.string.sbh8)));
        listpager1.add(new item_pager(getString(R.string.sbh9),getString(R.string.sbh10)));
        listpager1.add(new item_pager(getString(R.string.sbh11),getString(R.string.sbh12)));
        listpager1.add(new item_pager(getString(R.string.sbh13),getString(R.string.sbh14)));
        listpager1.add(new item_pager(getString(R.string.sbh15),getString(R.string.sbh16)));
        listpager1.add(new item_pager(getString(R.string.sbh17),""));
        listpager1.add(new item_pager(getString(R.string.sbh18),""));
        listpager1.add(new item_pager(getString(R.string.sbh19),""));
        listpager1.add(new item_pager(getString(R.string.sbh20),""));
        listpager1.add(new item_pager(getString(R.string.sbh21),""));
        listpager1.add(new item_pager(getString(R.string.sbh22),""));
        listpager1.add(new item_pager(getString(R.string.sbh23),""));
        listpager1.add(new item_pager(getString(R.string.sbh24),""));
        listpager1.add(new item_pager(getString(R.string.sbh25),""));
        listpager1.add(new item_pager(getString(R.string.sbh26),""));
        listpager1.add(new item_pager(getString(R.string.sbh27),""));
        listpager1.add(new item_pager(getString(R.string.sbh28),""));
        listpager1.add(new item_pager(getString(R.string.sbh29),""));
        listpager1.add(new item_pager(getString(R.string.sbh30),""));
        listpager1.add(new item_pager(getString(R.string.sbh31),getString(R.string.sbh32)));
        listpager1.add(new item_pager(getString(R.string.sbh33),getString(R.string.sbh34)));
        listpager1.add(new item_pager(getString(R.string.sbh35),getString(R.string.sbh36)));
        listpager1.add(new item_pager(getString(R.string.sbh37),""));
        listpager1.add(new item_pager(getString(R.string.sbh38),""));
        Adapterpager1 adapterpager1=new Adapterpager1(Azcartitle1.this,listpager1);
        viewPager.setAdapter(adapterpager1);

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
            mInterstitialAd.show(Azcartitle1.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }
}
