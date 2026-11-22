package com.appwalied.quran.quran_listening.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.databinding.ItemDownloadedSurahBinding;
import com.appwalied.quran.quran_listening.download.DownloadedSurahEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DownloadedSurahAdapter extends RecyclerView.Adapter<DownloadedSurahAdapter.ViewHolder> implements Filterable {

    private List<DownloadedSurahEntity> items = new ArrayList<>();
    private List<DownloadedSurahEntity> itemsFiltered = new ArrayList<>();
    private String activePlayingDownloadId = "";
    private final OnDownloadedSurahClickListener clickListener;

    public interface OnDownloadedSurahClickListener {
        void onPlayClick(DownloadedSurahEntity surah);
        void onDeleteClick(DownloadedSurahEntity surah);
    }

    public DownloadedSurahAdapter(OnDownloadedSurahClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setItems(List<DownloadedSurahEntity> list) {
        this.items = list != null ? list : new ArrayList<>();
        this.itemsFiltered = new ArrayList<>(this.items);
        notifyDataSetChanged();
    }

    public void setActivePlayingDownloadId(String downloadId) {
        this.activePlayingDownloadId = downloadId != null ? downloadId : "";
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDownloadedSurahBinding binding = ItemDownloadedSurahBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DownloadedSurahEntity item = itemsFiltered.get(position);

        holder.binding.tvSurahNumber.setText(String.valueOf(item.getSurahId()));
        holder.binding.tvSurahName.setText(item.getSurahName());

        String sizeFormatted = formatFileSize(item.getFileSize());
        String infoText = item.getReciterName() + " • " + sizeFormatted;
        holder.binding.tvReciterAndSize.setText(infoText);

        boolean isPlaying = item.getDownloadId().equals(activePlayingDownloadId);
        if (isPlaying) {
            holder.binding.cardDownloadedContainer.setCardBackgroundColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.chip_bg)
            );
        } else {
            holder.binding.cardDownloadedContainer.setCardBackgroundColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.card_bg)
            );
        }

        holder.binding.btnPlayOffline.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onPlayClick(item);
            }
        });

        holder.binding.cardDownloadedContainer.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onPlayClick(item);
            }
        });

        holder.binding.btnDeleteOffline.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onDeleteClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsFiltered.size();
    }

    private String formatFileSize(long bytes) {
        if (bytes <= 0) return "0 ب";
        if (bytes < 1024 * 1024) {
            float kb = bytes / 1024f;
            return String.format(Locale.getDefault(), "%.1f ك.ب", kb);
        }
        float mb = bytes / (1024f * 1024f);
        return String.format(Locale.getDefault(), "%.1f م.ب", mb);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint != null ? constraint.toString().trim() : "";
                List<DownloadedSurahEntity> filtered = new ArrayList<>();
                if (query.isEmpty()) {
                    filtered.addAll(items);
                } else {
                    for (DownloadedSurahEntity item : items) {
                        if (item.getSurahName().contains(query)
                                || item.getReciterName().contains(query)
                                || String.valueOf(item.getSurahId()).equals(query)) {
                            filtered.add(item);
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
                itemsFiltered = (List<DownloadedSurahEntity>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemDownloadedSurahBinding binding;

        public ViewHolder(@NonNull ItemDownloadedSurahBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
