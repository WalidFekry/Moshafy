package com.appwalied.quran.features.views;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.appwalied.quran.R;

import static android.view.View.GONE;

public class CustomDialogClass extends Dialog {

    public Activity c;
    public Dialog d;
    public TextView yes, title, message;
    public Options options;

    public CustomDialogClass(Activity a, Options options) {
        super(a);
        this.c = a;
        this.options = options;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_success_place_order_dialog);
        yes = findViewById(R.id.yesBtn);
        title = findViewById(R.id.title_custom_dialog);
        message = findViewById(R.id.message_custom_dialog);
        yes.setOnClickListener(view -> dismiss());
        initDialog();
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void initDialog() {

        if (options.message.equals("")) {
            message.setVisibility(GONE);
        }

        if (!options.title.equals("")) {
            title.setText(options.title);
        } else {
            title.setVisibility(GONE);
        }

        if (!options.message.equals("")) {
            message.setText(options.message);
        } else {
            message.setVisibility(GONE);
        }

    }

    public static class Options {
        public String yesString = "";
        public String noString = "";
        public String title = "";
        public String message = "";

        @DrawableRes
        public int imageID = 0;
    }

}