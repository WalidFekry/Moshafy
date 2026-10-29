package com.appwalied.quran.notifications_messages.utility;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.appwalied.quran.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class QuoteImageHelper {

    public static Bitmap generateQuoteBitmap(Context context, String quoteText, String dateText) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_quote_card_image, null, false);
        TextView tvText = view.findViewById(R.id.img_quote_text);
        TextView tvDate = view.findViewById(R.id.img_quote_date);

        if (tvText != null) {
            tvText.setText(quoteText.replace("_", "\n"));
        }
        if (tvDate != null && dateText != null && !dateText.trim().isEmpty()) {
            tvDate.setText("اقتباس يوم " + dateText);
        }

        int widthSpec = View.MeasureSpec.makeMeasureSpec(800, View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthSpec, heightSpec);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static void shareQuoteAsImage(Context context, String quoteText, String dateText) {
        try {
            Bitmap bitmap = generateQuoteBitmap(context, quoteText, dateText);
            File imagesFolder = new File(context.getCacheDir(), "images");
            if (!imagesFolder.exists()) {
                imagesFolder.mkdirs();
            }
            File imageFile = new File(imagesFolder, "quote_share_" + System.currentTimeMillis() + ".png");
            FileOutputStream stream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();

            Uri contentUri = FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".fileprovider",
                    imageFile
            );

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "مشاركة من تطبيق مُصحفي 🌸\nhttps://play.google.com/store/apps/details?id=com.appwalied.quran");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            context.startActivity(Intent.createChooser(shareIntent, "مشاركة الاقتباس كصورة"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "حدث خطأ أثناء إعداد الصورة للمشاركة", Toast.LENGTH_SHORT).show();
        }
    }

    public static void saveQuoteImageToGallery(Context context, String quoteText, String dateText) {
        try {
            Bitmap bitmap = generateQuoteBitmap(context, quoteText, dateText);
            String filename = "Moshafy_Quote_" + System.currentTimeMillis() + ".png";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Moshafy");
                values.put(MediaStore.Images.Media.IS_PENDING, 1);

                ContentResolver resolver = context.getContentResolver();
                Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                if (uri != null) {
                    OutputStream out = resolver.openOutputStream(uri);
                    if (out != null) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.close();
                    }
                    values.clear();
                    values.put(MediaStore.Images.Media.IS_PENDING, 0);
                    resolver.update(uri, values, null, null);
                    Toast.makeText(context, "تم حفظ الصورة بنجاح في المعرض 🖼️", Toast.LENGTH_LONG).show();
                }
            } else {
                File picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File moshafyDirectory = new File(picturesDirectory, "Moshafy");
                if (!moshafyDirectory.exists()) {
                    moshafyDirectory.mkdirs();
                }
                File imageFile = new File(moshafyDirectory, filename);
                FileOutputStream out = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();

                MediaStore.Images.Media.insertImage(
                        context.getContentResolver(),
                        imageFile.getAbsolutePath(),
                        imageFile.getName(),
                        "Quote from Moshafy app"
                );
                Toast.makeText(context, "تم حفظ الصورة بنجاح في المعرض 🖼️", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "فشل حفظ الصورة، يرجى المحاولة لاحقاً", Toast.LENGTH_SHORT).show();
        }
    }
}
