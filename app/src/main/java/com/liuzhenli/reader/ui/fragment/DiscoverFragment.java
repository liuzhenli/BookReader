package com.liuzhenli.reader.ui.fragment;

import android.os.Bundle;

import com.liuzhenli.reader.base.BaseRVFragment;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.ui.activity.BookListActivity;
import com.liuzhenli.reader.ui.adapter.DiscoverFragmentAdapter;
import com.liuzhenli.reader.ui.contract.DiscoverContract;
import com.liuzhenli.reader.ui.presenter.DiscoverPresenter;
import com.liuzhenli.reader.view.recyclerview.EasyRecyclerView;
import com.micoredu.readerlib.bean.BookSourceBean;
import com.microedu.reader.R;


import java.util.List;

import butterknife.BindView;

/**
 * describe:推荐书源列表
 *
 * @author Liuzhenli on 2019-11-09 22:28
 */
public class DiscoverFragment extends BaseRVFragment<DiscoverPresenter, BookSourceBean> implements DiscoverContract.View {
    @BindView(R.id.recyclerView)
    EasyRecyclerView mRecyclerView;

    public static DiscoverFragment getInstance() {
        DiscoverFragment instance = new DiscoverFragment();
        Bundle bundle = new Bundle();
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_discover;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        appComponent.inject(this);
    }


    @Override
    public void initData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void configViews() {
        mPresenter.getSource();
        initAdapter(DiscoverFragmentAdapter.class, true, false, true);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mPresenter.getSource();
    }

    @Override
    public void showSource(List<BookSourceBean> bookSourceData) {
        mRecyclerView.setRefreshing(false);
        mAdapter.clear();
        mAdapter.addAll(bookSourceData);
    }


    @Override
    public void onItemClick(int position) {
        BookListActivity.start(mContext,mAdapter.getItem(position));
    }

    @Override
    public void showError(Exception e) {
        mRecyclerView.setRefreshing(false);
    }

    @Override
    public void complete() {
        mRecyclerView.setRefreshing(false);
    }
}
