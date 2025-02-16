package com.appwalied.quran.quran.quran_reading;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.appwalied.quran.R;
import com.appwalied.quran.quran.quran_reading.fragment.SurahFragment;
import com.appwalied.quran.utils.shared_helper.SharedHelper;
import com.appwalied.quran.utils.shared_helper.SharedPrefsConstants;
import com.appwalied.quran.utils.shared_helper.views.CustomDialogClass;

public class QuranRead extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran_read);

        if (!SharedHelper.getBoolean(this, SharedPrefsConstants.QURAN_READING_FIRST_TIME)) {
            SharedHelper.putBoolean(this, SharedPrefsConstants.QURAN_READING_FIRST_TIME, true);
            CustomDialogClass.Options options = new CustomDialogClass.Options();
            options.title = "ملاحظة هامة";
            options.message = "في هذا القسم، يمكنك قراءة القرآن الكريم بسهولة مع خيارات مريحة تساعدك على التدبر والتلاوة 📖";            CustomDialogClass customDialogClass = new CustomDialogClass(this, options);
            customDialogClass.show();
        }

        FragmentManager fragmentManager;
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fragment, SurahFragment.newInstance()).commit();
    }
}
