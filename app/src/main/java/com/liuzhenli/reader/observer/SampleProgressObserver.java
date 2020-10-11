package com.liuzhenli.reader.observer;

import android.os.Looper;

import com.liuzhenli.reader.ReaderApplication;
import com.liuzhenli.reader.base.BaseContract;
import com.liuzhenli.reader.exception.ApiException;
import com.liuzhenli.reader.utils.ToastUtil;


/**
 * @author Liuzhenli
 * @since 2019-07-06 10:34
 */
public abstract class SampleProgressObserver<T> extends BaseObserver<T> {

    private BaseContract.BaseView mView;

    public SampleProgressObserver() {
    }

    public SampleProgressObserver(BaseContract.BaseView view) {
        this.mView = view;
    }

    @Override
    protected void onError(ApiException e) {
        if (ReaderApplication.isDebug) {
            e.printStackTrace();
        }
        if (mView != null) {
            mView.showError(e);
        }
        //定义什么类型的的错误信息不弹吐司
        if (mView != null) {
            String displayMessage = e.getDisplayMessage();
            if (displayMessage != null && !displayMessage.contains("#")) {
                showErrorToast(displayMessage);
            }
        }
    }

    private void showErrorToast(String ex) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            ToastUtil.showCenter(ex);
        }
    }


    @Override
    public void onComplete() {
        if (mView != null) {
            mView.complete();
        }
    }
}
