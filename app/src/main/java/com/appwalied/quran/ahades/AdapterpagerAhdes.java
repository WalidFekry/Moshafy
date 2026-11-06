package com.appwalied.quran.ahades;

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

import com.appwalied.quran.databinding.ItemPagerahdesBinding;

import java.util.List;
import java.util.Locale;

public class AdapterpagerAhdes extends PagerAdapter {

    private final List<item_ahdes> listahdes;
    private final Context context;

    public AdapterpagerAhdes(List<item_ahdes> listahdes, Context context) {
        this.listahdes = listahdes;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listahdes != null ? listahdes.size() : 0;
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
        ItemPagerahdesBinding binding = ItemPagerahdesBinding.inflate(
                LayoutInflater.from(context), container, false
        );

        item_ahdes item = listahdes.get(position);

        binding.name.setText(item.getName());
        binding.title.setText(item.getTitle());
        String numberText = String.format(Locale.getDefault(), "حديث رقم %d", position + 1);
        binding.number.setText(numberText);

        // Share Action
        binding.shares.setOnClickListener(v -> {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            String shareBody = item.getName() + "\n\n" +
                    item.getTitle() + "\n\n" +
                    "مشاركة من تطبيق مصحفي القرآن الكريم:\nhttps://play.google.com/store/apps/details?id=" + context.getPackageName();
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            context.startActivity(Intent.createChooser(sendIntent, "مشاركة الحديث الشريف:"));
        });

        // Copy Action
        binding.copys.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                String copyText = item.getName() + "\n" + item.getTitle();
                ClipData clip = ClipData.newPlainText("HadithText", copyText);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "تم نسخ الحديث الشريف بنجاح 📋", Toast.LENGTH_SHORT).show();
            }
        });

        container.addView(binding.getRoot());
        return binding.getRoot();
    }
}
