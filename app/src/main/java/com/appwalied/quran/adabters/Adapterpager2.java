package com.appwalied.quran.adabters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class Adapterpager2 extends PagerAdapter {
    int a = 0;
    int c = 18;
    Context context;
    List<item_pager> listpager2;

    public Adapterpager2(Context context, List<item_pager> listpager2) {
        this.context = context;
        this.listpager2 = listpager2;
    }


    @Override
    public int getCount() {
        return listpager2.size();


    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == o);

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.it_pager, container, false);
        final TextView tests1 = (TextView) view.findViewById(R.id.texts1);
        final TextView tests2 = (TextView) view.findViewById(R.id.texts2);
        tests1.setText(listpager2.get(position).getText1());
        tests2.setText(listpager2.get(position).getText2());
        MaterialIconView minus = (MaterialIconView) view.findViewById(R.id.minus);
        MaterialIconView plus = (MaterialIconView) view.findViewById(R.id.plus);
        MaterialIconView share = (MaterialIconView) view.findViewById(R.id.share);
        MaterialIconView starts = (MaterialIconView) view.findViewById(R.id.stars);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c > 13) {
                    c--;
                } else
                    Toast.makeText(context, "لايمكن تصغير الخط اصغر من ذلك", Toast.LENGTH_SHORT).show();
                tests1.setTextSize(c);
                tests2.setTextSize(c);

            }


        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c < 50) {
                    c++;
                } else
                    Toast.makeText(context, "لايمكن تكبير الخط اكبر من ذلك", Toast.LENGTH_SHORT).show();
                tests1.setTextSize(c);
                tests2.setTextSize(c);

            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, " تطبيق مصحفي ");
                sendIntent.putExtra(Intent.EXTRA_TEXT, listpager2.get(position).getText1() + "\n" + listpager2.get(position).getText2() + "\n" + "مشاركه نصوص من تطبيق'مصحفي \n" + "\n" + "\n" + " https://t.co/fkNQTMLNxn\n");
                sendIntent.setType("text/plain");
                context.startActivity(Intent.createChooser(sendIntent, "مشاركه تطبيق مصحفي:"));
                Toast.makeText(context, "جزاك الله خيرا لمشاركه التطبيق", Toast.LENGTH_SHORT).show();

            }
        });
        starts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.co/fkNQTMLNxn"));
                context.startActivity(intent);

            }
        });
        container.addView(view);

        return view;


    }
}