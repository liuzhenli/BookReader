package com.liuzhenli.reader.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import com.liuzhenli.common.BitIntentDataManager;
import com.liuzhenli.common.constant.AppConstant;
import com.liuzhenli.reader.base.BaseActivity;
import com.liuzhenli.reader.base.BaseBean;
import com.liuzhenli.reader.base.BaseRVFragment;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.ui.activity.BookDetailActivity;
import com.liuzhenli.reader.ui.activity.ReaderActivity;
import com.liuzhenli.reader.ui.adapter.BookListAdapter;
import com.liuzhenli.reader.ui.contract.BookCategoryContract;
import com.liuzhenli.reader.ui.presenter.BookCategoryPresenter;
import com.micoredu.readerlib.bean.BookShelfBean;
import com.micoredu.readerlib.bean.SearchBookBean;
import com.microedu.reader.R;

import java.util.List;

import static com.liuzhenli.common.BitIntentDataManager.DATA_KEY;

/**
 * Description:某个分类下的书列表
 *
 * @author liuzhenli 2020/10/20
 * Email: 848808263@qq.com
 */
public class BookCategoryFragment extends BaseRVFragment<BookCategoryPresenter, SearchBookBean> implements BookCategoryContract.View {

    public static final String URL = "url";
    public static final String PAGE = "page";
    public static final String TAG = "tag";


    private String mUrl;
    private String mTag;

    public static BookCategoryFragment getInstance(String url, String tag) {
        BookCategoryFragment instance = new BookCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(URL, url);
        bundle.putString(TAG, tag);
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_booklist;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public void initData() {
        if (getArguments() != null) {
            mUrl = getArguments().getString(URL);
            mTag = getArguments().getString(TAG);
        }
    }

    @Override
    public void configViews() {
        initAdapter(BookListAdapter.class, true, true, true);
        mPresenter.getBookList(mUrl, mPage, mTag);
    }

    @Override
    public void onItemClick(int position) {
        SearchBookBean item = mAdapter.getItem(position);
        String dataKey = String.valueOf(System.currentTimeMillis());
        Intent intent = new Intent(mContext, BookDetailActivity.class);
        intent.putExtra(BookDetailActivity.OPEN_FROM, AppConstant.BookOpenFrom.OPEN_FROM_SEARCH);
        intent.putExtra(DATA_KEY, dataKey);
        BitIntentDataManager.getInstance().putData(dataKey, item);
        ((BaseActivity) activity).startActivityByAnim(intent, android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        mPage++;
        mPresenter.getBookList(mUrl, mPage, mTag);
    }

    @Override
    public void showError(Exception e) {

    }

    @Override
    public void complete() {

    }

    @Override
    public void showBookList(List<SearchBookBean> data) {
        mAdapter.clear();
        mAdapter.addAll(data);
    }
}
