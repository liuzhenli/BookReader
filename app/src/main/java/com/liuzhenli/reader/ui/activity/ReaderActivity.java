package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.liuzhenli.reader.ReaderApplication;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.ui.contract.ReadContract;
import com.liuzhenli.reader.ui.presenter.ReadPresenter;
import com.liuzhenli.reader.utils.BatteryUtil;
import com.liuzhenli.reader.view.menu.ReadBottomMenu;
import com.liuzhenli.reader.view.menu.ReadBrightnessMenu;
import com.liuzhenli.reader.view.menu.ReadSettingMenu;
import com.liuzhenli.reader.view.menu.ReadTopBarMenu;
import com.micoredu.readerlib.BaseReaderActivity;
import com.micoredu.readerlib.animation.PageAnimation;
import com.micoredu.readerlib.bean.BookInfoBean;
import com.micoredu.readerlib.bean.BookShelfBean;
import com.micoredu.readerlib.helper.BookshelfHelper;
import com.micoredu.readerlib.helper.ReadBookControl;
import com.micoredu.readerlib.page.PageView;
import com.micoredu.readerlib.bean.BookChapterBean;
import com.micoredu.readerlib.page.PageLoader;
import com.microedu.reader.R;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.liuzhenli.common.BitIntentDataManager.DATA_KEY;
import static com.liuzhenli.common.BitIntentDataManager.getInstance;

/**
 * describe:阅读页
 *
 * @author Liuzhenli on 2019-11-09 19:29
 * @since 1.0.0
 */
public class ReaderActivity extends BaseReaderActivity implements ReadContract.View {
    public static final String BOOK_ID = "bookId";
    public static final String CHAPTER_ID = "chapterID";
    public static final String PROGRESS = "progress";
    public static final String OPEN_FROM = "openFrom";


    private final static int OPEN_FROM_OTHER = 0;
    public final static int OPEN_FROM_APP = 1;

    @BindView(R.id.menu_top_bar)
    ReadTopBarMenu mTopBar;

    @BindView(R.id.reader_page_view)
    PageView mPageView;
    /***底部设置菜单*/
    @BindView(R.id.view_read_bottom_menu)
    ReadBottomMenu mVBottomMenu;
    /***设置菜单**/
    @BindView(R.id.view_read_setting_menu)
    ReadSettingMenu mVSettingMenu;
    /***亮度**/
    @BindView(R.id.view_read_brightness_setting)
    ReadBrightnessMenu mVBrightnessSettingMenu;

    private PageLoader mPageLoader;
    private int mCurrentChapterIndex;

    @Inject
    ReadPresenter mPresenter;
    private long mBookId;
    private int mChapterId;
    private float mProgress;
    private int mOpenFrom;
    private String mDataKey;
    /***是否在书架里*/
    private boolean isInBookShelf;

    private String mNoteUrl;
    private BookShelfBean mBookShelf;

    public static void start(Context context, String bookId, String chapterId, float progress, String openFrom) {
        Intent intent = new Intent(context, ReaderActivity.class);
        intent.putExtra(BOOK_ID, bookId);
        intent.putExtra(CHAPTER_ID, chapterId);
        intent.putExtra(PROGRESS, progress);
        intent.putExtra(OPEN_FROM, openFrom);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mNoteUrl = savedInstanceState.getString("noteUrl");
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_read;
    }


    @Override
    protected void initData() {
        AppComponent appComponent = ReaderApplication.getInstance().getAppComponent();
        appComponent.inject(this);

        mDataKey = getIntent().getStringExtra(DATA_KEY);
        mBookId = getIntent().getLongExtra(BOOK_ID, -1);
        mChapterId = getIntent().getIntExtra(CHAPTER_ID, 0);
        mProgress = getIntent().getFloatExtra(PROGRESS, 0);
        mOpenFrom = getIntent().getIntExtra(OPEN_FROM, OPEN_FROM_OTHER);


        String bookShelfKey = getIntent().getStringExtra(DATA_KEY);
        if (bookShelfKey != null) {
            mBookShelf = (BookShelfBean) getInstance().getData(bookShelfKey);
            mNoteUrl = mBookShelf.getNoteUrl();
        }
    }

