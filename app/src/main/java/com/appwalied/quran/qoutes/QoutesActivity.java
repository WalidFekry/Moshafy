package com.appwalied.quran.qoutes;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.rahman.dialog.Activity.SmartDialog;
import com.rahman.dialog.ListenerCallBack.SmartDialogClickListener;
import com.rahman.dialog.Utilities.SmartDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class QoutesActivity extends BaseActivity implements Adaptersada.OnMessengerClick {
    public static final int PERMISSION_CODE = 6458;
    private final String TAG = "TAG";
    RecyclerView rec2;
    List<new_item_post> list2;
    String name = "https://post.walid-fekry.com/auto_notification/auto_notification_apps/api_post_apps.php";
    ProgressBar progressBar;
    private String post;
    private AppCompatImageButton back;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_qoutes);
        back = findViewById(R.id.back_button);
        back.setOnClickListener((v) -> finish());
        setUpAds();
        getHandler().postDelayed(this::LoadAds, 4000);
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            new SmartDialogBuilder(QoutesActivity.this)
                    .setTitle("ملاحظه")
                    .setSubTitle("لمزيد من الرسائل المتجدده يوميا تاكد انك متصل با الانترنت")
                    .setCancalable(false)
                    .setTitleFont(Typeface.createFromAsset(getAssets(), "jazeera.ttf")) //set title font
                    .setSubTitleFont(Typeface.createFromAsset(getAssets(), "jazeera.ttf")) //set sub title font
                    .setNegativeButtonHide(true) //hide cancel button
                    .setPositiveButton("ok", new SmartDialogClickListener() {
                        @Override
                        public void onClick(SmartDialog smartDialog) {
                            smartDialog.dismiss();
                        }
                    }).build().show();
        }
        rec2 = findViewById(R.id.rec2);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rec2.setLayoutManager(mLayoutManager);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.layout_anmation);
        rec2.setLayoutAnimation(animation);
        new DownloadTask().execute(name);
    }


    private void setUpAds() {
        AdView adView = findViewById(R.id.rsttfal);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);

        InterstitialAd.load(this, getString(R.string.Biny1), adRequest,
                new InterstitialAdLoadCallback() {
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
            mInterstitialAd.show(QoutesActivity.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }



    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray post = response.optJSONArray("post");
            list2 = new ArrayList<>();
            for (int i = 0; i < post.length(); i++) {
                JSONObject posts = post.optJSONObject(i);
                String title = posts.getString("title");
                String imageurl = posts.getString("imageurl");
                int id = posts.getInt("id");
                String createdAt = posts.getString("created_at");
                list2.add(new new_item_post(title, imageurl, id, createdAt));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onMessengerItemClick(new_item_post item) {

    }


    public class DownloadTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
//            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    parseResult(response.toString());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
//            progressBar.setVisibility(View.GONE);
            if (result == 1) {
                Adaptersada adaptersada = new Adaptersada(list2, QoutesActivity.this);
                rec2.setAdapter(adaptersada);
            } else {
                list2 = new ArrayList<>();
                list2.add(new new_item_post(getResources().getString(R.string.my42)));
                list2.add(new new_item_post(getResources().getString(R.string.my1)));
                list2.add(new new_item_post(getResources().getString(R.string.my2)));
                list2.add(new new_item_post(getResources().getString(R.string.my3)));
                list2.add(new new_item_post(getResources().getString(R.string.my4)));
                list2.add(new new_item_post(getResources().getString(R.string.my5)));
                list2.add(new new_item_post(getResources().getString(R.string.my6)));
                list2.add(new new_item_post(getResources().getString(R.string.my7)));
                list2.add(new new_item_post(getResources().getString(R.string.my8)));
                list2.add(new new_item_post(getResources().getString(R.string.my9)));
                list2.add(new new_item_post(getResources().getString(R.string.my10)));
                list2.add(new new_item_post(getResources().getString(R.string.my11)));
                list2.add(new new_item_post(getResources().getString(R.string.my12)));
                list2.add(new new_item_post(getResources().getString(R.string.my13)));
                list2.add(new new_item_post(getResources().getString(R.string.my14)));
                list2.add(new new_item_post(getResources().getString(R.string.my15)));
                list2.add(new new_item_post(getResources().getString(R.string.my16)));
                list2.add(new new_item_post(getResources().getString(R.string.my17)));
                list2.add(new new_item_post(getResources().getString(R.string.my18)));
                list2.add(new new_item_post(getResources().getString(R.string.my19)));
                list2.add(new new_item_post(getResources().getString(R.string.my21)));
                list2.add(new new_item_post(getResources().getString(R.string.my22)));
                list2.add(new new_item_post(getResources().getString(R.string.my23)));
                list2.add(new new_item_post(getResources().getString(R.string.my26)));
                list2.add(new new_item_post(getResources().getString(R.string.my27)));
                list2.add(new new_item_post(getResources().getString(R.string.my28)));
                list2.add(new new_item_post(getResources().getString(R.string.my29)));
                list2.add(new new_item_post(getResources().getString(R.string.my30)));
                list2.add(new new_item_post(getResources().getString(R.string.my31)));
                list2.add(new new_item_post(getResources().getString(R.string.my32)));
                list2.add(new new_item_post(getResources().getString(R.string.my33)));
                list2.add(new new_item_post(getResources().getString(R.string.my34)));
                list2.add(new new_item_post(getResources().getString(R.string.my35)));
                list2.add(new new_item_post(getResources().getString(R.string.my36)));
                list2.add(new new_item_post(getResources().getString(R.string.my37)));
                list2.add(new new_item_post(getResources().getString(R.string.my38)));
                list2.add(new new_item_post(getResources().getString(R.string.my39)));
                list2.add(new new_item_post(getResources().getString(R.string.my40)));
                list2.add(new new_item_post(getResources().getString(R.string.my41)));
                list2.add(new new_item_post(getResources().getString(R.string.staticvalue)));
                Adaptersada adapter2 = new Adaptersada(list2, QoutesActivity.this);
                rec2.setAdapter(adapter2);
                Toast.makeText(QoutesActivity.this, "توجد مشكله با الاتصال تاكد ان الانترنت متاح لديك ", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
