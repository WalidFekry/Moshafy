package com.appwalied.quran.ahades;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;

import java.util.ArrayList;
import java.util.concurrent.Executors;

public class ActivityAhdes extends BaseActivity {
    private RecyclerView recahdes;
    private ArrayList<item_ahdes> listahdes;
    private AppCompatImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ahdes);

        back = findViewById(R.id.back_button);
        recahdes = findViewById(R.id.recahdes);
        if (back != null) {
            back.setOnClickListener(v -> finish());
        }

        recahdes.setLayoutManager(new LinearLayoutManager(this));
        listahdes = new ArrayList<>();

        showLoading();
        Executors.newSingleThreadExecutor().execute(() -> {
            DatabaseAcsessahdes dp = DatabaseAcsessahdes.getInstance(getApplicationContext());
            ArrayList<item_ahdes> data = dp.getalldata();
            runOnUiThread(() -> {
                if (!isFinishing() && !isDestroyed()) {
                    listahdes.clear();
                    listahdes.addAll(data);
                    AdapterRecyclerahdes adapter = new AdapterRecyclerahdes(ActivityAhdes.this, listahdes);
                    recahdes.setAdapter(adapter);
                    hideLoading();
                }
            });
        });
    }
}
