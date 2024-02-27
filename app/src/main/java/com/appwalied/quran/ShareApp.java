package com.appwalied.quran;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.appwalied.quran.base.BaseActivity;

public class ShareApp extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_app);
    }
    public  void obkla(View view){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "\uD83D\uDE0Dصدقه جاريه\uD83D\uDC9A");
        sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.about1) + "\n" + "لتحميل التطبيق انقر هنا \uD83D\uDC47\uD83D\uDC47 :\n" +
                "\n" +
                "https://t.co/fkNQTMLNxn");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void onBackClicked(View view) {
        onBackPressed();
    }


}