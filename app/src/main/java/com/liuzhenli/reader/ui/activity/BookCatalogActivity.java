package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.liuzhenli.common.constant.AppConstant;
import com.liuzhenli.reader.base.BaseActivity;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.ui.contract.BookCatalogContract;
import com.liuzhenli.reader.ui.presenter.BookCatalogPresenter;
import com.microedu.reader.R;

/**
 * 书目录
 *
 * @author liuzhenli 2019.12.13
 */
public class BookCatalogActivity extends BaseActivity<BookCatalogPresenter> implements BookCatalogContract.View {
    private String mBookId;
    private String mBookPath;

    public static void start(Context context, String bookId,String localPath) {
        Intent intent = new Intent(context, BookCatalogActivity.class);
        intent.putExtra(AppConstant.BOOK_ID, bookId);
        intent.putExtra(AppConstant.LOCAL_BOOK_PATH, localPath);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_bookcatalog);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_bookcatalog;
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
        mBookId = getIntent().getStringExtra(AppConstant.BOOK_ID);
        mBookPath = getIntent().getStringExtra(AppConstant.LOCAL_BOOK_PATH);
    }

    @Override
    protected void configViews() {

    }

    @Override
    public void showError(Exception e) {

    }

    @Override
    public void complete() {

    }
}
