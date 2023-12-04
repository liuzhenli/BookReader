package com.liuzhenli.reader.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.liuzhenli.common.base.BaseListener
import com.liuzhenli.common.widget.filter.util.SimpleAnimationListener
import com.liuzhenli.reader.ui.adapter.BookSourceViewAdapter
import com.micoredu.reader.R
import com.micoredu.reader.bean.BookSource

/**
 * Description:Book source selection view
 */
class BookSourceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var adapter: BookSourceViewAdapter? = null
    private var mCloseAnimation: Animation? = null
    private var mShowAnimation: Animation? = null
    private var mRecyclerView: RecyclerView? = null
    private fun init() {
        adapter = BookSourceViewAdapter(context)
        val mRoot =
            LayoutInflater.from(context).inflate(R.layout.view_book_source, this) as ViewGroup
        mRecyclerView = mRoot.findViewById(R.id.recycler_view)
        mRecyclerView?.layoutManager = GridLayoutManager(context, 3)
        mRecyclerView?.adapter = adapter
        adapter?.setOnItemClickListener { position: Int ->
            if (onItemClick != null) {
                adapter?.getItem(position)?.let { onItemClick?.onResponse(it) }
            }
            visibility = GONE
        }
        mShowAnimation = AnimationUtils.loadAnimation(context, R.anim.top_in)
        mShowAnimation?.setAnimationListener(object : SimpleAnimationListener() {
            override fun onAnimationEnd(animation: Animation) {
                super.onAnimationEnd(animation)
                visibility = VISIBLE
            }
        })
        mShowAnimation?.duration = 300
        mCloseAnimation = AnimationUtils.loadAnimation(context, R.anim.top_out)
        mCloseAnimation?.setAnimationListener(object : SimpleAnimationListener() {
            override fun onAnimationEnd(animation: Animation) {
                super.onAnimationEnd(animation)
                visibility = GONE
            }
        })
        mCloseAnimation?.duration = 300
    }

    fun setData(data: List<BookSource?>) {
        adapter?.clear()
        adapter?.addAll(data)
        adapter?.notifyDataSetChanged()
    }

    private var onItemClick: BaseListener<BookSource>? = null

    init {
        init()
    }

    fun setOnItemClick(onItemClick: BaseListener<BookSource>?) {
        this.onItemClick = onItemClick
    }

    fun close() {
        startAnimation(mCloseAnimation)
    }

    fun show() {
        if (isClosed) {
            visibility = VISIBLE
            startAnimation(mShowAnimation)
        }
    }

    val isShowing: Boolean
        get() {
            verifyViewInit()
            return isShown
        }
    val isClosed: Boolean
        get() {
            verifyViewInit()
            return !isShowing
        }

    fun verifyViewInit() {
        checkNotNull(mRecyclerView) { "you must init recyclerView first" }
    }
}