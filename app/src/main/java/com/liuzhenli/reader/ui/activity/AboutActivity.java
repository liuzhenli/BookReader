package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.TextView;

import androidx.core.text.StringKt;

import com.liuzhenli.common.BitIntentDataManager;
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
public class AboutActivity extends BaseActivity {

    @BindView(R.id.tv_version_info)
    TextView tvVersionInfo;
    @BindView(R.id.tv_version_check_update)
    TextView tvVersionCheckUpdate;
    @BindView(R.id.tv_about_contact)
    TextView tvAboutContact;
    @BindView(R.id.tv_disclaimer)
    TextView tvDisclaimer;

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
            ToastUtil.showToast("程序猿正在努力开发该功能~");
        });

        ClickUtils.click(tvAboutContact, o -> {
            //发送邮件
            openIntent(Intent.ACTION_SENDTO, "mailto:hpuzhenli@163.com");
        });
        ClickUtils.click(tvDisclaimer, o -> {
            //免责声明
            String key = System.currentTimeMillis() + "";
            BitIntentDataManager.getInstance().putData(key, mContext.getResources().getString(R.string.disclaimer_content));
            ContentActivity.start(mContext, key, "免责声明");
        });
    }

    void openIntent(String intentName, String address) {
        try {
            Intent intent = new Intent(intentName);
            intent.setData(Uri.parse(address));
            startActivity(intent);
        } catch (Exception e) {
            toast(getResources().getString(R.string.can_not_open));
        }
    }

}
