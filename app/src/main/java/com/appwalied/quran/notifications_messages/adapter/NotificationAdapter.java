package com.appwalied.quran.notifications_messages.adapter;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.notifications_messages.model.NotificationModel;
import com.appwalied.quran.notifications_messages.utility.QuoteImageHelper;
import com.appwalied.quran.notifications_messages.utility.SavedQuotesManager;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final List<NotificationModel> list;
    private OnSavedStateChangedListener onSavedStateChangedListener;

    public interface OnSavedStateChangedListener {
        void onSavedStateChanged();
    }

    public NotificationAdapter(List<NotificationModel> list) {
        this.list = list;
    }

    public void setOnSavedStateChangedListener(OnSavedStateChangedListener listener) {
        this.onSavedStateChangedListener = listener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_message, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationViewHolder holder, int position) {
        NotificationModel notification = list.get(position);
        Context itemContext = holder.itemView.getContext();

        holder.title.setText(notification.getTitle().replace("_", "\n"));
        holder.messageNumber.setText(String.valueOf(notification.getId()));

        String dateStr = notification.getCreatedAt() != null ? notification.getCreatedAt().split(" ")[0] : "";
        holder.messageTime.setText(!dateStr.isEmpty() ? dateStr : "اليوم");

        boolean isSaved = SavedQuotesManager.isQuoteSaved(itemContext, notification.getId());
        holder.starButton.setIcon(isSaved ? MaterialDrawableBuilder.IconValue.BOOKMARK : MaterialDrawableBuilder.IconValue.BOOKMARK_OUTLINE);

        holder.starButton.setOnClickListener(v -> {
            boolean newlySaved = SavedQuotesManager.toggleSaveQuote(itemContext, notification);
            holder.starButton.setIcon(newlySaved ? MaterialDrawableBuilder.IconValue.BOOKMARK : MaterialDrawableBuilder.IconValue.BOOKMARK_OUTLINE);
            Toast.makeText(itemContext, newlySaved ? "تم الحفظ في المحفوظات 🔖" : "تم الحذف من المحفوظات", Toast.LENGTH_SHORT).show();
            if (onSavedStateChangedListener != null) {
                onSavedStateChangedListener.onSavedStateChanged();
            }
        });

        holder.shareImageButton.setOnClickListener(v ->
                QuoteImageHelper.shareQuoteAsImage(itemContext, notification.getTitle(), dateStr)
        );

        holder.downloadImageButton.setOnClickListener(v ->
                QuoteImageHelper.saveQuoteImageToGallery(itemContext, notification.getTitle(), dateStr)
        );

        holder.whatsappButton.setOnClickListener(v -> shareOnWhatsApp(itemContext, notification.getTitle()));
        holder.copyButton.setOnClickListener(v -> copyToClipboard(itemContext, notification.getTitle()));
        holder.facebookButton.setOnClickListener(v -> shareOnFacebook(itemContext, notification.getTitle()));
    }

    private void shareOnFacebook(Context context, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setPackage("com.facebook.katana");
        intent.putExtra(Intent.EXTRA_TEXT, text + "\n\n" + "تم النسخ من تطبيق مُصحفي 🌸");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "تطبيق فيسبوك غير مثبت!", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareOnWhatsApp(Context context, String text) {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, text + "\n\n" + "تم النسخ من تطبيق مُصحفي 🌸\nhttps://play.google.com/store/apps/details?id=com.appwalied.quran");
        try {
            context.startActivity(whatsappIntent);
        } catch (ActivityNotFoundException ex) {
            openLink(context, "https://play.google.com/store/apps/details?id=com.whatsapp");
        }
    }

    private void copyToClipboard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText("Moshafy Quote", text + "\n\n" + "تم النسخ من تطبيق مُصحفي 🌸");
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "تم نسخ النص إلى الحافظة 📋", Toast.LENGTH_SHORT).show();
        }
    }

    private void openLink(Context context, String url) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        MaterialIconView starButton, shareImageButton, downloadImageButton, whatsappButton, copyButton, facebookButton;
        TextView title;
        AppCompatTextView messageNumber, messageTime;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            starButton = itemView.findViewById(R.id.starquran);
            shareImageButton = itemView.findViewById(R.id.share_image);
            downloadImageButton = itemView.findViewById(R.id.download_image);
            copyButton = itemView.findViewById(R.id.copyquran);
            whatsappButton = itemView.findViewById(R.id.qumsgwhats);
            messageNumber = itemView.findViewById(R.id.message_number);
            messageTime = itemView.findViewById(R.id.message_time);
            facebookButton = itemView.findViewById(R.id.facebook_share);
        }
    }
}
