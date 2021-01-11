package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.text.StringKt;

import com.liuzhenli.common.BaseApplication;
import com.liuzhenli.common.BitIntentDataManager;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.common.utils.Constant;
import com.liuzhenli.reader.ReaderApplication;
import com.liuzhenli.reader.base.BaseActivity;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.utils.AppConfigManager;
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

    @BindView(R.id.tv_gg_group_1)
    TextView mTvQQGroup1;
    @BindView(R.id.tv_gg_group_0)
    TextView mTvQQGroup0;

    @BindView(R.id.iv_new_version_icon)
    ImageView mIvNewVersionIcon;
    @BindView(R.id.tv_new_version_info)
    TextView mTvNewVersionInfo;

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
            //版本有更新
            if (AppConfigManager.getInstance().getNewVersion() > BaseApplication.getInstance().mVersionCode) {
                toast(AppConfigManager.getInstance().getNewVersionIntro());
            } else {
                toast("已经是最新版本");
            }
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

        configQQGroup();

        //版本有更新
        if (AppConfigManager.getInstance().getNewVersion() > BaseApplication.getInstance().mVersionCode) {
            mIvNewVersionIcon.setVisibility(View.VISIBLE);
            mTvNewVersionInfo.setVisibility(View.VISIBLE);
        } else {
            mIvNewVersionIcon.setVisibility(View.GONE);
            mTvNewVersionInfo.setVisibility(View.GONE);
        }

    }

    private void configQQGroup() {
        ClickUtils.click(mTvQQGroup0, o -> joinQQGroup(Constant.QQGroup.QQ_272343970));
        ClickUtils.click(mTvQQGroup1, o -> joinQQGroup(Constant.QQGroup.QQ_1140723995));
    }

    /****************
     *
     * 发起添加群流程。群号：阅读①(1140723995) 的 key 为： py5-vU4j3y7mobTS3IkZMKKJAFbiKRgl
     * 调用 joinQQGroup(py5-vU4j3y7mobTS3IkZMKKJAFbiKRgl) 即可发起手Q客户端申请加群 阅读①(1140723995)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回false表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
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
