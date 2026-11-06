package com.appwalied.quran.sonan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.databinding.ItemRecyclerviewayaBinding;

import java.util.List;

public class AdapterRecyclerAya extends RecyclerView.Adapter<AdapterRecyclerAya.ViewHolder> {
    private final Context context;
    private final List<AyaItem> listaya;

    public AdapterRecyclerAya(Context context, List<AyaItem> listaya) {
        this.context = context;
        this.listaya = listaya;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecyclerviewayaBinding binding = ItemRecyclerviewayaBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AyaItem item = listaya.get(position);
        holder.binding.nameaya.setText(item.getName());

        holder.binding.linearll.setOnClickListener(v -> {
            Intent intent = new Intent(context, MaintitlepagerAya.class);
            intent.putExtra("positon", item.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaya != null ? listaya.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemRecyclerviewayaBinding binding;

        public ViewHolder(@NonNull ItemRecyclerviewayaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
