package com.micoredu.reader.ui.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.liuzhenli.common.BitIntentDataManager;
import com.liuzhenli.common.constant.RxBusTag;
import com.liuzhenli.common.utils.AppSharedPreferenceHelper;
import com.liuzhenli.common.utils.BatteryUtil;
import com.liuzhenli.common.utils.IntentUtils;
import com.liuzhenli.common.utils.ScreenUtils;
import com.liuzhenli.common.utils.ShareUtils;
import com.liuzhenli.common.widget.bar.BarHide;
import com.liuzhenli.common.utils.ToastUtil;
import com.liuzhenli.common.widget.bar.ImmersionBar;
import com.micoredu.reader.ReaderComponent;
import com.micoredu.reader.R;
import com.micoredu.reader.animation.PageAnimation;
import com.micoredu.reader.bean.BookShelfBean;
import com.micoredu.reader.bean.BookmarkBean;
import com.micoredu.reader.bean.OpenChapterBean;
import com.micoredu.reader.bean.ReplaceRuleBean;
import com.micoredu.reader.databinding.ActReadBinding;
import com.micoredu.reader.helper.BookshelfHelper;
import com.micoredu.reader.helper.ReadConfigManager;
import com.micoredu.reader.model.ReplaceRuleManager;
import com.micoredu.reader.page.PageLoaderNet;
import com.micoredu.reader.page.PageView;
import com.micoredu.reader.bean.BookChapterBean;
import com.micoredu.reader.page.PageLoader;
import com.micoredu.reader.reciever.BatteryAndTimeChangeReceiver;
import com.micoredu.reader.ui.contract.ReadContract;
import com.micoredu.reader.ui.presenter.ReadPresenter;
import com.liuzhenli.common.widget.DialogUtil;
import com.micoredu.reader.widgets.ReadLongPressPop;
import com.micoredu.reader.widgets.ReplaceRuleDialog;
import com.micoredu.reader.widgets.menu.ReadBottomMenu;
import com.micoredu.reader.widgets.menu.ReadBrightnessMenu;
import com.micoredu.reader.widgets.menu.ReadSettingMenu;
import com.micoredu.reader.widgets.menu.ReadTopBarMenu;
import com.microedu.theme.ViewUtil;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

import static com.liuzhenli.common.BitIntentDataManager.DATA_KEY;
import static com.liuzhenli.common.BitIntentDataManager.getInstance;
import static com.liuzhenli.common.constant.AppConstant.BookOpenFrom.OPEN_FROM_APP;

/**
 * describe:阅读页
 *
 * @author Liuzhenli on 2019-11-09 19:29
 * @since 1.0.0
 */
public class ReaderActivity extends BaseReaderActivity<ReadPresenter, ActReadBinding> implements ReadContract.View {
    public static final String NOTE_URL = "noteUrl";
    public static final String CHAPTER_ID = "chapterID";
    public static final String PROGRESS = "progress";
    public static final String OPEN_FROM = "openFrom";

    private Boolean startShareAnim = false;
    private float mAlpha = 0F;
    private PageLoader mPageLoader;
    private int mCurrentChapterIndex;
    /***章节id mChapterId  durChapterIndex*/
    private int mChapterId;
    private float mProgress;
    private int mOpenFrom;
    private String mDataKey;
    /***是否在书架里*/
    private boolean mIsInBookShelf = false;

    private String mNoteUrl;
    private BookShelfBean mBookShelf;
    private int lastX, lastY;
    private BatteryAndTimeChangeReceiver batteryAndTimeChangeReceiver;

