package com.appwalied.quran.islamicstory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.MediaRouteButton;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.sonan.MaintitlepagerAya;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import net.steamcrafted.materialiconlib.MaterialIconView;

public class Maintitlenewstory extends BaseActivity {
    TextView titlestory,header;
    String title,headert;
    int a = 0;
    int c = 18;
    MaterialIconView min,plus;
    private static final String TAG = "TAG";
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintitlenewstory);
        titlestory=(TextView)findViewById(R.id.titlestory);
        header=(TextView)findViewById(R.id.header);
        Intent intent=getIntent();
        title=intent.getStringExtra("storytitle");
        headert=intent.getStringExtra("headert");
        titlestory.setText(title);
        header.setText(headert);
        min=(MaterialIconView)findViewById(R.id.min);
        plus=(MaterialIconView)findViewById(R.id.plus);
        min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c > 13) {
                    c--;
                } else
                    Toast.makeText(Maintitlenewstory.this, "لايمكن تصغير الخط اصغر من ذلك", Toast.LENGTH_SHORT).show();
                titlestory.setTextSize(c);
                header.setTextSize(c);
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c < 50) {
                    c++;
                } else
                    Toast.makeText(Maintitlenewstory.this, "لايمكن تكبير الخط اكبر من ذلك", Toast.LENGTH_SHORT).show();
                titlestory.setTextSize(c);
                header.setTextSize(c);


            }
        });

        setUpAds();
        getHandler().postDelayed(this::LoadAds, 4000);
    }

    public void copy(View view){
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("",headert+"\n"+  title+
                "\n"+   "https://bit.ly/3jMaYsR\n");
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "تم نسخ النصوص", Toast.LENGTH_SHORT).show();


    }
    public void share(View view){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT," تطبيق مصحفي");
        sendIntent.putExtra(Intent.EXTRA_TEXT,headert+"\n"+title+ "مشاركه نصوص من تطبيق مصحفي  \n" +"\n"+ "\n"+"https://bit.ly/3jMaYsR\n");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent,"مشاركه تطبيق مصحفي:"));
        Toast.makeText(this, "جزاك الله خيرا لمشاركه التطبيق", Toast.LENGTH_SHORT).show();
    }


    public void onBackClicked(View view) {
        onBackPressed();
    }

    private  void setUpAds(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = findViewById(R.id.M2);

        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);

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
            mInterstitialAd.show(Maintitlenewstory.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }
}