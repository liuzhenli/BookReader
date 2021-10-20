package com.liuzhenli.reader.ui.activity

import android.content.Context
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.liuzhenli.common.AppComponent
import com.liuzhenli.common.base.BaseActivity
import com.liuzhenli.common.constant.ARouterConstants
import com.liuzhenli.reader.DaggerReadBookComponent
import com.liuzhenli.reader.ui.contract.ManageBookshelfContract
import com.liuzhenli.reader.ui.presenter.ManageBookPresenter
import com.micoredu.reader.bean.BookShelfBean
import com.microedu.reader.R
import com.microedu.reader.databinding.ActivityManagebookshelfBinding
import java.lang.Exception

/**
 * Description:
 *
 * @author  liuzhenli 2021/10/19
 * Email: 848808263@qq.com
 */
@Route(path = ARouterConstants.ACT_MANAGE_BOOKSHELF)
class ManageBookShelfActivity : BaseActivity<ManageBookPresenter>(), ManageBookshelfContract.View {

    private var binding: ActivityManagebookshelfBinding? = null

    companion object {
        fun start(context: Context) {
            ARouter.getInstance().build(ARouterConstants.ACT_MANAGE_BOOKSHELF).navigation(context)
        }
    }

    override fun bindContentView(): View {
        binding = ActivityManagebookshelfBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun setupActivityComponent(appComponent: AppComponent?) {
        DaggerReadBookComponent.builder().build().inject(this)
    }

    override fun initToolBar() {
        mTvTitle.text = resources.getString(R.string.arrange_bookshelf)
    }

    override fun initData() {
    }

    override fun configViews() {
        mPresenter.getBookList();
    }

    override fun showBookList(books: BookShelfBean) {
    }

    override fun showError(e: Exception?) {
    }

    override fun complete() {
    }
}