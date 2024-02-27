package com.appwalied.quran;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.appwalied.quran.adabters.AdapterQuranmsg;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ldoublem.loadingviewlib.view.LVGears;

import java.util.ArrayList;

public class QuranicMessage extends AppCompatActivity {

    LVGears lvgears;
    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<QuranMsg> list;
    AdapterQuranmsg adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_quranic_message);


        AdView adView = (AdView) findViewById(R.id.qumsgbanner);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);


        if (!isConnected())
        {
            NoInternetDialog alert = new NoInternetDialog();
            alert.showDialog(this);
        }

        lvgears=(LVGears)findViewById(R.id.QuLvb);
        lvgears.startAnim();
        lvgears.setViewColor(R.color.white);


        recyclerView = (RecyclerView) findViewById(R.id.quranicmessage_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<QuranMsg>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);


        reference = FirebaseDatabase.getInstance().getReference().child("QuranicMessage");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    QuranMsg quranMsg = dataSnapshot1.getValue(QuranMsg.class);
                    list.add(quranMsg);
                }
                adapter = new AdapterQuranmsg(QuranicMessage.this, list);
                recyclerView.setAdapter(adapter);

                //Activity Splash When No Internet.
                lvgears.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(QuranicMessage.this, "حدث خطأ ما... رجاء المحاولة مرة أخرى", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public class NoInternetDialog {

        public void showDialog(QuranicMessage activity) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.noin_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


            Button mDialogOk = dialog.findViewById(R.id.ok);
            mDialogOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }

            });

            dialog.show();
        }
    }


    //Checking Internet.
    private boolean isConnected()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}