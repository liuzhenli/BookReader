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
import com.liuzhenli.reader.ui.adapter.BookMarkAdapter;
import com.liuzhenli.reader.utils.DataDiffUtil;
import com.liuzhenli.reader.utils.ToastUtil;
import com.liuzhenli.reader.view.loading.DialogUtil;
import com.liuzhenli.reader.view.recyclerview.adapter.RecyclerArrayAdapter;
import com.liuzhenli.reader.view.recyclerview.swipe.OnRefreshListener;
import com.liuzhenli.reader.view.recyclerview.swipe.ZLSwipeRefreshLayout;
import com.micoredu.readerlib.bean.BookChapterBean;
import com.micoredu.readerlib.bean.BookmarkBean;
import com.micoredu.readerlib.helper.BookshelfHelper;
import com.microedu.reader.R;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import butterknife.BindView;

public class BookMarkFragment extends BaseFragment {

    @BindView(R.id.zl_swipe_refresh)
    ZLSwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private BookMarkAdapter mAdapter;

    private boolean mIsFromReadPage;

    public static BookMarkFragment getInstance(boolean isFromReadPage) {
        BookMarkFragment instance = new BookMarkFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFromReadPage", isFromReadPage);
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
        mIsFromReadPage = getArguments() != null && getArguments().getBoolean("isFromReadPage");
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
        mAdapter = new BookMarkAdapter(mContext,mIsFromReadPage);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.addAll(getParentActivity().getBookMarkList());
        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                BookmarkBean item = mAdapter.getItem(position);
                getParentActivity().getBookShelf().setFinalDate(System.currentTimeMillis());
                BookshelfHelper.saveBookToShelf(getParentActivity().getBookShelf());

                Intent intent = new Intent(getContext(), ReaderActivity.class);
                intent.putExtra(ReaderActivity.OPEN_FROM, Constant.BookOpenFrom.FROM_BOOKSHELF);
                intent.putExtra(ReaderActivity.CHAPTER_ID, item.getChapterIndex());

                intent.putExtra(ReaderActivity.PROGRESS, item.getPageIndex());

                String bookKey = String.valueOf(System.currentTimeMillis());
                intent.putExtra(BitIntentDataManager.DATA_KEY, bookKey);
                BitIntentDataManager.getInstance().putData(bookKey, getParentActivity().getBookShelf().clone());

                mContext.startActivity(intent);

            }
        });
        mAdapter.setOnItemLongClickListener(position -> {
            DialogUtil.showMessagePositiveDialog(mContext, "提示", "是否删除该书签?", "取消", null, "删除", new QMUIDialogAction.ActionListener() {
                @Override
                public void onClick(QMUIDialog dialog, int index) {
                    BookshelfHelper.delBookmark(mAdapter.getItem(position));
                    mAdapter.remove(position);
                    ToastUtil.showToast("书签已删除");
                }
            }, true);
            return false;
        });
    }

    private BookChapterListActivity getParentActivity() {
        return (BookChapterListActivity) getActivity();
    }

    public void refreshData() {
        if (mAdapter.getCount() > 0) {
            DataDiffUtil.diffResult(mAdapter, getParentActivity().getBookMarkList(), new DataDiffUtil.ItemSameCallBack<BookmarkBean>() {
                @Override
                public boolean isItemSame(BookmarkBean oldItem, BookmarkBean newItem) {
                    return oldItem != null && newItem != null;
                }

                @Override
                public boolean isContentSame(BookmarkBean oldItem, BookmarkBean newItem) {
                    return oldItem.getId() == newItem.getId();
                }
            });
        } else {
            mAdapter.clear();
            mAdapter.addAll(getParentActivity().getBookMarkList());
        }
    }
}
