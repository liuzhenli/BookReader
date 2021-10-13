package com.micoredu.reader.ui.contract;

import com.liuzhenli.common.base.BaseContract;
import com.micoredu.reader.bean.ReadHistory;

import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2021/10/12
 * Email: 848808263@qq.com
 */
public class ReadHistoryContract {
    public interface View extends BaseContract.BaseView {
        void showHistory(List<ReadHistory> data);

        void showDeleteResult();
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getHistory();

        void deleteItem(int position);
    }
}
