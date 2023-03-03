package com.micoredu.reader.ui.read

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.viewModel
import com.github.liuyueyi.quick.transfer.ChineseUtils
import com.github.liuyueyi.quick.transfer.constants.TransType
import com.liuzhenli.common.theme.ViewUtils
import com.liuzhenli.common.utils.AppConfig
import com.liuzhenli.common.BaseActivity
import com.micoredu.reader.bean.Book
import com.micoredu.reader.constant.EventBus
import com.micoredu.reader.help.config.ReadBookConfig
import com.micoredu.reader.help.config.ThemeConfig
import com.micoredu.reader.model.ReadBook
import com.micoredu.reader.page.ContentTextView
import com.micoredu.reader.page.ReadView
import com.micoredu.reader.page.provider.TextPageFactory
import com.micoredu.reader.ui.activity.BookChapterListActivity
import com.micoredu.reader.utils.dialogs.alert
import com.micoredu.reader.utils.observeEvent
import com.micoredu.reader.utils.postEvent
import com.micoredu.reader.widgets.menu.ReadBottomMenu
import com.micoredu.reader.widgets.menu.ReadSettingMenu
import com.micoredu.reader.widgets.menu.ReadTopBarMenu
import com.microedu.lib.reader.R
import com.microedu.lib.reader.databinding.FragmentReaderBinding
import kotlinx.coroutines.launch

/**
 * Description:reader page
 */
