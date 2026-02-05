package com.appwalied.quran.masbaha;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.utils.shared_helper.SharedPrefsConstants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MasbahaActivity extends BaseActivity {

    private ViewPager recycler_masbaha;
    private View view;
    private SharedPreferences sharedPreferences;

    private TextView tv_title, tv_desc;
    private FloatingActionButton view1, view2, view3, view4, view5;
    private FloatingActionButton iv_reset;
    private AppCompatImageButton back;
    private FrameLayout adsContainer;

    private int selection = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masbaha);

        checkAndShowDialog(
                SharedPrefsConstants.MASBAHA_FIRST_TIME,
                "يحتوي هذا القسم على المسبحة الإلكترونية تعمل بدون اتصال انترنت 💙"
        );

        sharedPreferences = getSharedPreferences("masbaha", MODE_PRIVATE);

        back = findViewById(R.id.back_button);
        recycler_masbaha = findViewById(R.id.recycler_masbaha);
        adsContainer = findViewById(R.id.adsContainer);

        tv_title = findViewById(R.id.tv_title);
        tv_desc = findViewById(R.id.tv_desc);

        view1 = findViewById(R.id.iv_1);
        view2 = findViewById(R.id.iv_2);
        view3 = findViewById(R.id.iv_3);
        view4 = findViewById(R.id.iv_4);
        view5 = findViewById(R.id.iv_5);
        iv_reset = findViewById(R.id.iv_reset);

        recycler_masbaha.setEnabled(false);

        iv_reset.setOnClickListener(v -> {
            sharedPreferences.edit()
                    .putInt("index" + selection, 0)
                    .apply();
            setAdapter(selection);
        });

        view1.setOnClickListener(v -> {
            selection = 1;
            setAdapter(1);
        });

        view2.setOnClickListener(v -> {
            selection = 2;
            setAdapter(2);
        });

        view3.setOnClickListener(v -> {
            selection = 3;
            setAdapter(3);
        });

        view4.setOnClickListener(v -> {
            selection = 4;
            setAdapter(4);
        });

        view5.setOnClickListener(v -> {
            selection = 5;
            setAdapter(5);
        });

        setAdapter(1);

        view = findViewById(R.id.view);
        view.setOnClickListener(v -> {
            int current = recycler_masbaha.getCurrentItem();

            if (selection == 5 && current < 999)
                recycler_masbaha.setCurrentItem(current + 1, true);
            else if (current < 99)
                recycler_masbaha.setCurrentItem(current + 1, true);
            else
                recycler_masbaha.setCurrentItem(0, true);

            sharedPreferences.edit()
                    .putInt("index" + selection, recycler_masbaha.getCurrentItem())
                    .apply();
        });

        back.setOnClickListener(v -> finish());

        showBanner(adsContainer);
    }

    private void setAdapter(final int type) {

        recycler_masbaha.setAdapter(
                new FragmentStatePagerAdapter(
                        getSupportFragmentManager(),
                        FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
                ) {
                    @Override
                    public Fragment getItem(int position) {
                        return PagerFragment.newInstance(position, type);
                    }

                    @Override
                    public int getCount() {
                        return type == 5 ? 1000 : 100;
                    }
                }
        );

        recycler_masbaha.setCurrentItem(
                sharedPreferences.getInt("index" + type, 0),
                false
        );

        recycler_masbaha.setOffscreenPageLimit(0);

        switch (type) {
            case 1:
                tv_title.setText("لا إله إلا الله");
                tv_desc.setText("فضلها : عن النبي صلى الله عليه وسلم أنه قال : من قال لا إله إلا الله صدقًا من قلبه دخل الجنة");
                break;

            case 2:
                tv_title.setText("أستغفر الله العظيم وأتوب إليه");
                tv_desc.setText("فضلها : قال رسول الله ﷺ: يا أيها الناس توبوا إلى الله، فإني أتوب في اليوم إليه مائة مرة.");
                break;

            case 3:
                tv_title.setText("سُبْحَانَ اللهِ، وَالْحْمْدُ لِلهِ، وَلَا إِلٰهَ إِلَّا اللهُ، وَاللهُ أَكْبَرُ");
                tv_desc.setText("فضلها : تُغْرَسُ لَكَ بِكُلِّ وَاحِدَةٍ شَجَرَةٌ فِي الْجَنَّةِ");
                break;

            case 4:
                tv_title.setText("اللهم صلِّ على سيدنا محمد");
                tv_desc.setText("فضلها : من صلى عليَّ حين يصبح عشرًا وحين يمسي عشرًا أدركته شفاعتي يوم القيامة");
                break;

            case 5:
                tv_title.setText("سبحان الله وبحمده");
                tv_desc.setText("فضلها : من قالها مائة مرة حُطَّتْ خطاياه وإن كانت مثل زبد البحر");
                break;
        }
    }
}
