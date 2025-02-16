package com.appwalied.quran.quran.quran_reading.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.appwalied.quran.R;
import com.appwalied.quran.quran.quran_reading.Interface.OnItemClickListener;
import com.appwalied.quran.quran.quran_reading.fragment.AyatFragment;
import com.appwalied.quran.quran.quran_reading.model.Surah;
import com.appwalied.quran.quran.quran_reading.utilities.Utils;

import java.util.ArrayList;

public class SuraListAdapter extends RecyclerView.Adapter<SuraListAdapter.SurahViewHolder> {
    private ArrayList<Surah> surahArrayList;
    private Context context;

    public SuraListAdapter(ArrayList<Surah> surahArrayList, Context context) {
        this.surahArrayList = surahArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public SurahViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_for_surah, null, false);
        return new SurahViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SurahViewHolder holder, int position) {

        final Surah surah = surahArrayList.get(position);

        holder.surah_id.setText(Utils.getBnNumber(String.valueOf(surah.getId())));
        holder.sura_name_arabic.setText(surah.getName());

        if (position % 2 == 0) {
            holder.row_surah.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));

        } else {
            holder.row_surah.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
        }


        holder.SetOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                AyatFragment ayatFragment = new AyatFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("sura_id", surah.getId());
                bundle.putString("sura_name", surah.getName());
                ayatFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, ayatFragment).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return surahArrayList != null ? surahArrayList.size() : 0;
    }

    public class SurahViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener//current clickListerner
    {
        public TextView surah_id;
        public TextView sura_name_arabic;
        public LinearLayout row_surah;
        public OnItemClickListener mItemClickListener;

        public SurahViewHolder(View itemView) {
            super(itemView);

            surah_id = itemView.findViewById(R.id.surah_id);
            sura_name_arabic = itemView.findViewById(R.id.sura_name_arabic);
            row_surah = itemView.findViewById(R.id.row_surah);

            itemView.setOnClickListener(this);
        }


        public void SetOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {

            mItemClickListener.onItemClick(v);
        }

    }
}
