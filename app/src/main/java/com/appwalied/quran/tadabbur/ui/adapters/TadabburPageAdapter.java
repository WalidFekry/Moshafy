package com.appwalied.quran.tadabbur.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.tadabbur.model.QuranV2Page;

import java.util.ArrayList;
import java.util.List;

public class TadabburPageAdapter extends RecyclerView.Adapter<TadabburPageAdapter.ViewHolder> {

    public interface OnPageClickListener {
        void onPageClick(QuranV2Page page);
    }

    private final List<QuranV2Page> pageList = new ArrayList<>();
    private final OnPageClickListener listener;

    public TadabburPageAdapter(OnPageClickListener listener) {
        this.listener = listener;
    }

    public void setPages(List<QuranV2Page> list) {
        this.pageList.clear();
        if (list != null) {
            this.pageList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tadabbur_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuranV2Page page = pageList.get(position);
        holder.tvNum.setText(String.valueOf(page.getPageNumber()));
        holder.tvTitle.setText("صفحة " + page.getPageNumber());

        String details = "سورة " + page.getSurahsSummary() + " (الجزء " + page.getJuzNumber() + ")";
        holder.tvSurahs.setText(details);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPageClick(page);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pageList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNum, tvTitle, tvSurahs;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNum = itemView.findViewById(R.id.tv_page_num);
            tvTitle = itemView.findViewById(R.id.tv_page_title);
            tvSurahs = itemView.findViewById(R.id.tv_page_surahs);
        }
    }
}
