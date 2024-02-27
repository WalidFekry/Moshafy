package com.appwalied.quran.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public abstract class BaseDialog extends DialogFragment implements com.appwalied.quran.base.IBaseDialog {


    private BaseActivity baseNewActivity;

    public BaseActivity getBaseActivity() {
        return baseNewActivity;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            this.baseNewActivity = (BaseActivity) context;
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getContext());
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }


    public void show(FragmentManager fragmentManager, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment prevFragment = fragmentManager.findFragmentByTag(tag);
        if (prevFragment != null) {
            transaction.remove(prevFragment);
        }
        transaction.addToBackStack(tag);
        show(transaction, tag);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    protected abstract void setUpDialogViews();

    @Override
    public void dismissDialog(String tag) {
        dismiss();
//        getBaseActivity().onFragmentDetached(tag);
    }


    @Override
    public void showLoading() {
        if (baseNewActivity != null) {
            baseNewActivity.showLoading();
        }
    }

    @Override
    public void showMessage(int resId) {
        if (baseNewActivity != null) {
            baseNewActivity.showMessage(resId);
        }
    }

    @Override
    public void showMessage(String message) {
        if (baseNewActivity != null) {
            baseNewActivity.showMessage(message);
        }
    }

    @Override
    public void hideLoading() {
        if (baseNewActivity != null) {
            baseNewActivity.hideLoading();
        }
    }

    @Override
    public void hideKeyboard() {
        if (baseNewActivity != null) {
            baseNewActivity.hideKeyboard();
        }
    }
}
