package com.appwalied.quran.ahades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;

import java.util.ArrayList;

public class ActivityAhdes extends BaseActivity {
    RecyclerView recahdes;
    ArrayList<item_ahdes> listahdes;
    private static int SPLASH_TIME_OUT = 5000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ahdes);



        recahdes = (RecyclerView) findViewById(R.id.recahdes);
        recahdes.setLayoutManager(new LinearLayoutManager(this));
        listahdes = new ArrayList<>();
        DatabaseAcsessahdes dp = DatabaseAcsessahdes.getInstance(this);
        dp.opean();
        listahdes = dp.getalldata();
        dp.close();
        AdapterRecyclerahdes adapter = new AdapterRecyclerahdes(ActivityAhdes.this, listahdes);
        recahdes.setAdapter(adapter);

    }

    public void onBackClicked(View view) {
        onBackPressed();
    }


}