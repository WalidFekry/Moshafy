package com.appwalied.quran;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.io.IOException;
import java.io.InputStream;

public class sql0title098 extends AppCompatActivity {
    TextView ts, ooo01, ooo02, ooo03;
    InputStream os;
    String path = "";
    int a = 0;
    int c = 18;
    boolean isScreenOn = false;
    CardView setScreenOn,setScreenOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sql0title098);
        ts = (TextView) findViewById(R.id.ts);
        ooo01 = (TextView) findViewById(R.id.ooo01);
        ooo02 = (TextView) findViewById(R.id.ooo02);
        ooo03 = (TextView) findViewById(R.id.ooo03);
        setScreenOn = findViewById(R.id.screen_on);
        setScreenOff = findViewById(R.id.screen_off);
        int id = getIntent().getIntExtra("id", 0);
        String text = "";
        try {
            //اي تغير في رقم من دول التطبيق هيضرب  .... تمام
            if (id == R.id.a1) {
                os = getAssets().open("1.txt");

                ooo03.setText("عدد آيات سورة الفاتحة : 7");
                ooo01.setText("النزول : مكية Makki");

            }
            //get file when user click on button
            //serch for sura num and show it in the ui
            else if (id == R.id.a2) {
                os = getAssets().open("02.txt");
                ooo03.setText(" عدد آيات سورة البقرة : 286");
                ooo01.setText(" النزول : مدنية Madani");

            } else if (id == R.id.a3) {
                os = getAssets().open("3");
                ooo03.setText("عدد آيات سورة آل عمران : 200");
                ooo01.setText("النزول : مدنية Madani");
            } else if (id == R.id.a4) {
                os = getAssets().open("4");
                ooo03.setText(" عدد آيات سورة النساء : 176");
                ooo01.setText("النزول : مدنية Madani");
            } else if (id == R.id.a5) {
                os = getAssets().open("5");
                ooo03.setText("عدد آيات سورة المائدة : 120");
                ooo01.setText(" النزول : مدنية Madani");
            } else if (id == R.id.a6) {
                os = getAssets().open("6");
                ooo03.setText("عدد آيات سورة الأنعام : 165");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a7) {
                os = getAssets().open("07");
                ooo03.setText("عدد آيات سورة الأعراف : 206");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a8) {
                os = getAssets().open("8");
                ooo03.setText(" عدد آيات سورة الأنفال : 75");
                ooo01.setText(" النزول : مدنية Madani");
            } else if (id == R.id.a9) {
                os = getAssets().open("9");
                ooo03.setText("عدد آيات سورة التوبة : 129");
                ooo01.setText(" النزول : مدنية Madani");
            } else if (id == R.id.a10) {
                os = getAssets().open("10");
                ooo03.setText("عدد آيات سورة يونس : 109");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a11) {
                os = getAssets().open("11");
                ooo03.setText(" عدد آيات سورة هود : 123");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a12) {
                os = getAssets().open("12");
                ooo03.setText(" عدد آيات سورة يوسف : 111");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a13) {
                os = getAssets().open("13");
                ooo03.setText(" عدد آيات سورة الرعد : 43");
                ooo01.setText(" النزول : مدنية Madani");
            } else if (id == R.id.a14) {
                os = getAssets().open("14");
                ooo03.setText(" عدد آيات سورة إبراهيم : 52");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a15) {
                os = getAssets().open("15");
                ooo03.setText("عدد آيات سورة الحجر : 99");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a16) {
                os = getAssets().open("16");
                ooo03.setText("عدد آيات سورة النحل : 128");
                ooo01.setText("النزول : مكية Makki");
            } else if (id == R.id.a17) {
                os = getAssets().open("17");
                ooo03.setText(" عدد آيات سورة الإسراء : 111");
                ooo01.setText("النزول : مكية Makki");
            } else if (id == R.id.a18) {
                os = getAssets().open("18");
                ooo03.setText("عدد آيات سورة الكهف : 110");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a19) {
                os = getAssets().open("19");
                ooo03.setText("عدد آيات سورة مريم : 98");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a20) {
                os = getAssets().open("20");
                ooo03.setText("عدد آيات سورة طه : 135");
                ooo01.setText("النزول : مكية Makki");
            } else if (id == R.id.a21) {
                os = getAssets().open("21");
                ooo03.setText("عدد آيات سورة الأنبياء : 112");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a22) {
                os = getAssets().open("22");
                ooo03.setText("عدد آيات سورة الحج : 78");
                ooo01.setText("النزول : مدنية Madani");
            } else if (id == R.id.a23) {
                os = getAssets().open("23");
                ooo03.setText(" عدد آيات سورة المؤمنون : 118");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a24) {
                os = getAssets().open("24");
                ooo03.setText(" عدد آيات سورة النور : 64");
                ooo01.setText(" النزول : مدنية Madani");
            } else if (id == R.id.a25) {
                os = getAssets().open("25");
                ooo03.setText("عدد آيات سورة الفرقان : 77");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a26) {
                os = getAssets().open("26");
                ooo03.setText("عدد آيات سورة الشعراء : 227");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a27) {
                os = getAssets().open("27");
                ooo03.setText(" عدد آيات سورة النمل : 93");
                ooo01.setText("النزول : مكية Makki");
            } else if (id == R.id.a28) {
                os = getAssets().open("28");
                ooo03.setText(" عدد آيات سورة القصص : 88");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a29) {
                os = getAssets().open("29");
                ooo03.setText(" عدد آيات سورة العنكبوت : 69");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a30) {
                os = getAssets().open("30");
                ooo03.setText(" عدد آيات سورة الروم : 60");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a31) {
                os = getAssets().open("31");
                ooo03.setText("عدد آيات سورة لقمان : 34");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a32) {
                os = getAssets().open("32");
                ooo03.setText(" عدد آيات سورة السجدة : 30");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a33) {
                os = getAssets().open("33");
                ooo03.setText(" عدد آيات سورة الأحزاب : 73");
                ooo01.setText(" النزول : مدنية Madani");
            } else if (id == R.id.a34) {
                os = getAssets().open("34");
                ooo03.setText("عدد آيات سورة سبأ : 54");
                ooo01.setText("النزول : مكية Makki");
            } else if (id == R.id.a35) {
                os = getAssets().open("35");
                ooo03.setText(" عدد آيات سورة فاطر : 45");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a36) {
                os = getAssets().open("36");
                ooo03.setText(" عدد آيات سورة يس : 83");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a37) {
                os = getAssets().open("37");
                ooo03.setText("عدد آيات سورة الصافات : 182");
                ooo01.setText("النزول : مكية Makki");
            } else if (id == R.id.a38) {
                os = getAssets().open("38");
                ooo03.setText("عدد آيات سورة ص : 88");
                ooo01.setText("النزول : مكية Makki");
            } else if (id == R.id.a39) {
                os = getAssets().open("39");
                ooo03.setText(" عدد آيات سورة الزمر : 75");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a40) {
                os = getAssets().open("40");
                ooo03.setText(" عدد آيات سورة غافر : 85");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a41) {
                os = getAssets().open("41");
                ooo03.setText(" عدد آيات سورة فصلت : 54");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a42) {
                os = getAssets().open("42");
                ooo03.setText(" عدد آيات سورة الشورى : 53");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a43) {
                os = getAssets().open("43");
                ooo03.setText("عدد آيات سورة الزخرف : 89");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a44) {
                os = getAssets().open("44");
                ooo03.setText("عدد آيات سورة الدخان : 59");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a45) {
                os = getAssets().open("45");
                ooo03.setText("عدد آيات سورة الجاثية : 37");
                ooo01.setText("النزول : مكية Makki");
            } else if (id == R.id.a46) {
                os = getAssets().open("46");
                ooo03.setText(" عدد آيات سورة الأحقاف : 35");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a47) {
                os = getAssets().open("47");
                ooo03.setText(" عدد آيات سورة محمد : 38");
                ooo01.setText(" النزول : مدنية Madani");
            } else if (id == R.id.a48) {
                os = getAssets().open("48");
                ooo03.setText(" عدد آيات سورة الفتح : 29");
                ooo01.setText(" النزول : مدنية Madani");
            } else if (id == R.id.a49) {
                os = getAssets().open("49");
                ooo03.setText(" عدد آيات سورة الحجرات : 18");
                ooo01.setText(" النزول : مدنية Madani");
            } else if (id == R.id.a50) {
                os = getAssets().open("50");
                ooo03.setText(" عدد آيات سورة ق : 45");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a51) {
                os = getAssets().open("51");

                ooo03.setText("عدد آيات سورة الذاريات : 60");
                ooo01.setText("النزول : مكية Makki");

            } else if (id == R.id.a52) {
                os = getAssets().open("52");
                ooo03.setText("عدد آيات سورة الطور : 49");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a53) {
                os = getAssets().open("53");
                ooo03.setText(" عدد آيات سورة النجم : 62");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a54) {
                os = getAssets().open("54");
                ooo03.setText("عدد آيات سورة القمر : 55");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a55) {
                os = getAssets().open("55");

                ooo03.setText("عدد آيات سورة الرحمن : 78");
                ooo01.setText(" النزول : مدنية Madani");
            } else if (id == R.id.a56) {
                os = getAssets().open("56");
                ooo03.setText(" عدد آيات سورة الواقعة : 96");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a57) {
                os = getAssets().open("57");
                ooo03.setText("عدد آيات سورة الحديد : 29");
                ooo01.setText(" النزول : مدنية Madani");
            } else if (id == R.id.a58) {
                os = getAssets().open("58");
                ooo03.setText(" عدد آيات سورة المجادلة : 22");
                ooo01.setText(" النزول : مدنية Madani");
            } else if (id == R.id.a59) {
                os = getAssets().open("59");

                ooo03.setText(" عدد آيات سورة الحشر : 24");
                ooo01.setText("النزول : مدنية Madani");
            } else if (id == R.id.a60) {
                os = getAssets().open("60");
                ooo03.setText("عدد آيات سورة الممتحنة : 13");
                ooo01.setText("النزول : مدنية Madani");
            } else if (id == R.id.a61) {
                os = getAssets().open("61");

                ooo03.setText(" عدد آيات سورة الصف : 14");
                ooo01.setText("النزول : مدنية Madani");
            } else if (id == R.id.a62) {
                os = getAssets().open("62");
                ooo03.setText("عدد آيات سورة الجمعة : 11");
                ooo01.setText(" النزول : مدنية Madani");
            } else if (id == R.id.a63) {
                os = getAssets().open("63");

                ooo03.setText(" عدد آيات سورة المنافقون : 11");
                ooo01.setText(" النزول : مدنية Madani");
            } else if (id == R.id.a64) {
                os = getAssets().open("64");
                ooo03.setText("عدد آيات سورة التغابن : 18");
                ooo01.setText(" النزول : مدنية Madani");
            } else if (id == R.id.a65) {
                os = getAssets().open("65");
                ooo03.setText("عدد آيات سورة الطلاق : 12");
                ooo01.setText("النزول : مدنية Madani");
            } else if (id == R.id.a66) {
                os = getAssets().open("66");
                ooo03.setText("عدد آيات سورة التحريم : 12");
                ooo01.setText("النزول : مدنية Madani");
            } else if (id == R.id.a67) {
                os = getAssets().open("67");
                ooo03.setText(" عدد آيات سورة الملك : 30");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a68) {
                os = getAssets().open("68");
                ooo03.setText("عدد آيات سورة القلم : 52");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a69) {
                os = getAssets().open("69");

                ooo03.setText("عدد آيات سورة الحاقة : 52");
                ooo01.setText("النزول : مكية Makki");
            } else if (id == R.id.a70) {
                os = getAssets().open("70");
                ooo03.setText(" عدد آيات سورة المعارج : 44");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a71) {
                os = getAssets().open("71");
                ooo03.setText("عدد آيات سورة نوح : 28");
                ooo01.setText("النزول : مكية Makki");
            } else if (id == R.id.a72) {
                os = getAssets().open("72");
                ooo03.setText(" عدد آيات سورة الجن : 28");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a73) {
                os = getAssets().open("73");
                ooo03.setText("عدد آيات سورة المزمل : 20");
                ooo01.setText("النزول : مكية Makki");
            } else if (id == R.id.a74) {
                os = getAssets().open("74");
                ooo03.setText(" عدد آيات سورة المدثر : 56");
                ooo01.setText("النزول : مكية Makki");
            } else if (id == R.id.a75) {
                os = getAssets().open("75");
                ooo03.setText(" عدد آيات سورة القيامة : 40");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a76) {
                os = getAssets().open("76");
                ooo03.setText(" عدد آيات سورة الإنسان : 31");
                ooo01.setText("النزول : مدنية Madani");
            } else if (id == R.id.a77) {
                os = getAssets().open("77");
                ooo03.setText("عدد آيات سورة المرسلات : 50");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a78) {
                os = getAssets().open("78");
                ooo03.setText(" عدد آيات سورة النبأ : 40");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a79) {
                os = getAssets().open("79");
                ooo03.setText("عدد آيات سورة النازعات : 46");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a80) {
                os = getAssets().open("80");
                ooo03.setText(" عدد آيات سورة عبس : 42");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a81) {
                os = getAssets().open("81");
                ooo03.setText("عدد آيات سورة التكوير : 29");
                ooo01.setText("النزول : مكية Makki");
            } else if (id == R.id.a82) {
                os = getAssets().open("82");
                ooo03.setText(" عدد آيات سورة الانفطار : 19");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a83) {
                os = getAssets().open("83");
                ooo03.setText(" عدد آيات سورة المطففين : 36");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a84) {
                os = getAssets().open("84");
                ooo03.setText("عدد آيات سورة الانشقاق : 25");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a85) {
                os = getAssets().open("85");
                ooo03.setText(" عدد آيات سورة البروج : 22");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a86) {
                os = getAssets().open("86");
                ooo03.setText(" عدد آيات سورة الطارق : 17");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a87) {
                os = getAssets().open("87");
                ooo03.setText(" عدد آيات سورة الأعلى : 19");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a88) {
                os = getAssets().open("88");
                ooo03.setText(" عدد آيات سورة الغاشية : 26");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a89) {
                os = getAssets().open("89");
                ooo03.setText(" عدد آيات سورة الفجر : 30");
                ooo01.setText("النزول : مكية Makki");
            } else if (id == R.id.a90) {
                os = getAssets().open("90");
                ooo03.setText("عدد آيات سورة البلد : 20");
                ooo01.setText("النزول : مكية Makki");
            } else if (id == R.id.a91) {
                os = getAssets().open("91");
                ooo03.setText(" عدد آيات سورة الشمس : 15");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a92) {
                os = getAssets().open("92");

                ooo03.setText(" عدد آيات سورة الليل : 21");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a93) {
                os = getAssets().open("93");
                ooo03.setText(" عدد آيات سورة الضحى : 11");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a94) {
                os = getAssets().open("94");
                ooo03.setText(" عدد آيات سورة الشرح : 8");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a95) {
                os = getAssets().open("95");
                ooo03.setText(" عدد آيات سورة التين : 8");
                ooo01.setText("النزول : مكية Makki");
            } else if (id == R.id.a96) {
                os = getAssets().open("96");

                ooo03.setText(" عدد آيات سورة العلق : 19");
                ooo01.setText("النزول : مكية Makki");
            } else if (id == R.id.a97) {
                os = getAssets().open("97");
                ooo03.setText(" عدد آيات سورة القدر : 5");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a98) {
                os = getAssets().open("98");
                ooo03.setText(" عدد آيات سورة البينة : 8");
                ooo01.setText(" النزول : مدنية Madani");
            } else if (id == R.id.a99) {
                os = getAssets().open("99");
                ooo03.setText(" عدد آيات سورة الزلزلة : 8");
                ooo01.setText(" النزول : مدنية Madani");
            } else if (id == R.id.a100) {
                os = getAssets().open("100");
                ooo03.setText("عدد آيات سورة العاديات : 11");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a101) {
                os = getAssets().open("101");
                ooo03.setText("عدد آيات سورة القارعة : 11");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a102) {
                os = getAssets().open("102");
                ooo03.setText(" عدد آيات سورة التكاثر : 8");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a103) {
                os = getAssets().open("103");
                ooo03.setText(" عدد آيات سورة العصر : 3");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a104) {
                os = getAssets().open("104");
                ooo03.setText("عدد آيات سورة الهمزة : 9");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a105) {
                os = getAssets().open("105");
                ooo03.setText(" عدد آيات سورة الفيل : 5");
                ooo01.setText("النزول : مكية Makki");
            } else if (id == R.id.a106) {
                os = getAssets().open("106");
                ooo03.setText("عدد آيات سورة قريش : 4");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a107) {
                os = getAssets().open("107");
                ooo03.setText(" عدد آيات سورة الماعون : 7");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a108) {
                os = getAssets().open("108");
                ooo03.setText(" عدد آيات سورة الكوثر : 3");
                ooo01.setText("النزول : مكية Makki");
            } else if (id == R.id.a109) {
                os = getAssets().open("109");

                ooo03.setText(" عدد آيات سورة الكافرون : 6");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a110) {
                os = getAssets().open("110");
                ooo03.setText("عدد آيات سورة النصر : 3");
                ooo01.setText(" النزول : مدنية Madani");
            } else if (id == R.id.a111) {
                os = getAssets().open("111");
                ooo03.setText(" عدد آيات سورة المسد : 5");
                ooo01.setText("النزول : مكية Makki");
            } else if (id == R.id.a112) {
                os = getAssets().open("112");
                ooo03.setText("عدد آيات سورة الإخلاص : 4");
                ooo01.setText(" النزول : مكية Makki");
            } else if (id == R.id.a113) {
                os = getAssets().open("113");
                ooo03.setText("عدد آيات سورة الفلق : 5");
                ooo02.setText("");
                ooo01.setText(" النزول : مكية Makki");
            } else {
                os = getAssets().open("114");
                ooo03.setText(" عدد آيات سورة الناس : 6");
                ooo01.setText(" النزول : مكية Makki");
            }
            int size = os.available();
            //file is aval
            byte[] bytes = new byte[size];
            //get bytes in fille
            os.read(bytes);
            os.close();
            //close this
            text = new String(bytes);
//but is file in ui text
        } catch (IOException e) {
            e.printStackTrace();
        }
        ts.setText(text);
        //show this


    }

    public void plus(View view) {
        if (c < 50) {
            c++;
        } else
            Toast.makeText(sql0title098.this, "لايمكن تكبير الخط اكبر من ذلك", Toast.LENGTH_SHORT).show();
        ts.setTextSize(c);


    }

    public void minus(View view) {

        if (c > 13) {
            c--;
        } else
            Toast.makeText(sql0title098.this, "لايمكن تصغير الخط اصغر من ذلك", Toast.LENGTH_SHORT).show();
        ts.setTextSize(c);


    }

    public void star(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.co/fkNQTMLNxn")));
    }

    public void screenOn(View view) {
        if (!isScreenOn) {
            setScreenOn.setVisibility(View.GONE);
            setScreenOff.setVisibility(View.VISIBLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            Toast.makeText(this, "تم تفعيل ابقاء الشاشة اثناء القراءة", Toast.LENGTH_SHORT).show();
            isScreenOn = true;
        } else {
            setScreenOn.setVisibility(View.VISIBLE);
            setScreenOff.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            Toast.makeText(this, "تم ايقاف ابقاء الشاشة اثناء القراءة", Toast.LENGTH_SHORT).show();
            isScreenOn = false;
        }
    }
}
