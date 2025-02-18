package com.appwalied.quran.monw3at;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.appwalied.quran.R;
import com.appwalied.quran.roqia.RoqiaNoIternet;

public class Diffrentis extends AppCompatActivity {
    private AppCompatImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diffrentis);
        back = findViewById(R.id.back_button);
        back.setOnClickListener(v -> finish());
    }

    public void Title1(View view) {
        startActivity(new Intent(Diffrentis.this, Title1.class));
    }

    public void Title3(View view) {
        Intent i = new Intent(Diffrentis.this, MainAhdes.class);
        startActivity(i);
    }

    public void Title4(View view) {
        startActivity(new Intent(Diffrentis.this, Mainwsaya.class));
    }

    public void Title5(View view) {
        startActivity(new Intent(Diffrentis.this, Mainhagandomra.class));
    }


    public void Titlea(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.co/IuFGow3uyK")));

    }

    public void Title(View view) {
        startActivity(new Intent(Diffrentis.this, RoqiaNoIternet.class));
    }
}
