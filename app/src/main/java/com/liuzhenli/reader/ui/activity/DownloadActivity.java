package com.liuzhenli.reader.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.MenuItem;

import com.liuzhenli.reader.base.BaseRvActivity;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.service.DownloadService;
import com.liuzhenli.reader.ui.adapter.DownloadAdapter;
import com.liuzhenli.reader.ui.contract.DownloadContract;
import com.liuzhenli.reader.ui.presenter.DownloadPresenter;
import com.micoredu.readerlib.bean.DownloadBookBean;
import com.microedu.reader.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Description:
 *
 * @author liuzhenli 2021/2/2
 * Email: 848808263@qq.com
 */
public class DownloadActivity extends BaseRvActivity<DownloadPresenter, DownloadBookBean> implements DownloadContract.View {

    private DownloadReceiver receiver;

    @Override
    protected int getLayoutId() {
        return R.layout.act_download;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
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
        switch (id) {
            case R.id.action_cancel:
                DownloadService.cancelDownload(this);
                break;
            case android.R.id.home:
                finish();
                break;
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
