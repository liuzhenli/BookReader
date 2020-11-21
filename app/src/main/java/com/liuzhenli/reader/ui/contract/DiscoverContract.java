package com.liuzhenli.reader.ui.contract;

import com.liuzhenli.reader.base.BaseContract;
import com.micoredu.readerlib.bean.BookSourceBean;

import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2020/9/12
 * Email: 848808263@qq.com
 */
public class DiscoverContract {
    public interface View extends BaseContract.BaseView {
        void showSource(List<BookSourceBean> bookSourceList);
    }


    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getSource();
    }

}
