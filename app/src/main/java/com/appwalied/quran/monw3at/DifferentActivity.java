package com.appwalied.quran.monw3at;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageButton;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.monw3at.roqia.RoqiaNoIternet;
import com.appwalied.quran.utils.shared_helper.SharedPrefsConstants;

public class DifferentActivity extends BaseActivity {
    private AppCompatImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diffrentis);
        back = findViewById(R.id.back_button);
        back.setOnClickListener(v -> finish());
        checkAndShowDialog(SharedPrefsConstants.MONW3AT_FIRST_TIME, "في قسم منوعات إسلامية، ستجد الأذكار، الرقية الشرعية، الأحاديث، الوصايا، سنن الرسول ﷺ، وأحكام الحج والعمرة 💙");
    }

    public void Title1(View view) {
        startActivity(new Intent(DifferentActivity.this, Title1.class));
    }

    public void Title3(View view) {
        Intent i = new Intent(DifferentActivity.this, MainAhdes.class);
        startActivity(i);
    }

    public void Title4(View view) {
        startActivity(new Intent(DifferentActivity.this, Mainwsaya.class));
    }

    public void Title5(View view) {
        startActivity(new Intent(DifferentActivity.this, Mainhagandomra.class));
    }


    public void Titlea(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.co/IuFGow3uyK")));

    }

    public void Title(View view) {
        startActivity(new Intent(DifferentActivity.this, RoqiaNoIternet.class));
    }
}
