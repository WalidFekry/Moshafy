package com.appwalied.quran.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.appwalied.quran.R;


import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseNewActivity extends AppCompatActivity implements IBaseView {


    private Dialog loadingDialog;

    private CompositeDisposable compositeDisposable;


    public Dialog getLoadingDialog() {
        return loadingDialog;
    }



    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Mobile Ads init
        compositeDisposable = new CompositeDisposable();
    }

    protected abstract void setUpView();



    @Override
    public void showLoading() {
        if (loadingDialog != null) {
            hideLoading();
        }
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    @Override
    public void showMessage(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
