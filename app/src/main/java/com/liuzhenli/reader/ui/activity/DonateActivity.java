package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.reader.ReaderApplication;
import com.liuzhenli.reader.base.BaseActivity;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.utils.ToastUtil;
import com.microedu.reader.R;

import butterknife.BindView;

/**
 * Description:
 *
 * @author liuzhenli 2020/8/10
 * Email: 848808263@qq.com
 */
public class DonateActivity extends BaseActivity {


    public static void start(Context context) {
        Intent intent = new Intent(context, DonateActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_denote;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initToolBar() {
        mTvTitle.setText(R.string.donate);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void configViews() {

    }

}
