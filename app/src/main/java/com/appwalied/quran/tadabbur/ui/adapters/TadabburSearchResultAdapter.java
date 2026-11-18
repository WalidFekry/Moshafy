package com.appwalied.quran.tadabbur.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.tadabbur.model.QuranV2Ayah;

import java.util.ArrayList;
import java.util.List;

public class TadabburSearchResultAdapter extends RecyclerView.Adapter<TadabburSearchResultAdapter.ViewHolder> {

    public interface OnResultClickListener {
        void onResultClick(QuranV2Ayah ayah);
    }

    private final List<QuranV2Ayah> results = new ArrayList<>();
    private final OnResultClickListener listener;

    public TadabburSearchResultAdapter(OnResultClickListener listener) {
        this.listener = listener;
    }

    public void setResults(List<QuranV2Ayah> list) {
        this.results.clear();
        if (list != null) {
            this.results.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tadabbur_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuranV2Ayah ayah = results.get(position);
        holder.tvSurahInfo.setText("سورة " + ayah.getSoraNameAr() + " - الآية " + ayah.getAyaNo());
        holder.tvPageInfo.setText("صفحة " + ayah.getPage() + " • الجزء " + ayah.getJozz());
        holder.tvSnippet.setText(ayah.getAyaText());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onResultClick(ayah);
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSurahInfo, tvPageInfo, tvSnippet;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSurahInfo = itemView.findViewById(R.id.tv_search_surah_info);
            tvPageInfo = itemView.findViewById(R.id.tv_search_page_info);
            tvSnippet = itemView.findViewById(R.id.tv_search_snippet);
        }
    }
}
