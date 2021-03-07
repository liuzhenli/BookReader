package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.liuzhenli.common.BitIntentDataManager;
import com.liuzhenli.common.base.BaseActivity;
import com.liuzhenli.common.AppComponent;
import com.microedu.reader.databinding.ActContentBinding;

/**
 * Description:
 *
 * @author liuzhenli 2020/11/18
 * Email: 848808263@qq.com
 */
public class ContentActivity extends BaseActivity {

    private String mTitle;
    private String mContent;
    private ActContentBinding binding;

    public static void start(Context context, String contentKey, String title) {
        Intent intent = new Intent(context, ContentActivity.class);
        intent.putExtra(BitIntentDataManager.DATA_KEY, contentKey);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    protected View bindContentView() {
        binding = ActContentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
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
        binding.mTvContent.setText(mContent);
    }
}
