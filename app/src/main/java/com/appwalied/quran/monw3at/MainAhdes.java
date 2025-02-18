package com.appwalied.quran.monw3at;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import com.appwalied.quran.R;
import com.appwalied.quran.adabters.Adapterrec2;
import com.appwalied.quran.adabters.itemrec;

import java.util.ArrayList;
import java.util.List;

public class MainAhdes extends AppCompatActivity {
    List<itemrec> list;
    RecyclerView rec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ahdes);

        list=new ArrayList<>();
        rec=(RecyclerView)findViewById(R.id.rec2);
        rec.setLayoutManager(new LinearLayoutManager(this));
        list.add(new itemrec(getResources().getString(R.string.d1)));
        list.add(new itemrec(getResources().getString(R.string.d2)));
        list.add(new itemrec(getResources().getString(R.string.d3)));
        list.add(new itemrec(getResources().getString(R.string.d4)));
        list.add(new itemrec(getResources().getString(R.string.d5)));
        list.add(new itemrec(getResources().getString(R.string.d6)));
        list.add(new itemrec(getResources().getString(R.string.d7)));
        list.add(new itemrec(getResources().getString(R.string.d8)));
        list.add(new itemrec(getResources().getString(R.string.d9)));
        list.add(new itemrec(getResources().getString(R.string.d10)));
        list.add(new itemrec(getResources().getString(R.string.d11)));
        list.add(new itemrec(getResources().getString(R.string.d12)));
        list.add(new itemrec(getResources().getString(R.string.d13)));
        list.add(new itemrec(getResources().getString(R.string.d14)));
        list.add(new itemrec(getResources().getString(R.string.d15)));
        list.add(new itemrec(getResources().getString(R.string.d16)));
        list.add(new itemrec(getResources().getString(R.string.d17)));
        list.add(new itemrec(getResources().getString(R.string.d18)));
        list.add(new itemrec(getResources().getString(R.string.d19)));
        list.add(new itemrec(getResources().getString(R.string.d20)));
        Adapterrec2 adapterrec2=new Adapterrec2(list,MainAhdes.this);
        rec.setAdapter(adapterrec2);
    }
}
