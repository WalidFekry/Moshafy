package com.appwalied.quran.quranread.newquran;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.appwalied.quran.R;
import com.appwalied.quran.quranread.newquran.fragment.SurahFragment;

public class QuranRead extends AppCompatActivity {

    private static Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_quran_read);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);



        FragmentManager fragmentManager;
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment, SurahFragment.newInstance())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int count = fragmentManager.getBackStackEntryCount();
        if (count <= 0) {
            finish();
        } else {
//            getSupportActionBar().setTitle(getString(R.string.quran_read));
        }
        super.onBackPressed();
    }




}
