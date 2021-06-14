package com.liuzhenli.write.ui.contract;


import com.liuzhenli.common.base.BaseBean;
import com.liuzhenli.common.base.BaseContract;
import com.liuzhenli.write.bean.WriteChapter;

/**
 * Description:
 *
 * @author liuzhenli 2021/1/20
 * Email: 848808263@qq.com
 */
public interface WriteBookContract {
    interface View extends BaseContract.BaseView {
        void showLocalData(BaseBean data);

        void showSaveChapterInfoResult(boolean data);

        void showAutoSaveDraftResult(boolean data);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        /**
         * 本地数据
         */
        void getLocalData();

        /**
         * 保存章节
         */
        void saveChapterInfo(WriteChapter chapter);

        /**
         * 自动保存草稿
         */
        void autoSaveDraft(WriteChapter chapter, String content, String path);
    }
}
