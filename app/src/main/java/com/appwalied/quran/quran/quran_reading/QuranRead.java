package com.appwalied.quran.quran.quran_reading;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.appwalied.quran.R;
import com.appwalied.quran.quran.quran_reading.fragment.SurahFragment;

public class QuranRead extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran_read);
        FragmentManager fragmentManager;
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fragment, SurahFragment.newInstance()).commit();
    }
}
