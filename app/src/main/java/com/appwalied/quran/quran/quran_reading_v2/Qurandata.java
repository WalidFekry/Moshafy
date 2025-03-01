package com.appwalied.quran.quran.quran_reading_v2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.utils.shared_helper.SharedPrefsConstants;

public class Qurandata extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qurandata);
        AppCompatImageButton back = findViewById(R.id.back_button);
        back.setOnClickListener(v -> finish());

        checkAndShowDialog(
                SharedPrefsConstants.QURAN_READING_V2_FIRST_TIME,
                "في هذا القسم، يمكنك قراءة القرآن الكريم بواجهة مريحة، مع ميزات تسهل عليك التلاوة والتدبر 📖"
        );

        promptUserForRating();
    }

    public void ooo10o(View vi) {
        Intent intent = new Intent(Qurandata.this, QuranReadingActivity.class);
        intent.putExtra("id", vi.getId());
        startActivity(intent);
    }
}
