package com.appwalied.quran.azkar;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.appwalied.quran.R;
import com.appwalied.quran.adabters.Adapterpager2;
import com.appwalied.quran.adabters.item_pager;
import com.appwalied.quran.base.BaseActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

public class Azcartitle2 extends BaseActivity {
    private FrameLayout adsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azcartitle2);
        adsContainer = findViewById(R.id.adsContainer);

        List<item_pager> listpager2;
        int size=18;
        final ViewPager viewPager=(ViewPager)findViewById(R.id.viewpager1);
        listpager2=new ArrayList<>();
        listpager2.add(new item_pager(getString(R.string.msaa1),getString(R.string.msaa2)));
        listpager2.add(new item_pager(getString(R.string.msaa3),getString(R.string.msaa4)));
        listpager2.add(new item_pager(getString(R.string.msaa5),getString(R.string.msaa6)));
        listpager2.add(new item_pager(getString(R.string.msaa7),getString(R.string.msaa8)));
        listpager2.add(new item_pager(getString(R.string.msaa9),getString(R.string.msaa10)));
        listpager2.add(new item_pager(getString(R.string.msaa11),getString(R.string.msaa12)));
        listpager2.add(new item_pager(getString(R.string.msaa13),getString(R.string.msaa14)));
        listpager2.add(new item_pager(getString(R.string.msaa15),getString(R.string.msaa16)));
        listpager2.add(new item_pager(getString(R.string.msaa17),getString(R.string.msaa18)));
        listpager2.add(new item_pager(getString(R.string.msaa19),""));
        Adapterpager2 adapterpager=new Adapterpager2(Azcartitle2.this,listpager2);
        viewPager.setAdapter(adapterpager);

        showBanner(adsContainer);
    }
}
