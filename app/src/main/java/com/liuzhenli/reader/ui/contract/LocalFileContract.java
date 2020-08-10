package com.liuzhenli.reader.ui.contract;

import com.liuzhenli.reader.base.BaseContract;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-12-15 10:24
 */
public class LocalFileContract {
    public interface View extends BaseContract.BaseView {
        void showDirectory(ArrayList<HashMap<String, Object>> data, File file);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getDirectory(File file);
    }
}
