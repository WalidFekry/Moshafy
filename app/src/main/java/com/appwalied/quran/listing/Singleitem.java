package com.appwalied.quran.listing;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import com.appwalied.quran.About;
import com.appwalied.quran.R;

/**
 * Created by Abboudi_Aliwi on 15.04.2017.
 * Website : http://andrody.com/
 * our channel on YouTube : https://www.youtube.com/c/Andrody2015
 * our page on Facebook : https://www.facebook.com/andrody2015/
 * our group on Facebook : https://www.facebook.com/groups/Programming.Android.apps/
 * our group on Whatsapp : https://chat.whatsapp.com/56JaImwTTMnCbQL6raHh7A
 * our group on Telegram : https://t.me/joinchat/AAAAAAm387zgezDhwkbuOA
 * Playlist to build this application : https://www.youtube.com/playlist?list=PLNivoV2e4nZWDf3CsL4go0_7sTHqhgmWA
 * Preview the app from Google play : https://play.google.com/store/apps/details?id=com.andrody.waytosuccess
 */

public class Singleitem extends AppCompatActivity {

    SeekBar seek;
    int MAX = 60; // Highest value for text size
    int MIN = 20; // Lowest value for text size
    int PLUS = 2; //Provide the text size for each step

    TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snigle_item);



        TextView title = (TextView)findViewById(R.id.title_id);
        text = (TextView)findViewById(R.id.text_id);

        title.setText(getIntent().getExtras().getString("subjectt"));
        text.setText(getIntent().getExtras().getInt("textt"));

        seek = (SeekBar) findViewById(R.id.seeeekbar);
        seek.setMax((MAX-MIN)/PLUS);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text.setTextSize(MIN + (progress * PLUS));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu :
                //startActivity(new Intent(getBaseContext(),menu.class));
                break;
            case R.id.aboutme:
                startActivity(new Intent(getApplicationContext(), About.class));

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
