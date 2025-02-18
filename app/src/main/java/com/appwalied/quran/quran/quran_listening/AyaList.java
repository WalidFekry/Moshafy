package com.appwalied.quran.quran.quran_listening;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;

import com.appwalied.quran.R;
import com.appwalied.quran.adabters.LnaguageClass;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.quran.quran_listening.listening.AuthorClass;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.ArrayList;


public class AyaList extends BaseActivity {
    private static final String TAG = "AyaList";
    public static ArrayList<AuthorClass> listrecitesAya = new ArrayList<>();
    static String RecitesName = "";
    ListView listAya;
    String RecitesAYA = "";
    private InterstitialAd mInterstitialAd;
    private AppCompatImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aya_list);
        //get Recites
        Bundle b = getIntent().getExtras();
        RecitesName = b.getString("RecitesName");
        listAya = findViewById(R.id.listView);
        back = findViewById(R.id.back_button);
        //get list of recites
        listrecitesAya.clear();
        LnaguageClass lc = new LnaguageClass();
        listrecitesAya = lc.GuranAya(RecitesName);
        listAya.setAdapter(new VivzAdapter(this, listrecitesAya));
        back.setOnClickListener(v -> finish());
        setUpAds();
        getHandler().postDelayed(this::LoadAds, 4000);
    }

    private void DisplayAya() {
        Intent intent = new Intent(this, QuranListeningActivity.class);
        intent.putExtra("RecitesName", RecitesName);
        intent.putExtra("RecitesAYA", RecitesAYA);
        startActivity(intent);
    }

    private void setUpAds() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, getString(R.string.Biny3), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Log.i(TAG, "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i(TAG, loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });
    }

    private void LoadAds() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(AyaList.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }

    class VivzAdapter extends BaseAdapter {
        private final ArrayList<AuthorClass> listrecitesLocal;
        private final LayoutInflater mInflater;

        VivzAdapter(Context context, ArrayList<AuthorClass> listrecites) {
            this.listrecitesLocal = listrecites;
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return this.listrecitesLocal.size();
        }

        @Override
        public AuthorClass getItem(int position) {
            return this.listrecitesLocal.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.single_rowayalist, parent, false);
                holder = new ViewHolder();
                holder.title = convertView.findViewById(R.id.textView1);
                holder.cost = convertView.findViewById(R.id.textView2);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final AuthorClass temp = getItem(i);
            holder.title.setText(temp.RealName);
            holder.cost.setText(temp.StateName);

            convertView.setOnClickListener(v -> {
                for (int i1 = 0; i1 < listrecitesAya.size(); i1++) {
                    if (listrecitesAya.get(i1).RealName.equals(temp.RealName)) {
                        RecitesAYA = String.valueOf(i1);
                        DisplayAya();
                        break;
                    }
                }
            });

            return convertView;
        }

        static class ViewHolder {
            TextView title;
            TextView cost;
        }
    }

}
