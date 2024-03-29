package com.micoredu.reader.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.hwangjr.rxbus.RxBus;
import com.liuzhenli.common.BitIntentDataManager;
import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.constant.RxBusTag;
import com.liuzhenli.common.utils.Constant;
import com.liuzhenli.common.base.BaseFragment;
import com.liuzhenli.common.utils.DataDiffUtil;
import com.micoredu.reader.bean.OpenChapterBean;
import com.micoredu.reader.databinding.FragmentBookchapterlistBinding;
import com.micoredu.reader.ui.activity.BookChapterListActivity;
import com.micoredu.reader.ui.activity.ReaderActivity;
import com.micoredu.reader.bean.BookChapterBean;
import com.micoredu.reader.helper.BookshelfHelper;
import com.micoredu.reader.ui.adapter.BookChapterAdapter;

public class BookChapterListFragment extends BaseFragment {

    private BookChapterAdapter mAdapter;

    private boolean mIsFromReadPage;
    private int mChapterId;
    private FragmentBookchapterlistBinding inflate;

    public static BookChapterListFragment getInstance(boolean isFromReadPage, int chapterId) {
        BookChapterListFragment instance = new BookChapterListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFromReadPage", isFromReadPage);
        bundle.putInt("chapterId", chapterId);
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public View bindContentView(LayoutInflater inflater, ViewGroup container, boolean attachParent) {
        inflate = FragmentBookchapterlistBinding.inflate(inflater, container, attachParent);
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
        mChapterId = getArguments() != null ? getArguments().getInt("chapterId", 0) : 0;
    }

    @Override
    public void configViews() {
        inflate.mRefreshLayout.setOnRefreshListener(() -> inflate.mRefreshLayout.setRefreshing(false));
        inflate.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new BookChapterAdapter(mContext, mIsFromReadPage);
        inflate.recyclerView.setAdapter(mAdapter);
        mAdapter.addAll(getParentActivity().getChapterBeanList());
        mAdapter.setOnItemClickListener(position -> {
            BookChapterBean item = mAdapter.getItem(position);
            getParentActivity().getBookShelf().setFinalDate(System.currentTimeMillis());

            Intent intent = new Intent(getContext(), ReaderActivity.class);
            intent.putExtra(ReaderActivity.OPEN_FROM, Constant.BookOpenFrom.FROM_BOOKSHELF);
            intent.putExtra(ReaderActivity.CHAPTER_ID, item.getDurChapterIndex());

            String bookKey = String.valueOf(System.currentTimeMillis());
            intent.putExtra(BitIntentDataManager.DATA_KEY, bookKey);
            BitIntentDataManager.getInstance().putData(bookKey, getParentActivity().getBookShelf().clone());
            RxBus.get().post(RxBusTag.SKIP_TO_CHAPTER, new OpenChapterBean(item.getDurChapterIndex(), 0));
            mContext.startActivity(intent);
        });
        inflate.recyclerView.scrollToPosition(mChapterId);
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
