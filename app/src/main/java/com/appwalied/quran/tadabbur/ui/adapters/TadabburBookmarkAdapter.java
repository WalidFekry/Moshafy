package com.appwalied.quran.tadabbur.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.tadabbur.db.room.TadabburBookmarkEntity;

import java.util.ArrayList;
import java.util.List;

public class TadabburBookmarkAdapter extends RecyclerView.Adapter<TadabburBookmarkAdapter.ViewHolder> {

    public interface OnBookmarkClickListener {
        void onBookmarkClick(TadabburBookmarkEntity bookmark);
        void onDeleteClick(TadabburBookmarkEntity bookmark);
    }

    private final List<TadabburBookmarkEntity> bookmarks = new ArrayList<>();
    private final OnBookmarkClickListener listener;

    public TadabburBookmarkAdapter(OnBookmarkClickListener listener) {
        this.listener = listener;
    }

    public void setBookmarks(List<TadabburBookmarkEntity> list) {
        this.bookmarks.clear();
        if (list != null) {
            this.bookmarks.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tadabbur_bookmark, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TadabburBookmarkEntity bookmark = bookmarks.get(position);
        holder.tvSurahInfo.setText("سورة " + bookmark.getSoraNameAr() + " - الآية " + bookmark.getAyaNo() + " (ص " + bookmark.getPage() + ")");
        holder.tvText.setText(bookmark.getAyaText());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onBookmarkClick(bookmark);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(bookmark);
        });
    }

    @Override
    public int getItemCount() {
        return bookmarks.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSurahInfo, tvText;
        ImageView btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSurahInfo = itemView.findViewById(R.id.tv_bookmark_surah_info);
            tvText = itemView.findViewById(R.id.tv_bookmark_text);
            btnDelete = itemView.findViewById(R.id.btn_delete_bookmark);
        }
    }
}
