package com.liuzhenli.common.widget.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyRecyclerView
import com.airbnb.epoxy.TypedEpoxyController
import com.liuzhenli.common.R
import com.liuzhenli.common.databinding.LayoutEasyRecyclerviewBinding
import com.liuzhenli.common.utils.NetworkUtils
import com.liuzhenli.common.widget.recyclerview.decoration.DividerDecoration
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import splitties.init.appCtx

open class EasyRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    var binding: LayoutEasyRecyclerviewBinding
    private var mProgressId = 0
    private var mEmptyId = 0
    private var mErrorId = 0
    private var mClipToPadding = false
    private var mPadding = 0
    private var mPaddingTop = 0
    private var mPaddingBottom = 0
    private var mPaddingLeft = 0
    private var mPaddingRight = 0
    private var mScrollbarStyle = 0
    private var mScrollbar = 0
    private var mRefreshListener: OnRefreshListener? = null

    private var decorations: MutableList<RecyclerView.ItemDecoration> = ArrayList()
    private var easyDataObserver: EasyDataObserver? = null
    protected var mEmptyView: ViewGroup? = null

    companion object {
        private val TAG = EasyRecyclerView::class.java.name
        private fun log(content: String) {
            Log.i(TAG, content)
        }
    }

    init {
        initAttribute(attrs)
        binding =
            LayoutEasyRecyclerviewBinding.inflate(LayoutInflater.from(getContext()), this, true)
        binding.swipeRefreshLayout.isEnabled = false
        if (mProgressId != 0) {
            LayoutInflater.from(getContext()).inflate(mProgressId, binding.progressView)
        }
        if (mEmptyId != 0) {
            LayoutInflater.from(getContext()).inflate(mEmptyId, binding.emptyView)
        }
        if (mErrorId != 0) {
            LayoutInflater.from(getContext()).inflate(mErrorId, binding.errorView)
        }
        initRecyclerView()
    }

    private fun initAttribute(attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.EasyRecyclerView)
        try {
            mClipToPadding = a.getBoolean(R.styleable.EasyRecyclerView_recyclerClipToPadding, false)
            mPadding = a.getDimension(R.styleable.EasyRecyclerView_recyclerPadding, -1.0f).toInt()
            mPaddingTop =
                a.getDimension(R.styleable.EasyRecyclerView_recyclerPaddingTop, 0.0f).toInt()
            mPaddingBottom =
                a.getDimension(R.styleable.EasyRecyclerView_recyclerPaddingBottom, 0.0f).toInt()
            mPaddingLeft =
                a.getDimension(R.styleable.EasyRecyclerView_recyclerPaddingLeft, 0.0f).toInt()
            mPaddingRight =
                a.getDimension(R.styleable.EasyRecyclerView_recyclerPaddingRight, 0.0f).toInt()
            mScrollbarStyle = a.getInteger(R.styleable.EasyRecyclerView_scrollbarStyle, -1)
            mScrollbar = a.getInteger(R.styleable.EasyRecyclerView_scrollbars, -1)
            mEmptyId = a.getResourceId(R.styleable.EasyRecyclerView_layout_empty, 0)
            mProgressId = a.getResourceId(R.styleable.EasyRecyclerView_layout_progress, 0)
            mErrorId = a.getResourceId(
                R.styleable.EasyRecyclerView_layout_error,
                R.layout.common_net_error_view
            )
        } finally {
            a.recycle()
        }
    }

    /**
     * Implement this method to customize the AbsListView
     */
    private fun initRecyclerView() {
        setItemAnimator(null)
        binding.epoxyRecyclerView.setHasFixedSize(true)
        binding.epoxyRecyclerView.clipToPadding = mClipToPadding
        if (mPadding.toFloat() != -1.0f) {
            binding.epoxyRecyclerView.setPadding(mPadding, mPadding, mPadding, mPadding)
        } else {
            binding.epoxyRecyclerView.setPadding(
                mPaddingLeft,
                mPaddingTop,
                mPaddingRight,
                mPaddingBottom
            )
        }
        if (mScrollbarStyle != -1) {
            binding.epoxyRecyclerView.scrollBarStyle = mScrollbarStyle
        }
        when (mScrollbar) {
            0 -> isVerticalScrollBarEnabled = false
            1 -> isHorizontalScrollBarEnabled = false
            2 -> {
                isVerticalScrollBarEnabled = false
                isHorizontalScrollBarEnabled = false
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return try {
            binding.swipeRefreshLayout.dispatchTouchEvent(ev)
        } catch (e: Exception) {
            true
        }
    }

    fun setRecyclerPadding(left: Int, top: Int, right: Int, bottom: Int) {
        mPaddingLeft = left
        mPaddingTop = top
        mPaddingRight = right
        mPaddingBottom = bottom
        binding.epoxyRecyclerView.setPadding(
            mPaddingLeft,
            mPaddingTop,
            mPaddingRight,
            mPaddingBottom
        )
    }

    override fun setClipToPadding(isClip: Boolean) {
        binding.epoxyRecyclerView.clipToPadding = isClip
    }

    fun setEmptyView(emptyView: Int) {
        binding.emptyView.removeAllViews()
        LayoutInflater.from(context).inflate(emptyView, binding.emptyView)
    }

    fun setProgressView(progressView: Int) {
        binding.progressView.removeAllViews()
        LayoutInflater.from(context).inflate(progressView, binding.progressView)
    }

    fun setErrorView(errorView: Int) {
        binding.errorView.removeAllViews()
        LayoutInflater.from(context).inflate(errorView, binding.errorView)
    }

    fun scrollToPosition(position: Int) {
        binding.epoxyRecyclerView.scrollToPosition(position)
    }

    override fun setVerticalScrollBarEnabled(verticalScrollBarEnabled: Boolean) {
        binding.epoxyRecyclerView.isVerticalScrollBarEnabled = verticalScrollBarEnabled
    }

    override fun setHorizontalScrollBarEnabled(horizontalScrollBarEnabled: Boolean) {
        binding.epoxyRecyclerView.isHorizontalScrollBarEnabled = horizontalScrollBarEnabled
    }

    /**
     * Set the layout manager to the recycler
     */
    fun setLayoutManager(manager: RecyclerView.LayoutManager?) {
        binding.epoxyRecyclerView.layoutManager = manager
    }

    fun getLayoutManager(): RecyclerView.LayoutManager? {
        return binding.epoxyRecyclerView.layoutManager
    }

    /**
     * Set the ItemDecoration to the recycler
     */
    fun setItemDecoration(color: Int, height: Int, paddingLeft: Int, paddingRight: Int) {
        val itemDecoration = DividerDecoration(color, height, paddingLeft, paddingRight)
        itemDecoration.setDrawLastItem(false)
        decorations.add(itemDecoration)
        binding.epoxyRecyclerView.addItemDecoration(itemDecoration)
    }

    fun setController(controller: TypedEpoxyController<*>) {
        binding.epoxyRecyclerView.setController(controller)
        addDataObserver(controller.adapter)
    }


    class EasyDataObserver(private var recyclerView: EasyRecyclerView?) :
        RecyclerView.AdapterDataObserver() {
        fun onDestroy() {
            recyclerView = null
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            super.onItemRangeChanged(positionStart, itemCount)
            update()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            update()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            update()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
            update()
        }

        override fun onChanged() {
            super.onChanged()
            update()
        }

        //自动更改Container的样式
        private fun update() {
            log("update")
            var count = 0
            if (recyclerView?.adapter != null) {
                count = recyclerView?.adapter?.itemCount ?: 0
            }
            if (count == 0 && !NetworkUtils.isNetWorkAvailable(appCtx)) {
                recyclerView?.showError()
                return
            }
            if (count == 0) {
                log("no data:" + "show empty")
                recyclerView?.showEmpty()
            } else {
                log("has data")
                recyclerView?.showRecycler()
            }
        }
    }


    private fun addDataObserver(adapter: RecyclerView.Adapter<*>) {
        easyDataObserver = EasyDataObserver(this)
        adapter.registerAdapterDataObserver(easyDataObserver!!)
        showRecycler()
    }

    fun addDataObserverWithProgress(adapter: RecyclerView.Adapter<*>) {
        binding.epoxyRecyclerView.adapter = adapter
        easyDataObserver = EasyDataObserver(this)
        adapter.registerAdapterDataObserver(easyDataObserver!!)
        //只有Adapter为空时才显示ProgressView
        if (adapter.itemCount == 0) {
            showProgress()
        } else {
            showRecycler()
        }
    }

    /**
     * Remove the adapter from the recycler
     */
    fun clear() {
        binding.epoxyRecyclerView.adapter = null
    }

    private fun hideAll() {
        binding.emptyView.visibility = GONE
        binding.progressView.visibility = GONE
        binding.errorView.visibility = GONE
        binding.epoxyRecyclerView.visibility = GONE
    }

    fun showError() {
        log("showError")
        if (binding.errorView.childCount > 0) {
            hideAll()
            binding.errorView.visibility = VISIBLE
        } else {
            showRecycler()
        }
    }

    fun showEmpty() {
        log("showEmpty")
        if (binding.emptyView.childCount > 0) {
            hideAll()
            binding.emptyView.visibility = VISIBLE
        } else {
            showRecycler()
        }
    }

    fun showProgress() {
        log("showProgress")
        if (binding.progressView.childCount > 0) {
            hideAll()
            binding.progressView.visibility = VISIBLE
        } else {
            showRecycler()
        }
    }

    fun showRecycler() {
        log("showRecycler")
        if (binding.epoxyRecyclerView.visibility != VISIBLE) {
            binding.epoxyRecyclerView.visibility = VISIBLE
        }
        binding.emptyView.visibility = GONE
        binding.progressView.visibility = GONE
        binding.errorView.visibility = GONE
    }

    fun showTipViewAndDelayClose(tip: String?) {
        binding.tvTipView.text = tip
        val mShowAction: Animation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        )
        mShowAction.duration = 500
        binding.tvTipView.startAnimation(mShowAction)
        binding.tvTipView.visibility = VISIBLE
        binding.tvTipView.postDelayed({
            val mHiddenAction: Animation = TranslateAnimation(
                Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f
            )
            mHiddenAction.duration = 500
            binding.tvTipView.startAnimation(mHiddenAction)
            binding.tvTipView.visibility = GONE
        }, 2200)
    }

    fun showTipView(tip: String?) {
        binding.tvTipView.text = tip
        val mShowAction: Animation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        )
        mShowAction.duration = 500
        binding.tvTipView.startAnimation(mShowAction)
        binding.tvTipView.visibility = VISIBLE
    }

    fun hideTipView(delayMillis: Long) {
        binding.tvTipView.postDelayed({
            val mHiddenAction: Animation = TranslateAnimation(
                Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f
            )
            mHiddenAction.duration = 500
            binding.tvTipView.startAnimation(mHiddenAction)
            binding.tvTipView.visibility = GONE
        }, delayMillis)
    }

    fun setTipViewText(tip: String?) {
        if (!isTipViewVisible) showTipView(tip) else binding.tvTipView.text = tip
    }

    val isTipViewVisible: Boolean
        get() = binding.tvTipView.visibility == VISIBLE

    /**
     * Set the listener when refresh is triggered and enable the SwipeRefreshLayout
     */
    fun setRefreshListener(listener: OnRefreshListener) {
        binding.swipeRefreshLayout.isEnabled = true
        binding.swipeRefreshLayout.setOnRefreshListener(listener)
        val errorView = errorView
        errorView?.findViewById<View>(R.id.tv_retry)
            ?.setOnClickListener { v: View? -> listener.onRefresh(binding.swipeRefreshLayout) }
        mRefreshListener = listener
    }

    // 避免刷新的loadding和progressview 同时显示
    var isRefreshing: Boolean
        get() = binding.swipeRefreshLayout.isRefreshing
        set(isRefreshing) {
            binding.swipeRefreshLayout.post {
                if (isRefreshing) { // 避免刷新的loadding和progressview 同时显示
                    binding.progressView.visibility = GONE
                    binding.swipeRefreshLayout.autoRefreshAnimationOnly()
                } else {
                    binding.swipeRefreshLayout.finishRefresh()
                }
            }
        }

    fun setRefreshing(isRefreshing: Boolean, isCallbackListener: Boolean) {
        binding.swipeRefreshLayout.post {
            //binding.swipeRefreshLayout.isRefreshing = isRefreshing
            if (isRefreshing && isCallbackListener && mRefreshListener != null) {
                mRefreshListener?.onRefresh(binding.swipeRefreshLayout)
            }
        }
    }

    /**
     * Set the colors.xml for the SwipeRefreshLayout states
     */
    fun setRefreshingColorResources(@ColorRes vararg colRes: Int) {
        //binding.swipeRefreshLayout.setColorSchemeResources(*colRes)
    }

    /**
     * Set the colors.xml for the SwipeRefreshLayout states
     */
    fun setRefreshingColor(vararg col: Int) {
        //binding.swipeRefreshLayout.setColorSchemeColors(*col)
    }

    /**
     * Add the onItemTouchListener for the recycler
     */
    fun addOnItemTouchListener(listener: RecyclerView.OnItemTouchListener?) {
        binding.epoxyRecyclerView.addOnItemTouchListener(listener!!)
    }

    /**
     * Remove the onItemTouchListener for the recycler
     */
    fun removeOnItemTouchListener(listener: RecyclerView.OnItemTouchListener?) {
        binding.epoxyRecyclerView.removeOnItemTouchListener(listener!!)
    }

    fun setLoadMoreListener(listener: OnLoadMoreListener?) {
        binding.swipeRefreshLayout.setOnLoadMoreListener(listener)
    }

    fun setEnableAutoLoadMore() {
        binding.swipeRefreshLayout.setEnableAutoLoadMore(true)
    }

    fun finishLoadMore(success: Boolean? = true) {
        binding.swipeRefreshLayout.finishLoadMore(success ?: true)
    }

    fun finishLoadMore(delay: Int = 100) {
        binding.swipeRefreshLayout.finishLoadMore(delay)
    }

    fun finishLoadMoreWithNoMoreData() {
        binding.swipeRefreshLayout.finishLoadMoreWithNoMoreData()
    }

    /**
     * @return the recycler adapter
     */
    val adapter: RecyclerView.Adapter<*>?
        get() = binding.epoxyRecyclerView.adapter

    override fun setOnTouchListener(listener: OnTouchListener) {
        binding.epoxyRecyclerView.setOnTouchListener(listener)
    }

    fun setItemAnimator(animator: RecyclerView.ItemAnimator?) {
        binding.epoxyRecyclerView.itemAnimator = animator
    }

    fun addItemDecoration(itemDecoration: RecyclerView.ItemDecoration?) {
        binding.epoxyRecyclerView.addItemDecoration(itemDecoration!!)
    }

    fun addItemDecoration(itemDecoration: RecyclerView.ItemDecoration?, index: Int) {
        binding.epoxyRecyclerView.addItemDecoration(itemDecoration!!, index)
    }

    fun removeItemDecoration(itemDecoration: RecyclerView.ItemDecoration?) {
        binding.epoxyRecyclerView.removeItemDecoration(itemDecoration!!)
    }

    fun removeAllItemDecoration() {
        for (decoration in decorations) {
            binding.epoxyRecyclerView.removeItemDecoration(decoration)
        }
    }

    /**
     * @return inflated error view or null
     */
    var errorView: View?
        get() = if (binding.errorView.childCount > 0) binding.errorView.getChildAt(0) else null
        set(errorView) {
            binding.errorView.removeAllViews()
            binding.errorView.addView(errorView)
        }

    /**
     * @return inflated progress view or null
     */
    var progressView: View?
        get() = if (binding.progressView.childCount > 0) binding.progressView.getChildAt(0) else null
        set(progressView) {
            binding.progressView.removeAllViews()
            binding.progressView.addView(progressView)
        }

    /**
     * @return inflated empty view or null
     */
    var emptyView: View?
        get() = if (binding.emptyView.childCount > 0) binding.emptyView.getChildAt(0) else null
        set(emptyView) {
            binding.emptyView.removeAllViews()
            binding.emptyView.addView(emptyView)
        }

    val emptyViewText: TextView?
        get() = binding.emptyView.findViewById<TextView>(R.id.tv_empty_text)
    val emptyViewButton: TextView?
        get() = binding.emptyView.findViewById<TextView>(R.id.tv_empty_text)

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding.epoxyRecyclerView.adapter = null
    }

    val recyclerView: EpoxyRecyclerView
        get() = binding.epoxyRecyclerView

}