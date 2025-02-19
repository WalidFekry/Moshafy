package com.appwalied.quran.islamicstory;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.sahaba.item_story;

import java.util.ArrayList;

public class Mainnewstory extends BaseActivity {
    RecyclerView recstory;
    ArrayList<item_story> listabra;
    private AppCompatImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainnewstory);
        recstory = findViewById(R.id.recnewstory);
        back = findViewById(R.id.back_button);
        back.setOnClickListener(v -> finish());
        recstory.setLayoutManager(new LinearLayoutManager(this));
        listabra = new ArrayList<>();
        DatabaseAsstes5 dp = DatabaseAsstes5.getInstance(this);
        dp.opean();
        listabra = dp.getalldata();
        dp.close();
        Adapternewstory adapter = new Adapternewstory(Mainnewstory.this, listabra);
        recstory.setAdapter(adapter);
    }
}
