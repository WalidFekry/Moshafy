package com.appwalied.quran;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import guy4444.smartrate.SmartRate;


public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);




        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(this!=null && !isFinishing()){
                    SmartRate.Rate(About.this
                            , "تقييم التطبيق"
                            , "تقييمك للتطبيق يساعدنا علي التطوير المستمر وتقديم المزيد"
                            , "تقييم الان"
                            , "حسنا يمكنك تقيممنا الان علي جوجل بلاي"
                            , "اضغط هنا"
                            , "ليس الان"
                            , "Thanks "
                            , Color.parseColor("#c65164")                 , 2         );


                }
            }
        }, 5000);





    }
    public void oos1(View view){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.co/fkNQTMLNxn")));
    }

    public void walid (View view){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.co/fkNQTMLNxn")));
    }
    public void ooh1(View view){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT," تطبيق مصحفي ");
        sendIntent.putExtra(Intent.EXTRA_TEXT,"\n" +
                "قمنا بتصميم البرنامج ليكون بسيط و مجاني ليضم القران الكريم كامل بدون إنترنت و أذكار الصباح و المساء مكتوبة  ليساعدك على أن لا تنسى ذكر الله ابداً .\n \n" +
                "تفضل رابط تطبيق مصحفي  https://t.co/fkNQTMLNxn\n");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
    public void oon1(View view){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","prowalidfekry@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "رسالة إلى مطور تطبيق مصحفي");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
    public void oom1(View view){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",getResources().getString(R.string.m), null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.n));
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    public void walid99(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=6257553101128037563")));
    }


    public void onBackClicked(View view) {
        onBackPressed();
    }
}
