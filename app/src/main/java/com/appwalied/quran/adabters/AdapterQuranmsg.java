package com.appwalied.quran.adabters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appwalied.quran.QuranMsg;
import com.appwalied.quran.R;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;

public class AdapterQuranmsg extends RecyclerView.Adapter<AdapterQuranmsg.MyViewHolder> {

    Context context;
    ArrayList<QuranMsg> list;
    MaterialIconView rsael_rate, rsael_copy, rsael_share, rsael_whats;


    public AdapterQuranmsg(Context c, ArrayList<QuranMsg> p)
    {
        context = c;
        list = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.quranicmsg_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle().replace("_","\n"));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.rsael_item_textview);

            //Rsael Items OnClick
            rsael_copy = itemView.findViewById(R.id.qumsgcopy);
            rsael_share = itemView.findViewById(R.id.qumsgshare);
            rsael_rate = itemView.findViewById(R.id.qumsgrate);
            rsael_whats = itemView.findViewById(R.id.qumsgwhats);

            rsael_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String appname = v.getContext().getResources().getString(R.string.app_name);
                    ClipboardManager myClickboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData myClip = ClipData.newPlainText("text", title.getText().toString() +"\n"+
                            "\n"+"تم نسخ هذه النصوص من تطبيق مصحفي " +"\n"+  " https://t.co/fkNQTMLNxn\n");
                    myClickboard.setPrimaryClip(myClip);
                    Toast.makeText(context, "تم نسخ النص", Toast.LENGTH_SHORT).show();
                }
            });

            rsael_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT,"تطبيق مصحفي ");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, title.getText().toString() +"\n"+ "مشاركه نصوص من تطبيق مصحفي  \n" +"\n"+ "\n"+"https://t.co/fkNQTMLNxn");
                    shareIntent.setType("text/plain");
                    context.startActivity(Intent.createChooser(shareIntent,"مشاركه تطبيق مصحفي  مع الاصدقاء:"));
                }
            });

            rsael_rate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.co/fkNQTMLNxn"));
                    context.startActivity(intent);
                }
            });

            rsael_whats.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent whatsIntent = new Intent();
                    whatsIntent.setAction(Intent.ACTION_SEND);
                    whatsIntent.putExtra(Intent.EXTRA_TEXT, title.getText().toString());
                    whatsIntent.setType("text/plain");
                    whatsIntent.setPackage("com.whatsapp");
                    context.startActivity(whatsIntent);
                }
            });


        }
    }
}
