package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.reader.ReaderApplication;
import com.liuzhenli.reader.base.BaseActivity;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.utils.ToastUtil;
import com.microedu.reader.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description:
 *
 * @author liuzhenli 2020/8/10
 * Email: 848808263@qq.com
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.tv_version_info)
    TextView tvVersionInfo;
    @BindView(R.id.tv_version_check_update)
    TextView tvVersionCheckUpdate;
    @BindView(R.id.tv_about_contact)
    TextView tvAboutContact;

    public static void start(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initToolBar() {
        mTvTitle.setText("关于");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void configViews() {
        String appName = getResources().getString(R.string.app_name);
        String versionName = ReaderApplication.getInstance().mVersionName;
        int versionCode = ReaderApplication.getInstance().mVersionCode;
        String channel = ReaderApplication.getInstance().mVersionChannel;
        tvVersionInfo.setText(String.format("%s %s Build#%s %s ", appName, versionName, versionCode, channel));
        ClickUtils.click(tvVersionCheckUpdate, o -> {
            ToastUtil.showCenter("程序猿正在努力开发该功能~");
        });

        ClickUtils.click(tvAboutContact, o -> {
            //发送邮件
        });
    }

}
