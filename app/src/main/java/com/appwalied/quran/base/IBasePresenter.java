package com.appwalied.quran.base;

public interface IBasePresenter<V extends IBaseView> {

    void onAttach(V view);

    void onDetach();

}
