package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;

import com.liuzhenli.common.BitIntentDataManager;
import com.liuzhenli.reader.base.BaseActivity;
import com.liuzhenli.reader.network.AppComponent;
import com.microedu.reader.R;

import butterknife.BindView;

/**
 * Description:
 *
 * @author liuzhenli 2020/11/18
 * Email: 848808263@qq.com
 */
public class ContentActivity extends BaseActivity {

    @BindView(R.id.tv_content)
    TextView mTvContent;

    private String mTitle;
    private String mContent;

    public static void start(Context context, String contentKey, String title) {
        Intent intent = new Intent(context, ContentActivity.class);
        intent.putExtra(BitIntentDataManager.DATA_KEY, contentKey);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_content;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initToolBar() {
    }

    @Override
    protected void initData() {
        String key = getIntent().getStringExtra(BitIntentDataManager.DATA_KEY);
        if (!TextUtils.isEmpty(key)) {
            mContent = (String) BitIntentDataManager.getInstance().getData(key);
        }
        mTitle = getIntent().getStringExtra("title");
    }

    @Override
    protected void configViews() {
        mTvTitle.setText(mTitle);
        mTvContent.setText(mContent);
    }
}
