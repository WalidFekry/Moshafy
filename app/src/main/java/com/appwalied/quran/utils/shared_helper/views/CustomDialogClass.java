package com.appwalied.quran.utils.shared_helper.views;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.appwalied.quran.R;

import static android.view.View.GONE;

import java.util.Objects;

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
        Objects.requireNonNull(this.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void initDialog() {

        if (options.message.isEmpty()) {
            message.setVisibility(GONE);
        }

        if (!options.title.isEmpty()) {
            title.setText(options.title);
        } else {
            title.setVisibility(GONE);
        }

        if (!options.message.isEmpty()) {
            message.setText(options.message);
        } else {
            message.setVisibility(GONE);
        }

    }

    public static class Options {
        public String title = "";
        public String message = "";
    }

}