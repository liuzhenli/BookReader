package com.micoredu.reader.ui.activity;

import android.view.LayoutInflater;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.liuzhenli.common.constant.ARouterConstants;
import com.liuzhenli.common.widget.DialogUtil;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.micoredu.reader.R;
import com.micoredu.reader.ReaderBaseRVActivity;
import com.micoredu.reader.ReaderComponent;
import com.micoredu.reader.bean.ReadHistory;
import com.micoredu.reader.databinding.ActReadhistoryBinding;
import com.micoredu.reader.helper.AppReaderDbHelper;
import com.micoredu.reader.ui.adapter.ReadHistoryAdapter;
import com.micoredu.reader.ui.contract.ReadHistoryContract;
import com.micoredu.reader.ui.presenter.ReadHistoryPresenter;

import java.util.List;

/**
 * Description:read history page
 *
 * @author liuzhenli 2021/10/12
 * Email: 848808263@qq.com
 */
@Route(path = ARouterConstants.ACT_READ_HISTORY)
public class ReadHistoryActivity extends ReaderBaseRVActivity<ReadHistoryPresenter, ReadHistory,
        ActReadhistoryBinding> implements ReadHistoryContract.View, RecyclerArrayAdapter.OnItemLongClickListener {

    @Override
    protected void setupActivityComponent(ReaderComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    protected ActReadhistoryBinding inflateView(LayoutInflater inflater) {
        return ActReadhistoryBinding.inflate(inflater);
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
        mAdapter.setOnItemLongClickListener(this);
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
        ReadHistory item = mAdapter.getItem(position);
        ReaderActivity.start(this, item.noteUrl, null, -1, null);
    }

    @Override
    public void showHistory(List<ReadHistory> data) {
        mAdapter.addAll(data);
    }

    @Override
    public void showDeleteResult() {

    }


    @Override
    public boolean onItemLongClick(int position) {
        String msg = String.format("是否删除《%s》的阅读记录?", mAdapter.getItem(position).bookName);
        DialogUtil.showMessagePositiveDialog(mContext, getResources().getString(R.string.dialog_title),
                msg, getResources().getString(R.string.dialog_cancel), (dialog, index) -> {

                }, getResources().getString(R.string.ok), (dialog, index) -> {
                    AppReaderDbHelper.getInstance().getDatabase().getReadHistoryDao().deleteByBookName(mAdapter.getItem(position).bookName);
                    mAdapter.remove(position);
                }, true);
        return false;
    }
}
