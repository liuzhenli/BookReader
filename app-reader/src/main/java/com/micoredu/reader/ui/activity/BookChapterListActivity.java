package com.micoredu.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.liuzhenli.common.BaseApplication;
import com.liuzhenli.common.BitIntentDataManager;
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
 * Description:chapter list and bookmark list
 */
public class BookChapterListActivity extends BaseTabActivity<ActBookchapterlistBinding> {

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
    private String mBookUrl;
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

    public static void start(Context context, String bookUrl) {
        Intent intent = new Intent(context, BookChapterListActivity.class);
        intent.putExtra("bookUrl", bookUrl);
        context.startActivity(intent);
    }

    @Override
    protected ActBookchapterlistBinding inflateView(LayoutInflater inflater) {
        return ActBookchapterlistBinding.inflate(inflater);
    }


    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);

        mBookUrl = getIntent().getStringExtra("bookUrl");
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
        binding.icToolBar.tvToolbarRight.setText("倒序");
        ClickUtils.click(binding.icToolBar.tvToolbarRight, o -> {
            isAsc = !isAsc;
            ((BookMarkFragment) mFragmentList.get(1)).refreshData();
            if (isAsc) {
                binding.icToolBar.tvToolbarRight.setText("倒序");
            } else {
                binding.icToolBar.tvToolbarRight.setText("正序");
            }
        });
        binding.icToolBar.tvToolbarRight.setVisibility(View.VISIBLE);

        if (mBookShelf != null) {
            binding.icToolBar.tvToolbarTitle.setText(mBookShelf.getBook().getName());
        }

    }


    @Override
    protected List<Fragment> createTabFragments() {
        return Arrays.asList(BookChapterListFragment.getInstance(mBookUrl, mChapterId), BookMarkFragment.getInstance(mIsFromReadPage));
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
    protected void onResume() {
        super.onResume();
        //如果来自阅读页面,则需要改变一下颜色
        if (mIsFromReadPage) {
            changeTheme();
        }
    }

    private void changeTheme() {
        mTabLayout.setBackground(ReadConfigManager.getInstance().getTextBackground(this));
        if (ReadConfigManager.getInstance().getIsNightTheme()) {
            binding.icToolBar.tvToolbarTitle.setBackgroundColor(getResources().getColor(R.color.skin_night_reader_scene_bg_color));
            mTabLayout.setSelectedTabIndicatorColor(ReadConfigManager.getInstance().getTextColor());
        } else {
            mTabLayout.setSelectedTabIndicatorColor(ReadConfigManager.getInstance().getTextColor());
            binding.icToolBar.toolbar.setBackgroundColor(ReadConfigManager.getInstance().getBgColor());
        }
        binding.mViewRoot.setBackground(ReadConfigManager.getInstance().getTextBackground(this));
        mTabLayout.setTabTextColors(getResources().getColor(R.color.text_color_99), ReadConfigManager.getInstance().getTextColor());

        setupSystemBar();
    }
}
