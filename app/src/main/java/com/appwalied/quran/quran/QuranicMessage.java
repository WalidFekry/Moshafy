package com.appwalied.quran.quran;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.quran.model.QuranMsg;
import com.appwalied.quran.R;
import com.appwalied.quran.adabters.AdapterQuranmsg;
import com.appwalied.quran.utils.shared_helper.SharedPrefsConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuranicMessage extends BaseActivity {

    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<QuranMsg> list;
    AdapterQuranmsg adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quranic_message);

        checkAndShowDialog(SharedPrefsConstants.QURAN_MESSAGES_FIRST_TIME, "يحتوي هذا القسم على رسائل قرآنية 💙");

        recyclerView = findViewById(R.id.quranicmessage_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        reference = FirebaseDatabase.getInstance().getReference().child("QuranicMessage");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    QuranMsg quranMsg = dataSnapshot1.getValue(QuranMsg.class);
                    list.add(quranMsg);
                }
                adapter = new AdapterQuranmsg(QuranicMessage.this, list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(QuranicMessage.this, "حدث خطأ ما... رجاء المحاولة مرة أخرى", Toast.LENGTH_LONG).show();
            }
        });
    }
}