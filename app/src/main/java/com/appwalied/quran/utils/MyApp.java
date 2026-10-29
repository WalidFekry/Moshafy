package com.appwalied.quran.utils;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(() -> {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(this, initializationStatus -> {
            });
        }).start();
    }
}
