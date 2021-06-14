package com.liuzhenli.write.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.base.BaseFragment;
import com.liuzhenli.common.constant.ARouterConstants;
import com.liuzhenli.common.constant.AppConstant;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.liuzhenli.write.DaggerWriteBookComponent;
import com.liuzhenli.write.bean.WriteBook;
import com.liuzhenli.write.bean.WriteChapter;
import com.liuzhenli.write.databinding.FgWriteCreatebookBinding;
import com.liuzhenli.write.module.WriteModule;
import com.liuzhenli.write.ui.activity.WriteBookActivity;
import com.liuzhenli.write.ui.adapter.ChapterListAdapter;
import com.liuzhenli.write.ui.adapter.CreateBookBannerAdapter;
import com.liuzhenli.write.ui.contract.CreateBookContract;
import com.liuzhenli.write.ui.presenter.CreateBookPresenter;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.listener.OnPageChangeListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.liuzhenli.write.ui.activity.EditBookInfoActivity.WRITE_BOOK;

/**
 * Description: created books page
 *
 * @author liuzhenli 3/13/21
 * Email: 848808263@qq.com
 */
public class CreateBookFragment extends BaseFragment<CreateBookPresenter> implements CreateBookContract.View {

    private FgWriteCreatebookBinding mBinding;
    private CreateBookBannerAdapter mBannerAdapter;
    private List<WriteBook> mBookList;
    private List<WriteChapter> mChapterList;
    private WriteBook mBook;
    private ChapterListAdapter chapterListAdapter;
    private int mCurrentTabIndex;

    public static CreateBookFragment getInstance() {
        CreateBookFragment instance = new CreateBookFragment();
        Bundle bundle = new Bundle();
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public View bindContentView(LayoutInflater inflater, ViewGroup container, boolean attachParent) {
        mBinding = FgWriteCreatebookBinding.inflate(inflater, container, attachParent);
        return mBinding.getRoot();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerWriteBookComponent.builder().writeModule(new WriteModule()).build().inject(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getCreateBooks();
    }

    @Override
    public void configViews() {
        mBannerAdapter = new CreateBookBannerAdapter(mBookList);
        mBinding.banner.setAdapter(mBannerAdapter)
                //observe lifecycle
                .addBannerLifecycleObserver(this)
                .setIndicator(new CircleIndicator(mContext))
                .setOnBannerListener((data, position) -> {
                    WriteBook book = mBannerAdapter.getData(position);
                    if (data instanceof WriteBook) {
                        book = (WriteBook) data;
                    }
                    ARouter.getInstance().build(ARouterConstants.ACT_EDIT_BOOK_INFO).withSerializable(WRITE_BOOK, book).navigation();
                }).addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentTabIndex = position;
                mBook = mBookList.get(position);
                if (mBook.getId() == null) {
                    mBook.setId(0L);
                }
                showChapterList(mBook.getId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        chapterListAdapter = new ChapterListAdapter(mContext);
        mBinding.recyclerView.setAdapter(chapterListAdapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        chapterListAdapter.setOnItemClickListener(position ->
                ARouter.getInstance().build(ARouterConstants.ACT_WRITE_BOOK)
                        .withSerializable(WriteBookActivity.DATA, chapterListAdapter.getItem(position))
                        .withSerializable(WriteBookActivity.MODE, chapterListAdapter.getItem(position).getId() == null)
                        .navigation());

    }

    @Override
    public void showAllCreateBooks(List<WriteBook> books) {
        mBookList = books;
        mBannerAdapter.setDatas(books);
        mBannerAdapter.notifyDataSetChanged();
        if (mBookList.get(mCurrentTabIndex).getId() != null) {
            mPresenter.getChapterList(mBookList.get(mCurrentTabIndex).getId());
        }
    }

    @Override
    public void showChapterList(List<WriteChapter> chapters) {
        mChapterList = chapters;
        chapterListAdapter.clear();
        chapterListAdapter.addAll(chapters);
        chapterListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(Exception e) {

    }

    @Override
    public void complete() {

    }

    private void showChapterList(long bookId) {
        mPresenter.getChapterList(bookId);
    }
}
