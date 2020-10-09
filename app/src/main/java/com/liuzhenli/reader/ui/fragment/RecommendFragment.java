package com.liuzhenli.reader.ui.fragment;

import android.os.Bundle;

import com.liuzhenli.reader.base.BaseRVFragment;
import com.liuzhenli.reader.bean.BookSourceData;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.ui.adapter.RecommendFragmentAdapter;
import com.liuzhenli.reader.ui.contract.RecommendContract;
import com.liuzhenli.reader.ui.presenter.RecommendPresenter;
import com.liuzhenli.reader.view.recyclerview.EasyRecyclerView;
import com.microedu.reader.R;

import java.util.List;

import butterknife.BindView;

/**
 * describe:推荐书源列表
 *
 * @author Liuzhenli on 2019-11-09 22:28
 */
public class RecommendFragment extends BaseRVFragment<RecommendPresenter, BookSourceData> implements RecommendContract.View {
    @BindView(R.id.recyclerView)
    EasyRecyclerView mRecyclerView;

    public static RecommendFragment getInstance() {
        RecommendFragment instance = new RecommendFragment();
        Bundle bundle = new Bundle();
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_recommend;
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
        initAdapter(RecommendFragmentAdapter.class, true, false, true);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mPresenter.getSource();
    }

    @Override
    public void showSource(List<BookSourceData> bookSourceData) {
        mAdapter.clear();
        mAdapter.addAll(bookSourceData);
    }


    @Override
    public void onItemClick(int position) {

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
