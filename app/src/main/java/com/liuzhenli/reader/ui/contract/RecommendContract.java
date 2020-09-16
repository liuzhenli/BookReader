package com.liuzhenli.reader.ui.contract;

import com.liuzhenli.reader.base.BaseContract;

/**
 * Description:
 *
 * @author liuzhenli 2020/9/12
 * Email: 848808263@qq.com
 */
public class RecommendContract {
    public interface View  extends BaseContract.BaseView{
        void showSource();
    }


    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getSource();
    }

}
