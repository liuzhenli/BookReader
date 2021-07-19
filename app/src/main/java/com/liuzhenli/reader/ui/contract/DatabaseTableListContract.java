package com.liuzhenli.reader.ui.contract;

import com.liuzhenli.common.base.BaseContract;
import com.liuzhenli.reader.bean.DatabaseTable;

import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author liuzhenli 2021/2/25
 * Email: 848808263@qq.com
 */
public class DatabaseTableListContract {
    public interface View extends BaseContract.BaseView {
        void showDatabase(List<DatabaseTable> data);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void loadDatabase();
    }
}
