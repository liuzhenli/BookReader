package com.liuzhenli.common.widget.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;


import androidx.annotation.ColorRes;
import androidx.recyclerview.widget.RecyclerView;

import com.liuzhenli.common.BuildConfig;
import com.liuzhenli.common.R;
import com.liuzhenli.common.utils.NetworkUtils;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.liuzhenli.common.widget.recyclerview.decoration.DividerDecoration;
import com.liuzhenli.common.widget.recyclerview.swipe.ZLSwipeRefreshLayout;
import com.liuzhenli.common.widget.recyclerview.swipe.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;


/**
 * @author liuzhenli
 */
public class EasyRecyclerView extends FrameLayout {
    private Context mContext;

    public static final String TAG = "EasyRecyclerView";
    public static boolean DEBUG = BuildConfig.DEBUG;
    protected RecyclerView mRecycler;
    protected TextView tipView;
    protected ViewGroup mProgressView;
    protected ViewGroup mEmptyView;
    protected ViewGroup mErrorView;
    private int mProgressId;
    private int mEmptyId;
    private int mErrorId;

    protected boolean mClipToPadding;
    protected int mPadding;
    protected int mPaddingTop;
    protected int mPaddingBottom;
    protected int mPaddingLeft;
    protected int mPaddingRight;
    protected int mScrollbarStyle;
    protected int mScrollbar;

    protected RecyclerView.OnScrollListener mInternalOnScrollListener;
    protected RecyclerView.OnScrollListener mExternalOnScrollListener;

    protected ZLSwipeRefreshLayout mPtrLayout;
    protected OnRefreshListener mRefreshListener;

    public List<RecyclerView.ItemDecoration> decorations = new ArrayList<>();


    public ZLSwipeRefreshLayout getSwipeToRefresh() {
        return mPtrLayout;
    }

    public RecyclerView getRecyclerView() {
        return mRecycler;
    }

    public EasyRecyclerView(Context context) {
        this(context, null);
    }

