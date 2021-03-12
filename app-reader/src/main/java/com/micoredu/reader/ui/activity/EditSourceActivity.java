package com.micoredu.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;


import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.liuzhenli.common.base.BaseRvActivity;
import com.liuzhenli.common.constant.ARouterConstants;
import com.liuzhenli.common.constant.AppConstant;
import com.liuzhenli.common.constant.BookType;
import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.common.utils.ClipboardUtil;
import com.liuzhenli.common.utils.ScreenUtils;
import com.liuzhenli.common.utils.ToastUtil;
import com.micoredu.reader.DaggerReaderComponent;
import com.micoredu.reader.R;
import com.micoredu.reader.bean.BookSourceBean;
import com.micoredu.reader.bean.EditSource;
import com.micoredu.reader.databinding.ActEditsourceBinding;
import com.micoredu.reader.ui.adapter.EditSourceAdapter;
import com.micoredu.reader.ui.contract.EditSourceContract;
import com.micoredu.reader.ui.presenter.EditSourcePresenter;

import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;

/**
 * Description:
 *
 * @author liuzhenli 2020/11/16
 * Email: 848808263@qq.com
 */
public class EditSourceActivity extends BaseRvActivity<EditSourcePresenter, EditSource> implements EditSourceContract.View {

    public static final String BOOK_SOURCE = "book_source";
    private final List<EditSource> sourceEditList = new ArrayList<>();
    private final List<EditSource> findEditList = new ArrayList<>();

    private boolean mIsEditFind;
    private BookSourceBean mBookSource;
    private int serialNumber;
    private ActEditsourceBinding inflate;
    private boolean mIsSoftKeyBoardShowing;

    public static void start(Context context, BookSourceBean data) {
        Intent intent = new Intent(context, EditSourceActivity.class);
        intent.putExtra(BOOK_SOURCE, data);
        context.startActivity(intent);
    }

