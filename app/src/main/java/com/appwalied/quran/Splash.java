package com.appwalied.quran;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.appwalied.quran.base.BaseActivity;

public class Splash extends BaseActivity {
    private static final int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(Splash.this, MainActivity.class));
            finish();
        }, SPLASH_TIME_OUT);
    }
}
