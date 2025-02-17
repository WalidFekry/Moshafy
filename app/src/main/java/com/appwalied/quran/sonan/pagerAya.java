package com.appwalied.quran.sonan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.appwalied.quran.R;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.List;

public class pagerAya extends PagerAdapter {
    List<item_Aya> list2;
    Context context;
    public static int count=0;

    public pagerAya(List<item_Aya> list2, Context context) {
        this.list2 = list2;
        this.context = context;
    }

    LayoutInflater inflater;

    Typeface typeface;

    public pagerAya(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return list2.size();



    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {

        return  (view==o);

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {

        inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.item_pageraya,container,false);
        final TextView nameaya=(TextView)view.findViewById(R.id.nameaya);
        final TextView title=(TextView)view.findViewById(R.id.title);
        final TextView num=(TextView)view.findViewById(R.id.num);
        MaterialIconView share=(MaterialIconView)view.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT," تطبيق مصحفي ");
                sendIntent.putExtra(Intent.EXTRA_TEXT,"\n" +list2.get(position).getName()+"\n"+list2.get(position).getTitle()+"تفضل رابط التطبيق  https://t.co/fkNQTMLNxn \n");
                sendIntent.setType("text/plain");
                context.startActivity(Intent.createChooser(sendIntent,"مشاركه نصوص من  تطبيق مصحفي :"));

            }
        });
        MaterialIconView copy=(MaterialIconView)view.findViewById(R.id.copy);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager)context. getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("",list2.get(position).getName()+"\n"+list2.get(position).getTitle()+
                        "\n"+"تم نسخ من  تطبيق مصحفي " +"\n"+  "https://t.co/fkNQTMLNxn\n");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "تم نسخ النص", Toast.LENGTH_SHORT).show();

            }
        });
        nameaya.setText(list2.get(position).getName());
        title.setText(list2.get(position).getTitle());
        num.setText(String.valueOf(list2.get(position).getNum())+("-59"));
        container.addView(view);
        return view;
    }
}

