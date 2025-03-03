package com.appwalied.quran.base;

import android.widget.FrameLayout;

import androidx.annotation.StringRes;

public interface IBaseView {

    void showLoading();

    void showMessage(@StringRes int resId);

    void showMessage(String message);

    void hideLoading();

    void promptUserForRating();

    void saveLastPromptData(int newCount);

    void setUpAds();
    void showBanner(FrameLayout adsContainer);
}
