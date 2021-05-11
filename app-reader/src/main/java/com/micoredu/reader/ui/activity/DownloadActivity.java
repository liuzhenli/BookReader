package com.micoredu.reader.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.micoredu.reader.R;
import com.micoredu.reader.ReaderBaseRVActivity;
import com.micoredu.reader.ReaderComponent;
import com.micoredu.reader.databinding.ActDownloadBinding;
import com.micoredu.reader.service.DownloadService;
import com.micoredu.reader.bean.DownloadBookBean;
import com.micoredu.reader.ui.adapter.DownloadAdapter;
import com.micoredu.reader.ui.contract.DownloadContract;
import com.micoredu.reader.ui.presenter.DownloadPresenter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Description:
 *
 * @author liuzhenli 2021/2/2
 * Email: 848808263@qq.com
 */
public class DownloadActivity extends ReaderBaseRVActivity<DownloadPresenter, DownloadBookBean> implements DownloadContract.View {

    private DownloadReceiver receiver;
    private ActDownloadBinding binding;

    @Override
    protected View bindContentView() {
        binding = ActDownloadBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void setupActivityComponent(ReaderComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    protected void initToolBar() {
        mTvTitle.setText("离线下载");
    }

    @Override
    protected void initData() {
        receiver = new DownloadReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.addDownloadAction);
        filter.addAction(DownloadService.removeDownloadAction);
        filter.addAction(DownloadService.progressDownloadAction);
        filter.addAction(DownloadService.obtainDownloadListAction);
        filter.addAction(DownloadService.finishDownloadAction);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void configViews() {
        initAdapter(DownloadAdapter.class, false, false);

    }

    // 添加菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_download, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //菜单
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_cancel) {
            DownloadService.cancelDownload(this);
        } else if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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

    private static class DownloadReceiver extends BroadcastReceiver {

        WeakReference<DownloadActivity> ref;

        public DownloadReceiver(DownloadActivity activity) {
            this.ref = new WeakReference<>(activity);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            DownloadAdapter adapter = (DownloadAdapter) ref.get().mAdapter;
            if (adapter == null || intent == null) {
                return;
            }
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case DownloadService.addDownloadAction:
                        DownloadBookBean downloadBook = intent.getParcelableExtra("downloadBook");
                        adapter.add(downloadBook);
                        break;
                    case DownloadService.removeDownloadAction:
                        downloadBook = intent.getParcelableExtra("downloadBook");
                        adapter.remove(downloadBook);
                        break;
                    case DownloadService.progressDownloadAction:
                        downloadBook = intent.getParcelableExtra("downloadBook");
                        int index = adapter.getRealAllData().indexOf(downloadBook);
                        if (index == -1) {
                            adapter.add(downloadBook);
                        } else {
                            adapter.getRealAllData().set(index, downloadBook);
                        }
                        adapter.notifyDataSetChanged();
                        //adapter.upData(downloadBook);
                        break;
                    case DownloadService.finishDownloadAction:
                        adapter.clear();
                        //adapter.upDataS(null);
                        break;
                    case DownloadService.obtainDownloadListAction:
                        ArrayList<DownloadBookBean> downloadBooks = intent.getParcelableArrayListExtra("downloadBooks");
                        adapter.clear();
                        adapter.addAll(downloadBooks);
                        //adapter.upDataS(downloadBooks);
                        break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }
}
