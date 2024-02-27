package com.appwalied.quran;

import android.graphics.Color;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import guy4444.smartrate.SmartRate;

public class Quran_fnish extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(this!=null && !isFinishing()){
                    SmartRate.Rate(Quran_fnish.this
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
        }, 16000);

        super.onCreate(savedInstanceState);
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_quran_fnish);

        AdView adView = (AdView) findViewById(R.id.quranfnish);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
    }
}
