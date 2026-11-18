package com.appwalied.quran.tadabbur.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.tadabbur.model.QuranV2Surah;

import java.util.ArrayList;
import java.util.List;

public class TadabburSurahAdapter extends RecyclerView.Adapter<TadabburSurahAdapter.ViewHolder> {

    public interface OnSurahClickListener {
        void onSurahClick(QuranV2Surah surah);
    }

    private final List<QuranV2Surah> surahList = new ArrayList<>();
    private final OnSurahClickListener listener;

    public TadabburSurahAdapter(OnSurahClickListener listener) {
        this.listener = listener;
    }

    public void setSurahs(List<QuranV2Surah> list) {
        this.surahList.clear();
        if (list != null) {
            this.surahList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tadabbur_surah, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuranV2Surah surah = surahList.get(position);
        holder.tvNum.setText(String.valueOf(surah.getSoraNumber()));
        holder.tvNameAr.setText(surah.getNameAr());
        holder.tvNameEn.setText("(" + surah.getNameEn() + ")");

        String details = surah.getVersesCount() + " آية • الجزء " + surah.getStartJuzz() + " • ص " + surah.getStartPage();
        holder.tvDetails.setText(details);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSurahClick(surah);
            }
        });
    }

    @Override
    public int getItemCount() {
        return surahList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNum, tvNameAr, tvNameEn, tvDetails;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNum = itemView.findViewById(R.id.tv_surah_num);
            tvNameAr = itemView.findViewById(R.id.tv_surah_name_ar);
            tvNameEn = itemView.findViewById(R.id.tv_surah_name_en);
            tvDetails = itemView.findViewById(R.id.tv_surah_details);
        }
    }
}
