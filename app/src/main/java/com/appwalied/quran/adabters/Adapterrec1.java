package com.appwalied.quran.adabters;


import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appwalied.quran.R;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.List;

public class Adapterrec1 extends RecyclerView.Adapter<Adapterrec1.holder> {
    List<itemrec>list;
    Context context;

    public Adapterrec1(List<itemrec> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemrec,viewGroup,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, final int position) {

        holder.t6.setText(list.get(position).getTextrec());
        holder.sha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT,"تطبيق مصحفي ");
                sendIntent.putExtra(Intent.EXTRA_TEXT,list.get(position).getTextrec()+"\n"+ "مشاركه نصوص من تطبيق مصحفي  \n" +"\n"+ "\n"+"https://t.co/fkNQTMLNxn");
                sendIntent.setType("text/plain");
                context.startActivity(Intent.createChooser(sendIntent,"مشاركه تطبيق مصحفي  مع الاصدقاء:"));

            }
        });
        holder.cap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager)context. getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("",list.get(position).getTextrec()+"\n"+
                        "\n"+"تم نسخ هذه النصوص من تطبيق مصحفي " +"\n"+  " https://t.co/fkNQTMLNxn\n");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "تم نسخ النص", Toast.LENGTH_SHORT).show();


            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class holder extends RecyclerView.ViewHolder{

        TextView t6;
        MaterialIconView sha,cap;
        public holder(@NonNull View itemView) {
            super(itemView);
            t6=(TextView)itemView.findViewById(R.id.t6);
            sha=(MaterialIconView)itemView.findViewById(R.id.sha);
            cap=(MaterialIconView)itemView.findViewById(R.id.cap);
        }
    }
}
