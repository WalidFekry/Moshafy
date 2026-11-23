package com.appwalied.quran.tadabbur.ui.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.quran.utils.SajdahHelper;
import com.appwalied.quran.tadabbur.db.room.TadabburBookmarkEntity;
import com.appwalied.quran.tadabbur.db.room.TadabburLocalRepository;
import com.appwalied.quran.tadabbur.model.QuranV2Ayah;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TadabburAyahAdapter extends RecyclerView.Adapter<TadabburAyahAdapter.ViewHolder> {

    public interface OnAyahActionListener {
        void onOpenDetails(QuranV2Ayah ayah);
    }

    private final List<QuranV2Ayah> ayahList = new ArrayList<>();
    private final Set<String> bookmarkedAyahs = new HashSet<>();
    private final OnAyahActionListener listener;
    private float textSizeSp = 22f;

    public TadabburAyahAdapter(OnAyahActionListener listener) {
        this.listener = listener;
    }

    public void setAyahs(List<QuranV2Ayah> list) {
        this.ayahList.clear();
        if (list != null) {
            this.ayahList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void setBookmarkedAyahs(Set<String> bookmarkedKeys) {
        this.bookmarkedAyahs.clear();
        if (bookmarkedKeys != null) {
            this.bookmarkedAyahs.addAll(bookmarkedKeys);
        }
        notifyDataSetChanged();
    }

    public void setTextSizeSp(float sizeSp) {
        this.textSizeSp = sizeSp;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tadabbur_ayah, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuranV2Ayah ayah = ayahList.get(position);
        Context context = holder.itemView.getContext();

        // Set Ayah Text and Size
        holder.tvAyahText.setText(ayah.getAyaText());
        holder.tvAyahText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeSp);

        // Info Badge
        String badge = "آية " + ayah.getAyaNo() + " • ص " + ayah.getPage();
        holder.tvAyahBadge.setText(badge);

        // Sajdah Badge
        boolean isSajdah = SajdahHelper.isSajdahAyah(ayah.getSora(), ayah.getAyaNo());
        if (isSajdah) {
            holder.tvSajdahBadge.setVisibility(View.VISIBLE);
            holder.tvSajdahBadge.setOnClickListener(v ->
                    Toast.makeText(context, "موضع سجدة تلاوة", Toast.LENGTH_SHORT).show()
            );
        } else {
            holder.tvSajdahBadge.setVisibility(View.GONE);
        }

        // Bookmark state
        String key = ayah.getSora() + "_" + ayah.getAyaNo();
        boolean isBookmarked = bookmarkedAyahs.contains(key);
        if (isBookmarked) {
            holder.btnBookmark.setImageResource(R.drawable.ic_favorite);
        } else {
            holder.btnBookmark.setImageResource(R.drawable.ic_favorite_border);
        }

        // Click Listeners
        holder.cardAyah.setOnClickListener(v -> {
            if (listener != null) listener.onOpenDetails(ayah);
        });

        holder.btnOpenDetails.setOnClickListener(v -> {
            if (listener != null) listener.onOpenDetails(ayah);
        });

        holder.btnBookmark.setOnClickListener(v -> {
            TadabburBookmarkEntity entity = new TadabburBookmarkEntity(
                    ayah.getSora(),
                    ayah.getAyaNo(),
                    ayah.getPage(),
                    ayah.getJozz(),
                    ayah.getSoraNameAr(),
                    ayah.getAyaText(),
                    System.currentTimeMillis()
            );
            TadabburLocalRepository.getInstance(context).toggleBookmark(entity, isNowBookmarked -> {
                if (isNowBookmarked) {
                    bookmarkedAyahs.add(key);
                    Toast.makeText(context, "تمت إضافة الآية للمفضلة 🌸", Toast.LENGTH_SHORT).show();
                } else {
                    bookmarkedAyahs.remove(key);
                    Toast.makeText(context, "تمت إزالة الآية من المفضلة", Toast.LENGTH_SHORT).show();
                }
                notifyItemChanged(holder.getAdapterPosition());
            });
        });

        holder.btnCopy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                String copyText = ayah.getAyaText() + " [" + ayah.getSoraNameAr() + ": " + ayah.getAyaNo() + "]";
                ClipData clip = ClipData.newPlainText("Ayah Text", copyText);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "تم نسخ الآية الشريفة بنجاح 📋", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnShare.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String shareBody = "﴿ " + ayah.getAyaText() + " ﴾\n\n" +
                    "سورة " + ayah.getSoraNameAr() + " - الآية " + ayah.getAyaNo() + "\n" +
                    "مشارك من تطبيق مُصحفي 🌸";
            intent.putExtra(Intent.EXTRA_TEXT, shareBody);
            context.startActivity(Intent.createChooser(intent, "مشاركة الآية الكريمة"));
        });
    }

    @Override
    public int getItemCount() {
        return ayahList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View cardAyah;
        TextView tvAyahText, tvAyahBadge, tvSajdahBadge;
        MaterialButton btnOpenDetails;
        ImageView btnBookmark, btnCopy, btnShare;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardAyah = itemView.findViewById(R.id.card_ayah);
            tvAyahText = itemView.findViewById(R.id.tv_ayah_text);
            tvAyahBadge = itemView.findViewById(R.id.tv_ayah_info_badge);
            tvSajdahBadge = itemView.findViewById(R.id.tv_sajdah_badge);
            btnOpenDetails = itemView.findViewById(R.id.btn_open_details);
            btnBookmark = itemView.findViewById(R.id.btn_bookmark);
            btnCopy = itemView.findViewById(R.id.btn_copy);
            btnShare = itemView.findViewById(R.id.btn_share);
        }
    }
}
