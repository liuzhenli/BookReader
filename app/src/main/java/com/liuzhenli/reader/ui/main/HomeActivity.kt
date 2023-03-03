package com.liuzhenli.reader.ui.main

import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.airbnb.mvrx.MavericksView
import com.alibaba.android.arouter.launcher.ARouter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.liuzhenli.common.constant.ARouterConstants
import com.liuzhenli.common.utils.*
import com.liuzhenli.common.utils.IntentUtils.IMPORT_BOOK_SOURCE_QRCODE
import com.liuzhenli.reader.bean.MenuBean
import com.liuzhenli.reader.ui.activity.WebViewActivity
import com.liuzhenli.reader.ui.discover.DiscoverFragment
import com.liuzhenli.reader.ui.search.SearchActivity
import com.liuzhenli.reader.ui.shelf.BookShelfFragment
import com.liuzhenli.reader.view.ImportBookSourceDialog
import com.liuzhenli.reader.view.dialog.AddWxArticleDialog
import com.liuzhenli.common.BaseActivity
import com.micoredu.reader.R
import com.micoredu.reader.constant.EventBus
import com.micoredu.reader.databinding.ActivityHomeBinding
import com.micoredu.reader.ui.source.BookSourceActivity
import com.micoredu.reader.utils.observeEvent
import com.qmuiteam.qmui.kotlin.onClick


class HomeActivity : BaseActivity<ActivityHomeBinding>(), MavericksView {

    private val viewModel by viewModels<HomeViewModel>()

    private var importBookSourceDialog: ImportBookSourceDialog? = null
    private var isFragment: Fragment? = null
    private var bookShelfFragment: BookShelfFragment? = null
    private var discoverFragment: DiscoverFragment? = null

    private var currentTabIndex = 0;
    override fun invalidate() {
    }

