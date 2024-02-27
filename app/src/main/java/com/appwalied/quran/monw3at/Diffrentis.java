package com.appwalied.quran.monw3at;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.appwalied.quran.R;
import com.appwalied.quran.roqia.RoqiaNoIternet;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import guy4444.smartrate.SmartRate;

public class Diffrentis extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(this!=null && !isFinishing()){
                    SmartRate.Rate(Diffrentis.this
                            , "تقييم التطبيق"
                            , "تقييمك للتطبيق يساعدنا علي التطوير المستمر وتقديم المزيد"
                            , "تقييم الان"
                            , "حسنا يمكنك تقيممنا الان علي جوجل بلاي"
                            , "اضغط هنا"
                            , "ليس الان"
                            , "Thanks "
                            , Color.parseColor("#c65164")                 , 2         );


                }
            }
        }, 14000);



        super.onCreate(savedInstanceState);
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_diffrentis);


    }
    public void Title1 (View view){startActivity(new Intent(Diffrentis.this, Title1.class));}
    public void Title3 (View view){  Intent i = new Intent(Diffrentis.this, MainAhdes.class);
        startActivity(i);
       }
    public void Title4 (View view){startActivity(new Intent(Diffrentis.this, Mainwsaya.class));}
    public void Title5(View view){startActivity(new Intent(Diffrentis.this, Mainhagandomra.class));}




    public void Titlea(View view){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.co/IuFGow3uyK")));

    }
    public void Title(View view){
        startActivity(new Intent(Diffrentis.this, RoqiaNoIternet.class));
    }
}
