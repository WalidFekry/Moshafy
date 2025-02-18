package com.appwalied.quran.quran.quran_listening;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.appwalied.quran.R;
import com.appwalied.quran.adabters.LnaguageClass;
import com.appwalied.quran.quran.quran_listening.listening.AuthorClass;

import java.util.ArrayList;
import java.util.List;

public class RecitesName extends AppCompatActivity {
    public static int LanguageSelect = 1;
    private List<AuthorClass> listRecites = new ArrayList<>();
    private ListView lvRecites;
    private String recitesName = "";
    private AppCompatImageButton back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recites_name);

        lvRecites = findViewById(R.id.listView);
        back = findViewById(R.id.back_button);
        listRecites = new LnaguageClass().AutherList();
        lvRecites.setAdapter(new RecitesAdapter(listRecites));

        lvRecites.setOnItemClickListener((parent, view, position, id) -> {
            TextView txtRecitesName = view.findViewById(R.id.txtRecitesName);
            for (AuthorClass reciter : listRecites) {
                if (reciter.RealName.equals(txtRecitesName.getText().toString())) {
                    recitesName = reciter.ServerName;
                    displayAyaList();
                    break;
                }
            }
        });

        back.setOnClickListener(v -> finish());
    }


    private void displayAyaList() {
        if (!recitesName.isEmpty()) {
            Intent intent = new Intent(this, AyaList.class);
            intent.putExtra("RecitesName", recitesName);
            startActivity(intent);
        }
    }

    private class RecitesAdapter extends BaseAdapter {
        private final List<AuthorClass> recitesList;

        RecitesAdapter(List<AuthorClass> recitesList) {
            this.recitesList = recitesList;
        }

        @Override
        public int getCount() {
            return recitesList.size();
        }

        @Override
        public AuthorClass getItem(int position) {
            return recitesList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recites_ticket, parent, false);
            }
            TextView txtRecitesName = convertView.findViewById(R.id.txtRecitesName);
            txtRecitesName.setText(getItem(position).RealName);
            return convertView;
        }
    }
}