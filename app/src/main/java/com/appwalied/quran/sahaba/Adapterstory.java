package com.appwalied.quran.sahaba;

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

public class Adapterstory extends RecyclerView.Adapter<Adapterstory.holder> {
    Context context;
    List<item_story> listaya;

    public Adapterstory(Context context, List<item_story> listaya) {
        this.context = context;
        this.listaya = listaya;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_designstory,viewGroup,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, final int position) {
        holder.header.setText(listaya.get(position).getHeader());
        holder.linearll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final MediaPlayer mediaPlayer = MediaPlayer.create(v.getContext(), R.raw.clikkk);
//                mediaPlayer.start();
                Intent intent=new Intent(context, Mainstorytitle.class);
                intent.putExtra("storytitle",listaya.get(position).getTitle());
                intent.putExtra("headert",listaya.get(position).getHeader());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaya.size();
    }

    public class holder extends RecyclerView.ViewHolder{
        TextView header;
        LinearLayout linearll;
        public holder(@NonNull View itemView) {
            super(itemView);
            header=(TextView)itemView.findViewById(R.id.nameaya);
            linearll=(LinearLayout)itemView.findViewById(R.id.linearll);
        }
    }
}

