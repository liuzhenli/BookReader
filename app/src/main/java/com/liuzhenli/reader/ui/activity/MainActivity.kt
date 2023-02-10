/*
package com.liuzhenli.reader.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentPagerAdapter
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.balysv.materialmenu.MaterialMenuDrawable
import com.google.android.material.tabs.TabLayout
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.liuzhenli.common.constant.ARouterConstants
import com.liuzhenli.common.constant.RxBusTag
import com.liuzhenli.common.utils.*
import com.liuzhenli.reader.view.ChoseBackupFolderDialog
import com.liuzhenli.reader.view.ImportBookSourceDialog
import com.liuzhenli.reader.view.dialog.AddWxArticleDialog
import com.micoredu.reader.BaseActivity
import com.micoredu.reader.R
import com.micoredu.reader.bean.BookSource
import com.micoredu.reader.databinding.ActivityMainContainerBinding
import com.micoredu.reader.help.storage.Backup
import com.micoredu.reader.ui.source.BookSourceActivity
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import io.reactivex.functions.Consumer
import java.lang.Exception

*/
/**
 * @author liuzhenli
 *//*

@Route(path = ARouterConstants.ACT_MAIN)
class MainActivity : BaseActivity<ActivityMainContainerBinding>() {
    private var mCurrentPosition = 0
    private var mainTabAdapter: MainTabAdapter? = null

    */
/***发现页面的书源名字 *//*

    private var mDiscoverBookSourceName: String? = null
    private var choseBackupFolderDialogBuilder: ChoseBackupFolderDialog.ChoseBackupFolderDialogBuilder? = null
    private var qmuiDialog: QMUIDialog? = null
    private var importBookSourceDialog: ImportBookSourceDialog? = null

    val viewModel by viewModels<MainViewModel>()
    
    override fun inflateView(inflater: LayoutInflater?): ActivityMainContainerBinding {
        return ActivityMainContainerBinding.inflate(inflater!!)
    }

    override fun init(savedInstanceState: Bundle?) {
       
    }
    

     fun initToolBar() {
         binding.toolbar.overflowIcon =  resources.getDrawable(R.drawable.ic_more)
        FillContentUtil.setText(
            binding.tvToolbarTitle,
            resources.getStringArray(R.array.main_tab_names)[mCurrentPosition]
        )
         binding.tvToolbarTitle.requestFocus()
         binding.tvToolbarTitle.setOnClickListener { v: View? ->
            if (mCurrentPosition == 1) {
                val fragment: DiscoverFragment =
                    mainTabAdapter?.getItem(mCurrentPosition) as DiscoverFragment
                fragment.showBookSourceView()
            }
        }
    }

      fun initData() {
        requestPermissions()
        viewModel.dealDefaultFonts()
    }

      fun configViews() {
        setMenu()
        mainTabAdapter = MainTabAdapter(
            this@MainActivity,
            supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
         binding.viewMain.mViewPager.adapter = mainTabAdapter
         binding.viewMain.mTabLayout.setupWithViewPager( binding.viewMain.mViewPager)
        //打开左侧的设置页面
        ClickUtils.click( binding.mMaterialMenu, Consumer { o: Any? ->
            if (! binding.mDrawLayout.isDrawerOpen(
                     binding.viewMainLeft.mDrawLeft
                )
            ) {
                 binding.mDrawLayout.openDrawer(Gravity.LEFT, true)
            } else {
                 binding.mDrawLayout.closeDrawer( binding.viewMainLeft.mDrawLeft)
            }
        })
        for (i in 0 until  binding.viewMain.mTabLayout.tabCount) {
            if ( binding.viewMain.mTabLayout.getTabAt(i) != null) {
                 binding.viewMain.mTabLayout.getTabAt(i)!!
                    .setCustomView(mainTabAdapter!!.getTabView(i))
            }
        }
         binding.viewMain.mTabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                mCurrentPosition = tab.position
                if (mCurrentPosition == 1 && !TextUtils.isEmpty(mDiscoverBookSourceName)) {
                    FillContentUtil.setTextDrawable(
                        binding.tvToolbarTitle, mDiscoverBookSourceName, resources
                            .getDrawable(R.drawable.ic_down_filled), FillContentUtil.Place.right
                    )
                } else {
                    FillContentUtil.setText(binding.tvToolbarTitle, mainTabAdapter?.getPageTitle(mCurrentPosition))
                }
                 binding.mDrawLayout.closeDrawer( binding.viewMainLeft.mDrawLeft)
                setMenu()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
         binding.viewMainLeft.mDrawLeft.setOnClickListener(null)

        //feedback
        ClickUtils.click(
             binding.viewMainLeft.mViewFeedBack,
            Consumer { o: Any? -> WebViewActivity.start(this@MainActivity, Constant.FEEDBACK) })

        //manage book source
        ClickUtils.click(
             binding.viewMainLeft.mViewBookSourceManager,
            Consumer { o: Any? -> BookSourceActivity.start(this@MainActivity) })

        //drawableLayout on draw listener
         binding.mDrawLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                 binding.mMaterialMenu.setTransformationOffset(
                    MaterialMenuDrawable.AnimationState.BURGER_ARROW,
                    2 - slideOffset
                )
            }
        })
        //read history
        ClickUtils.click(
             binding.viewMainLeft.mViewReadHistory,
            Consumer { o: Any? ->
                ARouter.getInstance().build(ARouterConstants.ACT_READ_HISTORY).navigation()
            })
        //setting
        ClickUtils.click(
             binding.viewMainLeft.mViewSetting,
            Consumer { o: Any? -> SettingActivity.start(this@MainActivity) })
        //about page
        ClickUtils.click(
             binding.viewMainLeft.mViewAbout,
            Consumer { o: Any? -> AboutActivity.start(this@MainActivity) })
        //donate
        ClickUtils.click( binding.viewMainLeft.mViewDonate, Consumer { o: Any? -> })
         binding.viewMainLeft.mViewDonate.visibility = View.GONE
         binding.viewMain.mViewPager.offscreenPageLimit = 4
         binding.viewMainLeft.mViewNightMode.visibility = View.GONE
        JumpToLastPageUtil.openLastPage(this@MainActivity)
        viewModel.checkBackupPath()
        viewModel.checkWebDav()
    }

    private fun setMenu() {
        binding.toolbar.menu.clear()
        when (mCurrentPosition) {
            0 -> {
                binding.toolbar.inflateMenu(R.menu.menu_main)
                binding.toolbar.menu.findItem(R.id.item_arrange_bookshelf).isVisible = false
                binding.toolbar.setOnMenuItemClickListener { item: MenuItem ->
                     binding.mDrawLayout.closeDrawer( binding.viewMainLeft.mDrawLeft)
                    */
