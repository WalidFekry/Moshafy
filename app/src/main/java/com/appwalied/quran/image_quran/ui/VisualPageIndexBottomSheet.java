package com.appwalied.quran.image_quran.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.databinding.BottomSheetVisualPageIndexBinding;
import com.appwalied.quran.image_quran.data.ImageQuranConfig;
import com.appwalied.quran.image_quran.data.Riwaya;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

import java.io.File;

public class VisualPageIndexBottomSheet extends BottomSheetDialogFragment {

    public interface OnPageSelectedListener {
        void onPageSelected(int page);
    }

    private static final String ARG_RIWAYA = "arg_riwaya";
    private static final String ARG_CURRENT_PAGE = "arg_current_page";

    private BottomSheetVisualPageIndexBinding binding;
    private Riwaya riwaya = Riwaya.HAFS;
    private int currentPage = 1;
    private OnPageSelectedListener listener;

    public static VisualPageIndexBottomSheet newInstance(Riwaya riwaya, int currentPage) {
        VisualPageIndexBottomSheet fragment = new VisualPageIndexBottomSheet();
        Bundle args = new Bundle();
        args.putString(ARG_RIWAYA, riwaya.getKey());
        args.putInt(ARG_CURRENT_PAGE, currentPage);
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnPageSelectedListener(OnPageSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetVisualPageIndexBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            riwaya = Riwaya.fromKey(getArguments().getString(ARG_RIWAYA));
            currentPage = getArguments().getInt(ARG_CURRENT_PAGE, 1);
        }

        ThumbnailAdapter adapter = new ThumbnailAdapter();
        binding.rvVisualIndex.setAdapter(adapter);
        binding.rvVisualIndex.scrollToPosition(Math.max(0, currentPage - 1));
    }

    private class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_visual_page_thumbnail, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            int page = position + 1;
            holder.tvPage.setText("صفحة " + page);

            if (page == currentPage) {
                holder.cardThumbnail.setCardBackgroundColor(requireContext().getColor(R.color.colorPrimary));
                holder.tvPage.setTextColor(requireContext().getColor(R.color.white));
            } else {
                holder.cardThumbnail.setCardBackgroundColor(requireContext().getColor(R.color.card_bg));
                holder.tvPage.setTextColor(requireContext().getColor(R.color.text_primary));
            }

            File localFile = ImageQuranConfig.getLocalImageFile(requireContext(), riwaya, page);
            if (localFile.exists() && localFile.length() > 0) {
                Picasso.get().load(localFile).resize(180, 240).centerCrop().into(holder.ivThumbnail);
            } else {
                Picasso.get().load(ImageQuranConfig.getImageUrl(riwaya, page)).resize(180, 240).centerCrop().into(holder.ivThumbnail);
            }

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPageSelected(page);
                }
                dismiss();
            });
        }

        @Override
        public int getItemCount() {
            return ImageQuranConfig.TOTAL_PAGES;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final CardView cardThumbnail;
            final ImageView ivThumbnail;
            final TextView tvPage;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                cardThumbnail = itemView.findViewById(R.id.card_thumbnail);
                ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
                tvPage = itemView.findViewById(R.id.tv_thumbnail_page);
            }
        }
    }
}
