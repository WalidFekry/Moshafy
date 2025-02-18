package com.appwalied.quran.azkar;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.appwalied.quran.R;
import com.appwalied.quran.adabters.Adapterpager6;
import com.appwalied.quran.adabters.item_pager;

import java.util.ArrayList;
import java.util.List;

public class Azcartitle6 extends AppCompatActivity {
    List<item_pager> listpager7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azcartitle6);
        final ViewPager viewPager=(ViewPager)findViewById(R.id.viewpager1);
        listpager7=new ArrayList<>();
        listpager7.add(new item_pager(getString(R.string.manzel1),""));
        listpager7.add(new item_pager(getString(R.string.manzel2),""));
        listpager7.add(new item_pager(getString(R.string.masgeed),""));
        Adapterpager6 adapterpager7=new Adapterpager6(Azcartitle6.this,listpager7);
        viewPager.setAdapter(adapterpager7);
    }
}
