package com.appwalied.quran.sonan;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.appwalied.quran.databinding.ItemPagerayaBinding;

import java.util.List;
import java.util.Locale;

public class pagerAya extends PagerAdapter {

    private final List<AyaItem> list2;
    private final Context context;

    public pagerAya(List<AyaItem> list2, Context context) {
        this.list2 = list2;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list2 != null ? list2.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ItemPagerayaBinding binding = ItemPagerayaBinding.inflate(
                LayoutInflater.from(context), container, false
        );

        AyaItem item = list2.get(position);

        binding.nameaya.setText(item.getName());
        binding.title.setText(item.getTitle());
        String numberText = String.format(Locale.getDefault(), "سنة رقم %d", position + 1);
        binding.num.setText(numberText);

        // Share Action
        binding.share.setOnClickListener(v -> {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            String shareBody = item.getName() + "\n\n" +
                    item.getTitle() + "\n\n" +
                    "مشاركة من تطبيق مصحفي القرآن الكريم:\nhttps://play.google.com/store/apps/details?id=" + context.getPackageName();
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            context.startActivity(Intent.createChooser(sendIntent, "مشاركة السنة الشريفة:"));
        });

        // Copy Action
        binding.copy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                String copyText = item.getName() + "\n" + item.getTitle();
                ClipData clip = ClipData.newPlainText("SunnahText", copyText);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "تم نسخ النص بنجاح 📋", Toast.LENGTH_SHORT).show();
            }
        });

        container.addView(binding.getRoot());
        return binding.getRoot();
    }
}
