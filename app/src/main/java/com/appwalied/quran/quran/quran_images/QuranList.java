package com.appwalied.quran.quran.quran_images;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.appwalied.quran.R;

import java.util.ArrayList;

public class QuranList extends AppCompatActivity {
    ArrayList<Quran> quranlist;
    RecyclerView recyclerView;
    Quran_Adapter quran_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quran_list);


        int num = getIntent().getIntExtra("soranum", 1);

        quranlist = new ArrayList<>();
        switch (num) {
            case 1:

                quranlist.add(new Quran(R.drawable.s1));
                quranlist.add(new Quran(R.drawable.s2));
                quranlist.add(new Quran(R.drawable.s3));
                break;

            case 2:
                quranlist.add(new Quran(R.drawable.a1));
                quranlist.add(new Quran(R.drawable.a2));
                quranlist.add(new Quran(R.drawable.a3));
                quranlist.add(new Quran(R.drawable.a4));
                quranlist.add(new Quran(R.drawable.a5));
                quranlist.add(new Quran(R.drawable.a6));
                quranlist.add(new Quran(R.drawable.a7));
                quranlist.add(new Quran(R.drawable.a8));
                quranlist.add(new Quran(R.drawable.a9));
                quranlist.add(new Quran(R.drawable.a10));
                quranlist.add(new Quran(R.drawable.a11));
                quranlist.add(new Quran(R.drawable.a12));


                break;
        }


        recyclerView = findViewById(R.id.quranlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(QuranList.this, LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        quran_adapter = new Quran_Adapter(quranlist, QuranList.this);
        recyclerView.setAdapter(quran_adapter);
        recyclerView.setRotationY(180);
    }
}
