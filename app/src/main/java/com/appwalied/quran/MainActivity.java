package com.appwalied.quran;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.appwalied.quran.ahades.ActivityAhdes;
import com.appwalied.quran.ayakor.Ayakor;
import com.appwalied.quran.azkar.Azkar;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityMainBinding;
import com.appwalied.quran.islamicstory.Mainnewstory;
import com.appwalied.quran.masbaha.MasbahaActivity;
import com.appwalied.quran.monw3at.Diffrentis;
import com.appwalied.quran.notifications_messages.NotificationsMessagesActivity;
import com.appwalied.quran.quran.qouran_learning.QouranLearningActivity;
import com.appwalied.quran.quran.quran_listening.RecitesName;
import com.appwalied.quran.quranread.Quran_list;
import com.appwalied.quran.quran.quran_reading.QuranRead;
import com.appwalied.quran.sahaba.MainStory;
import com.appwalied.quran.sonan.MainAyaandabra;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.joda.time.DateTime;
import org.joda.time.chrono.IslamicChronology;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import softpro.naseemali.ShapedNavigationView;
import softpro.naseemali.ShapedViewSettings;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final String TAG = "TAG";
    TextView dd;
    ShimmerTextView shmer;
    Shimmer shimmer;
    String[] permission = new String[]{
            Manifest.permission.POST_NOTIFICATIONS
    };
    boolean isPermission = false;
    private final ActivityResultLauncher<String> requestPermissionLauncherNotification =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    isPermission = true;
                } else {
                    isPermission = false;
                    showPermissionDialog();
                }
            });

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setOnClickListeners();

        dd = findViewById(R.id.bb);
        shmer = findViewById(R.id.shmer);
        shimmer = new Shimmer();
        shimmer.start(shmer);
        DateTime dm = new DateTime();
        DateTime dh = dm.withChronology(IslamicChronology.getInstance());
        String stMonth = dh.toString("MM");
        if (stMonth.contains("10")) {
            String st = dh.plusDays(1).toString("d/ " + "من شهر شوال" + "/yyyy" + "من الهجري");
            dd.setText(st);
        } else if (stMonth.contains("01")) {
            String st = dh.plusDays(0).toString("d/ " + "من شهر محرم" + "/yyyy" + "من الهجري");
            dd.setText(st);
        } else if (stMonth.contains("02")) {
            String st = dh.plusDays(1).toString("d/ " + "من شهر صفر" + "/yyyy" + "من الهجري");
            dd.setText(st);
        } else if (stMonth.contains("03")) {
            String st = dh.plusDays(1).toString("d/ " + "من شهر ربيع الاول" + "/yyyy" + "من الهجري");
            dd.setText(st);
        } else if (stMonth.contains("04")) {
            String st = dh.plusDays(1).toString("d/ " + "من شهر ربيع الثاني" + "/yyyy" + "من الهجري");
            dd.setText(st);
        } else if (stMonth.contains("05")) {
            String st = dh.plusDays(1).toString("d/ " + "من شهر جمادى الاول" + "/yyyy" + "من الهجري");
            dd.setText(st);
        } else if (stMonth.contains("06")) {
            String st = dh.plusDays(0).toString("d/ " + "من شهر جمادى الثاني" + "/yyyy" + "من الهجري");
            dd.setText(st);
        } else if (stMonth.contains("07")) {
            String st = dh.plusDays(1).toString("d/ " + "من شهر رجب" + "/yyyy" + "من الهجري");
            dd.setText(st);
        } else if (stMonth.contains("08")) {
            String st = dh.plusDays(0).toString("d/ " + "من شهر شعبان" + "/yyyy" + "من الهجري");
            dd.setText(st);
        } else if (stMonth.contains("09")) {
            String st = dh.plusDays(0).toString("d/ " + "من شهر رمضان" + "/yyyy" + "من الهجري");
            dd.setText(st);
        } else if (stMonth.contains("11")) {
            String st = dh.plusDays(0).toString("d/ " + "من شهرذو القعدة" + "/yyyy" + "من الهجري");
            dd.setText(st);
        } else {
            String st = dh.plusDays(0).toString("d/" + "من شهر ذو الحجة" + "/yyyy" + "من الهجري");
            dd.setText(st);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ShapedNavigationView shapedNavigationView = findViewById(R.id.nav_view);
        shapedNavigationView.getSettings().setShapeType(ShapedViewSettings.ARC_CONCAVE);
        shapedNavigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        FirebaseMessaging.getInstance().subscribeToTopic("all");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            cheakNotificationPermission();
        }
    }

    private void setOnClickListeners() {
        binding.main.contentMain.notificationsMessages.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, NotificationsMessagesActivity.class));
        });
        binding.main.contentMain.quranLearning.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, QouranLearningActivity.class));
        });
        binding.main.contentMain.quranReading.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, QuranRead.class));
        });
        binding.main.contentMain.masbaha.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MasbahaActivity.class));
        });
    }

    private void cheakNotificationPermission() {
        if (!isPermission) {
            requestPermissionsNotfication();
        } else {
//            Toast.makeText(this, "Granted done", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestPermissionsNotfication() {
        if (ContextCompat.checkSelfPermission(this, permission[0]) == PackageManager.PERMISSION_GRANTED) {
            isPermission = true;
        } else {
            requestPermissionLauncherNotification.launch(permission[0]);
        }
    }

    private void showPermissionDialog() {
        new AlertDialog.Builder(this)
                .setMessage("من فضلك قم بالموافقة على الاشعارات لكي تصلك رسائل التفاؤل والإقتباسات من التطبيق ..")
                .setPositiveButton("الاعدادات", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        final PrettyDialog pDialog = new PrettyDialog(this);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setTitle("هل تريد الخروج من التطبيق !")
                .setMessage("كيف نطوره ليصبح افضل.. و لا تنسى تقييم التطبيق").
                setIcon(R.mipmap.ico_app)
                .setAnimationEnabled(true)
                .setMessageColor(R.color.colorPrimary)
                .setTitleColor(R.color.colorAccent)
                .setIconTint(R.color.white)
                .setGravity(Gravity.DISPLAY_CLIP_HORIZONTAL)
                .setTypeface(Typeface.createFromAsset(getAssets(), "flat5.otf"))
                .addButton(
                        "خروج",
                        R.color.white,
                        R.color.colorPrimary,
                        new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                finish();
                            }
                        }
                )

                .addButton(
                        "تقييم التطبيق",
                        R.color.white,
                        R.color.colorPrimary,
                        new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.co/fkNQTMLNxn")));
                            }
                        }

                )
                .show();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.site) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=201094674881&text=%D8%A7%D8%B3%D8%AA%D9%81%D8%B3%D8%A7%D8%B1%20%D8%A8%D8%AE%D8%B5%D9%88%D8%B5%20%D8%AA%D8%B7%D8%A8%D9%8A%D9%82%20%D9%85%D9%8F%D8%B5%D8%AD%D9%81%D9%8A%20..&source=&data=")));
        } else if (id == R.id.facepage) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/App.Maktbti")));

        } else if (id == R.id.facegroup) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/groups/440403217380641")));
        } else if (id == R.id.telee) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/App_Maktbti")));

        } else if (id == R.id.appshare2) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/sharer.php?u=https%3A%2F%2Ft.co%2FFp1vYCCiQv%3Famp")));

        } else if (id == R.id.apprate) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.co/fkNQTMLNxn")));
        } else if (id == R.id.appshare) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, " تطبيق مصحفي ");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "\n" +
                    "قمنا بتصميم البرنامج ليكون بسيط و مجاني ليضم القران الكريم كامل بدون إنترنت و أذكار الصباح و المساء مكتوبة  ليساعدك على أن لا تنسى ذكر الله ابداً .\n \n" +
                    "تفضل رابط تطبيق مصحفي  https://t.co/fkNQTMLNxn \n");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        } else if (id == R.id.policy) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/appquran/home?fbclid=IwAR16BN6g94eIYrAgVjkBXqb0dBYAJSk0Mq4uo7mtJe5g7bOzxdm9Ax2kCsI")));

        } else if (id == R.id.appabout) {
            startActivity(new Intent(getApplicationContext(), About.class));
            //  startActivity(new Intent(MainActivity.this,About.class));

        } else if (id == R.id.moreapp) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=6257553101128037563&fbclid=IwAR3MAupHsRMlNy-eSNy44UNoTTGq0U5lHIOdlxuHTR-sCxJAfuAUIGn2Veo")));

        } else if (id == R.id.appsug) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://walid-fekry.com/maktbti-plus/")));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void intentoo(View view) {
        Intent i = new Intent(this, Qurandata.class);
        startActivity(i);
    }

    public void quranicmsg(View view) {
        Intent qumsg = new Intent(MainActivity.this, QuranicMessage.class);
        startActivity(qumsg);
    }

    public void intentayakor(View view) {
        Intent intent = new Intent(MainActivity.this, Ayakor.class);
        startActivity(intent);
    }

    public void intentaz(View v) {
        startActivity(new Intent(MainActivity.this, Azkar.class));
    }


    public void intentdiff(View v) {
        startActivity(new Intent(MainActivity.this, Diffrentis.class));
    }

    public void quran_fnish(View v) {
        startActivity(new Intent(MainActivity.this, Quran_fnish.class));
    }

    public void oos1(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.co/fkNQTMLNxn")));
    }

    public void ooh1(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, " تطبيق مصحفي ");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "\n" +
                "قمنا بتصميم البرنامج ليكون بسيط و مجاني ليضم القران الكريم كامل بدون إنترنت و أذكار الصباح و المساء مكتوبة  ليساعدك على أن لا تنسى ذكر الله ابداً .\n \n" +
                "تفضل رابط تطبيق مصحفي  https://t.co/fkNQTMLNxn \n");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void about(View view) {
        startActivity(new Intent(MainActivity.this, About.class));
    }


    public void soundmp3Quran(View view) {
        startActivity(new Intent(getApplicationContext(), RecitesName.class));
    }

    public void quranonclick(View view) {
        int qurannum;
        switch (view.getId()) {
            case R.id.molk:
                qurannum = 1;
                Intent intent = new Intent(getApplicationContext(), Quran_list.class);
                intent.putExtra("soranum", qurannum);
                startActivity(intent);
                break;

            case R.id.khfa:
                qurannum = 2;
                Intent intent1 = new Intent(getApplicationContext(), Quran_list.class);
                intent1.putExtra("soranum", qurannum);
                startActivity(intent1);
                break;

        }
    }



    public void moshafmolem(View view) {

        if (isNetworkConnected()) {
            startActivity(new Intent(this, QouranLearningActivity.class));
        } else {
            Toast.makeText(this, "من فضلك تأكد من اتصالك بالإنترنت لتشغيل المصحف المعلم 🌸", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public void intentislamicstory(View view) {

        Intent intent = new Intent(MainActivity.this, Mainnewstory.class);
        startActivity(intent);
    }

    public void intentsahaba(View view) {
        Intent intent = new Intent(MainActivity.this, MainStory.class);
        startActivity(intent);
    }

    public void intentahades(View view) {
        Intent intent = new Intent(MainActivity.this, ActivityAhdes.class);
        startActivity(intent);
    }

    public void intentsonan(View view) {
        Intent intent = new Intent(MainActivity.this, MainAyaandabra.class);
        startActivity(intent);
    }

    public void shareapp(View view) {
        startActivity(new Intent(MainActivity.this, ShareApp.class));
    }
}
