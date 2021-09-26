package com.liuzhenli.reader.ui.contract;


import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.liuzhenli.common.base.BaseContract;
import com.liuzhenli.common.utils.filepicker.entity.FileItem;

import java.util.List;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-12-15 10:24
 */
public class LocalTxtContract {
    public interface View extends BaseContract.BaseView {
        void showLocalTxt(List<FileItem> fileList);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getLocalTxt(FragmentActivity activity);

        void getLocalTxt(Context context, String path);
    }
}
