package com.appwalied.quran.monw3at;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import com.appwalied.quran.R;
import com.appwalied.quran.adabters.Adapterrec3;
import com.appwalied.quran.adabters.itemrex3;

import java.util.ArrayList;
import java.util.List;

public class Mainhagandomra extends AppCompatActivity {
    RecyclerView rec4;
    List<itemrex3> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainhagandomra);
        rec4=(RecyclerView)findViewById(R.id.rec4);
        rec4.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();
        list.add(new itemrex3(getResources().getString(R.string.heg2), getResources().getString(R.string.heg1)));
        list.add(new itemrex3(getResources().getString(R.string.heg4), getResources().getString(R.string.heg3)));
        list.add(new itemrex3(getResources().getString(R.string.heg6), getResources().getString(R.string.heg5)));
        list.add(new itemrex3(getResources().getString(R.string.heg8), getResources().getString(R.string.heg7)));
        Adapterrec3 adapterrec3=new Adapterrec3(list,Mainhagandomra.this);
        rec4.setAdapter(adapterrec3);
    }
}
