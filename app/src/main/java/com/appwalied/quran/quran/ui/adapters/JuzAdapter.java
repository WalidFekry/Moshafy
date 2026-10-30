package com.appwalied.quran.quran.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.quran.db.models.Juz;

import java.util.ArrayList;
import java.util.List;

public class JuzAdapter extends RecyclerView.Adapter<JuzAdapter.JuzViewHolder> implements Filterable {

    private List<Juz> juzList = new ArrayList<>();
    private List<Juz> filteredList = new ArrayList<>();
    private OnJuzClickListener listener;

    public interface OnJuzClickListener {
        void onJuzClick(Juz juz);
    }

    public JuzAdapter(OnJuzClickListener listener) {
        this.listener = listener;
    }

    public void setJuzList(List<Juz> juzs) {
        this.juzList = new ArrayList<>(juzs);
        this.filteredList = new ArrayList<>(juzs);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public JuzViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_juz, parent, false);
        return new JuzViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JuzViewHolder holder, int position) {
        Juz juz = filteredList.get(position);
        holder.tvJuzNumber.setText(String.valueOf(juz.getJuzNumber()));
        holder.tvJuzTitle.setText("الجزء " + getArabicNumberString(juz.getJuzNumber()));
        holder.tvJuzStartInfo.setText("يبدأ من سورة " + juz.getStartSuraName() + " - آية " + juz.getStartAyahId() + " (صفحة " + juz.getStartPage() + ")");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onJuzClick(juz);
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
                List<Juz> results = new ArrayList<>();
                if (query.isEmpty()) {
                    results.addAll(juzList);
                } else {
                    for (Juz j : juzList) {
                        if (j.getStartSuraName().toLowerCase().contains(query) || String.valueOf(j.getJuzNumber()).equals(query)) {
                            results.add(j);
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
                filteredList = (List<Juz>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    private String getArabicNumberString(int number) {
        String[] digits = {"٠", "١", "٢", "٣", "٤", "٥", "٦", "٧", "٨", "٩"};
        StringBuilder sb = new StringBuilder();
        String str = String.valueOf(number);
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                sb.append(digits[c - '0']);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    static class JuzViewHolder extends RecyclerView.ViewHolder {
        TextView tvJuzNumber, tvJuzTitle, tvJuzStartInfo;

        public JuzViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJuzNumber = itemView.findViewById(R.id.tv_juz_number);
            tvJuzTitle = itemView.findViewById(R.id.tv_juz_title);
            tvJuzStartInfo = itemView.findViewById(R.id.tv_juz_start_info);
        }
    }
}
