package com.appwalied.quran.quran.quran_reading_v2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;

public class Qurandata extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qurandata);
        AppCompatImageButton back = findViewById(R.id.back_button);
        back.setOnClickListener(v -> finish());

        promptUserForRating();
    }

    public void ooo10o(View vi) {
        Intent intent = new Intent(Qurandata.this, QuranReadingActivity.class);
        intent.putExtra("id", vi.getId());
        startActivity(intent);
    }
}
