package com.appwalied.quran;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Qurandata extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
           WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_qurandata);
    }
    public void ooo10o (View vi){
        Intent intent=new Intent(Qurandata.this,sql0title098.class);
        intent.putExtra("id",vi.getId());
        startActivity(intent);
    }
}
