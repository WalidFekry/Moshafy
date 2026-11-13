package com.appwalied.quran.quran.ui.adapters;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.databinding.ItemAyahBinding;
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
        ItemAyahBinding binding = ItemAyahBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new AyahViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AyahViewHolder holder, int position) {
        Ayah ayah = ayahList.get(position);

        // Surah Header & Bismillah for first verse
        if (ayah.getVerseId() == 1) {
            holder.binding.llSurahHeader.setVisibility(View.VISIBLE);
            holder.binding.tvHeaderSurahName.setText("سورة " + (ayah.getSuraName() != null ? ayah.getSuraName() : ""));

            if (ayah.getSuraId() == 1 || ayah.getSuraId() == 9) {
                holder.binding.tvBismillah.setVisibility(View.GONE);
            } else {
                holder.binding.tvBismillah.setVisibility(View.VISIBLE);
                holder.binding.tvBismillah.setTypeface(QuranFontHelper.getFont(holder.itemView.getContext(), fontKey));
            }
        } else {
            holder.binding.llSurahHeader.setVisibility(View.GONE);
        }

        // Clean raw Uthmanic text
        String cleanText = cleanUthmanicText(ayah.getArabicUthmanic());
        String arabicNumber = getArabicNumberString(ayah.getVerseId());
        String numberText = "\u00A0\u200F﴿" + arabicNumber + "﴾\u200F";

        Typeface verseTypeface = QuranFontHelper.getFont(holder.itemView.getContext(), fontKey);
        Typeface numberTypeface = QuranFontHelper.getFont(holder.itemView.getContext(), QuranFontHelper.FONT_QURAN_MSG);

        SpannableStringBuilder ssb = new SpannableStringBuilder();

        int startVerse = ssb.length();
        ssb.append(cleanText);
        int endVerse = ssb.length();
        if (verseTypeface != null) {
            ssb.setSpan(new QuranFontHelper.CustomTypefaceSpan(verseTypeface), startVerse, endVerse, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        int startNumber = ssb.length();
        ssb.append(numberText);
        int endNumber = ssb.length();
        if (numberTypeface != null) {
            ssb.setSpan(new QuranFontHelper.CustomTypefaceSpan(numberTypeface), startNumber, endNumber, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        holder.binding.tvAyahText.setText(ssb);
        holder.binding.tvAyahText.setTextSize(fontSizeSp);

        // Badge info
        holder.binding.tvAyahInfoBadge.setText("الآية " + ayah.getVerseId() + " • صفحة " + ayah.getPageNumber() + " • الجزء " + ayah.getParaNumber());

        // Highlight state
        boolean isPlayingThis = (ayah.getVerseId() == playingAyahId);
        if (isPlayingThis) {
            holder.binding.cardAyahContainer.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.chip_bg));
            holder.binding.ivAyahPlayingIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.binding.cardAyahContainer.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.quran_page_bg));
            holder.binding.ivAyahPlayingIndicator.setVisibility(View.GONE);
        }

        holder.binding.llAyahContent.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onAyahClick(ayah);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ayahList != null ? ayahList.size() : 0;
    }

    public static String cleanUthmanicText(String rawText) {
        if (rawText == null) return "";
        return rawText.replaceAll("\\s*\\{[0-9٠-٩]+\\}\\s*$", "")
                .replaceAll("[{}]", "")
                .trim();
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
        final ItemAyahBinding binding;

        public AyahViewHolder(@NonNull ItemAyahBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