    @Override
    protected void configViews() {
        ButterKnife.bind(this);
        mTopBar.getToolBar().setNavigationOnClickListener(v -> onBackPressed());
        initPageView();
        //获取本书的信息
        if (mOpenFrom == OPEN_FROM_APP) {
            mPresenter.getBookInfo(mNoteUrl);
        }

        //底部menu中 菜单按钮点击从底部弹出一个章节菜单
        mVBottomMenu.setOnMenuElementClickListener(new ReadBottomMenu.OnElementClickListener() {
            @Override
            public void onMenuClick() {
                showBookChapterListView();
            }

            @Override
            public void onPreChapterClick() {
                mPageLoader.skipPreChapter();
            }

            @Override
            public void onNextChapterClick() {
                mPageLoader.skipNextChapter();
            }

            @Override
            public void onBrightnessClick() {
                mVBottomMenu.setVisibility(View.GONE);
                mTopBar.setVisibility(View.GONE);
                mVBrightnessSettingMenu.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNightModeClick() {
                toast("夜间模式");
            }

            @Override
            public void onSettingClick() {
                mVBottomMenu.setVisibility(View.GONE);
                mTopBar.setVisibility(View.GONE);
                mVSettingMenu.setVisibility(View.VISIBLE);
            }

            @Override
            public void onListenBookClick() {
                toast("听书");
            }

            @Override
            public void onChapterProgressed(int progress, boolean isStop) {

                mCurrentChapterIndex = (mPresenter.getChapterList().size() - 1) * progress / 100;
                if (isStop) {
                    mPageLoader.skipToChapter(mCurrentChapterIndex, 0);
                } else {
                    mTopBar.setChapterTitle(mPresenter.getChapterList().get(mCurrentChapterIndex).getDurChapterName());
                }
            }
        });
        mVSettingMenu.setReadSettingCallBack(new ReadSettingMenu.ReadSettingCallBack() {
            @Override
            public void onPageModeChange() {
                if (mPageLoader != null) {
                    mPageLoader.setPageMode(PageAnimation.Mode.getPageMode(ReadBookControl.getInstance().getPageMode()));
                }
            }
        });
    }

    /***显示书目录**/
    private void showBookChapterListView() {
        QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(this);
        builder.setGravityCenter(false)
                .setTitle("目录")
                .setAddCancelBtn(false)
                .setAllowDrag(false)
                .setNeedRightMark(true)
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    dialog.dismiss();
                    mCurrentChapterIndex = position;
                    mChapterId = mPresenter.getChapterList().get(mCurrentChapterIndex).getDurChapterIndex();
                    mPageLoader.skipToChapter(mChapterId, 0);
                });
        builder.setCheckedIndex(mCurrentChapterIndex);
        for (int i = 1; i <= mPresenter.getChapterList().size(); i++) {
            builder.addItem(mPresenter.getChapterList().get(i - 1).getDurChapterName().trim());
        }
        builder.build().show();
    }

    /***
     * 阅读页面信息
     */
    private void initPageView() {

        mPageLoader = mPageView.getPageLoader((BaseReaderActivity) mContext, mBookShelf, new PageLoader.Callback() {
            @Override
            public List<BookChapterBean> getChapterList() {
                return mPresenter.getChapterList();
            }

            /**
             * @param pos:切换章节的序号
             */
            @Override
            public void onChapterChange(int pos) {
                if (mPresenter.getChapterList().isEmpty()) {
                    return;
                }
                if (pos >= mPresenter.getChapterList().size()) {
                    return;
                }
                mCurrentChapterIndex = pos;
                mTopBar.setChapterTitle(mPresenter.getChapterList().get(mCurrentChapterIndex).getDurChapterName());
            }

            /***
             * @param chapters：返回章节目录
             */
            @Override
            public void onCategoryFinish(List<BookChapterBean> chapters) {
                mPresenter.setChapterList(chapters);
                mBookShelf.setChapterListSize(chapters.size());
                mCurrentChapterIndex = mBookShelf.getDurChapter();
                mBookShelf.setDurChapterName(chapters.get(mCurrentChapterIndex).getDurChapterName());
                mBookShelf.setLastChapterName(chapters.get(chapters.size() - 1).getDurChapterName());
                BookshelfHelper.saveBookToShelf(mBookShelf);
                mTopBar.setChapterTitle(mPresenter.getChapterList().get(mCurrentChapterIndex).getDurChapterName());
            }

            @Override
            public void onPageCountChange(int count) {
            }

            @Override
            public void onPageChange(int chapterIndex, int pageIndex, boolean resetReadAloud) {

            }

            @Override
            public void vipPop() {

            }
        });
        //电量信息
        mPageLoader.updateBattery(BatteryUtil.INSTANCE.getBatteryLevel(this));
        //书页点击
        mPageView.setTouchListener(new PageView.TouchListener() {
            @Override
            public void onTouch() {

            }

            @Override
            public void onTouchClearCursor() {

            }

            @Override
            public void onLongPress() {

            }

            @Override
            public void center() {
                if (mVBottomMenu.getVisibility() == View.VISIBLE) {
                    mVBottomMenu.setVisibility(View.GONE);
                    mTopBar.setVisibility(View.GONE);
                } else {
                    mVBottomMenu.setVisibility(View.VISIBLE);
                    mTopBar.setVisibility(View.VISIBLE);
                }
                if (mVSettingMenu.getVisibility() == View.VISIBLE) {
                    mVSettingMenu.setVisibility(View.GONE);
                    mTopBar.setVisibility(View.GONE);
                    mVBottomMenu.setVisibility(View.GONE);
                }

                if (mVBrightnessSettingMenu.getVisibility() == View.VISIBLE) {
                    mVBrightnessSettingMenu.setVisibility(View.GONE);
                    mVBottomMenu.setVisibility(View.GONE);
                    mTopBar.setVisibility(View.GONE);
                }
                mTopBar.getToolBar().setTitle(mBookShelf.getBookInfoBean().getName());
                mTopBar.getToolBar().setTitleTextColor(Color.WHITE);

            }

        });
        mPageLoader.refreshChapterList();
    }

    @Override
    public void showError(Exception e) {

    }

    @Override
    public void complete() {

    }

    @Override
    public void autoChangeSource() {

    }

    @Override
    public void showSnackBar(PageView pageView, String msg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void showBookInfo(BookInfoBean bookInfo) {

    }

    @Override
    public String getNoteUrl() {
        return mNoteUrl;
    }

    @Override
    public boolean isInBookShelf() {
        return isInBookShelf;
    }
}
