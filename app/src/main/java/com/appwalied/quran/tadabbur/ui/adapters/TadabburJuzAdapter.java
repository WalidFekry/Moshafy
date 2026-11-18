package com.appwalied.quran.tadabbur.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.tadabbur.model.QuranV2Juz;

import java.util.ArrayList;
import java.util.List;

public class TadabburJuzAdapter extends RecyclerView.Adapter<TadabburJuzAdapter.ViewHolder> {

    public interface OnJuzClickListener {
        void onJuzClick(QuranV2Juz juz);
    }

    private final List<QuranV2Juz> juzList = new ArrayList<>();
    private final OnJuzClickListener listener;

    public TadabburJuzAdapter(OnJuzClickListener listener) {
        this.listener = listener;
    }

    public void setJuzs(List<QuranV2Juz> list) {
        this.juzList.clear();
        if (list != null) {
            this.juzList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tadabbur_juz, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuranV2Juz juz = juzList.get(position);
        holder.tvNum.setText(String.valueOf(juz.getJuzNumber()));
        holder.tvTitle.setText("الجزء " + getArabicNumberWord(juz.getJuzNumber()));

        String info = "يبدأ من سورة " + juz.getStartSoraNameAr() + " - الآية " + juz.getStartAyaNo() + " (صفحة " + juz.getStartPage() + ")";
        holder.tvStartInfo.setText(info);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onJuzClick(juz);
            }
        });
    }

    @Override
    public int getItemCount() {
        return juzList.size();
    }

    private String getArabicNumberWord(int number) {
        String[] words = {
                "الأول", "الثاني", "الثالث", "الرابع", "الخامس", "السادس", "السابع", "الثامن", "التاسع", "العاشر",
                "الحادي عشر", "الثاني عشر", "الثالث عشر", "الرابع عشر", "الخامس عشر", "السادس عشر", "السابع عشر", "الثامن عشر", "التاسع عشر", "العشرون",
                "الحادي والعشرون", "الثاني والعشرون", "الثالث والعشرون", "الرابع والعشرون", "الخامس والعشرون", "السادس والعشرون", "السابع والعشرون", "الثامن والعشرون", "التاسع والعشرون", "الثلاثون"
        };
        if (number >= 1 && number <= 30) {
            return words[number - 1];
        }
        return String.valueOf(number);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNum, tvTitle, tvStartInfo;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNum = itemView.findViewById(R.id.tv_juz_num);
            tvTitle = itemView.findViewById(R.id.tv_juz_title);
            tvStartInfo = itemView.findViewById(R.id.tv_juz_start_info);
        }
    }
}
