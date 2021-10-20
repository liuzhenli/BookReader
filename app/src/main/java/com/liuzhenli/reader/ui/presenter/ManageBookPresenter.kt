package com.liuzhenli.reader.ui.presenter

import com.liuzhenli.common.base.RxPresenter
import com.liuzhenli.reader.ui.contract.ManageBookshelfContract
import javax.inject.Inject

/**
 * Description:
 *
 * @author  liuzhenli 2021/10/19
 * Email: 848808263@qq.com
 */
class ManageBookPresenter @Inject constructor() : RxPresenter<ManageBookshelfContract.View>(),
    ManageBookshelfContract.Presenter<ManageBookshelfContract.View> {

    override fun getBookList() {
    }
}