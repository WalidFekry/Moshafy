package com.appwalied.quran.azkar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.utils.shared_helper.SharedHelper;
import com.appwalied.quran.utils.shared_helper.views.CustomDialogClass;
import com.appwalied.quran.utils.shared_helper.SharedPrefsConstants;

import guy4444.smartrate.SmartRate;

public class Azkar extends BaseActivity {
    private AppCompatImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azkar);
        back = findViewById(R.id.back_button);
        back.setOnClickListener(v -> finish());

        checkAndShowDialog(
                SharedPrefsConstants.AZKAR_FIRST_TIME,
                "يحتوي هذا القسم على أذكار المسلم كاملة بدون انترنت ^_^"
        );

        promptUserForRating();
    }

    public void move1(View view) {
        Intent i = new Intent(Azkar.this, Azcartitle1.class);
        startActivity(i);
    }
    public void move2(View view) {
        startActivity(new Intent(Azkar.this, Azcartitle2.class));
    }

    public void move3(View v) {
        startActivity(new Intent(Azkar.this, Azcartitle3.class));
    }

    public void move4(View view) {
        startActivity(new Intent(Azkar.this, Azcartitle4.class));
    }

    public void move5(View view) {
        startActivity(new Intent(Azkar.this, Azcartitle5.class));
    }

    public void move6(View view) {
        startActivity(new Intent(Azkar.this, Azcartitle6.class));
    }


}

