package com.appwalied.quran.features.views;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class MySpannable extends ClickableSpan {

    private boolean isUnderline = false;

    /**
     * Constructor
     */
    public MySpannable(boolean isUnderline) {
        this.isUnderline = isUnderline;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(isUnderline);
        ds.setColor(Color.parseColor("#3498DB"));

    }

    @Override
    public void onClick(View widget) {

    }
}