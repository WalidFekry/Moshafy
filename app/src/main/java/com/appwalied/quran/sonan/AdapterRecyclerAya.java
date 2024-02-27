package com.appwalied.quran.sonan;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.appwalied.quran.R;

import java.util.List;

public class AdapterRecyclerAya  extends RecyclerView.Adapter<AdapterRecyclerAya.holder> {
    Context context;
    List<item_Aya>listaya;

    public AdapterRecyclerAya(Context context, List<item_Aya> listaya) {
        this.context = context;
        this.listaya = listaya;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recyclerviewaya,viewGroup,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, final int position) {
        holder.nameaya.setText(listaya.get(position).getName());
        holder.linearll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final MediaPlayer mediaPlayer = MediaPlayer.create(v.getContext(), R.raw.clikkk);
//                mediaPlayer.start();
                Intent intent=new Intent(context, MaintitlepagerAya.class);
                intent.putExtra("positon",listaya.get(position).getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaya.size();
    }

    public class holder extends RecyclerView.ViewHolder{
        TextView nameaya;
        LinearLayout linearll;
        public holder(@NonNull View itemView) {
            super(itemView);
            nameaya=(TextView)itemView.findViewById(R.id.nameaya);
            linearll=(LinearLayout)itemView.findViewById(R.id.linearll);
        }
    }
}