class ReaderActivity : BaseActivity<FragmentReaderBinding>(),
    MavericksView, ContentTextView.CallBack, ReadView.CallBack, ReadBook.CallBack,
    View.OnTouchListener {
    private val mViewMode: ReaderViewModel by viewModel()

    private var isShowingSearchResult = false
    override fun inflateView(inflater: LayoutInflater?): FragmentReaderBinding {
        return FragmentReaderBinding.inflate(inflater!!)
    }

    override fun invalidate() {

    }

    override fun init(savedInstanceState: Bundle?) {
        binding.cursorLeft.setOnTouchListener(this)
        binding.cursorRight.setOnTouchListener(this)
        ReadBook.callBack = this
        ReadBook.callBack?.notifyBookChanged()

        mViewMode.initData(this, intent)
        binding.mVBottomMenu.setOnMenuElementClickListener(object :
            ReadBottomMenu.OnElementClickListener {
            override fun onMenuClick() {
                //打卡目录
            }

            override fun onPreChapterClick() {
                ReadBook.moveToPrevChapter(upContent = true, toLast = false)
            }

            override fun onNextChapterClick() {
                ReadBook.moveToNextChapter(true)
            }

            override fun onBrightnessClick() {
                ViewUtils.showBottomView(binding.mVBrightnessSettingMenu)
            }

            override fun onNightModeClick() {
                AppConfig.isNightTheme = !AppConfig.isNightTheme
                ThemeConfig.applyDayNight(this@ReaderActivity)
            }

            override fun onSettingClick() {
                ViewUtils.hideBottomView(binding.mVBottomMenu)
                ViewUtils.hideTopView(binding.mTopBar)
                ViewUtils.showBottomView(binding.mVSettingMenu)
            }

            override fun onListenBookClick() {
            }

            override fun onChapterProgressed(progress: Int, isStop: Boolean) {

            }
        })

        binding.mVSettingMenu.setReadSettingCallBack(object : ReadSettingMenu.ReadSettingCallBack {
            override fun onPageAnimChanged() {
                ReadBook.book?.setPageAnim(ReadBookConfig.pageAnim)
                binding.mPageView.upPageAnim()
                ReadBook.loadContent(false)
            }

            override fun onTextStyleChanged() {
                ChineseUtils.unLoad(*TransType.values())
                postEvent(EventBus.UP_CONFIG, true)
            }

            override fun onTypeFaceClicked() {
                postEvent(EventBus.UP_CONFIG, false)
            }

            override fun onBackGroundChanged() {
                postEvent(EventBus.UP_CONFIG, false)
            }
        })
    }

    override fun observeLiveBus() = binding.run {
        super.observeLiveBus()
        observeEvent<Boolean>(EventBus.UP_CONFIG) {
            upSystemUiVisibility()
            binding.mPageView.upPageSlopSquare()
            binding.mPageView.upBg()
            binding.mPageView.upStyle()
            binding.mPageView.upBgAlpha()
            if (it) {
                ReadBook.loadContent(resetPageOffset = false)
            } else {
                binding.mPageView.upContent(resetPageOffset = false)
            }
        }
    }

    private fun upSystemUiVisibility() {}


    override val headerHeight: Int get() = binding.mPageView.curPage.headerHeight


    override val pageFactory: TextPageFactory get() = binding.mPageView.pageFactory

    override val isScroll: Boolean get() = binding.mPageView.isScroll


    override val isInitFinish: Boolean get() = mViewMode.isInitFinish
    override val isAutoPage = false
    override val autoPageProgress = 0

    override var isSelectingSearchResult = false
        set(value) {
            field = value && isShowingSearchResult
        }

    override fun onDestroy() {
        super.onDestroy()
        if (ReadBook.callBack === this) {
            ReadBook.callBack = null
        }
    }

    override fun upSelectedStart(x: Float, y: Float, top: Float) {

    }

    override fun upSelectedEnd(x: Float, y: Float) {

    }

    override fun onImageLongPress(x: Float, y: Float, src: String) {

    }

    override fun onCancelSelect() {

    }


    override fun showActionMenu() {
        if (binding.mVBottomMenu.visibility != View.VISIBLE && binding.mVSettingMenu.visibility != View.VISIBLE) {
            ViewUtils.showBottomView(binding.mVBottomMenu)
            ViewUtils.showTopView(binding.mTopBar)

            binding.mTopBar.setTitle(ReadBook.book?.name)
            binding.mTopBar.setChapterTitle(ReadBook.curTextChapter?.title)
            binding.mTopBar.toolBar.setTitleTextColor(Color.WHITE)
            binding.mTopBar.setOnMenuElementClickListener(object :
                ReadTopBarMenu.OnElementClickListener {
                override fun onBackClick() {
                    finish()
                }

                override fun onBookMarkClick() {
                    BookChapterListActivity.start(this@ReaderActivity, ReadBook.book?.bookUrl)
                }
            })
        } else {
            hideAllMenus()
        }
    }

    private fun hideAllMenus() {
        ViewUtils.hideBottomView(binding.mVBottomMenu)
        ViewUtils.hideTopView(binding.mTopBar)
        ViewUtils.hideBottomView(binding.mVSettingMenu)
        ViewUtils.hideBottomView(binding.mVBrightnessSettingMenu)
    }

    override fun screenOffTimerStart() {

    }

    override fun showTextActionMenu() {
    }

    override fun autoPageStop() {
    }

    override fun openChapterList() {
    }

    override fun addBookmark() {
    }

    override fun changeReplaceRuleState() {
    }

    override fun openSearchActivity(searchWord: String?) {
    }

    override fun upMenuView() {

    }

    override fun loadChapterList(book: Book) {
        ReadBook.upMsg(getString(R.string.toc_updateing))
        mViewMode.loadChapterList(book)
    }

    override fun upContent(
        relativePosition: Int,
        resetPageOffset: Boolean,
        success: (() -> Unit)?
    ) {
        Log.e("------", "upContent")
        launch {
            binding.mPageView.upContent(relativePosition, resetPageOffset)
        }
    }

    override fun pageChanged() {
        launch { }
    }

    override fun contentLoadFinish() {

    }

    override fun upPageAnim() {
        launch {
            binding.mPageView.upPageAnim()
        }

    }

    override fun notifyBookChanged() {
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean = binding.run {
        when (event.action) {
            //MotionEvent.ACTION_DOWN -> textActionMenu.dismiss()
            MotionEvent.ACTION_MOVE -> {
                when (v.id) {
                    R.id.cursorLeft -> mPageView.curPage.selectStartMove(
                        event.rawX + cursorLeft.width,
                        event.rawY - cursorLeft.height
                    )
                    R.id.cursorRight -> mPageView.curPage.selectEndMove(
                        event.rawX - cursorRight.width,
                        event.rawY - cursorRight.height
                    )
                }
            }
            MotionEvent.ACTION_UP -> showTextActionMenu()
        }
        return true
    }

    override fun onBackPressed() {
        if (binding.mVBottomMenu.visibility == View.VISIBLE || binding.mVSettingMenu.visibility == View.VISIBLE) {
            hideAllMenus()
        } else {
            super.onBackPressed()
        }
    }

    override fun finish() {
        ReadBook.book?.let {
            if (!ReadBook.inBookshelf) {
                if (!AppConfig.showAddToShelfAlert) {
                    mViewMode.removeFromBookshelf()
                    super.finish()
                } else {
                    alert(title = getString(R.string.add_to_shelf)) {
                        setMessage(getString(R.string.check_add_bookshelf, it.name))
                        okButton {
                            ReadBook.inBookshelf = true
                            setResult(Activity.RESULT_OK)
                            super.finish()
                        }
                        noButton {
                            mViewMode.removeFromBookshelf()
                            super.finish()
                        }
                    }
                }
            } else {
                super.finish()
            }
        } ?: super.finish()
    }

}