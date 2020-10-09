package com.liuzhenli.reader.ui.contract;

import com.liuzhenli.reader.base.BaseContract;
import com.liuzhenli.reader.bean.BookSourceData;

import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2020/9/12
 * Email: 848808263@qq.com
 */
public class RecommendContract {
    public interface View extends BaseContract.BaseView {
        void showSource(List<BookSourceData> bookSourceList);
    }


    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getSource();
    }

}
