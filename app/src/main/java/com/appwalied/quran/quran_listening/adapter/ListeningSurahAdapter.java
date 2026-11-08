package com.appwalied.quran.quran_listening.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.databinding.ItemListeningSurahBinding;
import com.appwalied.quran.quran_listening.model.SurahModel;

import java.util.ArrayList;
import java.util.List;

public class ListeningSurahAdapter extends RecyclerView.Adapter<ListeningSurahAdapter.SurahViewHolder> implements Filterable {

    private List<SurahModel> surahList = new ArrayList<>();
    private List<SurahModel> surahFiltered = new ArrayList<>();
    private List<Integer> availableSurahIds = new ArrayList<>();
    private int playingSurahId = -1;
    private final OnSurahClickListener clickListener;

    public interface OnSurahClickListener {
        void onSurahClick(SurahModel surah);
    }

    public ListeningSurahAdapter(OnSurahClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setSurahs(List<SurahModel> list, List<Integer> availableSurahIds, int playingSurahId) {
        this.surahList = list != null ? list : new ArrayList<>();
        this.availableSurahIds = availableSurahIds != null ? availableSurahIds : new ArrayList<>();
        this.playingSurahId = playingSurahId;
        filterAvailableSurahs();
    }

    public void setPlayingSurahId(int playingSurahId) {
        this.playingSurahId = playingSurahId;
        notifyDataSetChanged();
    }

    private void filterAvailableSurahs() {
        if (availableSurahIds == null || availableSurahIds.isEmpty()) {
            this.surahFiltered = new ArrayList<>(this.surahList);
        } else {
            List<SurahModel> filtered = new ArrayList<>();
            for (SurahModel s : surahList) {
                if (availableSurahIds.contains(s.getId())) {
                    filtered.add(s);
                }
            }
            this.surahFiltered = filtered;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SurahViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemListeningSurahBinding binding = ItemListeningSurahBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new SurahViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SurahViewHolder holder, int position) {
        SurahModel surah = surahFiltered.get(position);

        holder.binding.tvSurahNumber.setText(String.valueOf(surah.getId()));
        holder.binding.tvSurahName.setText(surah.getName());
        holder.binding.tvSurahTypeBadge.setText(surah.isMakkia() ? "مكية" : "مدنية");

        boolean isPlaying = (surah.getId() == playingSurahId);
        if (isPlaying) {
            holder.binding.cardSurahContainer.setCardBackgroundColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.chip_bg)
            );
            holder.binding.ivPlayingNow.setVisibility(View.VISIBLE);
        } else {
            holder.binding.cardSurahContainer.setCardBackgroundColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.card_bg)
            );
            holder.binding.ivPlayingNow.setVisibility(View.GONE);
        }

        holder.binding.cardSurahContainer.setOnClickListener(v -> {
            setPlayingSurahId(surah.getId());
            if (clickListener != null) {
                clickListener.onSurahClick(surah);
            }
        });
    }

    @Override
    public int getItemCount() {
        return surahFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint != null ? constraint.toString().trim() : "";
                List<SurahModel> filtered = new ArrayList<>();
                List<SurahModel> baseList = (availableSurahIds != null && !availableSurahIds.isEmpty()) ? surahFiltered : surahList;

                if (query.isEmpty()) {
                    filtered.addAll(baseList);
                } else {
                    for (SurahModel s : baseList) {
                        if (s.getName().contains(query) || String.valueOf(s.getId()).equals(query)) {
                            filtered.add(s);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filtered;
                return results;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence constraint, FilterResults results) {
                surahFiltered = (List<SurahModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class SurahViewHolder extends RecyclerView.ViewHolder {
        final ItemListeningSurahBinding binding;

        public SurahViewHolder(@NonNull ItemListeningSurahBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
