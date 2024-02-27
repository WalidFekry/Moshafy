package com.appwalied.quran.ahades;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;

import java.util.List;

public class AdapterRecyclerahdes extends RecyclerView.Adapter<AdapterRecyclerahdes.holder> {
    Context context;
    List<item_ahdes> listahdes;

    public AdapterRecyclerahdes(Context context, List<item_ahdes> listahdes) {
        this.context = context;
        this.listahdes = listahdes;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ahdes, viewGroup, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, final int position) {
        holder.Nameahdes.setText(listahdes.get(position).getName());
        holder.linnear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final MediaPlayer mediaPlayer = MediaPlayer.create(v.getContext(), R.raw.clikkk);
//                mediaPlayer.start();
                Intent intent = new Intent(context, Mainpagerahdes.class);
                intent.putExtra("positon", listahdes.get(position).getId());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listahdes.size();
    }

    public class holder extends RecyclerView.ViewHolder {
        TextView Nameahdes;
        LinearLayout linnear;

        public holder(@NonNull View itemView) {
            super(itemView);
            Nameahdes = (TextView) itemView.findViewById(R.id.Nameahdes);
            linnear = (LinearLayout) itemView.findViewById(R.id.linnear);

        }
    }
}