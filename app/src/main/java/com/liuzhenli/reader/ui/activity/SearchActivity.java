package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.internal.FlowLayout;
import com.liuzhenli.reader.base.BaseActivity;
import com.liuzhenli.reader.network.AppComponent;
import com.microedu.reader.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description:
 *
 * @author liuzhenli 2020/8/10
 * Email: 848808263@qq.com
 */
public class SearchActivity extends BaseActivity {


    @BindView(R.id.edit_general_search_content)
    EditText editGeneralSearchContent;
    @BindView(R.id.btn_general_search_clear)
    ImageView btnGeneralSearchClear;
    @BindView(R.id.tv_action_cancel)
    TextView tvActionCancel;
    @BindView(R.id.tv_general_search_history_title)
    TextView tvGeneralSearchHistoryTitle;
    @BindView(R.id.iv_clear_search_history)
    ImageView ivClearSearchHistory;
    @BindView(R.id.block_general_search_history_title)
    LinearLayout blockGeneralSearchHistoryTitle;
    @BindView(R.id.fl_general_search_history)
    FlowLayout flGeneralSearchHistory;
    @BindView(R.id.fl_search_result)
    FrameLayout flSearchResult;

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initToolBar() {
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void configViews() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
