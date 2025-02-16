package com.appwalied.quran.utils;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // تهيئة Google Mobile Ads SDK مرة واحدة عند بدء التطبيق
        MobileAds.initialize(this, initializationStatus -> {
            // يمكنك إضافة كود إضافي بعد التهيئة هنا
        });
    }
}
