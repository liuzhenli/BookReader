package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

    @BindView(R.id.tv_donate_zhifubao_kouling)
    TextView mViewDonateAliPay;
    @BindView(R.id.tv_donate_qq)
    TextView mViewQQ;
    @BindView(R.id.tv_donate_weixin)
    TextView mViewWX;
    @BindView(R.id.tv_donate_zfb)
    TextView mViewZFB;

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
        ClickUtils.click(mViewDonateAliPay, o -> {
            Uri uri = Uri.parse("https://qr.alipay.com/fkx16537qfnbficmp9dohb4");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        ClickUtils.click(mViewZFB, o -> {
            Uri uri = Uri.parse("https://gitee.com/liuzhenli/Donate/blob/master/imgs/img_zhifubao.jpg");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        ClickUtils.click(mViewQQ, o -> {
            Uri uri = Uri.parse("https://gitee.com/liuzhenli/Donate/blob/master/imgs/img_qq.png");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        ClickUtils.click(mViewWX, o -> {
            Uri uri = Uri.parse("https://gitee.com/liuzhenli/Donate/blob/master/imgs/img_weixin.png");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }
}
