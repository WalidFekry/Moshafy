package com.appwalied.quran.image_quran.ui;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.databinding.BottomSheetBookmarksBinding;
import com.appwalied.quran.image_quran.data.ImageQuranBookmarkEntity;
import com.appwalied.quran.image_quran.data.ImageQuranRepository;
import com.appwalied.quran.image_quran.data.Riwaya;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BookmarksBottomSheet extends BottomSheetDialogFragment {

    public interface OnBookmarkSelectedListener {
        void onBookmarkSelected(String riwayaKey, int page);
    }

    private BottomSheetBookmarksBinding binding;
    private OnBookmarkSelectedListener listener;
    private ImageQuranRepository repository;
    private final List<ImageQuranBookmarkEntity> bookmarkList = new ArrayList<>();
    private BookmarkAdapter adapter;

    public static BookmarksBottomSheet newInstance() {
        return new BookmarksBottomSheet();
    }

    public void setOnBookmarkSelectedListener(OnBookmarkSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetBookmarksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repository = ImageQuranRepository.getInstance(requireContext());

        adapter = new BookmarkAdapter();
        binding.rvBookmarks.setAdapter(adapter);

        binding.btnDeleteAllBookmarks.setOnClickListener(v -> {
            if (bookmarkList.isEmpty()) return;
            new AlertDialog.Builder(requireContext())
                    .setTitle("تأكيد الحذف")
                    .setMessage("هل أنت تأكد من رغبتك في حذف جميع العلامات المرجعية المحفوظة؟")
                    .setPositiveButton("حذف الكل", (dialog, which) -> {
                        repository.deleteAllBookmarks(() -> {
                            loadBookmarks();
                            Toast.makeText(requireContext(), "تم حذف جميع العلامات", Toast.LENGTH_SHORT).show();
                        });
                    })
                    .setNegativeButton("إلغاء", null)
                    .show();
        });

        loadBookmarks();
    }

    private void loadBookmarks() {
        repository.getAllBookmarks(list -> {
            bookmarkList.clear();
            if (list != null) {
                bookmarkList.addAll(list);
            }

            if (bookmarkList.isEmpty()) {
                binding.tvEmptyBookmarks.setVisibility(View.VISIBLE);
                binding.rvBookmarks.setVisibility(View.GONE);
            } else {
                binding.tvEmptyBookmarks.setVisibility(View.GONE);
                binding.rvBookmarks.setVisibility(View.VISIBLE);
            }
            adapter.notifyDataSetChanged();
        });
    }

    private class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookmark_row, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ImageQuranBookmarkEntity bookmark = bookmarkList.get(position);
            holder.tvTitle.setText(bookmark.getTitle());

            Riwaya r = Riwaya.fromKey(bookmark.getRiwayaKey());
            String dateStr = DateFormat.format("yyyy/MM/dd hh:mm a", bookmark.getCreatedAt()).toString();
            holder.tvDetails.setText(r.getDisplayName() + " • الجزء " + bookmark.getJuzNumber() + " • " + dateStr);

            holder.btnDelete.setOnClickListener(v -> {
                repository.deleteBookmark(bookmark.getId(), () -> {
                    loadBookmarks();
                    Toast.makeText(requireContext(), "تم حذف العلامة", Toast.LENGTH_SHORT).show();
                });
            });

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBookmarkSelected(bookmark.getRiwayaKey(), bookmark.getPageNumber());
                }
                dismiss();
            });
        }

        @Override
        public int getItemCount() {
            return bookmarkList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView tvTitle, tvDetails;
            final ImageView btnDelete;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_bookmark_title);
                tvDetails = itemView.findViewById(R.id.tv_bookmark_details);
                btnDelete = itemView.findViewById(R.id.btn_delete_bookmark);
            }
        }
    }
}
