package com.appwalied.quran.sonan;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;

import java.util.ArrayList;

public class MainAyaandabra extends BaseActivity {
    RecyclerView recaya;
    ArrayList<item_Aya> listaya;
    private AppCompatImageButton back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ayaandabra);
        recaya = findViewById(R.id.recaya);
        back = findViewById(R.id.back_button);
        back.setOnClickListener(v -> finish());
        recaya.setLayoutManager(new LinearLayoutManager(this));
        listaya = new ArrayList<>();
        DatabaseAcsessAya dp = DatabaseAcsessAya.getInstance(this);
        dp.opean();
        listaya = dp.getalldata();
        dp.close();
        AdapterRecyclerAya adapter = new AdapterRecyclerAya(MainAyaandabra.this, listaya);
        recaya.setAdapter(adapter);

    }
}