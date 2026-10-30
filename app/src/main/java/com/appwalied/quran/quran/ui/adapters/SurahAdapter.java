package com.appwalied.quran.quran.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.quran.db.models.Surah;

import java.util.ArrayList;
import java.util.List;

public class SurahAdapter extends RecyclerView.Adapter<SurahAdapter.SurahViewHolder> implements Filterable {

    private List<Surah> surahList = new ArrayList<>();
    private List<Surah> filteredList = new ArrayList<>();
    private List<Integer> favoriteSurahIds = new ArrayList<>();

    private OnSurahClickListener clickListener;
    private OnFavoriteClickListener favoriteClickListener;

    public interface OnSurahClickListener {
        void onSurahClick(Surah surah);
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Surah surah, boolean isFavorite);
    }

    public SurahAdapter(OnSurahClickListener clickListener, OnFavoriteClickListener favoriteClickListener) {
        this.clickListener = clickListener;
        this.favoriteClickListener = favoriteClickListener;
    }

    public void setSurahs(List<Surah> surahs) {
        this.surahList = new ArrayList<>(surahs);
        this.filteredList = new ArrayList<>(surahs);
        notifyDataSetChanged();
    }

    public void setFavorites(List<Integer> favoriteIds) {
        this.favoriteSurahIds = favoriteIds != null ? favoriteIds : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SurahViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_surah, parent, false);
        return new SurahViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SurahViewHolder holder, int position) {
        Surah surah = filteredList.get(position);
        holder.tvSurahNumber.setText(String.valueOf(surah.getId()));
        holder.tvSurahName.setText("سورة " + surah.getNameAr());
        holder.tvSurahDetails.setText("آياتها " + surah.getVersesCount() + " • صفحة " + surah.getStartPage());

        boolean isFav = favoriteSurahIds.contains(surah.getId());
        if (isFav) {
            holder.btnFavorite.setImageResource(R.drawable.ic_favorite);
            holder.btnFavorite.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorAccent));
        } else {
            holder.btnFavorite.setImageResource(R.drawable.ic_favorite_border);
            holder.btnFavorite.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.text_secondary));
        }

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onSurahClick(surah);
            }
        });

        holder.btnFavorite.setOnClickListener(v -> {
            if (favoriteClickListener != null) {
                favoriteClickListener.onFavoriteClick(surah, !isFav);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint == null ? "" : constraint.toString().trim().toLowerCase();
                List<Surah> results = new ArrayList<>();
                if (query.isEmpty()) {
                    results.addAll(surahList);
                } else {
                    for (Surah s : surahList) {
                        if (s.getNameAr().toLowerCase().contains(query) || String.valueOf(s.getId()).equals(query)) {
                            results.add(s);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = results;
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (List<Surah>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class SurahViewHolder extends RecyclerView.ViewHolder {
        TextView tvSurahNumber, tvSurahName, tvSurahDetails;
        ImageButton btnFavorite;

        public SurahViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSurahNumber = itemView.findViewById(R.id.tv_surah_number);
            tvSurahName = itemView.findViewById(R.id.tv_surah_name);
            tvSurahDetails = itemView.findViewById(R.id.tv_surah_details);
            btnFavorite = itemView.findViewById(R.id.btn_surah_favorite);
        }
    }
}
