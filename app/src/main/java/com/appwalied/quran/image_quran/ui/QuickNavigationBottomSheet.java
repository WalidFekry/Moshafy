package com.appwalied.quran.image_quran.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appwalied.quran.databinding.BottomSheetQuickNavigationBinding;
import com.appwalied.quran.image_quran.data.ImageQuranConfig;
import com.appwalied.quran.image_quran.data.Riwaya;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class QuickNavigationBottomSheet extends BottomSheetDialogFragment {

    public interface NavigationListener {
        void onJumpToPage(int page);
        void onSwitchRiwaya(Riwaya riwaya);
        void onOpenVisualIndex();
        void onOpenSurahIndex();
        void onOpenJuzIndex();
        void onOpenBookmarks();
    }

    private static final String ARG_RIWAYA = "arg_riwaya";
    private static final String ARG_CURRENT_PAGE = "arg_current_page";

    private BottomSheetQuickNavigationBinding binding;
    private Riwaya currentRiwaya = Riwaya.HAFS;
    private int currentPage = 1;
    private NavigationListener listener;

    public static QuickNavigationBottomSheet newInstance(Riwaya riwaya, int currentPage) {
        QuickNavigationBottomSheet fragment = new QuickNavigationBottomSheet();
        Bundle args = new Bundle();
        args.putString(ARG_RIWAYA, riwaya.getKey());
        args.putInt(ARG_CURRENT_PAGE, currentPage);
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnNavigationListener(NavigationListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetQuickNavigationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            currentRiwaya = Riwaya.fromKey(getArguments().getString(ARG_RIWAYA));
            currentPage = getArguments().getInt(ARG_CURRENT_PAGE, 1);
        }

        binding.etJumpPageNum.setHint("الصفحة الحالية: " + currentPage + " (1 - 604)");

        binding.btnConfirmJump.setOnClickListener(v -> {
            String input = binding.etJumpPageNum.getText().toString().trim();
            if (TextUtils.isEmpty(input)) {
                Toast.makeText(requireContext(), "يرجى كتابة رقم الصفحة", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int page = Integer.parseInt(input);
                if (page < 1 || page > ImageQuranConfig.TOTAL_PAGES) {
                    Toast.makeText(requireContext(), "رقم الصفحة يجب أن يكون بين 1 و 604", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (listener != null) {
                    listener.onJumpToPage(page);
                }
                dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "رقم صفحة غير صالح", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnQuickVisual.setOnClickListener(v -> {
            if (listener != null) listener.onOpenVisualIndex();
            dismiss();
        });

        binding.btnQuickSurah.setOnClickListener(v -> {
            if (listener != null) listener.onOpenSurahIndex();
            dismiss();
        });

        binding.btnQuickJuz.setOnClickListener(v -> {
            if (listener != null) listener.onOpenJuzIndex();
            dismiss();
        });

        binding.btnQuickBookmarks.setOnClickListener(v -> {
            if (listener != null) listener.onOpenBookmarks();
            dismiss();
        });

        binding.btnQuickSwitchRiwaya.setOnClickListener(v -> {
            Riwaya otherRiwaya = (currentRiwaya == Riwaya.HAFS) ? Riwaya.WARSH : Riwaya.HAFS;
            if (listener != null) listener.onSwitchRiwaya(otherRiwaya);
            dismiss();
        });
    }
}
