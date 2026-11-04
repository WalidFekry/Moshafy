package com.appwalied.quran.monw3at;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.adabters.Adapterrec1;
import com.appwalied.quran.adabters.itemrec;
import com.appwalied.quran.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class Title1 extends BaseActivity {
    List<itemrec> list;
    RecyclerView rec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title1);

        list=new ArrayList<>();
        rec= findViewById(R.id.rec1);
        rec.setLayoutManager(new LinearLayoutManager(this));
        // Azkar here
        Adapterrec1 adapterrec1=new Adapterrec1(list,Title1.this);
        rec.setAdapter(adapterrec1);
    }
}
