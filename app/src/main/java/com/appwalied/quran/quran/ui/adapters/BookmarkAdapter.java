package com.appwalied.quran.quran.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.quran.room.BookmarkEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder> {

    private List<BookmarkEntity> bookmarkList = new ArrayList<>();
    private OnBookmarkClickListener clickListener;
    private OnDeleteBookmarkClickListener deleteListener;

    public interface OnBookmarkClickListener {
        void onBookmarkClick(BookmarkEntity bookmark);
    }

    public interface OnDeleteBookmarkClickListener {
        void onDeleteBookmark(BookmarkEntity bookmark);
    }

    public BookmarkAdapter(OnBookmarkClickListener clickListener, OnDeleteBookmarkClickListener deleteListener) {
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
    }

    public void setBookmarks(List<BookmarkEntity> bookmarks) {
        this.bookmarkList = bookmarks != null ? bookmarks : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookmark, parent, false);
        return new BookmarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkViewHolder holder, int position) {
        BookmarkEntity bookmark = bookmarkList.get(position);
        holder.tvName.setText(bookmark.getBookmarkName());
        holder.tvDetails.setText("آية " + bookmark.getAyahId() + " (صفحة " + bookmark.getPageNumber() + " • جزء " + bookmark.getParaNumber() + ")");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        String dateStr = sdf.format(new Date(bookmark.getCreatedAt()));
        holder.tvDate.setText("تاريخ الحفظ: " + dateStr);

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onBookmarkClick(bookmark);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteBookmark(bookmark);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookmarkList.size();
    }

    static class BookmarkViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDetails, tvDate;
        ImageButton btnDelete;

        public BookmarkViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_bookmark_name);
            tvDetails = itemView.findViewById(R.id.tv_bookmark_details);
            tvDate = itemView.findViewById(R.id.tv_bookmark_date);
            btnDelete = itemView.findViewById(R.id.btn_delete_bookmark);
        }
    }
}
