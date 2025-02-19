package com.appwalied.quran.sahaba;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;

import java.util.ArrayList;

public class MainStory extends BaseActivity {
    RecyclerView recstory;
    ArrayList<item_story> listabra;

    private AppCompatImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_story);
        recstory = findViewById(R.id.recstory);
        back = findViewById(R.id.back_button);
        back.setOnClickListener(v -> finish());
        recstory.setLayoutManager(new LinearLayoutManager(this));
        listabra = new ArrayList<>();
        DatabaseAsstes4 dp = DatabaseAsstes4.getInstance(this);
        dp.opean();
        listabra = dp.getalldata();
        dp.close();
        Adapterstory adapter = new Adapterstory(MainStory.this, listabra);
        recstory.setAdapter(adapter);

        promptUserForRating();
    }

}
