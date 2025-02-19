package com.appwalied.quran.sahaba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import net.steamcrafted.materialiconlib.MaterialIconView;

public class Mainstorytitle extends BaseActivity {
    TextView titlestory, header;
    String title, headert;
    int c = 18;
    MaterialIconView min, plus;
    CardView cardheader;
    private AppCompatImageButton back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainstorytitle);
        back = findViewById(R.id.back_button);
        titlestory = findViewById(R.id.titlestory);
        header = findViewById(R.id.header);
        cardheader = findViewById(R.id.cardheader);
        back.setOnClickListener(v -> finish());
        Intent intent = getIntent();
        title = intent.getStringExtra("storytitle");
        headert = intent.getStringExtra("headert");
        titlestory.setText(title);
        header.setText(headert);
        min = findViewById(R.id.min);
        plus = findViewById(R.id.plus);
        min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c > 13) {
                    c--;
                } else
                    Toast.makeText(Mainstorytitle.this, "لايمكن تصغير الخط اصغر من ذلك", Toast.LENGTH_SHORT).show();
                titlestory.setTextSize(c);
                header.setTextSize(c);
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c < 50) {
                    c++;
                } else
                    Toast.makeText(Mainstorytitle.this, "لايمكن تكبير الخط اكبر من ذلك", Toast.LENGTH_SHORT).show();
                titlestory.setTextSize(c);
                header.setTextSize(c);


            }
        });
        setUpAds();
    }

    public void copy(View view) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("", headert + "\n" + title + "\n" + "https://bit.ly/3jMaYsR\n");
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "تم نسخ النصوص", Toast.LENGTH_SHORT).show();


    }

    public void share(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, " تطبيق مصحفي");
        sendIntent.putExtra(Intent.EXTRA_TEXT, headert + "\n" + title + "مشاركه نصوص من تطبيق مصحفي  \n" + "\n" + "\n" + "https://bit.ly/3jMaYsR\n");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "مشاركه تطبيق مصحفي:"));
        Toast.makeText(this, "جزاك الله خيرا لمشاركه التطبيق", Toast.LENGTH_SHORT).show();
    }

    public void onBackClicked(View view) {
        onBackPressed();
    }

    private void setUpAds() {

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = findViewById(R.id.M3);

        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);

    }

}
