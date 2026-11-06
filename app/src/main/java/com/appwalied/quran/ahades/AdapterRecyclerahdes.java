package com.appwalied.quran.ahades;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.databinding.ItemAhdesBinding;

import java.util.List;

public class AdapterRecyclerahdes extends RecyclerView.Adapter<AdapterRecyclerahdes.ViewHolder> {
    private final Context context;
    private final List<item_ahdes> listahdes;

    public AdapterRecyclerahdes(Context context, List<item_ahdes> listahdes) {
        this.context = context;
        this.listahdes = listahdes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAhdesBinding binding = ItemAhdesBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        item_ahdes item = listahdes.get(position);
        holder.binding.Nameahdes.setText(item.getName());

        holder.binding.linnear.setOnClickListener(v -> {
            Intent intent = new Intent(context, Mainpagerahdes.class);
            intent.putExtra("positon", item.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listahdes != null ? listahdes.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemAhdesBinding binding;

        public ViewHolder(@NonNull ItemAhdesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
