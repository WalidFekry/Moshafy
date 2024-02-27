package com.appwalied.quran.quran.qouran_learning;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.quran.qouran_learning.models.Ayah;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AyasAdapter extends RecyclerView.Adapter<AyasAdapter.Holder> {
    NumberFormat nf = NumberFormat.getInstance(new Locale("ar", "EG"));
    int selectedAyaIndex = 0;
    List<Ayah> ayahList;
    Interaction interaction;

    public AyasAdapter(List<Ayah> ayahList, Interaction interaction) {
        this.ayahList = ayahList;
        this.interaction = interaction;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.aya_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.ayaTextView.setText(nf.format(position + 1) + " - " + ayahList.get(holder.getAdapterPosition()).getText());
        holder.ayaTextView.setOnClickListener(view -> {
            interaction.onAyaClicked(ayahList.get(holder.getAdapterPosition()), position);
            selectedAyaIndex = holder.getAdapterPosition();
            notifyDataSetChanged();
        });

        if (selectedAyaIndex == holder.getAdapterPosition()) {
            holder.ayaTextView.setTextColor(ContextCompat.getColor(holder.ayaTextView.getContext(), R.color.black));
        } else {
            holder.ayaTextView.setTextColor(ContextCompat.getColor(holder.ayaTextView.getContext(), R.color.white));
        }
    }

    public void setSelectedAyaIndex(int selectedAyaIndex) {
        this.selectedAyaIndex = selectedAyaIndex;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ayahList.size();
    }

    interface Interaction {
        void onAyaClicked(Ayah ayah, int position);
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView ayaTextView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ayaTextView = itemView.findViewById(R.id.aya_text_view);
        }
    }
}
