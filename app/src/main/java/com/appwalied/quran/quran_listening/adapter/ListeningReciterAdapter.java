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
import com.appwalied.quran.databinding.ItemListeningReciterBinding;
import com.appwalied.quran.quran_listening.model.MoshafModel;
import com.appwalied.quran.quran_listening.model.ReciterModel;

import java.util.ArrayList;
import java.util.List;

public class ListeningReciterAdapter extends RecyclerView.Adapter<ListeningReciterAdapter.ReciterViewHolder> implements Filterable {

    private List<ReciterModel> reciters = new ArrayList<>();
    private List<ReciterModel> recitersFiltered = new ArrayList<>();
    private int selectedReciterId = -1;
    private final OnReciterClickListener clickListener;

    public interface OnReciterClickListener {
        void onReciterClick(ReciterModel reciter);
    }

    public ListeningReciterAdapter(OnReciterClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setReciters(List<ReciterModel> list, int selectedReciterId) {
        this.reciters = list != null ? list : new ArrayList<>();
        this.recitersFiltered = new ArrayList<>(this.reciters);
        this.selectedReciterId = selectedReciterId;
        notifyDataSetChanged();
    }

    public void setSelectedReciterId(int selectedReciterId) {
        this.selectedReciterId = selectedReciterId;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReciterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemListeningReciterBinding binding = ItemListeningReciterBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ReciterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReciterViewHolder holder, int position) {
        ReciterModel reciter = recitersFiltered.get(position);

        holder.binding.tvReciterName.setText(reciter.getName());

        MoshafModel mainMoshaf = reciter.getMainMoshaf();
        String rewayaInfo = mainMoshaf != null ? mainMoshaf.getName() + " • " + mainMoshaf.getSurahTotal() + " سورة" : "";
        holder.binding.tvReciterRewaya.setText(rewayaInfo);

        boolean isSelected = (reciter.getId() == selectedReciterId);
        if (isSelected) {
            holder.binding.cardReciterContainer.setCardBackgroundColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.chip_bg)
            );
            holder.binding.ivSelectedCheck.setVisibility(View.VISIBLE);
            holder.binding.ivSelectedCheck.setImageResource(R.drawable.ic_check);
            holder.binding.ivSelectedCheck.setColorFilter(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.colorPrimary)
            );
        } else {
            holder.binding.cardReciterContainer.setCardBackgroundColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.card_bg)
            );
            holder.binding.ivSelectedCheck.setVisibility(View.VISIBLE);
            holder.binding.ivSelectedCheck.setImageResource(R.drawable.ic_previous);
            holder.binding.ivSelectedCheck.setColorFilter(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.text_secondary)
            );
        }

        holder.binding.cardReciterContainer.setOnClickListener(v -> {
            setSelectedReciterId(reciter.getId());
            if (clickListener != null) {
                clickListener.onReciterClick(reciter);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recitersFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint != null ? constraint.toString().trim() : "";
                List<ReciterModel> filtered = new ArrayList<>();
                if (query.isEmpty()) {
                    filtered.addAll(reciters);
                } else {
                    for (ReciterModel r : reciters) {
                        if (r.getName().contains(query)) {
                            filtered.add(r);
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
                recitersFiltered = (List<ReciterModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ReciterViewHolder extends RecyclerView.ViewHolder {
        final ItemListeningReciterBinding binding;

        public ReciterViewHolder(@NonNull ItemListeningReciterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
