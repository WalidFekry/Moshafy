package com.appwalied.quran.monw3at;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.adabters.Adapterrec3;
import com.appwalied.quran.adabters.itemrex3;
import com.appwalied.quran.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class Mainwsaya extends BaseActivity {
    private static final String TAG = "TAG";
    RecyclerView rec3;
    List<itemrex3> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainwsaya);

        showBanner(findViewById(R.id.adsContainer));
        rec3 = (RecyclerView) findViewById(R.id.rec3);
        rec3.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        list.add(new itemrex3(getResources().getString(R.string.wsya2), getResources().getString(R.string.wsya1)));
        list.add(new itemrex3(getResources().getString(R.string.wsya4), getResources().getString(R.string.wsya3)));
        list.add(new itemrex3(getResources().getString(R.string.wsya6), getResources().getString(R.string.wsya5)));
        list.add(new itemrex3(getResources().getString(R.string.wsya8), getResources().getString(R.string.wsya7)));
        Adapterrec3 adapterrec3 = new Adapterrec3(list, Mainwsaya.this);
        rec3.setAdapter(adapterrec3);

        setUpAds();
    }
}
