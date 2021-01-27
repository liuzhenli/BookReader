package com.liuzhenli.reader.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liuzhenli.common.BitIntentDataManager;
import com.liuzhenli.common.utils.Constant;
import com.liuzhenli.reader.base.BaseFragment;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.ui.activity.BookChapterListActivity;
import com.liuzhenli.reader.ui.activity.ReaderActivity;
import com.liuzhenli.reader.ui.adapter.BookChapterAdapter;
import com.liuzhenli.reader.utils.DataDiffUtil;
import com.liuzhenli.reader.view.recyclerview.adapter.RecyclerArrayAdapter;
import com.liuzhenli.reader.view.recyclerview.swipe.OnRefreshListener;
import com.liuzhenli.reader.view.recyclerview.swipe.ZLSwipeRefreshLayout;
import com.micoredu.readerlib.bean.BookChapterBean;
import com.micoredu.readerlib.bean.BookShelfBean;
import com.micoredu.readerlib.helper.BookshelfHelper;
import com.microedu.reader.R;

import butterknife.BindView;

public class BookChapterListFragment extends BaseFragment {

    @BindView(R.id.zl_swipe_refresh)
    ZLSwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private BookChapterAdapter mAdapter;

    public static BookChapterListFragment getInstance() {
        BookChapterListFragment instance = new BookChapterListFragment();
        Bundle bundle = new Bundle();
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_bookchapterlist;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void attachView() {
    }

    @Override
    public void initData() {

    }

    @Override
    public void configViews() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setRefreshing(false);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new BookChapterAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.addAll(getParentActivity().getChapterBeanList());
        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                BookChapterBean item = mAdapter.getItem(position);
                getParentActivity().getBookShelf().setFinalDate(System.currentTimeMillis());
                BookshelfHelper.saveBookToShelf(getParentActivity().getBookShelf());

                Intent intent = new Intent(getContext(), ReaderActivity.class);
                intent.putExtra(ReaderActivity.OPEN_FROM, Constant.BookOpenFrom.FROM_BOOKSHELF);
                intent.putExtra(ReaderActivity.CHAPTER_ID, item.getDurChapterIndex());

                String bookKey = String.valueOf(System.currentTimeMillis());
                intent.putExtra(BitIntentDataManager.DATA_KEY, bookKey);
                BitIntentDataManager.getInstance().putData(bookKey, getParentActivity().getBookShelf().clone());

                mContext.startActivity(intent);

            }
        });
    }

    private BookChapterListActivity getParentActivity() {
        return (BookChapterListActivity) getActivity();
    }

    public void refreshData() {
        if (mAdapter.getCount() > 0) {
            DataDiffUtil.diffResult(mAdapter, getParentActivity().getChapterBeanList(), new DataDiffUtil.ItemSameCallBack<BookChapterBean>() {
                @Override
                public boolean isItemSame(BookChapterBean oldItem, BookChapterBean newItem) {
                    return oldItem != null && newItem != null;
                }

                @Override
                public boolean isContentSame(BookChapterBean oldItem, BookChapterBean newItem) {
                    return oldItem.getDurChapterIndex() == newItem.getDurChapterIndex();
                }
            });
        } else {
            mAdapter.clear();
            mAdapter.addAll(getParentActivity().getChapterBeanList());
        }

    }
}
