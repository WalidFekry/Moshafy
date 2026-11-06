package com.appwalied.quran.quran;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.appwalied.quran.adabters.AdapterQuranmsg;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityQuranicMessageBinding;
import com.appwalied.quran.quran.model.QuranMsg;
import com.appwalied.quran.utils.shared_helper.SharedPrefsConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuranicMessage extends BaseActivity {

    private ActivityQuranicMessageBinding binding;
    private DatabaseReference reference;
    private ArrayList<QuranMsg> list;
    private AdapterQuranmsg adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuranicMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkAndShowDialog(SharedPrefsConstants.QURAN_MESSAGES_FIRST_TIME, "يحتوي هذا القسم على رسائل قرآنية 💙");

        binding.backButton.setOnClickListener(v -> finish());

        binding.quranicmessageRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new AdapterQuranmsg(this, list);
        binding.quranicmessageRecyclerview.setAdapter(adapter);

        showLoading();
        reference = FirebaseDatabase.getInstance().getReference().child("QuranicMessage");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (isFinishing() || isDestroyed()) return;

                hideLoading();
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    QuranMsg quranMsg = dataSnapshot1.getValue(QuranMsg.class);
                    if (quranMsg != null) {
                        list.add(quranMsg);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (isFinishing() || isDestroyed()) return;

                hideLoading();
                Toast.makeText(QuranicMessage.this, "حدث خطأ ما... رجاء المحاولة مرة أخرى", Toast.LENGTH_LONG).show();
            }
        });

        showBanner(binding.adsContainer);
    }
}
