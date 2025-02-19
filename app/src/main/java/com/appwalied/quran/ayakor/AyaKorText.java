package com.appwalied.quran.ayakor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.appwalied.quran.R;

public class AyaKorText extends AppCompatActivity {

    TextView ayakortxt;
    Button btnup, btndown, btnstar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayakortext);

        btnup = findViewById(R.id.btn_up);
        btndown = findViewById(R.id.btn_down);
        btnstar = findViewById(R.id.btn_star);
        ayakortxt = findViewById(R.id.ayakortxt);


        btnup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ayakortxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, (ayakortxt.getTextSize() + 2f));
            }
        });

        btndown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ayakortxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, (ayakortxt.getTextSize() - 2f));

            }
        });

        btnstar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.co/fkNQTMLNxn")));

            }
        });

    }
}
