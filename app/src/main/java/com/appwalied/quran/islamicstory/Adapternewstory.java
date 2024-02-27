package com.appwalied.quran.islamicstory;

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
import com.appwalied.quran.sahaba.item_story;

import java.util.List;

public class Adapternewstory extends RecyclerView.Adapter<Adapternewstory.holder> {
    Context context;
    List<item_story> listaya;

    public Adapternewstory(Context context, List<item_story> listaya) {
        this.context = context;
        this.listaya = listaya;
    }

    @NonNull
    @Override
    public Adapternewstory.holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_newstory,viewGroup,false);
        return new Adapternewstory.holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapternewstory.holder holder, final int position) {
        holder.nameaya.setText(listaya.get(position).getHeader());
        holder.linearll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final MediaPlayer mediaPlayer = MediaPlayer.create(v.getContext(), R.raw.clikkk);
//                mediaPlayer.start();
                Intent intent=new Intent(context, Maintitlenewstory.class);
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
        TextView nameaya;
        LinearLayout linearll;
        public holder(@NonNull View itemView) {
            super(itemView);
            nameaya=(TextView)itemView.findViewById(R.id.header);
            linearll=(LinearLayout)itemView.findViewById(R.id.linearll);
        }
    }
}
