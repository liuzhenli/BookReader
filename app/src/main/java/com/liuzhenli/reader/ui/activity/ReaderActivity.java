package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.liuzhenli.common.constant.AppConstant;
import com.liuzhenli.common.utils.AppConfigManager;
import com.liuzhenli.reader.ReaderApplication;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.ui.contract.ReadContract;
import com.liuzhenli.reader.ui.presenter.ReadPresenter;
import com.liuzhenli.reader.utils.BatteryUtil;
import com.liuzhenli.reader.view.loading.DialogUtil;
import com.liuzhenli.reader.view.menu.ReadBottomMenu;
import com.liuzhenli.reader.view.menu.ReadBrightnessMenu;
import com.liuzhenli.reader.view.menu.ReadSettingMenu;
import com.liuzhenli.reader.view.menu.ReadTopBarMenu;
import com.micoredu.readerlib.BaseReaderActivity;
import com.micoredu.readerlib.animation.PageAnimation;
import com.micoredu.readerlib.bean.BookInfoBean;
import com.micoredu.readerlib.bean.BookShelfBean;
import com.micoredu.readerlib.helper.BookshelfHelper;
import com.micoredu.readerlib.helper.DocumentHelper;
import com.micoredu.readerlib.helper.ReadConfigManager;
import com.micoredu.readerlib.page.PageView;
import com.micoredu.readerlib.bean.BookChapterBean;
import com.micoredu.readerlib.page.PageLoader;
import com.micoredu.readerlib.utils.bar.BarHide;
import com.microedu.reader.R;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.io.File;
import java.io.PipedReader;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.liuzhenli.common.BitIntentDataManager.DATA_KEY;
import static com.liuzhenli.common.BitIntentDataManager.getInstance;
import static com.liuzhenli.reader.base.BaseActivity.START_SHEAR_ELE;

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

    private Boolean startShareAnim = false;
    /***app外部打开*/
    private final static int OPEN_FROM_OTHER = 0;
    /**
     * 从app内部打开
     */
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
    @BindView(R.id.fl_protect_eye_view)
    FrameLayout mVProtectEye;
    private float mAlpha = 0F;
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
        mPresenter.attachView(this);
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
        if (getIntent() != null) {
            startShareAnim = getIntent().getBooleanExtra(START_SHEAR_ELE, false);
            mOpenFrom = getIntent().getIntExtra(OPEN_FROM, AppConstant.BookOpenFrom.OPEN_FROM_APP);
        }

        AppComponent appComponent = ReaderApplication.getInstance().getAppComponent();
        appComponent.inject(this);

        mDataKey = getIntent().getStringExtra(DATA_KEY);
        mBookId = getIntent().getLongExtra(BOOK_ID, -1);
        mChapterId = getIntent().getIntExtra(CHAPTER_ID, 0);
        mProgress = getIntent().getFloatExtra(PROGRESS, 0);
        mOpenFrom = getIntent().getIntExtra(OPEN_FROM, OPEN_FROM_OTHER);


        if (mDataKey != null) {
            mBookShelf = (BookShelfBean) getInstance().getData(mDataKey);
            mNoteUrl = mBookShelf.getNoteUrl();
        }
    }

    @Override
    protected void initImmersionBar() {
        mImmersionBar.fullScreen(true);
        mImmersionBar.hideBar(BarHide.FLAG_HIDE_STATUS_BAR);
        mImmersionBar.init();
    }

    @Override
    protected void configViews() {
        ButterKnife.bind(this);
        mTopBar.getToolBar().setNavigationOnClickListener(v -> onBackPressed());
        initPageView();
        //获取本书的信息  本地书
        if (mOpenFrom == OPEN_FROM_APP) {
            mPresenter.getBookInfo(mNoteUrl);
            //网络书籍
        } else {

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
                boolean isNightTheme = ReadConfigManager.getInstance().getIsNightTheme();
                ReadConfigManager.getInstance().setIsNightTheme(!isNightTheme);
                mPageLoader.refreshUi();
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
        mVBrightnessSettingMenu.setCallback(this, new ReadBrightnessMenu.CallBack() {
            @Override
            public void onProtectEyeClick(boolean on) {
                if (on) {
                    mAlpha = 3E-1F;
                } else {
                    mAlpha = 0f;
                }
                AppConfigManager.setProtectEyeReadMode(on);
                mVProtectEye.setAlpha(mAlpha);
                mVProtectEye.invalidate();
            }
        });
        mVSettingMenu.setReadSettingCallBack(new ReadSettingMenu.ReadSettingCallBack() {
            @Override
            public void onPageAnimChanged() {
                mPageLoader.setPageMode(PageAnimation.Mode.getPageMode(ReadConfigManager.getInstance().getPageMode()));
            }

            @Override
            public void onTextStyleChanged() {
                if (mPageLoader != null) {
                    mPageLoader.setTextSize();
                }
            }

            @Override
            public void onTypeFaceClicked() {
                mPresenter.getFontFile();
            }

            /***background changed*/
            @Override
            public void onBackGroundChanged() {
                if (mPageLoader != null) {
                    ReadConfigManager.getInstance().initTextDrawableIndex();
                    mPageView.setBackground(ReadConfigManager.getInstance().getTextBackground(mContext));
                    mPageLoader.refreshUi();
                }
            }
        });
        mVBrightnessSettingMenu.setProtectedEyeMode(AppConfigManager.tetProtectEyeReadMode());
        mVProtectEye.setAlpha(mAlpha);
        mVBrightnessSettingMenu.setBrightnessFollowSystem(ReadConfigManager.getInstance().getLightFollowSys());
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
                mBookShelf.setDurChapterName(chapters.get(mBookShelf.getDurChapter()).getDurChapterName());
                mBookShelf.setLastChapterName(chapters.get(chapters.size() - 1).getDurChapterName());
                mTopBar.setChapterTitle(mPresenter.getChapterList().get(mCurrentChapterIndex).getDurChapterName());
            }

            @Override
            public void onPageCountChange(int count) {
            }

            @Override
            public void onPageChange(int chapterIndex, int pageIndex, boolean resetReadAloud) {
                //记录阅读位置
                mBookShelf.setDurChapter(chapterIndex);
                mBookShelf.setDurChapterPage(pageIndex);
                if (isInBookShelf) {
                    mPresenter.saveProgress(mBookShelf);
                }
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
    public void showFontFile(File[] files) {
        if (files == null || files.length == 0) {
            //获取字体列表
            DialogUtil.showOneButtonDialog(mContext, "添加字体",
                    "本软件默认使用系统字体,若更换字体,请先下载字体文件,然后拷贝到根目录Fonts文件夹下即可.",
                    (dialog, index) -> {
                        dialog.dismiss();
                    }
            );
        } else {
            String[] fonts = new String[files.length + 1];
            int current = 0;
            String fontPath = ReadConfigManager.getInstance().getFontPath();
            fonts[0] = "默认字体";
            for (int i = 0; i < files.length; i++) {
                fonts[i + 1] = files[i].getName();
                if (TextUtils.equals(fontPath, files[i].getAbsolutePath())) {
                    current = i + 1;
                }
            }
            DialogUtil.sowSingleChoiceDialog(mContext, fonts, (dialog, index) -> {
                if (index == 0) {
                    ReadConfigManager.getInstance().setReadBookFont(null);
                } else {
                    ReadConfigManager.getInstance().setReadBookFont(files[index - 1].getAbsolutePath());
                }
                mPageLoader.refreshUi();
                dialog.dismiss();
            }, current);
        }
    }

    @Override
    public String getNoteUrl() {
        return mNoteUrl;
    }

    @Override
    public boolean isInBookShelf() {
        return isInBookShelf;
    }

    @Override
    public void onBackPressed() {
        //如果不在书架,提示添加书架
        if (!BookshelfHelper.isInBookShelf(mBookShelf.getNoteUrl())) {
            showSaveDialog();
        } else {
            super.onBackPressed();
        }
    }

    private void showSaveDialog() {
        DialogUtil.showMessagePositiveDialog(mContext, "提示", "喜欢这本书就加入到书架吧?", "取消", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                ReaderActivity.super.onBackPressed();
            }
        }, "添加", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                mPresenter.saveProgress(mBookShelf);
                ReaderActivity.super.onBackPressed();
            }
        }, true);
    }

}
