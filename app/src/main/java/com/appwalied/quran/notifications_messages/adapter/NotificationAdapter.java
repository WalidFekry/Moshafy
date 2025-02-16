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

import com.airbnb.lottie.LottieAnimationView;
import com.appwalied.quran.R;
import com.appwalied.quran.notifications_messages.model.NotificationModel;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final List<NotificationModel> list;

    public NotificationAdapter(List<NotificationModel> list) {
        this.list = list;
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

        holder.messageTime.setText(notification.getCreatedAt() != null ?
                notification.getCreatedAt().split(" ")[0] : "2025-02-14");

        setClickListener(holder.shareButton, v -> shareNotification(itemContext, notification));
        setClickListener(holder.starButton, v -> openLink(itemContext, "https://t.co/fkNQTMLNxn"));
        setClickListener(holder.whatsappButton, v -> shareOnWhatsApp(itemContext, notification.getTitle()));
        setClickListener(holder.copyButton, v -> copyToClipboard(itemContext, notification.getTitle()));
        setClickListener(holder.facebookButton, v -> shareOnFacebook(itemContext, notification.getTitle()));
    }

    private void setClickListener(View view, View.OnClickListener listener) {
        view.setOnClickListener(listener);
    }

    private void shareOnFacebook(Context context, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setPackage("com.facebook.katana");
        intent.putExtra(Intent.EXTRA_TEXT, text + "\n" + "تم نسخ هذا النص من تطبيق مُصحفي ^_^");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "تطبيق فيسبوك غير مثبت!", Toast.LENGTH_LONG).show();
        }
    }


    private void shareNotification(Context context, NotificationModel notification) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "تطبيق (مُصحفي)");
        sendIntent.putExtra(Intent.EXTRA_TEXT, notification.getTitle() + "\n" +
                "تم النسخ من تطبيق المٌحقظ 💙 👇 https://t.co/fkNQTMLNxn \n");
        context.startActivity(Intent.createChooser(sendIntent, "مشاركة نصوص من تطبيق مُصحفي:"));
    }

    private void openLink(Context context, String url) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    private void shareOnWhatsApp(Context context, String text) {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, text + "\n" +
                "تم النسخ من تطبيق المٌحقظ 💙 👇 https://t.co/fkNQTMLNxn \n");
        try {
            context.startActivity(whatsappIntent);
        } catch (ActivityNotFoundException ex) {
            openLink(context, "https://play.google.com/store/apps/details?id=com.whatsapp");
        }
    }

    private void copyToClipboard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", text + "\n" + "تم نسخ هذا النص من تطبيق مُصحفي ^_^");
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "تم نسخ النص - قم بمشاركته الآن مع أصدقائك", Toast.LENGTH_LONG).show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        MaterialIconView whatsappButton, copyButton, facebookButton;
        TextView title;
        LottieAnimationView starButton, shareButton;
        AppCompatTextView messageNumber, messageTime;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            shareButton = itemView.findViewById(R.id.sharequran);
            starButton = itemView.findViewById(R.id.starquran);
            copyButton = itemView.findViewById(R.id.copyquran);
            whatsappButton = itemView.findViewById(R.id.qumsgwhats);
            messageNumber = itemView.findViewById(R.id.message_number);
            messageTime = itemView.findViewById(R.id.message_time);
            facebookButton = itemView.findViewById(R.id.facebook_share);
        }
    }
}
