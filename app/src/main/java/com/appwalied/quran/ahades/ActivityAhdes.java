package com.appwalied.quran.ahades;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;

import java.util.ArrayList;

public class ActivityAhdes extends BaseActivity {
    RecyclerView recahdes;
    ArrayList<item_ahdes> listahdes;
    private AppCompatImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ahdes);
        back = findViewById(R.id.back_button);
        recahdes = findViewById(R.id.recahdes);
        back.setOnClickListener(v -> finish());
        recahdes.setLayoutManager(new LinearLayoutManager(this));
        listahdes = new ArrayList<>();
        DatabaseAcsessahdes dp = DatabaseAcsessahdes.getInstance(this);
        dp.opean();
        listahdes = dp.getalldata();
        dp.close();
        AdapterRecyclerahdes adapter = new AdapterRecyclerahdes(ActivityAhdes.this, listahdes);
        recahdes.setAdapter(adapter);
    }



}