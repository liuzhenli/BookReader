package com.liuzhenli.common.base;

public interface BaseContract {

    interface BasePresenter<T> {

        void attachView(T view);

        void detachView();
    }

    interface BaseView {

        void showError(Exception e);

        void complete();

    }
}