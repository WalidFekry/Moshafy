package com.appwalied.quran.ahades;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.appwalied.quran.R;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.List;

public class AdapterpagerAhdes extends PagerAdapter{


    List<item_ahdes> listahdes;
    Context context;
    public static int count=0;
    LayoutInflater inflater;
    Typeface typeface;

    public AdapterpagerAhdes(List<item_ahdes> listahdes, Context context) {
        this.listahdes = listahdes;
        this.context = context;
    }

    public AdapterpagerAhdes(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return listahdes.size();



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
        View view=inflater.inflate(R.layout.item_pagerahdes,container,false);
        final TextView title=(TextView)view.findViewById(R.id.title);
        final TextView number=(TextView)view.findViewById(R.id.number);
        final TextView name=(TextView)view.findViewById(R.id.name);
        MaterialIconView share=(MaterialIconView)view.findViewById(R.id.shares);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT," تطبيق مصحفي ");
                sendIntent.putExtra(Intent.EXTRA_TEXT,listahdes.get(position).getTitle()+"\n"+"تفضل رابط التطبيق  https://bit.ly/3jMaYsR \n");
                sendIntent.setType("text/plain");
                context.startActivity(Intent.createChooser(sendIntent,"مشاركه نصوص من  تطبيق مصحفي :"));

            }
        });
        MaterialIconView copy=(MaterialIconView)view.findViewById(R.id.copys);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager)context. getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("","\n"+listahdes.get(position).getTitle()+
                        "\n"+"تم نسخ من  تطبيق مصحفي " +"\n"+  "https://bit.ly/3jMaYsR\n");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "تم نسخ النص", Toast.LENGTH_SHORT).show();

            }
        });
        title.setText(listahdes.get(position).getTitle());
        name.setText(listahdes.get(position).getName());
        number.setText(String.valueOf(listahdes.get(position).getId())+("-142"));
        container.addView(view);
        return view;
    }
}