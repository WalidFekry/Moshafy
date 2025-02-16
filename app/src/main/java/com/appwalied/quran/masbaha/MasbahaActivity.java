package com.appwalied.quran.masbaha;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.utils.shared_helper.SharedHelper;
import com.appwalied.quran.utils.shared_helper.SharedPrefsConstants;
import com.appwalied.quran.utils.shared_helper.views.CustomDialogClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MasbahaActivity extends BaseActivity {

    ViewPager recycler_masbaha;
    View view;
    SharedPreferences sharedPreferences;
    TextView tv_title, tv_desc;
    FloatingActionButton view1, view2, view3, view4, view5;
    FloatingActionButton iv_reset;
    AppCompatImageButton back;
    private int selection = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masbaha);

        if (!SharedHelper.getBoolean(this, SharedPrefsConstants.MASBAHA_FIRST_TIME)) {
            SharedHelper.putBoolean(this, SharedPrefsConstants.MASBAHA_FIRST_TIME, true);
            CustomDialogClass.Options options = new CustomDialogClass.Options();
            options.title = "ملاحظة هامة ";
            options.message = "يحتوي هذا القسم على المسبحة الإلكترونية تعمل بدون اتصال انترنت 💙";
            CustomDialogClass customDialogClass = new CustomDialogClass(this, options);
            customDialogClass.show();
        }

        sharedPreferences = getSharedPreferences("masbaha", MODE_PRIVATE);
        back = findViewById(R.id.back_button);
        recycler_masbaha = findViewById(R.id.recycler_masbaha);
        recycler_masbaha.setEnabled(false);
        tv_title = findViewById(R.id.tv_title);
        tv_desc = findViewById(R.id.tv_desc);
        view1 = findViewById(R.id.iv_1);
        view2 = findViewById(R.id.iv_2);
        view3 = findViewById(R.id.iv_3);
        view4 = findViewById(R.id.iv_4);
        view5 = findViewById(R.id.iv_5);
        iv_reset = findViewById(R.id.iv_reset);
        iv_reset.setOnClickListener(v -> {
            sharedPreferences.edit().putInt("index" + selection, 0).apply();
            setadapter(selection);

        });
        view1.setOnClickListener(v -> {
            selection = 1;
            setadapter(1);
        });
        view2.setOnClickListener(v -> {
            selection = 2;
            setadapter(2);
        });
        view3.setOnClickListener(v -> {
            selection = 3;
            setadapter(3);
        });
        view4.setOnClickListener(v -> {
            selection = 4;
            setadapter(4);
        });
        view5.setOnClickListener(v -> {
            selection = 5;
            setadapter(5);
        });
        setadapter(1);

        view = findViewById(R.id.view);
        view.setOnClickListener(v -> {
            if (selection == 5 && recycler_masbaha.getCurrentItem() < 999)
                recycler_masbaha.setCurrentItem(recycler_masbaha.getCurrentItem() + 1, true);
            else if (recycler_masbaha.getCurrentItem() < 99)
                recycler_masbaha.setCurrentItem(recycler_masbaha.getCurrentItem() + 1, true);

            else recycler_masbaha.setCurrentItem(0, true);
            sharedPreferences.edit().putInt("index" + selection, recycler_masbaha.getCurrentItem()).apply();
        });
      back.setOnClickListener(v -> finish());
    }

    private void setadapter(final int i) {
        recycler_masbaha.setAdapter(null);
        recycler_masbaha.invalidate();
        recycler_masbaha.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new com.appwalied.quran.masbaha.PagerFragment(position, i);
            }

            @Override
            public int getCount() {
                if (i == 5) return 1000;
                return 100;
            }
        });
        recycler_masbaha.setCurrentItem(sharedPreferences.getInt("index" + i, 0), false);

        recycler_masbaha.setOffscreenPageLimit(0);
        recycler_masbaha.invalidate();
        if (i == 1) {
            tv_title.setText("لا إله إلا الله");
            tv_desc.setText("فضلها : عن النبي صلى الله عليه وسلم أنه قال : من قال لا إله إلا الله صدقًا من قلبه دخل الجنة");
        } else if (i == 2) {
            tv_title.setText("أستغفر الله العظيم وأتوب إليه");
            tv_desc.setText("فضلها : قال رسول الله ﷺ: يا أيها الناس توبوا إلى الله، فإني أتوب في اليوم إليه مائة مرة.");
        } else if (i == 3) {
            tv_title.setText("سُبْحَانَ اللهِ، وَالْحْمدُ لِلهِ، وَلَا إِلهَ إِلَّا اللهُ، وَاللهُ أَكْبَرُ");
            tv_desc.setText("فضلها : قال رسول الله صلى الله عليه وسلم : سُبْحَانَ اللهِ، وَالْحْمدُ لِلهِ، وَلَا إِلهَ إِلَّا اللهُ، وَاللهُ أَكْبَرُ؛ تُغْرَسُ لَكَ بِكُلِّ وَاحِدَةٍ شَجَرَةٌ فِي الْجَنَّةِ");
        } else if (i == 4) {
            tv_title.setText("اللهم صلِّ على سيدنا محمد ");
            tv_desc.setText("فضلها : قال رسول الله صلى الله عليه وسلم : (من صلى عليَّ حين يصبح عشرًا ، وحين يمسي عشرًا ، أدركته شفاعتي يوم القيامة)");
        } else if (i == 5) {
            tv_title.setText("سبحان الله وبحمده");
            tv_desc.setText("فضلها : قال رسول الله صلى الله عليه وسلم (مَنْ قَالَ سُبْحَانَ اللَّهِ وَبِحَمْدِهِ فِي يَوْمٍ مِائَةَ مَرَّةٍ حُطَّتْ خَطَايَاهُ وَإِنْ كَانَتْ مِثْلَ زَبَدِ الْبَحْرِ)");
        }
    }
}
