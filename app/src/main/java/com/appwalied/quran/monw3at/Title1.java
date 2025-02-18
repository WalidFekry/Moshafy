package com.appwalied.quran.monw3at;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import com.appwalied.quran.R;
import com.appwalied.quran.adabters.Adapterrec1;
import com.appwalied.quran.adabters.itemrec;

import java.util.ArrayList;
import java.util.List;

public class Title1 extends AppCompatActivity {
    List<itemrec> list;
    RecyclerView rec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title1);

        list=new ArrayList<>();
        rec= findViewById(R.id.rec1);
        rec.setLayoutManager(new LinearLayoutManager(this));
        list.add(new itemrec(getResources().getString(R.string.t1)));
        list.add(new itemrec(getResources().getString(R.string.t2)));
        list.add(new itemrec(getResources().getString(R.string.t3)));
        list.add(new itemrec(getResources().getString(R.string.t4)));
        list.add(new itemrec(getResources().getString(R.string.t5)));
        list.add(new itemrec(getResources().getString(R.string.t6)));
        list.add(new itemrec(getResources().getString(R.string.t7)));
        list.add(new itemrec(getResources().getString(R.string.t8)));
        list.add(new itemrec(getResources().getString(R.string.t9)));
        list.add(new itemrec(getResources().getString(R.string.t10)));
        list.add(new itemrec(getResources().getString(R.string.t11)));
        Adapterrec1 adapterrec1=new Adapterrec1(list,Title1.this);
        rec.setAdapter(adapterrec1);
    }
}
