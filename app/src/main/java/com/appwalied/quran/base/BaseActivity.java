package com.appwalied.quran.base;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.appwalied.quran.R;

import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseActivity extends AppCompatActivity implements IBaseView {


    private static final String TAG = "BaseActivity";
    private CompositeDisposable p1;
    private Unbinder xl1;
    private Dialog loadingDialog;
    private Unbinder unbinder;

    private final Handler handler = new Handler(Looper.getMainLooper());


    public Handler xoi() {
        return handler;
    }

    public void spo(Unbinder unbinder) {
        this.xl1 = unbinder;
    }



    public CompositeDisposable cxs() {
        return p1;
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        p1 = new CompositeDisposable();
    }


    public Handler getHandler() {
        return handler;
    }


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


    public void setUnBinder(Unbinder unbinder) {
        this.unbinder = unbinder;
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

    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        if (xl1 != null) {
            xl1.unbind();
        }
        cxs().dispose();
        super.onDestroy();
    }
}
