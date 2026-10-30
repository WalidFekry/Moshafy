package com.appwalied.quran.quran.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.quran.db.models.Ayah;
import com.appwalied.quran.quran.utils.QuranFontHelper;

import java.util.ArrayList;
import java.util.List;

public class AyahAdapter extends RecyclerView.Adapter<AyahAdapter.AyahViewHolder> {

    private List<Ayah> ayahList = new ArrayList<>();
    private int playingAyahId = -1;
    private int fontSizeSp = 24;
    private String fontKey = "uthmanii";
    private OnAyahClickListener clickListener;

    public interface OnAyahClickListener {
        void onAyahClick(Ayah ayah);
    }

    public AyahAdapter(OnAyahClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setAyahs(List<Ayah> ayahs) {
        this.ayahList = ayahs != null ? ayahs : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setPlayingAyahId(int playingAyahId) {
        this.playingAyahId = playingAyahId;
        notifyDataSetChanged();
    }

    public void setFontSizeSp(int fontSizeSp) {
        this.fontSizeSp = fontSizeSp;
        notifyDataSetChanged();
    }

    public void setFontKey(String fontKey) {
        this.fontKey = fontKey;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AyahViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ayah, parent, false);
        return new AyahViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AyahViewHolder holder, int position) {
        Ayah ayah = ayahList.get(position);

        // Surah Header & Bismillah for first verse
        if (ayah.getVerseId() == 1) {
            holder.llSurahHeader.setVisibility(View.VISIBLE);
            holder.tvHeaderSurahName.setText("سورة " + (ayah.getSuraName() != null ? ayah.getSuraName() : ""));

            // Hide Bismillah for Surah 1 (Al-Fatihah where Bismillah is verse 1) and Surah 9 (At-Tawbah)
            if (ayah.getSuraId() == 1 || ayah.getSuraId() == 9) {
                holder.tvBismillah.setVisibility(View.GONE);
            } else {
                holder.tvBismillah.setVisibility(View.VISIBLE);
                holder.tvBismillah.setTypeface(QuranFontHelper.getFont(holder.itemView.getContext(), fontKey));
            }
        } else {
            holder.llSurahHeader.setVisibility(View.GONE);
        }

        // Verse Text
        String verseFormatted = ayah.getArabicUthmanic() + " ﴿" + getArabicNumberString(ayah.getVerseId()) + "﴾";
        holder.tvAyahText.setText(verseFormatted);
        holder.tvAyahText.setTextSize(fontSizeSp);
        holder.tvAyahText.setTypeface(QuranFontHelper.getFont(holder.itemView.getContext(), fontKey));

        // Badge info
        holder.tvAyahInfo.setText("الآية " + ayah.getVerseId() + " • صفحة " + ayah.getPageNumber() + " • الجزء " + ayah.getParaNumber());

        // Highlight state
        boolean isPlayingThis = (ayah.getVerseId() == playingAyahId);
        if (isPlayingThis) {
            holder.cardContainer.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.chip_bg));
            holder.ivPlayingIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.cardContainer.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.quran_page_bg));
            holder.ivPlayingIndicator.setVisibility(View.GONE);
        }

        holder.llAyahContent.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onAyahClick(ayah);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ayahList.size();
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

    static class AyahViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llSurahHeader, llAyahContent;
        TextView tvHeaderSurahName, tvBismillah, tvAyahText, tvAyahInfo;
        CardView cardContainer;
        ImageView ivPlayingIndicator;

        public AyahViewHolder(@NonNull View itemView) {
            super(itemView);
            llSurahHeader = itemView.findViewById(R.id.ll_surah_header);
            llAyahContent = itemView.findViewById(R.id.ll_ayah_content);
            tvHeaderSurahName = itemView.findViewById(R.id.tv_header_surah_name);
            tvBismillah = itemView.findViewById(R.id.tv_bismillah);
            tvAyahText = itemView.findViewById(R.id.tv_ayah_text);
            tvAyahInfo = itemView.findViewById(R.id.tv_ayah_info_badge);
            cardContainer = itemView.findViewById(R.id.card_ayah_container);
            ivPlayingIndicator = itemView.findViewById(R.id.iv_ayah_playing_indicator);
        }
    }
}
