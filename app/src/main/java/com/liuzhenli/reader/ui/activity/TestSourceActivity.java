package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.liuzhenli.common.constant.RxBusTag;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.reader.base.BaseRvActivity;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.ui.adapter.TestSourceAdapter;
import com.liuzhenli.reader.ui.contract.TestSourceContract;
import com.liuzhenli.reader.ui.presenter.TestSourcePresenter;
import com.micoredu.readerlib.content.Debug;
import com.microedu.reader.R;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * Description:
 *
 * @author liuzhenli 2020/11/18
 * Email: 848808263@qq.com
 */
public class TestSourceActivity extends BaseRvActivity<TestSourcePresenter, String> implements TestSourceContract.View {

    @BindView(R.id.tv_test_content)
    EditText mEtContent;
    @BindView(R.id.tv_search)
    View mBtnSearch;
    @BindView(R.id.view_search_indicator)
    AVLoadingIndicatorView mLoadingView;

    private String mBookSourceUrl;

    public static void start(Context context, String bookSourceUrl) {
        Intent intent = new Intent(context, TestSourceActivity.class);
        intent.putExtra("bookSourceUrl", bookSourceUrl);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.act_sourcetest;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    protected void initToolBar() {

    }

    @Override
    protected void initData() {
        mBookSourceUrl = getIntent().getStringExtra("bookSourceUrl");
    }

    @Override
    protected void configViews() {
        initAdapter(TestSourceAdapter.class, false, false);
        ClickUtils.click(mBtnSearch, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                startTest(mEtContent.getText().toString());
            }
        });
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

    private void startTest(String content) {
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(mBookSourceUrl)) {
            toast("请输入测试内容");
            return;
        }
        mLoadingView.setVisibility(View.VISIBLE);
        mAdapter.clear();
        Debug.newDebug(mBookSourceUrl, content, mPresenter.getCompositeDisposable());

    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(RxBusTag.PRINT_DEBUG_LOG)})
    public void printDebugLog(String msg) {
        mAdapter.add(msg);
        if (msg.equals("finish")) {
            hideDialog();
            mLoadingView.setVisibility(View.GONE);
        }
    }
}
