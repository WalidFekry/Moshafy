package com.appwalied.quran.quran.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.quran.db.models.Ayah;

import java.util.ArrayList;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {

    private List<Ayah> results = new ArrayList<>();
    private OnSearchResultClickListener listener;

    public interface OnSearchResultClickListener {
        void onSearchResultClick(Ayah ayah);
    }

    public SearchResultAdapter(OnSearchResultClickListener listener) {
        this.listener = listener;
    }

    public void setResults(List<Ayah> ayahs) {
        this.results = ayahs != null ? ayahs : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        Ayah ayah = results.get(position);
        holder.tvSurahInfo.setText("سورة " + ayah.getSuraName() + " - الآية " + ayah.getVerseId() + " (صفحة " + ayah.getPageNumber() + ")");
        holder.tvVerseText.setText(ayah.getArabicUthmanic());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSearchResultClick(ayah);
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    static class SearchResultViewHolder extends RecyclerView.ViewHolder {
        TextView tvSurahInfo, tvVerseText;

        public SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSurahInfo = itemView.findViewById(R.id.tv_search_surah_info);
            tvVerseText = itemView.findViewById(R.id.tv_search_verse_text);
        }
    }
}
