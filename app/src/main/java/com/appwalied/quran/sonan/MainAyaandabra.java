package com.appwalied.quran.sonan;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;

import java.util.ArrayList;
import java.util.concurrent.Executors;

public class MainAyaandabra extends BaseActivity {
    private RecyclerView recaya;
    private ArrayList<item_Aya> listaya;
    private AppCompatImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ayaandabra);

        recaya = findViewById(R.id.recaya);
        back = findViewById(R.id.back_button);
        if (back != null) {
            back.setOnClickListener(v -> finish());
        }

        recaya.setLayoutManager(new LinearLayoutManager(this));
        listaya = new ArrayList<>();

        showLoading();
        Executors.newSingleThreadExecutor().execute(() -> {
            DatabaseAcsessAya dp = DatabaseAcsessAya.getInstance(getApplicationContext());
            ArrayList<item_Aya> data = dp.getalldata();
            runOnUiThread(() -> {
                if (!isFinishing() && !isDestroyed()) {
                    listaya.clear();
                    listaya.addAll(data);
                    AdapterRecyclerAya adapter = new AdapterRecyclerAya(MainAyaandabra.this, listaya);
                    recaya.setAdapter(adapter);
                    hideLoading();
                }
            });
        });

        promptUserForRating();
    }
}
