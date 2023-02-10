/*
package com.liuzhenli.reader.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.liuzhenli.common.BitIntentDataManager;
import com.liuzhenli.common.constant.AppConstant;
import com.liuzhenli.common.constant.RxBusTag;
import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.common.base.BaseRVFragment;
import com.liuzhenli.reader.DaggerReadBookComponent;
import com.liuzhenli.reader.ui.activity.BookDetailActivity;
import com.micoredu.reader.bean.Book;
import com.micoredu.reader.ui.activity.ReaderActivity;
import com.liuzhenli.reader.ui.adapter.BookShelfAdapter;
import com.liuzhenli.reader.ui.contract.BookShelfContract;
import com.liuzhenli.reader.ui.presenter.BookShelfPresenter;
import com.liuzhenli.common.utils.Constant;
import com.liuzhenli.common.utils.DataDiffUtil;
import com.liuzhenli.common.utils.ToastUtil;
import com.liuzhenli.common.utils.image.ImageUtil;
import com.micoredu.reader.R;
import com.micoredu.reader.databinding.FragmentBookshelfBinding;

import java.util.List;

import static com.liuzhenli.common.BitIntentDataManager.DATA_KEY;

*/
/**
 * describe:bookShelf
 *
 * @author Liuzhenli on 2019-11-09 22:28
 *//*

public class BookShelfFragment extends BaseRVFragment<BookShelfPresenter, Book> implements BookShelfContract.View {

    */
/***是否启动第一次访问书架  如果是,需要检查更新*//*

    private boolean mFirstRequest = true;
    private FragmentBookshelfBinding inflate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View bindContentView(LayoutInflater inflater, ViewGroup container, boolean attachParent) {
        inflate = FragmentBookshelfBinding.inflate(inflater, container, attachParent);
        return inflate.getRoot();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerReadBookComponent.builder().build().inject(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void configViews() {
        initAdapter(BookShelfAdapter.class, false, false, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(RxBusTag.REFRESH_BOOK_LIST), @Tag(RxBusTag.UPDATE_BOOK_PROGRESS)})
    public void onAddOrRemoveBook(Book book) {
        mPresenter.queryBooks(false, 0);
    }

    @Override
    public void onItemClick(int position) {
        Book Book = mAdapter.getRealAllData().get(position);
        Book.setFinalDate(System.currentTimeMillis());
        BookHelp.saveBookToShelf(Book);
        Intent intent = new Intent(getContext(), ReaderActivity.class);
        intent.putExtra("openFrom", Constant.BookOpenFrom.FROM_BOOKSHELF);

        String bookKey = String.valueOf(System.currentTimeMillis());
        intent.putExtra(BitIntentDataManager.DATA_KEY, bookKey);
        BitIntentDataManager.getInstance().putData(bookKey, Book.clone());

        mContext.startActivity(intent);
    }


    @Override
    public void showBooks(List<Book> bookShelfBeanList) {
        if (mAdapter.getCount() != 0) {
            DataDiffUtil.diffResult(mAdapter, bookShelfBeanList, new DataDiffUtil.ItemSameCallBack<Book>() {
                @Override
                public boolean isItemSame(Book oldItem, Book newItem) {

                    return oldItem.getBookInfoBean() != null && oldItem.getBookInfoBean().getName() != null
                            && newItem.getBookInfoBean() != null
                            && oldItem.getBookInfoBean().getName().equals(newItem.getBookInfoBean().getName());
                }

                @Override
                public boolean isContentSame(Book oldItem, Book newItem) {
                    return false;
                }
            });
        } else {
            mAdapter.addAll(bookShelfBeanList);
        }
    }

    @Override
    public void onBookRemoved(Book Book) {
        mAdapter.getRealAllData().remove(Book);
        mAdapter.notifyDataSetChanged();
        ToastUtil.showToast("已从书架中移除");
    }

    @Override
    public void setRefreshingBook(Book data) {
        for (int i = 0; mAdapter != null && mAdapter.getRealAllData() != null
                && mAdapter.getRealAllData().size() > 0
                && i < mAdapter.getRealAllData().size(); i++) {
            if (TextUtils.equals(mAdapter.getRealAllData().get(i).getBookUrl(), data.getBookUrl())) {
                mAdapter.getRealAllData().get(i).setLoading(data.isLoading());
                mAdapter.notifyItemChanged(i);
            }
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mPresenter.queryBooks(mFirstRequest, 0);
        mFirstRequest = false;
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

    @SuppressLint("DefaultLocale")
    @Override
    public boolean onItemLongClick(int position) {

        Book Book = mAdapter.getRealAllData().get(position);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setContentView(R.layout.layout_bookshelf_bottom_sheet);
        bottomSheetDialog.findViewById(R.id.tv_delete_book).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.removeFromBookShelf(Book);
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
        TextView tvBookName = bottomSheetDialog.findViewById(R.id.mTvBookName);
        TextView tvBookAuthor = bottomSheetDialog.findViewById(R.id.tv_book_author);
        TextView tvBookSource = bottomSheetDialog.findViewById(R.id.mTvBookSource);
        TextView tvProgress = bottomSheetDialog.findViewById(R.id.tv_book_read_progress);
        TextView tvDes = bottomSheetDialog.findViewById(R.id.tv_book_des);
        ImageView mIvCover = bottomSheetDialog.findViewById(R.id.iv_book_cover);
        CheckBox mVAllowUpdate = bottomSheetDialog.findViewById(R.id.cb_allow_update);

        ClickUtils.click(mVAllowUpdate, o -> {
            Book.setAllowUpdate(!Book.getAllowUpdate());
            mVAllowUpdate.setChecked(Book.getAllowUpdate());
            mPresenter.updateBookInfo(Book);
        });
        //本地书,不显示允许更新
        if (TextUtils.equals(Book.getTag(), Book.LOCAL_TAG)) {
            mVAllowUpdate.setVisibility(View.GONE);
        } else {
            mVAllowUpdate.setVisibility(View.VISIBLE);
        }
        ClickUtils.click(tvBookName, o -> {
            String dataKey = String.valueOf(System.currentTimeMillis());
            Intent intent = new Intent(mContext, BookDetailActivity.class);
            intent.putExtra(BookDetailActivity.OPEN_FROM, AppConstant.BookOpenFrom.FROM_BOOKSHELF);
            intent.putExtra(DATA_KEY, dataKey);
            BitIntentDataManager.getInstance().putData(dataKey, Book);
            startActivity(intent);
        });
        ImageUtil.setImage(mContext, Book.getBookInfoBean().getCoverUrl(), R.drawable.book_cover, R.drawable.book_cover, mIvCover);
        String author = TextUtils.isEmpty(Book.getBookInfoBean().getAuthor()) ? "未知" : Book.getBookInfoBean().getAuthor();
        mVAllowUpdate.setChecked(Book.getAllowUpdate());
        tvBookName.setText(Book.getBookInfoBean().getName());
        tvBookAuthor.setText(String.format("作者:%s", author));
        tvBookSource.setText(String.format("书源:%s", Book.getBookInfoBean().getOrigin()));
        if (Book.getChapterListSize() == 0) {
            tvProgress.setText("已阅读:0%");
        } else {
            tvProgress.setText(String.format("已阅读:%.2f%s", Book.getDurChapter() * 100.f / Book.getChapterListSize(), "%"));
        }
        if (tvDes != null && Book.getBookInfoBean().getIntroduce() != null) {
            tvDes.setText(Html.fromHtml(Book.getBookInfoBean().getIntroduce()));
        }
        bottomSheetDialog.show();
        return false;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible) {
            onRefresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }
}
*/
