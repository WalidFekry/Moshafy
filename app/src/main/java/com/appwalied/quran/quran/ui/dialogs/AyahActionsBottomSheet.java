package com.appwalied.quran.quran.ui.dialogs;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appwalied.quran.R;
import com.appwalied.quran.quran.db.models.Ayah;
import com.appwalied.quran.quran.room.BookmarkEntity;
import com.appwalied.quran.quran.room.UserLocalRepository;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AyahActionsBottomSheet extends BottomSheetDialogFragment {

    private Ayah ayah;
    private OnAyahActionListener listener;

    public interface OnAyahActionListener {
        void onPlayAyah(Ayah ayah);
        void onBookmarkAdded(Ayah ayah);
    }

    public static AyahActionsBottomSheet newInstance(Ayah ayah, OnAyahActionListener listener) {
        AyahActionsBottomSheet fragment = new AyahActionsBottomSheet();
        fragment.ayah = ayah;
        fragment.listener = listener;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_ayah_actions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (ayah == null) {
            dismiss();
            return;
        }

        TextView tvTitle = view.findViewById(R.id.tv_dialog_ayah_title);
        TextView tvPreview = view.findViewById(R.id.tv_dialog_ayah_preview);

        View actionPlay = view.findViewById(R.id.action_play_ayah);
        View actionCopy = view.findViewById(R.id.action_copy_ayah);
        View actionShare = view.findViewById(R.id.action_share_ayah);
        View actionShareWithInfo = view.findViewById(R.id.action_share_with_info);
        View actionAddBookmark = view.findViewById(R.id.action_add_bookmark);

        tvTitle.setText("سورة " + (ayah.getSuraName() != null ? ayah.getSuraName() : "") + " - آية " + ayah.getVerseId());
        tvPreview.setText(ayah.getArabicUthmanic());

        actionPlay.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlayAyah(ayah);
            }
            dismiss();
        });

        actionCopy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Quran Ayah", ayah.getArabicUthmanic());
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "تم نسخ الآية الكريمة بنجاح", Toast.LENGTH_SHORT).show();
            }
            dismiss();
        });

        actionShare.setOnClickListener(v -> {
            shareText(ayah.getArabicUthmanic());
            dismiss();
        });

        actionShareWithInfo.setOnClickListener(v -> {
            String fullShareText = "﴿" + ayah.getArabicUthmanic() + "﴾\n[" + "سورة " + ayah.getSuraName() + " - آية " + ayah.getVerseId() + "]";
            shareText(fullShareText);
            dismiss();
        });

        actionAddBookmark.setOnClickListener(v -> {
            String bookmarkName = "علامة - سورة " + ayah.getSuraName() + " (" + ayah.getVerseId() + ")";
            BookmarkEntity entity = new BookmarkEntity(
                    ayah.getSuraId(),
                    ayah.getVerseId(),
                    ayah.getPageNumber(),
                    ayah.getParaNumber(),
                    bookmarkName,
                    System.currentTimeMillis()
            );

            UserLocalRepository.getInstance(requireContext()).addBookmark(entity, id -> {
                Toast.makeText(getContext(), "تم حفظ العلامة المرجعية بنجاح", Toast.LENGTH_SHORT).show();
                if (listener != null) {
                    listener.onBookmarkAdded(ayah);
                }
            });
            dismiss();
        });
    }

    private void shareText(String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "مشاركة الآية"));
    }
}
