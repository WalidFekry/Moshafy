package com.appwalied.quran.qoutes;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.airbnb.lottie.LottieAnimationView;
import com.squareup.picasso.Picasso;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Adaptersada  extends RecyclerView.Adapter<Adaptersada.holder2> {
    List<new_item_post> list2;
    Context context;

    private boolean enableSave;

    public void setEnableSave(boolean enableSave) {
        this.enableSave = enableSave;
    }

    public Adaptersada(List<new_item_post> list2, Context context) {
        this.list2 = list2;
        this.context = context;
    }


    public List<new_item_post> getItems() {
        return list2;
    }

    @NonNull
    @Override
    public holder2 onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view2 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_reslatfoal, viewGroup, false);
        return new holder2(view2);
    }

    @Override
    public void onBindViewHolder(@NonNull final holder2 holder2, final int possition) {
        holder2.title.setText(list2.get(possition).getTitle().replace("_", "\n"));
        holder2.messageNumber.setText(String.valueOf(list2.get(possition).getId()));
        if (list2.get(possition).getCreatedAt() != null) {
            holder2.messageTime.setText(list2.get(possition).getCreatedAt().split(" ")[0]);
        } else {
            String currentTime = "2021-01-05";
            holder2.messageTime.setText(currentTime);
        }
        Picasso.get().load(list2.get(possition).getImageurl()).into(holder2.imagggg);
        holder2.sharequran.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT," تطبيق  (مصحفي) ");
            sendIntent.putExtra(Intent.EXTRA_TEXT,list2.get(possition).getTitle()+ "\n"+
                    "تم النسخ من تطبيق مصحفي 💙 👇 https://t.co/fkNQTMLNxn \n");
            sendIntent.setType("text/plain");
            context.startActivity(Intent.createChooser(sendIntent,"مشاركه نصوص من  تطبيق مصحفي :"));
        });
        holder2.starquran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.co/fkNQTMLNxn")));
            }
        });


        holder2.whatsahdes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, holder2.title.getText().toString());
                try {
                    Objects.requireNonNull(context).startActivity(whatsappIntent);
                } catch (ActivityNotFoundException ex) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp")));
                }
            }
        });


        holder2.copyquran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("", list2.get(possition).getTitle() + "\n" +
                        "\n" + "تم نسخ هذا النص من تطبيق مصحفي ^_^");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "تم نسخ النص - قم بمشاركتة الآن مع أصدقائك", Toast.LENGTH_SHORT).show();

            }
        });
        holder2.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                final MediaPlayer mediaPlayer = MediaPlayer.create(v.getContext(), R.raw.clikkk);
                mediaPlayer.start();
                Resources res = context.getResources();
                final TypedArray myImages = res.obtainTypedArray(R.array.image);
                final Random random = new Random();
                int randomInt = random.nextInt(myImages.length());
                int drawableID = myImages.getResourceId(randomInt, -1);
                holder2.baccc.setBackgroundResource(drawableID);
                holder2.title.setTextColor(Color.WHITE);

            }
        });



    }



    @Override
    public int getItemCount() {
        return list2.size();
    }

    public interface OnMessengerClick {
        void onMessengerItemClick(new_item_post item);
    }

    public static class holder2 extends RecyclerView.ViewHolder {
        MaterialIconView whatsahdes, messenger, copyquran;
        TextView title;
        //        CardView carrd;
        LinearLayout baccc, bodyPicture;
        ImageView imagggg;
        LottieAnimationView starquran, sharequran;

        AppCompatTextView messageNumber;
        AppCompatTextView messageTime;

        LinearLayout time;

        public holder2(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            sharequran = (LottieAnimationView) itemView.findViewById(R.id.sharequran);
            starquran = (LottieAnimationView) itemView.findViewById(R.id.starquran);
            copyquran = (MaterialIconView) itemView.findViewById(R.id.copyquran);
            whatsahdes = (MaterialIconView) itemView.findViewById(R.id.qumsgwhats);
            baccc = (LinearLayout) itemView.findViewById(R.id.baccc);
            imagggg = (ImageView) itemView.findViewById(R.id.imagggg);
            bodyPicture = itemView.findViewById(R.id.body_picture);
            messageNumber = itemView.findViewById(R.id.message_number);
            messageTime = itemView.findViewById(R.id.message_time);
            time = itemView.findViewById(R.id.time);
        }
    }
}