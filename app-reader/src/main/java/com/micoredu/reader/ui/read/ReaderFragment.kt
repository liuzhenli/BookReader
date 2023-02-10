package com.micoredu.reader.ui.read

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.liuzhenli.common.utils.runOnUI
import com.micoredu.reader.BaseFragment
import com.micoredu.reader.bean.Book
import com.micoredu.reader.model.ReadBook
import com.micoredu.reader.page.ContentTextView
import com.micoredu.reader.page.ReadView
import com.micoredu.reader.page.provider.TextPageFactory
import com.microedu.lib.reader.R
import com.microedu.lib.reader.databinding.FragmentReaderBinding
import kotlinx.coroutines.launch

/**
 * Description:reader fragment
 */
class ReaderFragment : BaseFragment(R.layout.fragment_reader),
    MavericksView, ContentTextView.CallBack, ReadView.CallBack, ReadBook.CallBack,
    View.OnTouchListener {
    private val args: Bundle by args()

    val binding by viewBinding(FragmentReaderBinding::bind)
    var isShowingSearchResult = false

    private val mViewMode: ReaderViewModel by fragmentViewModel()
    override fun init(savedInstanceState: Bundle?) {
        mViewMode.initData(requireActivity(), args)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun invalidate() {
        binding.mPageView.setContentTextViewCallBack(this)
        binding.mPageView.setReadViewCallBack(this)
    }

    override val headerHeight: Int
        get() = if (isAdded) {
            binding.mPageView.curPage.headerHeight
        } else {
            0
        }

    override val pageFactory: TextPageFactory
        get() =
            binding.mPageView.pageFactory

    override val isScroll: Boolean
        get() = if (isAdded) {
            binding.mPageView.isScroll
        } else {
            false
        }

    override val isInitFinish: Boolean get() = mViewMode.isInitFinish
    override val isAutoPage = false
    override val autoPageProgress = 0

    override var isSelectingSearchResult = false
        set(value) {
            field = value && isShowingSearchResult
        }


    override fun onResume() {
        super.onResume()
        ReadBook.callBack = this
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.cursorLeft.setOnTouchListener(this)
        binding.cursorRight.setOnTouchListener(this)
        ReadBook.callBack?.notifyBookChanged()
        ReadBook.callBack = this
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
    }

    override fun screenOffTimerStart() {
    }

    override fun showTextActionMenu() {
    }

    override fun autoPageStop() {
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
        if (isAdded) {
            launch {
                binding.mPageView.upContent(relativePosition, resetPageOffset)
            }
        }
    }

    override fun pageChanged() {

    }

    override fun contentLoadFinish() {

    }

    override fun upPageAnim() {

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
}