    public EasyRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EasyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        if (attrs != null)
            initAttrs(attrs);
        initView();
    }

    protected void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SuperRecyclerView);
        try {
            mClipToPadding = a.getBoolean(R.styleable.SuperRecyclerView_recyclerClipToPadding, false);
            mPadding = (int) a.getDimension(R.styleable.SuperRecyclerView_recyclerPadding, -1.0f);
            mPaddingTop = (int) a.getDimension(R.styleable.SuperRecyclerView_recyclerPaddingTop, 0.0f);
            mPaddingBottom = (int) a.getDimension(R.styleable.SuperRecyclerView_recyclerPaddingBottom, 0.0f);
            mPaddingLeft = (int) a.getDimension(R.styleable.SuperRecyclerView_recyclerPaddingLeft, 0.0f);
            mPaddingRight = (int) a.getDimension(R.styleable.SuperRecyclerView_recyclerPaddingRight, 0.0f);
            mScrollbarStyle = a.getInteger(R.styleable.SuperRecyclerView_scrollbarStyle, -1);
            mScrollbar = a.getInteger(R.styleable.SuperRecyclerView_scrollbars, -1);

            mEmptyId = a.getResourceId(R.styleable.SuperRecyclerView_layout_empty, 0);
            mProgressId = a.getResourceId(R.styleable.SuperRecyclerView_layout_progress, 0);
            mErrorId = a.getResourceId(R.styleable.SuperRecyclerView_layout_error, R.layout.common_net_error_view);
        } finally {
            a.recycle();
        }
    }


    private void initView() {
        if (isInEditMode()) {
            return;
        }
        //生成主View
        View v = LayoutInflater.from(getContext()).inflate(R.layout.common_recyclerview, this);

        mPtrLayout = (ZLSwipeRefreshLayout) v.findViewById(R.id.ptr_layout);
        mPtrLayout.setEnabled(false);

        mProgressView = (ViewGroup) v.findViewById(R.id.progress);
        if (mProgressId != 0) LayoutInflater.from(getContext()).inflate(mProgressId, mProgressView);
        mEmptyView = (ViewGroup) v.findViewById(R.id.empty);
        if (mEmptyId != 0) LayoutInflater.from(getContext()).inflate(mEmptyId, mEmptyView);
        mErrorView = (ViewGroup) v.findViewById(R.id.error);
        if (mErrorId != 0) LayoutInflater.from(getContext()).inflate(mErrorId, mErrorView);
        initRecyclerView(v);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return mPtrLayout.dispatchTouchEvent(ev);
        } catch (Exception e) {
            return true;
        }

    }

    /**
     * set recycle view padding
     *
     * @param left   left padding
     * @param top    top padding
     * @param right  right padding
     * @param bottom bottom padding
     */
    public void setRecyclerPadding(int left, int top, int right, int bottom) {
        this.mPaddingLeft = left;
        this.mPaddingTop = top;
        this.mPaddingRight = right;
        this.mPaddingBottom = bottom;
        mRecycler.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
    }

    @Override
    public void setClipToPadding(boolean isClip) {
        mRecycler.setClipToPadding(isClip);
    }


    public void setEmptyView(View emptyView) {
        mEmptyView.removeAllViews();
        mEmptyView.addView(emptyView);
    }

    public void setProgressView(View progressView) {
        mProgressView.removeAllViews();
        mProgressView.addView(progressView);
    }

    public void setErrorView(View errorView) {
        mErrorView.removeAllViews();
        mErrorView.addView(errorView);
    }

    public void setEmptyView(int emptyView) {
        mEmptyView.removeAllViews();
        LayoutInflater.from(getContext()).inflate(emptyView, mEmptyView);
    }

    public void setProgressView(int progressView) {
        mProgressView.removeAllViews();
        LayoutInflater.from(getContext()).inflate(progressView, mProgressView);
    }

    public void setErrorView(int errorView) {
        mErrorView.removeAllViews();
        LayoutInflater.from(getContext()).inflate(errorView, mErrorView);
    }

    public void scrollToPosition(int position) {
        getRecyclerView().scrollToPosition(position);
    }

    /**
     * Implement this method to customize the AbsListView
     */
    protected void initRecyclerView(View view) {
        mRecycler = (RecyclerView) view.findViewById(android.R.id.list);
        tipView = (TextView) view.findViewById(R.id.tvTip);
        setItemAnimator(null);
        if (mRecycler != null) {
            mRecycler.setHasFixedSize(true);
            mRecycler.setClipToPadding(mClipToPadding);
            mInternalOnScrollListener = new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (mExternalOnScrollListener != null)
                        mExternalOnScrollListener.onScrolled(recyclerView, dx, dy);

                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (mExternalOnScrollListener != null)
                        mExternalOnScrollListener.onScrollStateChanged(recyclerView, newState);

                }
            };
            mRecycler.addOnScrollListener(mInternalOnScrollListener);

            if (mPadding != -1.0f) {
                mRecycler.setPadding(mPadding, mPadding, mPadding, mPadding);
            } else {
                mRecycler.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
            }
            if (mScrollbarStyle != -1) {
                mRecycler.setScrollBarStyle(mScrollbarStyle);
            }
            switch (mScrollbar) {
                case 0:
                    setVerticalScrollBarEnabled(false);
                    break;
                case 1:
                    setHorizontalScrollBarEnabled(false);
                    break;
                case 2:
                    setVerticalScrollBarEnabled(false);
                    setHorizontalScrollBarEnabled(false);
                    break;
            }
        }
    }

    @Override
    public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
        mRecycler.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
    }

    @Override
    public void setHorizontalScrollBarEnabled(boolean horizontalScrollBarEnabled) {
        mRecycler.setHorizontalScrollBarEnabled(horizontalScrollBarEnabled);
    }

    /**
     * Set the layout manager to the recycler
     *
     * @param manager
     */
    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        mRecycler.setLayoutManager(manager);
    }

    /**
     * Set the ItemDecoration to the recycler
     *
     * @param color
     * @param height
     * @param paddingLeft
     * @param paddingRight
     */
    public void setItemDecoration(int color, int height, int paddingLeft, int paddingRight) {
        DividerDecoration itemDecoration = new DividerDecoration(color, height, paddingLeft, paddingRight);
        itemDecoration.setDrawLastItem(false);
        decorations.add(itemDecoration);
        mRecycler.addItemDecoration(itemDecoration);
    }


    public static class EasyDataObserver extends RecyclerView.AdapterDataObserver {
        private EasyRecyclerView recyclerView;

        public EasyDataObserver(EasyRecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            update();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            update();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            update();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            update();
        }

        @Override
        public void onChanged() {
            super.onChanged();
            update();
        }

        //自动更改Container的样式
        private void update() {
            log("update");
            int count;
            if (recyclerView.getAdapter() instanceof RecyclerArrayAdapter) {
                count = ((RecyclerArrayAdapter) recyclerView.getAdapter()).getCount();
            } else {
                count = recyclerView.getAdapter().getItemCount();
            }

            if (count == 0 && !NetworkUtils.isNetWorkAvailable(recyclerView.getContext())) {
                recyclerView.showError();
                return;
            }

            if (count == 0 && ((RecyclerArrayAdapter) recyclerView.getAdapter()).getHeaderCount() == 0) {
                log("no data:" + "show empty");
                recyclerView.showEmpty();
            } else {
                log("has data");
                recyclerView.showRecycler();
            }
        }
    }

    /**
     * 设置适配器，关闭所有副view。展示recyclerView
     * 适配器有更新，自动关闭所有副view。根据条数判断是否展示EmptyView
     *
     * @param adapter
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecycler.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new EasyDataObserver(this));
        showRecycler();
    }

    /**
     * 设置适配器，关闭所有副view。展示进度条View
     * 适配器有更新，自动关闭所有副view。根据条数判断是否展示EmptyView
     *
     * @param adapter
     */
    public void setAdapterWithProgress(RecyclerView.Adapter adapter) {
        mRecycler.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new EasyDataObserver(this));
        //只有Adapter为空时才显示ProgressView
        if (adapter instanceof RecyclerArrayAdapter) {
            if (((RecyclerArrayAdapter) adapter).getCount() == 0) {
                showProgress();
            } else {
                showRecycler();
            }
        } else {
            if (adapter.getItemCount() == 0) {
                showProgress();
            } else {
                showRecycler();
            }
        }
    }

    /**
     * Remove the adapter from the recycler
     */
    public void clear() {
        mRecycler.setAdapter(null);
    }


    private void hideAll() {
        mEmptyView.setVisibility(View.GONE);
        mProgressView.setVisibility(View.GONE);
        mErrorView.setVisibility(GONE);
//        mPtrLayout.setRefreshing(false);
        mRecycler.setVisibility(View.INVISIBLE);
    }


    public void showError() {
        log("showError");
        if (mErrorView.getChildCount() > 0) {
            hideAll();
            mErrorView.setVisibility(View.VISIBLE);
        } else {
            showRecycler();
        }

    }

    public void showEmpty() {
        log("showEmpty");
        if (mEmptyView.getChildCount() > 0) {
            hideAll();
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            showRecycler();
        }
    }


    public void showProgress() {
        log("showProgress");
        if (mProgressView.getChildCount() > 0) {
            hideAll();
            mProgressView.setVisibility(View.VISIBLE);
        } else {
            showRecycler();
        }
    }


    public void showRecycler() {
        log("showRecycler");
        hideAll();
        mRecycler.setVisibility(View.VISIBLE);
    }

    public void showTipViewAndDelayClose(String tip) {
        tipView.setText(tip);
        Animation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        tipView.startAnimation(mShowAction);
        tipView.setVisibility(View.VISIBLE);

        tipView.postDelayed(() -> {
            Animation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    -1.0f);
            mHiddenAction.setDuration(500);
            tipView.startAnimation(mHiddenAction);
            tipView.setVisibility(View.GONE);
        }, 2200);
    }

    public void showTipView(String tip) {
        tipView.setText(tip);
        Animation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        tipView.startAnimation(mShowAction);
        tipView.setVisibility(View.VISIBLE);
    }

    public void hideTipView(long delayMillis) {
        tipView.postDelayed(() -> {
            Animation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    -1.0f);
            mHiddenAction.setDuration(500);
            tipView.startAnimation(mHiddenAction);
            tipView.setVisibility(View.GONE);
        }, delayMillis);
    }

    public void setTipViewText(String tip) {
        if (!isTipViewVisible())
            showTipView(tip);
        else
            tipView.setText(tip);
    }

    public boolean isTipViewVisible() {
        return tipView.getVisibility() == View.VISIBLE;
    }


    /**
     * Set the listener when refresh is triggered and enable the SwipeRefreshLayout
     *
     * @param listener
     */
    public void setRefreshListener(final OnRefreshListener listener) {
        mPtrLayout.setEnabled(true);
        mPtrLayout.setOnRefreshListener(listener);
        View errorView = getErrorView();
        if (errorView != null) {
            errorView.findViewById(R.id.tv_retry).setOnClickListener(v -> listener.onRefresh());
        }
        this.mRefreshListener = listener;
    }

    public void setRefreshing(final boolean isRefreshing) {
        mPtrLayout.post(() -> {
            if (isRefreshing) { // 避免刷新的loadding和progressview 同时显示
                mProgressView.setVisibility(View.GONE);
            }
            mPtrLayout.setRefreshing(isRefreshing);
        });
    }

    public void setRefreshing(final boolean isRefreshing, final boolean isCallbackListener) {
        mPtrLayout.post(() -> {
            mPtrLayout.setRefreshing(isRefreshing);
            if (isRefreshing && isCallbackListener && mRefreshListener != null) {
                mRefreshListener.onRefresh();
            }
        });
    }

    /**
     * Set the colors.xml for the SwipeRefreshLayout states
     *
     * @param colRes
     */
    public void setRefreshingColorResources(@ColorRes int... colRes) {
        mPtrLayout.setColorSchemeResources(colRes);
    }

    /**
     * Set the colors.xml for the SwipeRefreshLayout states
     *
     * @param col
     */
    public void setRefreshingColor(int... col) {
        mPtrLayout.setColorSchemeColors(col);
    }

    /**
     * Set the scroll listener for the recycler
     *
     * @param listener
     */
    public void setOnScrollListener(RecyclerView.OnScrollListener listener) {
        mExternalOnScrollListener = listener;
    }

    /**
     * Add the onItemTouchListener for the recycler
     *
     * @param listener
     */
    public void addOnItemTouchListener(RecyclerView.OnItemTouchListener listener) {
        mRecycler.addOnItemTouchListener(listener);
    }

    /**
     * Remove the onItemTouchListener for the recycler
     *
     * @param listener
     */
    public void removeOnItemTouchListener(RecyclerView.OnItemTouchListener listener) {
        mRecycler.removeOnItemTouchListener(listener);
    }

    /**
     * @return the recycler adapter
     */
    public RecyclerView.Adapter getAdapter() {
        return mRecycler.getAdapter();
    }


    public void setOnTouchListener(OnTouchListener listener) {
        mRecycler.setOnTouchListener(listener);
    }

    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        mRecycler.setItemAnimator(animator);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecycler.addItemDecoration(itemDecoration);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration, int index) {
        mRecycler.addItemDecoration(itemDecoration, index);
    }

    public void removeItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecycler.removeItemDecoration(itemDecoration);
    }

    public void removeAllItemDecoration() {
        for (RecyclerView.ItemDecoration decoration : decorations) {
            mRecycler.removeItemDecoration(decoration);
        }
    }


    /**
     * @return inflated error view or null
     */
    public View getErrorView() {
        if (mErrorView.getChildCount() > 0) return mErrorView.getChildAt(0);
        return null;
    }

    /**
     * @return inflated progress view or null
     */
    public View getProgressView() {
        if (mProgressView.getChildCount() > 0) return mProgressView.getChildAt(0);
        return null;
    }


    /**
     * @return inflated empty view or null
     */
    public View getEmptyView() {
        if (mEmptyView.getChildCount() > 0) return mEmptyView.getChildAt(0);
        return null;
    }

    private static void log(String content) {
        if (DEBUG) {
            Log.i(TAG, content);
        }
    }

}
