package com.appwalied.quran.adabters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.databinding.QuranicmsgItemBinding;
import com.appwalied.quran.quran.model.QuranMsg;

import java.util.ArrayList;

public class AdapterQuranmsg extends RecyclerView.Adapter<AdapterQuranmsg.MyViewHolder> {

    private final Context context;
    private final ArrayList<QuranMsg> list;

    public AdapterQuranmsg(Context c, ArrayList<QuranMsg> p) {
        this.context = c;
        this.list = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuranicmsgItemBinding binding = QuranicmsgItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        QuranMsg quranMsg = list.get(position);
        if (quranMsg == null || quranMsg.getTitle() == null) return;

        String formattedText = quranMsg.getTitle().replace("_", "\n");
        holder.binding.rsaelItemTextview.setText(formattedText);

        // Copy Action
        holder.binding.qumsgcopy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                String copyContent = formattedText + "\n\n" +
                        "تم النسخ من تطبيق مصحفي القرآن الكريم:\nhttps://play.google.com/store/apps/details?id=" + context.getPackageName();
                ClipData clip = ClipData.newPlainText("QuranicMsg", copyContent);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "تم نسخ النص إلى الحافظة 📋", Toast.LENGTH_SHORT).show();
            }
        });

        // Share Action
        holder.binding.qumsgshare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareBody = formattedText + "\n\n" +
                    "مشاركة من تطبيق مصحفي القرآن الكريم:\nhttps://play.google.com/store/apps/details?id=" + context.getPackageName();
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            context.startActivity(Intent.createChooser(shareIntent, "مشاركة الرسالة القرآنية:"));
        });

        // Rate Action
        holder.binding.qumsgrate.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName()));
            context.startActivity(intent);
        });

        // WhatsApp Action
        holder.binding.qumsgwhats.setOnClickListener(v -> {
            try {
                Intent whatsIntent = new Intent(Intent.ACTION_SEND);
                whatsIntent.setType("text/plain");
                whatsIntent.setPackage("com.whatsapp");
                whatsIntent.putExtra(Intent.EXTRA_TEXT, formattedText + "\n\n" + "مشاركة من تطبيق مصحفي 🌸\nhttps://play.google.com/store/apps/details?id=" + context.getPackageName());
                context.startActivity(whatsIntent);
            } catch (Exception e) {
                Intent openStore = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp"));
                context.startActivity(openStore);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        final QuranicmsgItemBinding binding;

        public MyViewHolder(@NonNull QuranicmsgItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