/*when (item.itemId) {
                        R.id.item_search -> SearchActivity.start(this@MainActivity)
                        R.id.item_add_from_local -> ImportLocalBookActivity.start(this@MainActivity)
                        R.id.item_arrange_bookshelf -> ManageBookShelfActivity.start(this@MainActivity)
                        R.id.item_import_book_source -> showImportBookSourceDialog()
                        R.id.item_web_server -> {
                            val startedThisTime: Boolean = WebService.startService(this)
                            if (!startedThisTime) {
                                ToastUtil.showToast(getString(R.string.web_service_already_started_hint))
                            }
                        }
                        else -> {}
                    }*//*

                    false
                }
            }
            1 -> {
                binding.toolbar.inflateMenu(R.menu.menu_discover)
                binding.toolbar.setOnMenuItemClickListener { item: MenuItem ->
                     binding.mDrawLayout.closeDrawer( binding.viewMainLeft.mDrawLeft)
                    when (item.itemId) {
//                        R.id.item_search -> SearchActivity.start(this@MainActivity)
//                        R.id.item_arrange_book_source -> BookSourceActivity.start(this@MainActivity)
                        else -> {}
                    }
                    false
                }
            }
            else -> {}
        }
    }

    private fun requestPermissions() {}
    override fun onResume() {
        super.onResume()
        upDataReadTime(true)
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = [Tag(RxBusTag.CHANGE_DISCOVER_BOOK_SOURCE)])
    fun onBookSourceChange(book: BookSource?) {
        if (book != null) {
            mDiscoverBookSourceName = book.bookSourceName
        }
        if (mCurrentPosition == 1) {
            FillContentUtil.setTextDrawable(
                binding.tvToolbarTitle, mDiscoverBookSourceName, resources.getDrawable(
                    R.drawable.ic_down_filled
                ), FillContentUtil.Place.right
            )
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = [Tag(RxBusTag.RECREATE)])
    fun onRecreateEvent(recreate: Boolean?) {
        recreate()
    }

    override fun onBackPressed() {
        if (mCurrentPosition == 1) {
            val fragment: DiscoverFragment =
                mainTabAdapter?.getItem(mCurrentPosition) as DiscoverFragment
            if (fragment.isShowBookSourceMenu()) {
                fragment.dismissBookSourceMenu()
            } else {
                moveTaskToBack(true)
                //super.onBackPressed();
            }
        } else {
            moveTaskToBack(true)
            //super.onBackPressed();
        }
    }

    override fun onPause() {
        super.onPause()
        Backup.autoBack(this@MainActivity)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
     fun showCheckBackupPathResult(isEmpty: Boolean) {
        if (isEmpty) {
            choseBackupFolderDialogBuilder =
                ChoseBackupFolderDialog.ChoseBackupFolderDialogBuilder(this)
            choseBackupFolderDialogBuilder!!.setCanceledOnTouchOutside(false)
                .setSelectClickListener(View.OnClickListener { v: View? ->
                    this@MainActivity.registerForActivityResult<Uri?, Uri>(
                        ActivityResultContracts.OpenDocumentTree(),
                        ActivityResultCallback<Uri> { result: Uri? ->
                            if (result != null) {
                                contentResolver.takePersistableUriPermission(
                                    result,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                                )
                                AppSharedPreferenceHelper.setBackupPath(result.toString())
                                choseBackupFolderDialogBuilder!!.setFolderEnable(true)
                            }
                        }).launch(null)
                })
                .setOkClickListener(View.OnClickListener { v: View? -> qmuiDialog?.dismiss() })
                .setCancelable(false)
            qmuiDialog = choseBackupFolderDialogBuilder?.create()
            qmuiDialog?.show()
        }
    }

    private fun showImportBookSourceDialog() {
        if (importBookSourceDialog == null) {
            importBookSourceDialog = ImportBookSourceDialog(this, R.style.BottomSheetStyle)
            importBookSourceDialog!!.setCameraClickListener(View.OnClickListener { v: View? ->
                ARouter.getInstance()
                    .build(ARouterConstants.ACT_QRCODE)
                    .navigation(this, IMPORT_BOOK_SOURCE_QRCODE)
            }).setImportWxSource(
                View.OnClickListener { v: View? ->
                    val addWxArticleDialog = AddWxArticleDialog(this@MainActivity)
                    addWxArticleDialog.setOkButtonClickListener(View.OnClickListener { v1: View? ->
                        ClipboardUtil.copyToClipboard("异书房")
                        IntentUtils.openWeChat(this@MainActivity)
                    })
                    addWxArticleDialog.show()
                })
            importBookSourceDialog?.setOkButtonClickListener(View.OnClickListener { v: View? ->
                if (TextUtils.isEmpty(importBookSourceDialog!!.getUserInput())) {
                    ToastUtil.showToast(resources.getString(R.string.input_book_source_url))
                } else {
                    showProgress()
                    viewModel.importSource(importBookSourceDialog?.getUserInput())
                }
            })
            importBookSourceDialog?.setDirClick(View.OnClickListener { v: View? ->
                IntentUtils.selectFileSys(
                    this@MainActivity,
                    IntentUtils.IMPORT_BOOK_SOURCE_LOCAL
                )
            })
            importBookSourceDialog!!.setCanceledOnTouchOutside(true)
        }
        importBookSourceDialog?.show()
    }
    

     fun showBookSource(list: List<BookSource>) {
        dismissProgress()
        importBookSourceDialog?.dismiss()
        ToastUtil.showToast(String.format("成功导入%s个书源", list.size))
    }

     fun showError(e: Exception) {}
     fun complete() {}
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = [Tag(RxBusTag.UPDATE_READ)])
    fun upDataReadTime(change: Boolean?) {
//        val all = TimeUtils.formatToHour(
//            AppReaderDbHelper.getInstance().getDatabase().getReadHistoryDao().getAllTime()
//        )
//        val today = TimeUtils.formatToHour(
//            AppReaderDbHelper.getInstance().getDatabase().getReadHistoryDao().getTodayAllTime(
//                DateUtils.getToadyMillis()
//            )
//        )
//         binding.viewMainLeft.mViewReadHistory.text =
//            String.format(resources.getString(R.string.read_records), today)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == IMPORT_BOOK_SOURCE_QRCODE) {
                if (data != null) {
                    val result = data.getStringExtra("result")
                    //如果是http开头,访问网络
                    if (result != null) {
                        showProgress()
                        viewModel.importSource(result)
                    } else {
                        ToastUtil.showToast(resources.getString(R.string.type_un_correct))
                    }
                }
            } else if (requestCode == IntentUtils.IMPORT_BOOK_SOURCE_LOCAL) {
                if (data != null && data.data != null) {
                    viewModel.importSourceFromLocal(data.data!!)
                }
            }
        }
    }

    companion object {
        const val IMPORT_BOOK_SOURCE_QRCODE = 10092
        const val IMPORT_BOOK_SOURCE = 1000
    }

}*/
