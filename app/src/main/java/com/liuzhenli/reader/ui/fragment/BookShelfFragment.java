package com.liuzhenli.reader.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.liuzhenli.common.BitIntentDataManager;
import com.liuzhenli.common.constant.RxBusTag;
import com.liuzhenli.reader.base.BaseRVFragment;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.ui.activity.ReaderActivity;
import com.liuzhenli.reader.ui.adapter.BookShelfAdapter;
import com.liuzhenli.reader.ui.contract.BookShelfContract;
import com.liuzhenli.reader.ui.presenter.BookShelfPresenter;
import com.liuzhenli.reader.utils.Constant;
import com.liuzhenli.reader.utils.ToastUtil;
import com.micoredu.readerlib.bean.BookShelfBean;
import com.micoredu.readerlib.helper.BookshelfHelper;
import com.microedu.reader.R;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

import java.util.List;

/**
 * describe:书架
 *
 * @author Liuzhenli on 2019-11-09 22:28
 */
public class BookShelfFragment extends BaseRVFragment<BookShelfPresenter, BookShelfBean> implements BookShelfContract.View {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.get().register(this);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_bookshelf;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public void initData() {
        mPresenter.queryBooks(false, 0);
    }

    @Override
    public void configViews() {
        initAdapter(BookShelfAdapter.class, false, false, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(RxBusTag.HAD_ADD_BOOK)})
    public void onAddOrRemoveBook(BookShelfBean book) {
        mPresenter.queryBooks(false, 0);
    }

    @Override
    public void onItemClick(int position) {
        BookShelfBean bookShelfBean = mAdapter.getRealAllData().get(position);
        bookShelfBean.setFinalDate(System.currentTimeMillis());
        BookshelfHelper.saveBookToShelf(bookShelfBean);
        Intent intent = new Intent(getContext(), ReaderActivity.class);
        intent.putExtra("openFrom", Constant.BookOpenFrom.OPEN_FROM_APP);

        String bookKey = String.valueOf(System.currentTimeMillis());
        intent.putExtra(BitIntentDataManager.DATA_KEY, bookKey);
        BitIntentDataManager.getInstance().putData(bookKey, bookShelfBean.clone());

        mContext.startActivity(intent);
    }


    @Override
    public void showBooks(List<BookShelfBean> bookShelfBeanList) {
        mAdapter.addAll(bookShelfBeanList);
    }

    @Override
    public void onBookRemoved(BookShelfBean bookShelfBean) {
        mAdapter.getRealAllData().remove(bookShelfBean);
        mAdapter.notifyDataSetChanged();
        ToastUtil.showCenter("已从书架中移除");
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mPresenter.queryBooks(false, 0);
    }

    @Override
    public void showError(Exception e) {
        mRecyclerView.setRefreshing(false);
    }

    @Override
    public void complete() {
        mRecyclerView.setRefreshing(false);
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
    }

    @Override
    public boolean onItemLongClick(int position) {

        BookShelfBean bookShelfBean = mAdapter.getRealAllData().get(position);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setContentView(R.layout.layout_bookshelf_bottom_sheet);
        bottomSheetDialog.findViewById(R.id.tv_delete_book).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.removeFromBookShelf(bookShelfBean);
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.findViewById(R.id.tv_continue_read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(position);
                bottomSheetDialog.dismiss();
            }
        });
        TextView tvBookName = bottomSheetDialog.findViewById(R.id.tv_book_name);
        TextView tvBookAuthor = bottomSheetDialog.findViewById(R.id.tv_book_author);
        TextView tvBookSource = bottomSheetDialog.findViewById(R.id.tv_book_source);
        TextView tvProgress = bottomSheetDialog.findViewById(R.id.tv_book_read_progress);
        TextView tvDes = bottomSheetDialog.findViewById(R.id.tv_book_des);

        String author = TextUtils.isEmpty(bookShelfBean.getBookInfoBean().getAuthor()) ? "未知" : bookShelfBean.getBookInfoBean().getAuthor();

        tvBookName.setText(bookShelfBean.getBookInfoBean().getName());
        tvBookAuthor.setText(String.format("作者:%s", author));
        tvBookSource.setText(String.format("书源:%s", "本地"));
        tvProgress.setText(String.format("已阅读:%s", "1%"));
        tvDes.setText(bookShelfBean.getBookInfoBean().getIntroduce());
        bottomSheetDialog.show();
        return false;
    }
}
