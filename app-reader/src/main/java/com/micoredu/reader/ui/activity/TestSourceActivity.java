package com.micoredu.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.liuzhenli.common.constant.RxBusTag;
import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.common.base.BaseRvActivity;
import com.micoredu.reader.DaggerReaderComponent;
import com.microedu.lib.reader.databinding.ActSourcetestBinding;
import com.micoredu.reader.model.Debug;
import com.micoredu.reader.ui.adapter.TestSourceAdapter;
import com.micoredu.reader.ui.contract.TestSourceContract;
import io.reactivex.functions.Consumer;

/**
 * Description:
 *
 * @author liuzhenli 2020/11/18
 * Email: 848808263@qq.com
 */
/*
public class TestSourceActivity extends BaseRvActivity<TestSourcePresenter, String,ActSourcetestBinding> implements TestSourceContract.View {

    private String mBookSourceUrl;

    public static void start(Context context, String bookSourceUrl) {
        Intent intent = new Intent(context, TestSourceActivity.class);
        intent.putExtra("bookSourceUrl", bookSourceUrl);
        context.startActivity(intent);
    }


    @Override
    protected ActSourcetestBinding inflateView(LayoutInflater inflater) {
        return ActSourcetestBinding.inflate(inflater);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerReaderComponent.builder().build().inject(this);
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
        ClickUtils.click(binding.mBtnSearch, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                startTest(binding.mEtContent.getText().toString());
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
        binding.mSearchIndicator.setVisibility(View.VISIBLE);
        mAdapter.clear();
        Debug.INSTANCE.newDebug(mBookSourceUrl, content, mPresenter.getCompositeDisposable());

    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(RxBusTag.PRINT_DEBUG_LOG)})
    public void printDebugLog(String msg) {
        mAdapter.add(msg);
        if (msg.equals("finish")) {
            hideDialog();
            binding.mSearchIndicator.setVisibility(View.GONE);
        }
    }
}


 */