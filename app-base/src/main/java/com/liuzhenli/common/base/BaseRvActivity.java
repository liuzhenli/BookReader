package com.liuzhenli.common.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewbinding.ViewBinding;

import com.liuzhenli.common.R;
import com.liuzhenli.common.utils.NetworkUtils;
import com.liuzhenli.common.widget.recyclerview.adapter.OnLoadMoreListener;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.liuzhenli.common.widget.recyclerview.swipe.OnRefreshListener;

import java.lang.reflect.Constructor;

/**
 * @author Liuzhenli
 * @since 2019-07-07 08:38
 */
public abstract class BaseRvActivity<P extends BaseContract.BasePresenter, DATA, VB extends ViewBinding> extends BaseActivity<P, VB> implements OnLoadMoreListener, OnRefreshListener, RecyclerArrayAdapter.OnItemClickListener {

    protected RecyclerArrayAdapter<DATA> mAdapter;

    /**
     * 页数
     */
    protected int mPage = 1;
    /**
     * 每页有几条数据
     */
    protected int mCount = 20;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initAdapter(boolean refreshable, boolean loadMoreAble) {
        initAdapter(refreshable, loadMoreAble, null);
    }

    private void initAdapter(boolean refreshable, boolean loadmoreable, View view) {
        if (mAdapter != null) {
            mAdapter.setOnItemClickListener(this);
            mAdapter.setError(R.layout.common_error_view).setOnClickListener(v -> mAdapter.resumeMore());
            if (loadmoreable) {
                mAdapter.setMore(R.layout.common_more_view, this);
                mAdapter.setNoMore(R.layout.common_nomore_view);
            }
            if (view != null) {
                mAdapter.setZeroView(view);
            }
            if (refreshable && mRecyclerView != null) {
                mRecyclerView.setRefreshListener(this);
            }
        }
        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setItemDecoration(ContextCompat.getColor(this, R.color.divider), 1, 0, 0);
            mRecyclerView.setAdapterWithProgress(mAdapter);
        }
    }


    protected void initAdapter(Class<? extends RecyclerArrayAdapter<DATA>> clazz, boolean refreshable, boolean loadMoreAble) {
        mAdapter = (RecyclerArrayAdapter) createInstance(clazz);
        initAdapter(refreshable, loadMoreAble);
    }

    protected void initAdapter(Class<? extends RecyclerArrayAdapter<DATA>> clazz, boolean refreshable, boolean loadMoreAble, View zeroView) {
        mAdapter = (RecyclerArrayAdapter) createInstance(clazz);
        initAdapter(refreshable, loadMoreAble, zeroView);
    }

    private Object createInstance(Class<?> cls) {
        Object obj;
        try {
            Constructor c1 = cls.getDeclaredConstructor(Context.class);
            c1.setAccessible(true);
            obj = c1.newInstance(mContext);
        } catch (Exception e) {
            obj = null;
        }
        return obj;
    }

    @Override
    public void onLoadMore() {
        if (!NetworkUtils.isConnected(getApplicationContext())) {
            mAdapter.pauseMore();
        }
    }

    @Override
    public void onRefresh() {
        mPage = 0;
        if (!NetworkUtils.isConnected(getApplicationContext())) {
            mAdapter.pauseMore();
        }
    }

    protected void loaddingError() {
        if (mAdapter.getCount() == 0) {
            mAdapter.clear();
        }
        mAdapter.pauseMore();
        mRecyclerView.setRefreshing(false);
    }
}