    @Override
    protected View bindContentView() {
        inflate = ActEditsourceBinding.inflate(getLayoutInflater());
        return inflate.getRoot();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerReaderComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    protected void initToolBar() {
        mTvTitle.setText("编辑书源");
    }

    @Override
    protected void initData() {
        mBookSource = (BookSourceBean) getIntent().getSerializableExtra(BOOK_SOURCE);
        if (mBookSource != null) {
            serialNumber = mBookSource.getSerialNumber();
        } else {
            mBookSource = new BookSourceBean();
            mTvTitle.setText("新建书源");
        }

        mToolBar.inflateMenu(R.menu.menu_edit_source);
        //菜单
        mToolBar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.item_save_source) {
                if (!canSaveBookSource()) {
                    return false;
                }
                mPresenter.saveBookSource(getBookSource(true));
            } else if (itemId == R.id.item_test_source) {
                if (!canSaveBookSource()) {
                    return false;
                }
                TestSourceActivity.start(mContext, getBookSource(true).getBookSourceUrl());
            } else if (itemId == R.id.item_source_copy) {
                ClipboardUtil.copyToClipboard(getBookSourceStr(true));
            } else if (itemId == R.id.item_source_copy_no_find) {
                ClipboardUtil.copyToClipboard(getBookSourceStr(false));
            } else if (itemId == R.id.item_to_source_rule) {
                ARouter.getInstance()
                        .build(ARouterConstants.ACT_WEB)
                        .withString("url", AppConstant.URL_SOURCE_RULE)
                        .navigation();
            } else if (itemId == R.id.item_to_source_past) {
                String content = ClipboardUtil.getContent();
                if (isEmpty(content) || content.trim().length() < 10) {
                    toast("好像不是书源哦");
                } else {
                    mPresenter.getBookSourceFromString(content);
                }
            }
            return false;
        });
    }

    @Override
    protected void configViews() {
        initAdapter(EditSourceAdapter.class, false, false);
        setText(mBookSource);
        ClickUtils.click(inflate.mEditFind, o -> {
            mIsEditFind = !mIsEditFind;
            mAdapter.clear();
            if (mIsEditFind) {
                inflate.mEditFind.setText(R.string.back);
                mAdapter.addAll(findEditList);
            } else {
                inflate.mEditFind.setText(R.string.edit_find);
                mAdapter.addAll(sourceEditList);
            }
        });
        inflate.mCbSourceEnable.setChecked(mBookSource.getEnable());

        inflate.mRvPunctuation.setData(key -> {
            if (isEmpty(key)) {
                return;
            }
            View view = getWindow().getDecorView().findFocus();
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                int start = editText.getSelectionStart();
                int end = editText.getSelectionEnd();
                Editable edit = editText.getEditableText();
                if (start < 0 || start >= edit.length()) {
                    edit.append(key);
                } else {
                    //光标所在位置插入文字
                    edit.replace(start, end, key);
                }
            }
        });

        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalChange());

    }


    class OnGlobalChange implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            Rect rect = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            int screenHeight = ScreenUtils.getScreenHeight(EditSourceActivity.this);
            int keyBordHeight = screenHeight - rect.bottom;
            if (keyBordHeight > screenHeight / 5) {
                mIsSoftKeyBoardShowing = true;
                showKeyboardTopPopupWindow();
            } else {
                mIsSoftKeyBoardShowing = false;
                closePopupWindow();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closePopupWindow();
    }

    @Override
    public void showError(Exception e) {

    }


    private boolean canSaveBookSource() {
        if (TextUtils.isEmpty(getBookSource(true).getBookSourceName()) || TextUtils.isEmpty(getBookSource(true).getBookSourceUrl())) {
            toast(getResources().getString(R.string.non_null_source_name_url));
            return false;
        }
        return true;
    }

    @Override
    public void complete() {

    }

    private void setText(BookSourceBean bookSourceBean) {
        sourceEditList.clear();
        findEditList.clear();
        mAdapter.notifyDataSetChanged();
        sourceEditList.add(new EditSource("bookSourceUrl", bookSourceBean.getBookSourceUrl(), R.string.book_source_url));
        sourceEditList.add(new EditSource("bookSourceName", bookSourceBean.getBookSourceName(), R.string.book_source_name));
        sourceEditList.add(new EditSource("bookSourceGroup", bookSourceBean.getBookSourceGroup(), R.string.book_source_group));
        sourceEditList.add(new EditSource("loginUrl", bookSourceBean.getLoginUrl(), R.string.book_source_login_url));
        //搜索
        sourceEditList.add(new EditSource("ruleSearchUrl", bookSourceBean.getRuleSearchUrl(), R.string.rule_search_url));
        sourceEditList.add(new EditSource("ruleSearchList", bookSourceBean.getRuleSearchList(), R.string.rule_search_list));
        sourceEditList.add(new EditSource("ruleSearchName", bookSourceBean.getRuleSearchName(), R.string.rule_search_name));
        sourceEditList.add(new EditSource("ruleSearchAuthor", bookSourceBean.getRuleSearchAuthor(), R.string.rule_search_author));
        sourceEditList.add(new EditSource("ruleSearchKind", bookSourceBean.getRuleSearchKind(), R.string.rule_search_kind));
        sourceEditList.add(new EditSource("ruleSearchLastChapter", bookSourceBean.getRuleSearchLastChapter(), R.string.rule_search_last_chapter));
        sourceEditList.add(new EditSource("ruleSearchIntroduce", bookSourceBean.getRuleSearchIntroduce(), R.string.rule_search_introduce));
        sourceEditList.add(new EditSource("ruleSearchCoverUrl", bookSourceBean.getRuleSearchCoverUrl(), R.string.rule_search_cover_url));
        sourceEditList.add(new EditSource("ruleSearchNoteUrl", bookSourceBean.getRuleSearchNoteUrl(), R.string.rule_search_note_url));
        //详情页
        sourceEditList.add(new EditSource("ruleBookUrlPattern", bookSourceBean.getRuleBookUrlPattern(), R.string.book_url_pattern));
        sourceEditList.add(new EditSource("ruleBookInfoInit", bookSourceBean.getRuleBookInfoInit(), R.string.rule_book_info_init));
        sourceEditList.add(new EditSource("ruleBookName", bookSourceBean.getRuleBookName(), R.string.rule_book_name));
        sourceEditList.add(new EditSource("ruleBookAuthor", bookSourceBean.getRuleBookAuthor(), R.string.rule_book_author));
        sourceEditList.add(new EditSource("ruleCoverUrl", bookSourceBean.getRuleCoverUrl(), R.string.rule_cover_url));
        sourceEditList.add(new EditSource("ruleIntroduce", bookSourceBean.getRuleIntroduce(), R.string.rule_introduce));
        sourceEditList.add(new EditSource("ruleBookKind", bookSourceBean.getRuleBookKind(), R.string.rule_book_kind));
        sourceEditList.add(new EditSource("ruleBookLastChapter", bookSourceBean.getRuleBookLastChapter(), R.string.rule_book_last_chapter));
        sourceEditList.add(new EditSource("ruleChapterUrl", bookSourceBean.getRuleChapterUrl(), R.string.rule_chapter_list_url));
        //目录页
        sourceEditList.add(new EditSource("ruleChapterUrlNext", bookSourceBean.getRuleChapterUrlNext(), R.string.rule_chapter_list_url_next));
        sourceEditList.add(new EditSource("ruleChapterList", bookSourceBean.getRuleChapterList(), R.string.rule_chapter_list));
        sourceEditList.add(new EditSource("ruleChapterName", bookSourceBean.getRuleChapterName(), R.string.rule_chapter_name));
        sourceEditList.add(new EditSource("ruleContentUrl", bookSourceBean.getRuleContentUrl(), R.string.rule_content_url));
        //正文页
        sourceEditList.add(new EditSource("ruleContentUrlNext", bookSourceBean.getRuleContentUrlNext(), R.string.rule_content_url_next));
        sourceEditList.add(new EditSource("ruleBookContent", bookSourceBean.getRuleBookContent(), R.string.rule_book_content));
        sourceEditList.add(new EditSource("ruleBookContentReplace", bookSourceBean.getRuleBookContentReplace(), R.string.rule_book_content_replace));
        sourceEditList.add(new EditSource("httpUserAgent", bookSourceBean.getHttpUserAgent(), R.string.source_user_agent));

        //发现
        findEditList.add(new EditSource("ruleFindUrl", bookSourceBean.getRuleFindUrl(), R.string.rule_find_url));
        findEditList.add(new EditSource("ruleFindList", bookSourceBean.getRuleFindList(), R.string.rule_find_list));
        findEditList.add(new EditSource("ruleFindName", bookSourceBean.getRuleFindName(), R.string.rule_find_name));
        findEditList.add(new EditSource("ruleFindAuthor", bookSourceBean.getRuleFindAuthor(), R.string.rule_find_author));
        findEditList.add(new EditSource("ruleFindKind", bookSourceBean.getRuleFindKind(), R.string.rule_find_kind));
        findEditList.add(new EditSource("ruleFindIntroduce", bookSourceBean.getRuleFindIntroduce(), R.string.rule_find_introduce));
        findEditList.add(new EditSource("ruleFindLastChapter", bookSourceBean.getRuleFindLastChapter(), R.string.rule_find_last_chapter));
        findEditList.add(new EditSource("ruleFindCoverUrl", bookSourceBean.getRuleFindCoverUrl(), R.string.rule_find_cover_url));
        findEditList.add(new EditSource("ruleFindNoteUrl", bookSourceBean.getRuleFindNoteUrl(), R.string.rule_find_note_url));
        mAdapter.clear();
        mAdapter.addAll(sourceEditList);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closePopupWindow();
    }

    private BookSourceBean getBookSource(boolean hasFind) {
        BookSourceBean bookSource = new BookSourceBean();
        for (EditSource itemData : sourceEditList) {
            String value = itemData.getValue();
            switch (itemData.getKey()) {
                case "bookSourceUrl":
                    bookSource.setBookSourceUrl(value);
                    break;
                case "bookSourceName":
                    bookSource.setBookSourceName(value);
                    break;
                case "bookSourceGroup":
                    bookSource.setBookSourceGroup(value);
                    break;
                case "loginUrl":
                    bookSource.setLoginUrl(value);
                    break;
                case "ruleSearchUrl":
                    bookSource.setRuleSearchUrl(value);
                    break;
                case "ruleSearchList":
                    bookSource.setRuleSearchList(value);
                    break;
                case "ruleSearchName":
                    bookSource.setRuleSearchName(value);
                    break;
                case "ruleSearchAuthor":
                    bookSource.setRuleSearchAuthor(value);
                    break;
                case "ruleSearchKind":
                    bookSource.setRuleSearchKind(value);
                    break;
                case "ruleSearchIntroduce":
                    bookSource.setRuleSearchIntroduce(value);
                    break;
                case "ruleSearchLastChapter":
                    bookSource.setRuleSearchLastChapter(value);
                    break;
                case "ruleSearchCoverUrl":
                    bookSource.setRuleSearchCoverUrl(value);
                    break;
                case "ruleSearchNoteUrl":
                    bookSource.setRuleSearchNoteUrl(value);
                    break;
                case "ruleBookUrlPattern":
                    bookSource.setRuleBookUrlPattern(value);
                    break;
                case "ruleBookInfoInit":
                    bookSource.setRuleBookInfoInit(value);
                    break;
                case "ruleBookName":
                    bookSource.setRuleBookName(value);
                    break;
                case "ruleBookAuthor":
                    bookSource.setRuleBookAuthor(value);
                    break;
                case "ruleCoverUrl":
                    bookSource.setRuleCoverUrl(value);
                    break;
                case "ruleIntroduce":
                    bookSource.setRuleIntroduce(value);
                    break;
                case "ruleBookKind":
                    bookSource.setRuleBookKind(value);
                    break;
                case "ruleBookLastChapter":
                    bookSource.setRuleBookLastChapter(value);
                    break;
                case "ruleChapterUrl":
                    bookSource.setRuleChapterUrl(value);
                    break;
                case "ruleChapterUrlNext":
                    bookSource.setRuleChapterUrlNext(value);
                    break;
                case "ruleChapterList":
                    bookSource.setRuleChapterList(value);
                    break;
                case "ruleChapterName":
                    bookSource.setRuleChapterName(value);
                    break;
                case "ruleContentUrl":
                    bookSource.setRuleContentUrl(value);
                    break;
                case "ruleContentUrlNext":
                    bookSource.setRuleContentUrlNext(value);
                    break;
                case "ruleBookContent":
                    bookSource.setRuleBookContent(value);
                    break;
                case "ruleBookContentReplace":
                    bookSource.setRuleBookContentReplace(value);
                    break;
                case "httpUserAgent":
                    bookSource.setHttpUserAgent(value);
                    break;
                default:
                    break;

            }
        }
        if (hasFind) {
            for (EditSource sourceEdit : findEditList) {
                String value = sourceEdit.getValue();
                switch (sourceEdit.getKey()) {
                    case "ruleFindUrl":
                        bookSource.setRuleFindUrl(value);
                        break;
                    case "ruleFindList":
                        bookSource.setRuleFindList(value);
                        break;
                    case "ruleFindName":
                        bookSource.setRuleFindName(value);
                        break;
                    case "ruleFindAuthor":
                        bookSource.setRuleFindAuthor(value);
                        break;
                    case "ruleFindKind":
                        bookSource.setRuleFindKind(value);
                        break;
                    case "ruleFindIntroduce":
                        bookSource.setRuleFindIntroduce(value);
                        break;
                    case "ruleFindLastChapter":
                        bookSource.setRuleFindLastChapter(value);
                        break;
                    case "ruleFindCoverUrl":
                        bookSource.setRuleFindCoverUrl(value);
                        break;
                    case "ruleFindNoteUrl":
                        bookSource.setRuleFindNoteUrl(value);
                        break;
                    default:
                        break;
                }
            }
        }
        bookSource.setSerialNumber(serialNumber);
        bookSource.setEnable(inflate.mCbSourceEnable.isChecked());
        bookSource.setBookSourceType(inflate.cbIsAudio.isChecked() ? BookType.AUDIO : null);
        return bookSource;
    }

    @Override
    public void showSaveBookResult() {
        ToastUtil.showLongToast(mContext, getResources().getString(R.string.success));
    }

    @Override
    public void showBookSource(BookSourceBean data) {
        try {
            if (data != null) {
                setText(data);
            } else {
                toast("好像不是书源内容");
            }
        } catch (Exception e) {
            toast("数据格式不对");
            e.printStackTrace();
        }
    }

    public String getBookSourceStr(boolean hasFind) {
        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();
        return gson.toJson(getBookSource(hasFind));
    }

    private void showKeyboardTopPopupWindow() {
        if (isFinishing()) {
            return;
        }
        if (inflate.mRvPunctuation != null && inflate.mRvPunctuation.getVisibility() == View.VISIBLE) {
            return;
        }
        if (inflate.mRvPunctuation != null & !this.isFinishing()) {
            inflate.mRvPunctuation.setVisibility(View.VISIBLE);
        }
    }

    private void closePopupWindow() {
        if (inflate.mRvPunctuation != null && inflate.mRvPunctuation.getVisibility() == View.VISIBLE) {
            inflate.mRvPunctuation.setVisibility(View.GONE);
        }
    }
}
