package com.micoredu.reader.ui.activity;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.liuzhenli.common.constant.ARouterConstants;
import com.micoredu.reader.R;
import com.micoredu.reader.ReaderBaseRVActivity;
import com.micoredu.reader.ReaderComponent;
import com.micoredu.reader.bean.ReadHistory;
import com.micoredu.reader.databinding.ActReadhistoryBinding;
import com.micoredu.reader.ui.adapter.ReadHistoryAdapter;
import com.micoredu.reader.ui.contract.ReadHistoryContract;
import com.micoredu.reader.ui.presenter.ReadHistoryPresenter;

import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2021/10/12
 * Email: 848808263@qq.com
 */
@Route(path = ARouterConstants.ACT_READ_HISTORY)
public class ReadHistoryActivity extends ReaderBaseRVActivity<ReadHistoryPresenter, ReadHistory> implements ReadHistoryContract.View {


    ActReadhistoryBinding binding;

    @Override
    protected View bindContentView() {
        binding = ActReadhistoryBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }


    @Override
    protected void setupActivityComponent(ReaderComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    protected void initToolBar() {
        mTvTitle.setText(R.string.read_history);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void configViews() {
        initAdapter(ReadHistoryAdapter.class, false, false);
        mPresenter.getHistory();
    }

    @Override
    public void showError(Exception e) {

    }

    @Override
    public void complete() {

    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void showHistory(List<ReadHistory> data) {
        mAdapter.addAll(data);
    }

    @Override
    public void showDeleteResult() {

    }


}