    public static void start(Context context, String noteUrl, String chapterId, float progress, String openFrom) {
        Intent intent = new Intent(context, ReaderActivity.class);
        intent.putExtra(NOTE_URL, noteUrl);
        intent.putExtra(CHAPTER_ID, chapterId);
        intent.putExtra(PROGRESS, progress);
        intent.putExtra(OPEN_FROM, openFrom);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mNoteUrl = savedInstanceState.getString(NOTE_URL);
            mIsInBookShelf = savedInstanceState.getBoolean("isAdd");
        }
    }

    @Override
    protected ActReadBinding inflateView(LayoutInflater inflater) {
        return ActReadBinding.inflate(inflater);
    }

    @Override
    protected void setupActivityComponent(ReaderComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    protected void initToolBar() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (batteryAndTimeChangeReceiver == null && mPageLoader != null) {
            batteryAndTimeChangeReceiver = new BatteryAndTimeChangeReceiver(mPageLoader);
            batteryAndTimeChangeReceiver.register(mContext, batteryAndTimeChangeReceiver);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mBookShelf != null) {
            outState.putString("noteUrl", mBookShelf.getNoteUrl());
            outState.putBoolean("isAdd", mIsInBookShelf);
            String key = String.valueOf(System.currentTimeMillis());
            String bookKey = "book" + key;
            getIntent().putExtra("bookKey", bookKey);
            BitIntentDataManager.getInstance().putData(bookKey, mBookShelf.clone());
            String chapterListKey = "chapterList" + key;
            getIntent().putExtra("chapterListKey", chapterListKey);
            BitIntentDataManager.getInstance().putData(chapterListKey, mPresenter.getChapterList());
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initData() {
        if (getIntent() != null) {
            startShareAnim = getIntent().getBooleanExtra(START_SHEAR_ELE, false);
            mOpenFrom = getIntent().getIntExtra(OPEN_FROM, OPEN_FROM_APP);
            mDataKey = getIntent().getStringExtra(DATA_KEY);
            mNoteUrl = getIntent().getStringExtra(NOTE_URL);
            mChapterId = getIntent().getIntExtra(CHAPTER_ID, 0);
            mProgress = getIntent().getFloatExtra(PROGRESS, 0);
        }
        if (mDataKey != null) {
            mBookShelf = (BookShelfBean) getInstance().getData(mDataKey);
            mNoteUrl = mBookShelf.getNoteUrl();
        }

        if (mBookShelf == null) {
            mBookShelf = BookshelfHelper.getBook(mNoteUrl);
        }

        if (mBookShelf == null) {
            List<BookShelfBean> allBook = BookshelfHelper.getAllBook();
            if (allBook != null && allBook.size() > 0) {
                mBookShelf = allBook.get(0);
            }
        }

        if (mBookShelf != null && mPresenter.getChapterList().isEmpty()) {
            mPresenter.setChapterList(BookshelfHelper.getChapterList(mBookShelf.getNoteUrl()));
            mPresenter.updateBookInfo(mBookShelf.getBookInfoBean());
            mIsInBookShelf = BookshelfHelper.isInBookShelf(mBookShelf.getNoteUrl());
        } else {
            mBookShelf = new BookShelfBean();
            mBookShelf.setNoteUrl("empty book");
        }

        if (mChapterId > 0) {
            mBookShelf.setDurChapter(mChapterId);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void configViews() {
        binding.mTopBar.getToolBar().setNavigationOnClickListener(v -> onBackPressed());
        initPageView();
        //底部menu中 菜单按钮点击从底部弹出一个章节菜单
        binding.mVBottomMenu.setOnMenuElementClickListener(new ReadBottomMenu.OnElementClickListener() {
            @Override
            public void onMenuClick() {
                BookChapterListActivity.start(mContext, mBookShelf, mPresenter.getChapterList(), false, true, mChapterId);
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
                ViewUtil.hideBottomView(binding.mVBottomMenu);
                ViewUtil.hideTopView(binding.mTopBar);
                ViewUtil.showBottomView(binding.mVBrightnessSettingMenu);
            }

            @Override
            public void onNightModeClick() {
                boolean isNightTheme = ReadConfigManager.getInstance().getIsNightTheme();
                ReadConfigManager.getInstance().setIsNightTheme(!isNightTheme);
                ReadConfigManager.getInstance().initTextDrawableIndex();
                ReadConfigManager.getInstance().setTextDrawableIndex(ReadConfigManager.getInstance().getTextDrawableIndex());
                binding.mPageView.setBackground(ReadConfigManager.getInstance().getTextBackground(mContext));
                mPageLoader.refreshUi();
                binding.mVBottomMenu.changeTheme();
            }

            @Override
            public void onSettingClick() {
                ViewUtil.hideBottomView(binding.mVBottomMenu);
                ViewUtil.hideTopView(binding.mTopBar);
                ViewUtil.showBottomView(binding.mVSettingMenu);
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
                    binding.mTopBar.setChapterTitle(mPresenter.getChapterList().get(mCurrentChapterIndex).getDurChapterName());
                }
            }
        });
        binding.mVBrightnessSettingMenu.setCallback(this, new ReadBrightnessMenu.CallBack() {
            @Override
            public void onProtectEyeClick(boolean on) {
                if (on) {
                    mAlpha = 3E-1F;
                } else {
                    mAlpha = 0f;
                }
                AppSharedPreferenceHelper.setProtectEyeReadMode(on);
                binding.mVProtectEye.setAlpha(mAlpha);
                binding.mVProtectEye.invalidate();
            }
        });
        binding.mVSettingMenu.setReadSettingCallBack(new ReadSettingMenu.ReadSettingCallBack() {
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
                    binding.mPageView.setBackground(ReadConfigManager.getInstance().getTextBackground(mContext));
                    mPageLoader.refreshUi();
                }
            }
        });
        binding.mVBrightnessSettingMenu.setProtectedEyeMode(AppSharedPreferenceHelper.tetProtectEyeReadMode());
        binding.mVProtectEye.setAlpha(mAlpha);
        binding.mVBrightnessSettingMenu.setBrightnessFollowSystem(ReadConfigManager.getInstance().getLightFollowSys());
        binding.cursorLeft.setOnTouchListener(this);
        binding.cursorRight.setOnTouchListener(this);
        binding.mViewRoot.setOnTouchListener(this);
        binding.mTopBar.setOnMenuElementClickListener(new ReadTopBarMenu.OnElementClickListener() {
            @Override
            public void onBackClick() {

            }

            @Override
            public void onMenuClick() {
                toast("已添加书签");
                String content = mPageLoader.getContent();
                String saveContent = null;
                if (!TextUtils.isEmpty(content) && content.trim().length() > 0) {
                    String[] split = content.trim().split("\n");
                    for (int i = 1; i < split.length; i++) {
                        if (split[i].length() > 0) {
                            saveContent = split[i];
                            break;
                        }
                    }
                }
                BookshelfHelper.saveBookmark(new BookmarkBean(System.currentTimeMillis(), mNoteUrl,
                        mBookShelf.getBookInfoBean().getName(), mBookShelf.getDurChapterName(), mBookShelf.getDurChapter(), mBookShelf.getDurChapterPage(), saveContent));
            }
        });
        setBrightness();
        binding.mVBottomMenu.changeTheme();
    }

    /**
     * 阅读页面信息
     */
    private void initPageView() {
        mPageLoader = binding.mPageView.getPageLoader((BaseReaderActivity) mContext, mBookShelf, new PageLoader.Callback() {
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
                binding.mTopBar.setChapterTitle(mPresenter.getChapterList().get(mCurrentChapterIndex).getDurChapterName());
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
                binding.mTopBar.setChapterTitle(mPresenter.getChapterList().get(mCurrentChapterIndex).getDurChapterName());
            }

            @Override
            public void onPageCountChange(int count) {
            }

            @Override
            public void onPageChange(int chapterIndex, int pageIndex, boolean resetReadAloud) {
                mChapterId = chapterIndex;
                //记录阅读位置
                mBookShelf.setDurChapter(chapterIndex);
                mBookShelf.setDurChapterPage(pageIndex);
                if (mIsInBookShelf) {
                    mPresenter.saveProgress(mBookShelf);
                }
                mPresenter.saveReadHistory();
            }

            @Override
            public void vipPop() {

            }
        });
        //电量信息
        mPageLoader.updateBattery(BatteryUtil.INSTANCE.getBatteryLevel(this));
        //书页点击
        binding.mPageView.setTouchListener(new PageView.TouchListener() {
            @Override
            public void onTouch() {
                binding.mPageView.post(new Runnable() {
                    @Override
                    public void run() {
                        ViewUtil.hideBottomView(binding.mVBrightnessSettingMenu);
                        ViewUtil.hideBottomView(binding.mVBottomMenu);
                        ViewUtil.hideTopView(binding.mTopBar);
                        ViewUtil.hideBottomView(binding.mVSettingMenu);
                    }
                });
            }

            @Override
            public void onTouchClearCursor() {
                clearSelect();
            }

            @Override
            public void onLongPress() {
                if (!binding.mPageView.isRunning()) {
                    selectTextCursorShow();
                    showAction(binding.cursorLeft);
                }
            }

            @Override
            public void center() {
                if (binding.mVBottomMenu.getVisibility() != View.VISIBLE) {
                    ViewUtil.showBottomView(binding.mVBottomMenu);
                    ViewUtil.showTopView(binding.mTopBar);
                }
                binding.mTopBar.getToolBar().setTitle(mBookShelf.getBookInfoBean().getName());
                binding.mTopBar.getToolBar().setTitleTextColor(Color.WHITE);
            }

        });
        mPageLoader.refreshChapterList();
        initReadLongPressPop();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.cursorLeft || v.getId() == R.id.cursorRight) {
            int ea = event.getAction();
            switch (ea) {
                case MotionEvent.ACTION_DOWN:
                    // 获取触摸事件触摸位置的原始X坐标
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    binding.readLongPress.setVisibility(View.INVISIBLE);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int dx = (int) event.getRawX() - lastX;
                    int dy = (int) event.getRawY() - lastY;
                    int l = v.getLeft() + dx;
                    int b = v.getBottom() + dy;
                    int r = v.getRight() + dx;
                    int t = v.getTop() + dy;

                    v.layout(l, t, r, b);
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    v.postInvalidate();

                    //移动过程中要画线
                    binding.mPageView.setSelectMode(PageView.SelectMode.SelectMoveForward);

                    int hh = binding.cursorLeft.getHeight();
                    int ww = binding.cursorLeft.getWidth();

                    if (v.getId() == R.id.cursorLeft) {
                        binding.mPageView.setFirstSelectTxtChar(binding.mPageView.getCurrentTxtChar(lastX + ww, lastY - hh));
                    } else {
                        binding.mPageView.setLastSelectTxtChar(binding.mPageView.getCurrentTxtChar(lastX - ww, lastY - hh));
                    }

                    binding.mPageView.invalidate();

                    break;
                case MotionEvent.ACTION_UP:
                    showAction(v);
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    @Override
    public void showError(Exception e) {

    }

    @Override
    public void complete() {

    }

    @Override
    public void autoChangeSource() {
        mPresenter.changeBookSource();
    }

    /***
     * 没有上一页或者没有下一页
     */
    @Override
    public void showSnackBar(PageView pageView, String msg) {
        ToastUtil.showToast(msg);
    }

    @Override
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(true);
        mImmersionBar.hideBar(BarHide.FLAG_HIDE_STATUS_BAR);
        mImmersionBar.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        unregisterReceiver(batteryAndTimeChangeReceiver);
        if (mPageLoader != null) {
            mPageLoader.closeBook();
        }
    }

    @Override
    public void showBookInfo(BookShelfBean bookInfo) {
        this.mBookShelf = bookInfo;
    }

    @Override
    public void showFontFile(File[] files) {
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

    @Override
    public BookShelfBean getBookShelf() {
        return this.mBookShelf;
    }

    @Override
    public void showChangeBookSourceResult(BookShelfBean book) {
        this.mBookShelf = book;
        if (mPageLoader != null && mPageLoader instanceof PageLoaderNet) {
            ((PageLoaderNet) mPageLoader).changeSourceFinish(book);
        }
    }

    @Override
    public String getNoteUrl() {
        return mNoteUrl;
    }

    @Override
    public boolean isInBookShelf() {
        return mIsInBookShelf;
    }

    @Override
    public void onBackPressed() {
        //如果不在书架,提示添加书架
        if (mBookShelf == null) {
            super.onBackPressed();
        } else if (!BookshelfHelper.isInBookShelf(mBookShelf.getNoteUrl())) {
            showSaveDialog();
        } else {
            mPresenter.saveProgress(mBookShelf);
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

    /**
     * 长按选择按钮
     */
    private void initReadLongPressPop() {
        binding.readLongPress.setListener(new ReadLongPressPop.OnBtnClickListener() {
            @Override
            public void copySelect() {
                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText(null, binding.mPageView.getSelectStr());
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clipData);
                    toast("内容已经复制");
                }
                clearSelect();
            }

            @Override
            public void search() {
                IntentUtils.openBrowser(mContext, String.format("https://www.baidu.com/s?wd=%s", binding.mPageView.getSelectStr()));
                clearSelect();
            }

            @Override
            public void replaceSelect() {
                ReplaceRuleBean oldRuleBean = new ReplaceRuleBean();
                oldRuleBean.setReplaceSummary(binding.mPageView.getSelectStr().trim());
                oldRuleBean.setEnable(true);
                oldRuleBean.setRegex(binding.mPageView.getSelectStr().trim());
                oldRuleBean.setIsRegex(false);
                oldRuleBean.setReplacement("");
                oldRuleBean.setSerialNumber(0);
                oldRuleBean.setUseTo(String.format("%s,%s", mBookShelf.getBookInfoBean().getName(), mBookShelf.getTag()));

                ReplaceRuleDialog.builder(mContext, oldRuleBean, mBookShelf, ReplaceRuleDialog.DefaultUI)
                        .setPositiveButton(replaceRuleBean1 ->
                                ReplaceRuleManager.saveData(replaceRuleBean1)
                                        .subscribe(new SingleObserver<Boolean>() {
                                            @Override
                                            public void onSubscribe(Disposable d) {

                                            }

                                            @Override
                                            public void onSuccess(Boolean aBoolean) {
                                                binding.cursorLeft.setVisibility(View.INVISIBLE);
                                                binding.cursorRight.setVisibility(View.INVISIBLE);
                                                binding.readLongPress.setVisibility(View.INVISIBLE);
                                                binding.mPageView.setSelectMode(PageView.SelectMode.Normal);
//                                                moDialogHUD.dismiss();
                                                refresh(false);
                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }
                                        })).show();
            }

            @Override
            public void replaceSelectAd() {
                String selectString = binding.mPageView.getSelectStr();
                if (selectString != null) {
                    String spacer = null;
                    String name = (mBookShelf.getBookInfoBean().getName());
                    if (name != null) {
                        if (name.trim().length() > 0) {
                            spacer = "|" + Pattern.quote(name.trim());
                        }
                    }
                    name = (mBookShelf.getBookInfoBean().getAuthor());
                    if (name != null) {
                        if (name.trim().length() > 0) {
                            if (spacer != null) {
                                spacer = spacer + "|" + Pattern.quote(name.trim());
                            } else {
                                spacer = "|" + Pattern.quote(name.trim());
                            }
                        }
                    }
                    String rule = "(\\s*\n\\s*" + spacer + ")";
                    selectString = ReplaceRuleManager.formateAdRule(
                            selectString.replaceAll(rule, "\n")
                    );

                    Log.i("selectString.afterAd2", selectString);

                }
                ReplaceRuleBean oldRuleBean = new ReplaceRuleBean();
                oldRuleBean.setReplaceSummary(getString(R.string.replace_ad) + "-" + mBookShelf.getTag());
                oldRuleBean.setEnable(true);
                oldRuleBean.setRegex(selectString);
                oldRuleBean.setIsRegex(false);
                oldRuleBean.setReplacement("");
                oldRuleBean.setSerialNumber(0);
                oldRuleBean.setUseTo(String.format(mBookShelf.getTag()));

                ReplaceRuleDialog.builder(mContext, oldRuleBean, mBookShelf, ReplaceRuleDialog.AddAdUI)
                        .setPositiveButton(replaceRuleBean1 ->
                                ReplaceRuleManager.mergeAdRules(replaceRuleBean1)
                                        .subscribe(new SingleObserver<Boolean>() {
                                            @Override
                                            public void onSubscribe(Disposable d) {

                                            }

                                            @Override
                                            public void onSuccess(Boolean aBoolean) {
                                                binding.cursorLeft.setVisibility(View.INVISIBLE);
                                                binding.cursorRight.setVisibility(View.INVISIBLE);
                                                binding.readLongPress.setVisibility(View.INVISIBLE);
                                                binding.mPageView.setSelectMode(PageView.SelectMode.Normal);
//                                                moDialogHUD.dismiss();
                                                refresh(false);
                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                        })).show();

            }

            @Override
            public void share() {
                ShareUtils.INSTANCE.share(mContext, binding.mPageView.getSelectStr());
                clearSelect();
            }
        });
    }

    /**
     * 显示
     */
    private void selectTextCursorShow() {
        if (binding.mPageView.getFirstSelectTxtChar() == null || binding.mPageView.getLastSelectTxtChar() == null) {
            return;
        }
        //show Cursor on current position
        cursorShow();
        //set current word selected
        binding.mPageView.invalidate();
    }

    private void cursorShow() {
        binding.cursorLeft.setVisibility(View.VISIBLE);
        binding.cursorRight.setVisibility(View.VISIBLE);
        int hh = binding.cursorLeft.getHeight();
        int ww = binding.cursorLeft.getWidth();
        if (binding.mPageView.getFirstSelectTxtChar() != null) {
            binding.cursorLeft.setX(binding.mPageView.getFirstSelectTxtChar().getTopLeftPosition().x - ww);
            binding.cursorLeft.setY(binding.mPageView.getFirstSelectTxtChar().getBottomLeftPosition().y);
            binding.cursorRight.setX(binding.mPageView.getFirstSelectTxtChar().getBottomRightPosition().x);
            binding.cursorRight.setY(binding.mPageView.getFirstSelectTxtChar().getBottomRightPosition().y);
        }
    }


    public void showAction(View clickView) {

        int hh = binding.cursorLeft.getHeight();
        int ww = binding.cursorLeft.getWidth();
        if (binding.mPageView.getFirstSelectTxtChar() != null && binding.mPageView.getLastSelectTxtChar() != null) {
            binding.cursorLeft.setX(Objects.requireNonNull(binding.mPageView.getFirstSelectTxtChar().getTopLeftPosition()).x - ww);
            binding.cursorLeft.setY(Objects.requireNonNull(binding.mPageView.getFirstSelectTxtChar().getBottomLeftPosition()).y);
            binding.cursorRight.setX(Objects.requireNonNull(binding.mPageView.getLastSelectTxtChar().getBottomRightPosition()).x);
            binding.cursorRight.setY(binding.mPageView.getLastSelectTxtChar().getBottomRightPosition().y);
        }

        binding.readLongPress.setVisibility(View.VISIBLE);
        //弹窗的宽度
        int width = binding.readLongPress.getWidth();
        //弹窗的起始位置
        int x = (int) (binding.cursorLeft.getX() / 2 + binding.cursorLeft.getWidth() / 2 + binding.cursorRight.getX() / 2 + binding.cursorLeft.getWidth() / 2);
        //1.如果选择的文本行数大于1,则靠中间防止  2.如果太靠右，则靠左
        int[] aa = ScreenUtils.getScreenSize(this);
        if (binding.mPageView.getSelectLines() > 1) {
            binding.readLongPress.setX(ScreenUtils.getScreenWidth(ReaderActivity.this) / 2f - binding.readLongPress.getWidth() / 2f);
        } else if (x < width / 2 + ScreenUtils.dpToPx(8)) {
            binding.readLongPress.setX(ScreenUtils.dpToPx(8));
        } else if (x > (aa[0] - width / 2 - ScreenUtils.dpToPx(8))) {
            binding.readLongPress.setX(aa[0] - width - ScreenUtils.dpToPx(8));
        } else {
            binding.readLongPress.setX(x - width / 2f);
        }

        //如果太靠上
        if ((binding.cursorLeft.getY() - ScreenUtils.spToPx(ReadConfigManager.getInstance().getTextSize()) - ScreenUtils.dpToPx(60)) < 0) {
            binding.readLongPress.setY(binding.cursorLeft.getY() - ScreenUtils.spToPx(ReadConfigManager.getInstance().getTextSize()));
        } else {
            binding.readLongPress.setY(binding.cursorLeft.getY() - ScreenUtils.spToPx(ReadConfigManager.getInstance().getTextSize()) - ScreenUtils.dpToPx(60));
        }
    }

    /**
     * 刷新
     */
    public void refresh(boolean recreate) {
        if (recreate) {
            recreate();
        } else {
            binding.mViewRoot.setBackground(ReadConfigManager.getInstance().getTextBackground(this));
            if (mPageLoader != null) {
                mPageLoader.refreshUi();
            }
            //readInterfacePop.setBg();
        }
    }


    private void setBrightness() {
        if (ReadConfigManager.getInstance().getLightFollowSys()) {
            ScreenUtils.setScreenBrightness(this, -1);
        } else {
            ScreenUtils.setScreenBrightness(this, ReadConfigManager.getInstance().getLight());
        }
    }

    private void clearSelect() {
        binding.cursorLeft.setVisibility(View.INVISIBLE);
        binding.cursorRight.setVisibility(View.INVISIBLE);
        binding.readLongPress.setVisibility(View.INVISIBLE);
        binding.mPageView.clearSelect();
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(RxBusTag.SKIP_TO_CHAPTER)})
    public void skipToChapter(OpenChapterBean openChapterBean) {
        mPageLoader.skipToChapter(openChapterBean.getChapterIndex(), openChapterBean.getPageIndex());
    }

    @Override
    public Map<String, Object> getPageInfo(Map<String, Object> data) {
        data.put("bookShelf", mBookShelf);
        return data;
    }
}
