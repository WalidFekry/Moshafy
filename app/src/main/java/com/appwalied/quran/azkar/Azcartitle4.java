package com.appwalied.quran.azkar;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.appwalied.quran.R;
import com.appwalied.quran.adabters.Adapterpager4;
import com.appwalied.quran.adabters.item_pager;

import java.util.ArrayList;
import java.util.List;

public class Azcartitle4 extends AppCompatActivity {
    List<item_pager> listpager5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azcartitle4);

        final ViewPager viewPager=(ViewPager)findViewById(R.id.viewpager1);
        listpager5=new ArrayList<>();
        listpager5.add(new item_pager(getString(R.string.noom1),""));
        listpager5.add(new item_pager(getString(R.string.noom2),getString(R.string.noom3)));
        listpager5.add(new item_pager(getString(R.string.noom4),""));
        listpager5.add(new item_pager(getString(R.string.noom6),""));
        listpager5.add(new item_pager(getString(R.string.noom7),""));
        listpager5.add(new item_pager(getString(R.string.noom8),""));
        Adapterpager4 adapterpager5=new Adapterpager4(Azcartitle4.this,listpager5);
        viewPager.setAdapter(adapterpager5);
    }
}
