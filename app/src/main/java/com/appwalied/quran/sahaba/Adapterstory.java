package com.appwalied.quran.sahaba;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.databinding.ItemDesignstoryBinding;

import java.util.List;

public class Adapterstory extends RecyclerView.Adapter<Adapterstory.ViewHolder> {
    private final Context context;
    private final List<item_story> listaya;

    public Adapterstory(Context context, List<item_story> listaya) {
        this.context = context;
        this.listaya = listaya;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDesignstoryBinding binding = ItemDesignstoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        item_story item = listaya.get(position);
        holder.binding.nameaya.setText(item.getHeader());

        holder.binding.linearll.setOnClickListener(v -> {
            Intent intent = new Intent(context, Mainstorytitle.class);
            intent.putExtra("storytitle", item.getTitle());
            intent.putExtra("headert", item.getHeader());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaya != null ? listaya.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemDesignstoryBinding binding;

        public ViewHolder(@NonNull ItemDesignstoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
