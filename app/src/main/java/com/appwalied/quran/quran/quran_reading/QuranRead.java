package com.appwalied.quran.quran.quran_reading;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.quran.quran_reading.fragment.SurahFragment;
import com.appwalied.quran.utils.shared_helper.SharedHelper;
import com.appwalied.quran.utils.shared_helper.SharedPrefsConstants;
import com.appwalied.quran.utils.shared_helper.views.CustomDialogClass;

public class QuranRead extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran_read);

        checkAndShowDialog(SharedPrefsConstants.QURAN_READING_FIRST_TIME,"في هذا القسم، يمكنك قراءة القرآن الكريم بسهولة مع خيارات مريحة تساعدك على التدبر والتلاوة 📖");
        FragmentManager fragmentManager;
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fragment, SurahFragment.newInstance()).commit();

        promptUserForRating();
    }
}
