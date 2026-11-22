package com.appwalied.quran.image_quran.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.image_quran.data.ImageQuranConfig;
import com.appwalied.quran.image_quran.data.Riwaya;
import com.appwalied.quran.image_quran.download.ImageQuranDownloadManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Executors;

public class ImageQuranPagerAdapter extends RecyclerView.Adapter<ImageQuranPagerAdapter.PageViewHolder> {

    public interface OnPageClickListener {
        void onPageSingleTap(int page);
    }

    private final Context context;
    private Riwaya riwaya;
    private OnPageClickListener clickListener;

    public ImageQuranPagerAdapter(Context context, Riwaya riwaya) {
        this.context = context;
        this.riwaya = riwaya;
    }

    public void setRiwaya(Riwaya riwaya) {
        this.riwaya = riwaya;
        notifyDataSetChanged();
    }

    public void setOnPageClickListener(OnPageClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_quran_page, parent, false);
        return new PageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        int page = ImageQuranConfig.positionToPage(position);
        holder.bind(page, riwaya);
    }

    @Override
    public int getItemCount() {
        return ImageQuranConfig.TOTAL_PAGES;
    }

    class PageViewHolder extends RecyclerView.ViewHolder {
        final ZoomableImageView ivPage;
        final ProgressBar progressBar;
        final TextView tvError;

        PageViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPage = itemView.findViewById(R.id.iv_quran_page);
            progressBar = itemView.findViewById(R.id.progress_page_loading);
            tvError = itemView.findViewById(R.id.tv_page_error);

            ivPage.setOnSingleTapListener(() -> {
                int adapterPos = getBindingAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION && clickListener != null) {
                    clickListener.onPageSingleTap(ImageQuranConfig.positionToPage(adapterPos));
                }
            });
        }

        void bind(int page, Riwaya currentRiwaya) {
            ivPage.resetZoom();
            tvError.setVisibility(View.GONE);

            File localFile = ImageQuranConfig.getLocalImageFile(context, currentRiwaya, page);
            if (localFile.exists() && localFile.length() > 0) {
                progressBar.setVisibility(View.GONE);
                Picasso.get()
                        .load(localFile)
                        .into(ivPage);
            } else {
                progressBar.setVisibility(View.VISIBLE);
                String url = ImageQuranConfig.getImageUrl(currentRiwaya, page);

                Picasso.get()
                        .load(url)
                        .into(ivPage, new Callback() {
                            @Override
                            public void onSuccess() {
                                progressBar.setVisibility(View.GONE);

                                // Save downloaded image locally for offline reuse
                                if (ivPage.getDrawable() instanceof BitmapDrawable) {
                                    Bitmap bitmap = ((BitmapDrawable) ivPage.getDrawable()).getBitmap();
                                    if (bitmap != null) {
                                        Executors.newSingleThreadExecutor().execute(() -> {
                                            try (FileOutputStream fos = new FileOutputStream(localFile)) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 95, fos);
                                                fos.flush();
                                            } catch (Exception ignored) {
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                progressBar.setVisibility(View.GONE);
                                tvError.setVisibility(View.VISIBLE);
                            }
                        });
            }
        }
    }
}
