package com.appwalied.quran.quran.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.quran.audio.Reciter;

import java.util.ArrayList;
import java.util.List;

public class ReciterAdapter extends RecyclerView.Adapter<ReciterAdapter.ReciterViewHolder> implements Filterable {

    private List<Reciter> reciterList = new ArrayList<>();
    private List<Reciter> filteredList = new ArrayList<>();
    private String selectedFolder = "Alafasy_128kbps";
    private OnReciterClickListener listener;

    public interface OnReciterClickListener {
        void onReciterClick(Reciter reciter);
    }

    public ReciterAdapter(OnReciterClickListener listener) {
        this.listener = listener;
    }

    public void setReciters(List<Reciter> reciters, String activeFolder) {
        this.reciterList = new ArrayList<>(reciters);
        this.filteredList = new ArrayList<>(reciters);
        this.selectedFolder = activeFolder;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReciterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reciter, parent, false);
        return new ReciterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReciterViewHolder holder, int position) {
        Reciter reciter = filteredList.get(position);
        holder.tvArName.setText(reciter.getArabicName());
        holder.tvEnName.setText(reciter.getName());
        holder.tvBitrate.setText(reciter.getBitrate() != null && !reciter.getBitrate().isEmpty() ? reciter.getBitrate() : "MP3");

        boolean isSelected = reciter.getSubfolder().equals(selectedFolder);
        holder.ivCheck.setVisibility(isSelected ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onReciterClick(reciter);
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
                List<Reciter> results = new ArrayList<>();
                if (query.isEmpty()) {
                    results.addAll(reciterList);
                } else {
                    for (Reciter r : reciterList) {
                        if (r.getArabicName().toLowerCase().contains(query) ||
                                r.getName().toLowerCase().contains(query) ||
                                r.getSubfolder().toLowerCase().contains(query)) {
                            results.add(r);
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
                filteredList = (List<Reciter>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ReciterViewHolder extends RecyclerView.ViewHolder {
        TextView tvArName, tvEnName, tvBitrate;
        ImageView ivCheck;

        public ReciterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvArName = itemView.findViewById(R.id.tv_reciter_ar_name);
            tvEnName = itemView.findViewById(R.id.tv_reciter_en_name);
            tvBitrate = itemView.findViewById(R.id.tv_reciter_bitrate);
            ivCheck = itemView.findViewById(R.id.iv_active_reciter_check);
        }
    }
}
