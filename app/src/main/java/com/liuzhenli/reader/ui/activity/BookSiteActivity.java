package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.liuzhenli.reader.base.BaseActivity;
import com.liuzhenli.reader.base.BaseBean;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.ui.contract.BookSiteContract;
import com.liuzhenli.reader.ui.presenter.BookSitePresenter;
import com.micoredu.readerlib.bean.BookSourceBean;
import com.microedu.reader.R;

import butterknife.BindView;

/**
 * Description:书源站点
 *
 * @author liuzhenli 2020/10/9
 * Email: 848808263@qq.com
 */
public class BookSiteActivity extends BaseActivity<BookSitePresenter> implements BookSiteContract.View {
    public static final String BOOK_SOURCE_DATA = "book_source_data";

    @BindView(R.id.fl_rank_menu_left)
    ListView flRankMenuLeft;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;

    private BookSourceBean mBookSource;

    public static void start(Context context, BookSourceBean data) {
        Intent intent = new Intent(context, BookSiteActivity.class);
        intent.putExtra(BOOK_SOURCE_DATA, data);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_booksite;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    protected void initToolBar() {
        if (mBookSource != null) {
            mTvTitle.setText(mBookSource.getBookSourceName());
        }
    }

    @Override
    protected void initData() {
        mBookSource = (BookSourceBean) getIntent().getSerializableExtra(BOOK_SOURCE_DATA);
    }

    @Override
    protected void configViews() {
        //flRankMenuLeft.setAdapter(new BookCategoryAdapter(mBookSource, mContext));

    }

    @Override
    public void showBookList(BaseBean data) {

    }

    @Override
    public void showError(Exception e) {

    }

    @Override
    public void complete() {

    }
}
