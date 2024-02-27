package com.appwalied.quran.sahaba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;

import java.util.ArrayList;

public class MainStory extends BaseActivity {
    RecyclerView recstory;
    ArrayList<item_story> listabra;
    //   private InterstitialAd mInterstitialAd3;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_story);



        recstory = (RecyclerView) findViewById(R.id.recstory);
        recstory.setLayoutManager(new LinearLayoutManager(this));
        listabra = new ArrayList<>();
        DatabaseAsstes4 dp = DatabaseAsstes4.getInstance(this);
        dp.opean();
        listabra = dp.getalldata();
        dp.close();
        Adapterstory adapter = new Adapterstory(MainStory.this, listabra);
        recstory.setAdapter(adapter);


    }


    public void onBackClicked(View view) {
        onBackPressed();
    }
}
