package com.appwalied.quran.sahaba;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;

import java.util.ArrayList;
import java.util.concurrent.Executors;

public class MainStory extends BaseActivity {
    private RecyclerView recstory;
    private ArrayList<item_story> listabra;
    private AppCompatImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_story);

        recstory = findViewById(R.id.recstory);
        back = findViewById(R.id.back_button);
        if (back != null) {
            back.setOnClickListener(v -> finish());
        }

        recstory.setLayoutManager(new LinearLayoutManager(this));
        listabra = new ArrayList<>();

        showLoading();
        Executors.newSingleThreadExecutor().execute(() -> {
            DatabaseAsstes4 dp = DatabaseAsstes4.getInstance(getApplicationContext());
            ArrayList<item_story> data = dp.getalldata();
            runOnUiThread(() -> {
                if (!isFinishing() && !isDestroyed()) {
                    listabra.clear();
                    listabra.addAll(data);
                    Adapterstory adapter = new Adapterstory(MainStory.this, listabra);
                    recstory.setAdapter(adapter);
                    hideLoading();
                }
            });
        });

        promptUserForRating();
    }
}
