package com.appwalied.quran;

import android.Manifest;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.appwalied.quran.quran.ui.QuranActivity;
import com.appwalied.quran.quran.ui.QuranReadingActivity;
import com.google.android.material.button.MaterialButton;
import com.appwalied.quran.utils.HijriCalendarHelper;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
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
import com.appwalied.quran.quran.QuranicMessage;
import com.appwalied.quran.quran.quran_images.QuranList;
import com.appwalied.quran.quran.quran_listening.RecitesName;
import com.appwalied.quran.sahaba.MainStory;
import com.appwalied.quran.sonan.MainAyaandabra;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.romainpiel.shimmer.Shimmer;
import libs.mjn.prettydialog.PrettyDialog;
import softpro.naseemali.ShapedNavigationView;
import softpro.naseemali.ShapedViewSettings;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1;
    private ActivityMainBinding binding;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.theme_mode) {
            showThemeChooserDialog();
            return true;
        } else if (id == R.id.show_notifications) {
            Intent i = new Intent(this, NotificationsMessagesActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setOnClickListeners();
        setupToolbar();
        setupDrawer();
        setupNavigationView();

        setupShimmer();
        updateHijriDateDisplay();

        setupBackDispatcher();

        requestNotificationPermission();
        FirebaseMessaging.getInstance().subscribeToTopic("all");
    }

    private void setupBackDispatcher() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                    return;
                }

                final PrettyDialog pDialog = new PrettyDialog(MainActivity.this);
                pDialog.setCanceledOnTouchOutside(false);
                pDialog.setTitle("هل تريد الخروج من التطبيق ؟")
                        .setMessage("كيف نطوره ليصبح أفضل؟ ولا تنسَ تقييم التطبيق 🌸")
                        .setIcon(R.mipmap.ico_app)
                        .setAnimationEnabled(true)
                        .setMessageColor(R.color.colorPrimary)
                        .setTitleColor(R.color.colorAccent)
                        .setIconTint(R.color.white)
                        .setGravity(Gravity.DISPLAY_CLIP_HORIZONTAL)
                        .setTypeface(Typeface.createFromAsset(getAssets(), "flat5.otf"))
                        .addButton("خروج", R.color.white, R.color.colorPrimary, MainActivity.this::finish)
                        .addButton("تقييم التطبيق", R.color.white, R.color.colorAccent, () -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.co/fkNQTMLNxn"))))
                        .show();
            }
        });
    }

    private void setupShimmer() {
        Shimmer shimmer = new Shimmer();
        shimmer.start(binding.main.contentMain.shmer);
    }


    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "رائع! 🎉 الآن ستتلقى اقتباسات ورسائل تحفيزية يوميًا! 😊", Toast.LENGTH_LONG).show();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
                    showSettingsDialog();
                } else {
                    Toast.makeText(this, "😔 بدون الإشعارات، قد تفوتك رسائل تحفيزية واقتباسات تمنحك الطاقة! يمكنك تفعيلها لاحقًا من الإعدادات. ⚡", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("تفعيل الإشعارات 📢");
        builder.setMessage("لكي تصلك اقتباسات يومية ورسائل تحفيزية، تحتاج إلى تفعيل الإشعارات من الإعدادات.");

        builder.setPositiveButton("فتح الإعدادات", (dialog, which) -> {
            dialog.dismiss();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        });

        builder.setNegativeButton("لاحقًا", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupNavigationView() {
        ShapedNavigationView shapedNavigationView = findViewById(R.id.nav_view);
        shapedNavigationView.getSettings().setShapeType(ShapedViewSettings.ARC_CONCAVE);
        shapedNavigationView.setNavigationItemSelectedListener(this);
    }

    private void setOnClickListeners() {
        binding.main.contentMain.cardHijriDate.setOnClickListener(v -> showHijriAdjustmentDialog());
        binding.main.contentMain.quranReading.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, QuranActivity.class)));
        binding.main.contentMain.notificationsMessages.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, NotificationsMessagesActivity.class)));
        binding.main.contentMain.masbaha.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MasbahaActivity.class)));
        binding.main.contentMain.quranListening.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RecitesName.class));
        });
        binding.main.contentMain.azkar.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Azkar.class)));
        binding.main.contentMain.islamicSections.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Diffrentis.class)));
        binding.main.contentMain.quranMessages.setOnClickListener(v -> {
            if (isNetworkConnected()) {
                startActivity(new Intent(MainActivity.this, QuranicMessage.class));
            } else {
                Toast.makeText(this, "من فضلك تأكد من اتصالك بالإنترنت لفتح الرسائل القرآنية 🌸", Toast.LENGTH_SHORT).show();
            }
        });
        binding.main.contentMain.islamicStories.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Mainnewstory.class)));
        binding.main.contentMain.sahabaStories.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MainStory.class)));
        binding.main.contentMain.shortAhadiths.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ActivityAhdes.class)));
        binding.main.contentMain.sunnah.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MainAyaandabra.class)));
        binding.main.contentMain.khfa.setOnClickListener(v -> {
            Intent intent = new Intent(this, QuranList.class);
            intent.putExtra("soranum", 2);
            startActivity(intent);
        });
        binding.main.contentMain.molk.setOnClickListener(v -> {
            Intent intent = new Intent(this, QuranList.class);
            intent.putExtra("soranum", 1);
            startActivity(intent);
        });
        binding.main.contentMain.ayakor.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Ayakor.class)));
        binding.main.contentMain.quranFnish.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Quran_fnish.class)));
        binding.main.contentMain.shareApp.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ShareApp.class)));
        binding.main.contentMain.about.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, About.class)));
        binding.main.contentMain.rateApp.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.co/fkNQTMLNxn"))));
        binding.main.contentMain.cardAlmuhfzPromo.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.walid.almuhfz"))));
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                return capabilities != null && (
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                );
            } else {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                return activeNetwork != null && activeNetwork.isConnected();
            }
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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
            sendIntent.putExtra(Intent.EXTRA_TEXT, "\nقمنا بتصميم البرنامج ليكون بسيط و مجاني ليضم القران الكريم كامل بدون إنترنت و أذكار الصباح و المساء مكتوبة  ليساعدك على أن لا تنسى ذكر الله ابداً .\n \nتفضل رابط تطبيق مصحفي  https://t.co/fkNQTMLNxn \n");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (id == R.id.policy) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/appquran/home?fbclid=IwAR16BN6g94eIYrAgVjkBXqb0dBYAJSk0Mq4uo7mtJe5g7bOzxdm9Ax2kCsI")));
        } else if (id == R.id.appabout) {
            startActivity(new Intent(getApplicationContext(), About.class));
        } else if (id == R.id.moreapp) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=6257553101128037563&fbclid=IwAR3MAupHsRMlNy-eSNy44UNoTTGq0U5lHIOdlxuHTR-sCxJAfuAUIGn2Veo")));
        } else if (id == R.id.appsug) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://walid-fekry.com/maktbti-plus/")));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    public void ooh1(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, " تطبيق مصحفي ");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "\nقمنا بتصميم البرنامج ليكون بسيط و مجاني ليضم القران الكريم كامل بدون إنترنت و أذكار الصباح و المساء مكتوبة  ليساعدك على أن لا تنسى ذكر الله ابداً .\n \nتفضل رابط تطبيق مصحفي  https://t.co/fkNQTMLNxn \n");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void updateHijriDateDisplay() {
        HijriCalendarHelper.HijriDateResult result = HijriCalendarHelper.getHijriDate(this);
        binding.main.contentMain.tvHijriDate.setText(result.getFormattedHijriDate());
        binding.main.contentMain.tvGregorianDate.setText(HijriCalendarHelper.getGregorianDateFormatted());
    }

    private void showHijriAdjustmentDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_adjust_hijri);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        final int[] selectedOffset = {HijriCalendarHelper.getOffset(this)};

        TextView tvPreviewDate = dialog.findViewById(R.id.tv_dialog_preview_date);
        TextView tvOffsetBadge = dialog.findViewById(R.id.tv_dialog_offset_badge);
        TextView tvOffsetText = dialog.findViewById(R.id.tv_offset_text);
        View btnMinus = dialog.findViewById(R.id.btn_minus);
        View btnPlus = dialog.findViewById(R.id.btn_plus);

        MaterialButton chipMinus2 = dialog.findViewById(R.id.chip_minus_2);
        MaterialButton chipMinus1 = dialog.findViewById(R.id.chip_minus_1);
        MaterialButton chipZero = dialog.findViewById(R.id.chip_zero);
        MaterialButton chipPlus1 = dialog.findViewById(R.id.chip_plus_1);
        MaterialButton chipPlus2 = dialog.findViewById(R.id.chip_plus_2);

        MaterialButton[] chips = {chipMinus2, chipMinus1, chipZero, chipPlus1, chipPlus2};
        int[] offsets = {-2, -1, 0, 1, 2};

        View btnCancel = dialog.findViewById(R.id.btn_cancel);
        View btnSave = dialog.findViewById(R.id.btn_save);

        int primaryColor = ContextCompat.getColor(this, R.color.colorPrimary);
        int strokeColor = ContextCompat.getColor(this, R.color.card_stroke);
        int primaryTextColor = ContextCompat.getColor(this, R.color.text_primary);
        int whiteTextColor = ContextCompat.getColor(this, R.color.text_white);

        Runnable updateUI = () -> {
            HijriCalendarHelper.HijriDateResult res = HijriCalendarHelper.getHijriDateWithOffset(selectedOffset[0]);
            tvPreviewDate.setText(res.getFormattedHijriDate());
            tvOffsetBadge.setText("(" + HijriCalendarHelper.getOffsetSpannableText(selectedOffset[0]) + ")");
            String offsetLabel;
            if (selectedOffset[0] == 0) {
                offsetLabel = "0 يوم (الافتراضي)";
            } else if (selectedOffset[0] > 0) {
                offsetLabel = "+" + selectedOffset[0] + " يوم";
            } else {
                offsetLabel = selectedOffset[0] + " يوم";
            }
            tvOffsetText.setText(offsetLabel);

            for (int i = 0; i < chips.length; i++) {
                if (chips[i] == null) continue;
                boolean isSelected = (selectedOffset[0] == offsets[i]);
                if (isSelected) {
                    chips[i].setBackgroundColor(primaryColor);
                    chips[i].setTextColor(whiteTextColor);
                    chips[i].setStrokeColor(ColorStateList.valueOf(primaryColor));
                } else {
                    chips[i].setBackgroundColor(Color.TRANSPARENT);
                    chips[i].setTextColor(primaryTextColor);
                    chips[i].setStrokeColor(ColorStateList.valueOf(strokeColor));
                }
            }
        };

        updateUI.run();

        btnMinus.setOnClickListener(v -> {
            if (selectedOffset[0] > -2) {
                selectedOffset[0]--;
                updateUI.run();
            }
        });

        btnPlus.setOnClickListener(v -> {
            if (selectedOffset[0] < 2) {
                selectedOffset[0]++;
                updateUI.run();
            }
        });

        chipMinus2.setOnClickListener(v -> { selectedOffset[0] = -2; updateUI.run(); });
        chipMinus1.setOnClickListener(v -> { selectedOffset[0] = -1; updateUI.run(); });
        chipZero.setOnClickListener(v -> { selectedOffset[0] = 0; updateUI.run(); });
        chipPlus1.setOnClickListener(v -> { selectedOffset[0] = 1; updateUI.run(); });
        chipPlus2.setOnClickListener(v -> { selectedOffset[0] = 2; updateUI.run(); });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            HijriCalendarHelper.setOffset(MainActivity.this, selectedOffset[0]);
            updateHijriDateDisplay();
            Toast.makeText(MainActivity.this, "تم حفظ تعديل التاريخ الهجري بنجاح", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }
}