    override fun inflateView(inflater: LayoutInflater?): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(inflater!!)
    }

    override fun init(savedInstanceState: Bundle?) {
        setMenu(currentTabIndex)
        binding.navView.menu.clear()
        binding.navView.itemIconTintList = null

        getBottomTab().forEach {
            binding.navView.menu.add(it.groupId, it.homeId, it.order, it.name)
            binding.navView.menu.findItem(it.homeId).apply {
                setIcon(it.drawableId)
                actionView = getImageButton()
            }
        }
        binding.navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)


        //判断activity是否重建，如果不是，则不需要重新建立fragment.
        if (savedInstanceState == null) {
            val fm = supportFragmentManager
            val ft = fm.beginTransaction()
            if (bookShelfFragment == null) {
                bookShelfFragment = BookShelfFragment()
            }
            isFragment = bookShelfFragment
            ft.replace(R.id.fl_container, bookShelfFragment!!).commit()
        }

        //open left drawer
        binding.mMaterialMenu.setOnClickListener {
            if (!binding.mDrawLayout.isDrawerOpen(
                    binding.viewMainLeft.mDrawLeft
                )
            ) {
                binding.mDrawLayout.openDrawer(Gravity.LEFT, true)
            } else {
                binding.mDrawLayout.closeDrawer(binding.viewMainLeft.mDrawLeft)
            }
        }
        binding.viewMainLeft.mViewReadHistory.onClick {
            ARouter.getInstance().build(ARouterConstants.ACT_READ_HISTORY).navigation()
        }
        binding.viewMainLeft.mViewBookSourceManager.onClick {
            ARouter.getInstance().build(ARouterConstants.ACT_BOOK_SOURCE).navigation()
        }
        binding.viewMainLeft.mViewFeedBack.onClick {
            WebViewActivity.start(this@HomeActivity, Constant.FEEDBACK)
        }
        binding.viewMainLeft.mViewDonate.onClick {

        }
        binding.viewMainLeft.mViewSetting.onClick {

        }
        binding.viewMainLeft.mViewAbout.onClick {

        }
    }

    private fun getImageButton(): ImageButton {
        val imageButton = ImageButton(this)
        imageButton.setOnLongClickListener {
            false
        }
        return imageButton
    }

    private fun switchContent(from: Fragment?, to: Fragment?) {
        if (isFragment !== to) {
            isFragment = to
            val fm: FragmentManager = supportFragmentManager
            //添加渐隐渐现的动画
            val ft: FragmentTransaction = fm.beginTransaction()
            if (to != null) {
                if (!to.isAdded) {// 先判断是否被add过
                    if (from != null) {
                        ft.hide(from).add(R.id.fl_container, to).commit()
                    }
                } else {
                    if (from != null) {
                        ft.hide(from).show(to).commit()
                    }
                }
            }
        }
    }

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.id_shelf -> {
                    if (bookShelfFragment == null) {
                        bookShelfFragment = BookShelfFragment()
                    }
                    switchContent(isFragment, bookShelfFragment)
                    setMenu(0)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.id_discover -> {
                    if (discoverFragment == null) {
                        discoverFragment = DiscoverFragment()
                    }
                    switchContent(isFragment, discoverFragment)
                    setMenu(1)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }


    private fun getBottomTab(): List<MenuBean> {
        return mutableListOf(
            MenuBean(
                0, R.id.id_shelf, 0,
                R.drawable.icon_main_tab_bookshelf,
                resources.getString(R.string.bookshelf)
            ),

            MenuBean(
                0, R.id.id_discover, 2,
                R.drawable.icon_main_tab_discover,
                resources.getString(R.string.find)
            )
        )
    }


    private fun setMenu(tabIndex: Int = 0) {
        binding.toolbar.menu.clear()
        currentTabIndex = tabIndex
        when (tabIndex) {
            0 -> {
                binding.tvToolbarTitle.text = resources.getString(R.string.bookshelf)
                binding.tvToolbarTitle.setOnClickListener { }
                binding.toolbar.inflateMenu(R.menu.menu_main)
                binding.toolbar.menu.findItem(R.id.item_arrange_bookshelf).isVisible = false
                binding.toolbar.setOnMenuItemClickListener { item: MenuItem ->
                    binding.mDrawLayout.closeDrawer(binding.viewMainLeft.mDrawLeft)
                    when (item.itemId) {
                        R.id.item_search -> startActivity<SearchActivity> {}
                        R.id.item_add_from_local -> {}
                        R.id.item_arrange_bookshelf -> {}
                        R.id.item_import_book_source -> showImportBookSourceDialog()
                        R.id.item_web_server -> {
//                            val startedThisTime: Boolean = WebService.startService(this)
//                            if (!startedThisTime) {
//                                ToastUtil.showToast(getString(R.string.web_service_already_started_hint))
//                            }
                        }
                        else -> {}
                    }
                    false
                }
            }
            1 -> {
                binding.tvToolbarTitle.text = resources.getString(R.string.discover)
                binding.tvToolbarTitle.setOnClickListener {
                    discoverFragment?.configTitle()
                }

                binding.toolbar.inflateMenu(R.menu.menu_discover)
                binding.toolbar.setOnMenuItemClickListener { item: MenuItem ->
                    binding.mDrawLayout.closeDrawer(binding.viewMainLeft.mDrawLeft)
                    when (item.itemId) {
                        R.id.item_search -> startActivity<SearchActivity> { }
                        R.id.item_arrange_book_source -> startActivity<BookSourceActivity> { }
                        else -> {}
                    }
                    false
                }
            }
            else -> {}
        }
    }


    private fun showImportBookSourceDialog() {
        if (importBookSourceDialog == null) {
            importBookSourceDialog = ImportBookSourceDialog(this, R.style.BottomSheetStyle)
            importBookSourceDialog!!.setCameraClickListener(View.OnClickListener {
                ARouter.getInstance()
                    .build(ARouterConstants.ACT_QRCODE)
                    .navigation(this, IMPORT_BOOK_SOURCE_QRCODE)
            }).setImportWxSource {
                val addWxArticleDialog = AddWxArticleDialog(this@HomeActivity)
                addWxArticleDialog.setOkButtonClickListener(View.OnClickListener {
                    ClipboardUtil.copyToClipboard("异书房")
                    IntentUtils.openWeChat(this@HomeActivity)
                })
                addWxArticleDialog.show()
            }
            importBookSourceDialog?.setOkButtonClickListener(View.OnClickListener {
                if (TextUtils.isEmpty(importBookSourceDialog?.userInput)) {
                    ToastUtil.showToast(resources.getString(R.string.input_book_source_url))
                } else {
                    showProgress()
                    viewModel.importSource(importBookSourceDialog?.userInput)
                }
            })
            importBookSourceDialog?.setDirClick(View.OnClickListener {
                IntentUtils.selectFileSys(
                    this@HomeActivity,
                    IntentUtils.IMPORT_BOOK_SOURCE_LOCAL
                )
            })
            importBookSourceDialog!!.setCanceledOnTouchOutside(true)
        }
        importBookSourceDialog?.show()
    }

    override fun observeLiveBus() {
        observeEvent<String>(EventBus.CHANGE_DISCOVER_TITLE) {
            if (currentTabIndex == 1) {
                binding.tvToolbarTitle.text = it
            }
        }
    }

}