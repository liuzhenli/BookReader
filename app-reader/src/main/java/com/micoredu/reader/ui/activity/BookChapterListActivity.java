package com.micoredu.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.liuzhenli.common.BitIntentDataManager;
import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.base.BaseContract;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.common.base.BaseTabActivity;
import com.micoredu.reader.R;
import com.micoredu.reader.bean.BookChapterBean;
import com.micoredu.reader.bean.BookShelfBean;
import com.micoredu.reader.bean.BookmarkBean;
import com.micoredu.reader.databinding.ActBookchapterlistBinding;
import com.micoredu.reader.helper.BookshelfHelper;
import com.micoredu.reader.helper.ReadConfigManager;
import com.micoredu.reader.ui.fragment.BookChapterListFragment;
import com.micoredu.reader.ui.fragment.BookMarkFragment;

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

    private BookShelfBean mBookShelf;
    /***正序*/
    private List<BookChapterBean> mChapterBeanList;
    /***倒序*/
    private final List<BookChapterBean> mChapterList = new ArrayList<>();
    private List<BookmarkBean> mBookMarkList = new ArrayList<>();
    private final List<BookmarkBean> mBookMarkDesc = new ArrayList<>();

    private boolean mIsBookMark;
    private boolean mIsFromReadPage;

    private boolean isAsc = true;
    private int mChapterId;

    public static void start(Context context, BookShelfBean bookShelf, List<BookChapterBean> chapterBeanList, boolean isBookMark, boolean isFromReadPage, int chapterId) {
        Intent intent = new Intent(context, BookChapterListActivity.class);
        String key = String.valueOf(System.currentTimeMillis());

        String bookKey = "book" + key;
        intent.putExtra("isBookMark", isBookMark);
        intent.putExtra("isFromReadPage", isFromReadPage);
        intent.putExtra("bookKey", bookKey);
        intent.putExtra("chapterId", chapterId);
        BitIntentDataManager.getInstance().putData(bookKey, bookShelf.clone());

        String chapterListKey = "chapterList" + key;
        intent.putExtra("chapterListKey", chapterListKey);
        BitIntentDataManager.getInstance().putData(chapterListKey, chapterBeanList);

        context.startActivity(intent);
    }

    public static void start(Context context, BookShelfBean bookShelf, List<BookChapterBean> chapterBeanList) {
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

        if (mBookShelf != null && mBookShelf.getBookInfoBean() != null) {
            mTvTitle.setText(mBookShelf.getBookInfoBean().getName());
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initData() {
        String bookshelfKey = getIntent().getStringExtra("bookKey");
        mBookShelf = (BookShelfBean) BitIntentDataManager.getInstance().getData(bookshelfKey);
        mIsBookMark = getIntent().getBooleanExtra("isBookMark", false);
        mIsFromReadPage = getIntent().getBooleanExtra("isFromReadPage", false);
        mChapterId = getIntent().getIntExtra("chapterId", 0);
        String chapterListKey = getIntent().getStringExtra("chapterListKey");
        mChapterBeanList = (List<BookChapterBean>) BitIntentDataManager.getInstance().getData(chapterListKey);

        if (mChapterBeanList != null) {
            for (int i = 0; i < mChapterBeanList.size(); i++) {
                mChapterList.add(0, mChapterBeanList.get(i));
            }
        }

        if (mBookShelf != null && mBookShelf.getBookInfoBean() != null) {
            mBookMarkList = BookshelfHelper.getBookmarkList(mBookShelf.getBookInfoBean().getName());
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

    public BookShelfBean getBookShelf() {
        return mBookShelf;
    }

    public List<BookChapterBean> getChapterBeanList() {
        if (!isAsc) {
            return mChapterList;
        }
        return mChapterBeanList;
    }

    public List<BookmarkBean> getBookMarkList() {
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
