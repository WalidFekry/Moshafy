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

public class Adapterrec2 extends RecyclerView.Adapter<Adapterrec2.holder2>{
    List<itemrec>list;
    Context context;

    public Adapterrec2(List<itemrec> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public holder2 onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemrec2,viewGroup,false);
        return new holder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder2 holder2, final int position) {
        holder2.t6.setText(list.get(position).getTextrec());
        holder2.sha.setOnClickListener(new View.OnClickListener() {
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
        holder2.cap.setOnClickListener(new View.OnClickListener() {
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

    public class holder2 extends RecyclerView.ViewHolder{
        TextView t6;
        MaterialIconView sha,cap;
        public holder2(@NonNull View itemView) {
            super(itemView);
            t6=(TextView)itemView.findViewById(R.id.t61);
            sha=(MaterialIconView)itemView.findViewById(R.id.sha1);
            cap=(MaterialIconView)itemView.findViewById(R.id.cap1);
        }
    }
}


