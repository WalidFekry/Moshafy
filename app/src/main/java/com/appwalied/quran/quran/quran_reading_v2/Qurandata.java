package com.appwalied.quran.quran.quran_reading_v2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;

import com.appwalied.quran.R;

public class Qurandata extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qurandata);
        AppCompatImageButton back = findViewById(R.id.back_button);
        back.setOnClickListener(v -> finish());
    }

    public void ooo10o(View vi) {
        Intent intent = new Intent(Qurandata.this, QuranReadingActivity.class);
        intent.putExtra("id", vi.getId());
        startActivity(intent);
    }
}
