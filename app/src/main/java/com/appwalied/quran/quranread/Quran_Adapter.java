package com.appwalied.quran.quranread;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;

import java.util.ArrayList;

public class Quran_Adapter extends RecyclerView.Adapter<Quran_Adapter.QuranViewHolder> {
    ArrayList<Quran> quranlist;
    Context context;

    public Quran_Adapter(ArrayList<Quran> quranlist, Context context) {
        this.quranlist = quranlist;
        this.context = context;
    }

    @NonNull
    @Override
    public QuranViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View form = LayoutInflater.from(context).inflate(R.layout.quran_row, viewGroup, false);
        QuranViewHolder quranViewHolder = new QuranViewHolder(form);

        return quranViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuranViewHolder holder, int i) {
        Quran quran = quranlist.get(i);
        holder.imageView.setBackgroundResource(quran.getImagemnum());
        holder.imageView.setRotationY(180);
    }

    @Override
    public int getItemCount() {
        return quranlist.size();
    }

    class QuranViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public QuranViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageid);
        }
    }
}
