package com.micoredu.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.liuzhenli.common.BaseApplication;
import com.liuzhenli.common.BitIntentDataManager;
import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.base.BaseContract;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.common.base.BaseTabActivity;
import com.microedu.lib.reader.R;
import com.micoredu.reader.bean.Book;
import com.micoredu.reader.bean.BookChapter;
import com.micoredu.reader.bean.Bookmark;
import com.micoredu.reader.dao.AppDatabase;
import com.micoredu.reader.model.ReadBook;
import com.micoredu.reader.ui.fragment.BookChapterListFragment;
import com.micoredu.reader.ui.bookmark.BookMarkFragment;
import com.micoredu.reader.utils.ReadConfigManager;
import com.microedu.lib.reader.databinding.ActBookchapterlistBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2021/1/26
 * Email: 848808263@qq.com
 */
public class BookChapterListActivity extends BaseTabActivity<BaseContract.BasePresenter, ActBookchapterlistBinding> {

    private ReadBook mBookShelf;
    /***正序*/
    private List<BookChapter> mChapterBeanList;
    /***倒序*/
    private final List<BookChapter> mChapterList = new ArrayList<>();
    private List<Bookmark> mBookMarkList = new ArrayList<>();
    private final List<Bookmark> mBookMarkDesc = new ArrayList<>();

    private boolean mIsBookMark;
    private boolean mIsFromReadPage;

    private boolean isAsc = true;
    private int mChapterId;

    public static void start(Context context, Book bookShelf, List<BookChapter> chapterBeanList, boolean isBookMark, boolean isFromReadPage, int chapterId) {
        Intent intent = new Intent(context, BookChapterListActivity.class);
        String key = String.valueOf(System.currentTimeMillis());

        String bookKey = "book" + key;
        intent.putExtra("isBookMark", isBookMark);
        intent.putExtra("isFromReadPage", isFromReadPage);
        intent.putExtra("bookKey", bookKey);
        intent.putExtra("chapterId", chapterId);
        BitIntentDataManager.getInstance().putData(bookKey, bookShelf);

        String chapterListKey = "chapterList" + key;
        intent.putExtra("chapterListKey", chapterListKey);
        BitIntentDataManager.getInstance().putData(chapterListKey, chapterBeanList);

        context.startActivity(intent);
    }

    public static void start(Context context, Book bookShelf, List<BookChapter> chapterBeanList) {
        start(context, bookShelf, chapterBeanList, false, false, 0);
    }

    @Override
    protected ActBookchapterlistBinding inflateView(LayoutInflater inflater) {
        return ActBookchapterlistBinding.inflate(inflater);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initToolBar() {
        mTvRight.setText("倒序");
        ClickUtils.click(mTvRight, o -> {
            isAsc = !isAsc;
            ((BookChapterListFragment) mFragmentList.get(0)).refreshData();
            ((BookMarkFragment) mFragmentList.get(1)).refreshData();
            if (isAsc) {
                mTvRight.setText("倒序");
            } else {
                mTvRight.setText("正序");
            }
        });
        mTvRight.setVisibility(View.VISIBLE);

        if (mBookShelf != null) {
            mTvTitle.setText(mBookShelf.getBook().getName());
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initData() {
        String bookshelfKey = getIntent().getStringExtra("bookKey");
        mBookShelf = (ReadBook) BitIntentDataManager.getInstance().getData(bookshelfKey);
        mIsBookMark = getIntent().getBooleanExtra("isBookMark", false);
        mIsFromReadPage = getIntent().getBooleanExtra("isFromReadPage", false);
        mChapterId = getIntent().getIntExtra("chapterId", 0);
        String chapterListKey = getIntent().getStringExtra("chapterListKey");
        mChapterBeanList = (List<BookChapter>) BitIntentDataManager.getInstance().getData(chapterListKey);

        if (mChapterBeanList != null) {
            for (int i = 0; i < mChapterBeanList.size(); i++) {
                mChapterList.add(0, mChapterBeanList.get(i));
            }
        }

        if (mBookShelf != null) {
            mBookMarkList = AppDatabase.Companion.createDatabase(BaseApplication.Companion.getInstance()).getBookmarkDao().getByBook(mBookShelf.getBook().getName(), mBookShelf.getBook().getAuthor());
            for (int i = 0; i < mBookMarkList.size(); i++) {
                mBookMarkDesc.add(0, mBookMarkList.get(i));
            }
        }

    }

    @Override
    protected List<Fragment> createTabFragments() {
        return Arrays.asList(BookChapterListFragment.getInstance(mIsFromReadPage, mChapterId), BookMarkFragment.getInstance(mIsFromReadPage));
    }

    @Override
    protected List<String> createTabTitles() {
        ArrayList<String> titles = new ArrayList<>();
        titles.add("目录");
        titles.add("书签");
        return titles;
    }

    public ReadBook getBookShelf() {
        return mBookShelf;
    }

    public List<BookChapter> getChapterBeanList() {
        if (!isAsc) {
            return mChapterList;
        }
        return mChapterBeanList;
    }

    public List<Bookmark> getBookMarkList() {
        if (isAsc) {
            return mBookMarkList;
        }
        return mBookMarkDesc;
    }

    @Override
    protected void configViews() {
        super.configViews();
        if (mIsBookMark) {
            mVp.setCurrentItem(1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //如果来自阅读页面,则需要改变一下颜色
        if (mIsFromReadPage) {
            changeTheme();
        }
    }

    private void changeTheme() {
        mTabLayout.setBackground(ReadConfigManager.getInstance().getTextBackground(mContext));
        if (ReadConfigManager.getInstance().getIsNightTheme()) {
            mToolBar.setBackgroundColor(getResources().getColor(R.color.skin_night_reader_scene_bg_color));
            mImmersionBar.statusBarColor(R.color.skin_night_reader_scene_bg_color);
            mTabLayout.setSelectedTabIndicatorColor(ReadConfigManager.getInstance().getTextColor());
        } else {
            mTabLayout.setSelectedTabIndicatorColor(ReadConfigManager.getInstance().getTextColor());
            mImmersionBar.statusBarColorInt(ReadConfigManager.getInstance().getBgColor());
            mToolBar.setBackgroundColor(ReadConfigManager.getInstance().getBgColor());
        }
        binding.mViewRoot.setBackground(ReadConfigManager.getInstance().getTextBackground(mContext));
        mTabLayout.setTabTextColors(getResources().getColor(R.color.text_color_99), ReadConfigManager.getInstance().getTextColor());
        mImmersionBar.statusBarDarkFont(false);
        mImmersionBar.init();
    }
}
