package com.appwalied.quran.azkar;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.viewpager.widget.ViewPager;

import com.appwalied.quran.R;
import com.appwalied.quran.adabters.Adapterpager3;
import com.appwalied.quran.adabters.item_pager;
import com.appwalied.quran.base.BaseActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import java.util.List;

public class Azcartitle3 extends BaseActivity {
    List<item_pager> listpager3;
    MaterialIconView plus;
    int size = 18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_azcartitle3);

        AdView adView = (AdView) findViewById(R.id.Azcartitle3);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager3);
        listpager3 = new ArrayList<>();
        listpager3.add(new item_pager(getString(R.string.azkarrmdan1), getString(R.string.azkarrmdan2)));
        listpager3.add(new item_pager(getString(R.string.azkarrmdan3), getString(R.string.azkarrmdan4)));
        listpager3.add(new item_pager(getString(R.string.azkarrmdan5), getString(R.string.azkarrmdan6)));
        listpager3.add(new item_pager(getString(R.string.azkarrmdan7), getString(R.string.azkarrmdan8)));
        listpager3.add(new item_pager(getString(R.string.azkarrmdan9), getString(R.string.azkarrmdan10)));
        listpager3.add(new item_pager(getString(R.string.azkarrmdan11), getString(R.string.azkarrmdan12)));
        listpager3.add(new item_pager(getString(R.string.azkarrmdan13), getString(R.string.azkarrmdan14)));
        listpager3.add(new item_pager(getString(R.string.azkarrmdan15), getString(R.string.azkarrmdan16)));
        listpager3.add(new item_pager(getString(R.string.ftor1), ""));
        listpager3.add(new item_pager(getString(R.string.ftor2), getString(R.string.ftor3)));
        listpager3.add(new item_pager(getString(R.string.ftor4), getString(R.string.ftor5)));
        listpager3.add(new item_pager(getString(R.string.ftor6), getString(R.string.ftor7)));
        listpager3.add(new item_pager(getString(R.string.ftor8), getString(R.string.ftor9)));
        listpager3.add(new item_pager(getString(R.string.ftor10), getString(R.string.ftor11)));
        listpager3.add(new item_pager(getString(R.string.ftor12), getString(R.string.ftor13)));

        Adapterpager3 adapterpager3 = new Adapterpager3(Azcartitle3.this, listpager3);
        viewPager.setAdapter(adapterpager3);
    }


}
