package com.liuzhenli.reader.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.liuzhenli.common.BitIntentDataManager;
import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.utils.Constant;
import com.liuzhenli.common.base.BaseFragment;
import com.liuzhenli.reader.ui.activity.BookChapterListActivity;
import com.liuzhenli.reader.ui.activity.ReaderActivity;
import com.liuzhenli.reader.ui.adapter.BookMarkAdapter;
import com.liuzhenli.reader.utils.DataDiffUtil;
import com.liuzhenli.common.utils.ToastUtil;
import com.liuzhenli.reader.view.loading.DialogUtil;
import com.micoredu.readerlib.bean.BookmarkBean;
import com.micoredu.readerlib.helper.BookshelfHelper;
import com.microedu.reader.databinding.FragmentBookmarkBinding;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

public class BookMarkFragment extends BaseFragment {

    private BookMarkAdapter mAdapter;

    private boolean mIsFromReadPage;
    private FragmentBookmarkBinding inflate;

    public static BookMarkFragment getInstance(boolean isFromReadPage) {
        BookMarkFragment instance = new BookMarkFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFromReadPage", isFromReadPage);
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public View bindContentView(LayoutInflater inflater, ViewGroup container, boolean attachParent) {
        inflate = FragmentBookmarkBinding.inflate(inflater, container, attachParent);
        return inflate.getRoot();
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
        inflate.mRefreshLayout.setOnRefreshListener(() -> inflate.mRefreshLayout.setRefreshing(false));
        inflate.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new BookMarkAdapter(mContext, mIsFromReadPage);

        inflate.recyclerView.setAdapter(mAdapter);
        mAdapter.addAll(getParentActivity().getBookMarkList());
        mAdapter.setOnItemClickListener(position -> {
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
