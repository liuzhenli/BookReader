package com.micoredu.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;


import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.liuzhenli.common.constant.ARouterConstants;
import com.liuzhenli.common.constant.AppConstant;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.common.utils.ClipboardUtil;
import com.liuzhenli.common.utils.ScreenUtils;
import com.liuzhenli.common.utils.ToastUtil;
import com.microedu.lib.reader.R;
import com.micoredu.reader.bean.BookSource;
import com.micoredu.reader.bean.EditEntity;
import com.micoredu.reader.constant.BookType;
import com.microedu.lib.reader.databinding.ActEditsourceBinding;
import com.micoredu.reader.ui.adapter.EditSourceAdapter;
import com.micoredu.reader.ui.contract.EditSourceContract;

import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;

/**
 * Description:edit book source page
 *
 * @author liuzhenli 2020/11/16
 * Email: 848808263@qq.com
 */
/*
public class EditSourceActivity extends ReaderBaseRVActivity<EditSourcePresenter, EditEntity, ActEditsourceBinding> implements EditSourceContract.View {

    public static final String BOOK_SOURCE = "book_source";
    private final List<EditEntity> sourceEditList = new ArrayList<>();
    private final List<EditEntity> findEditList = new ArrayList<>();

    private boolean mIsEditFind;
    private BookSource mBookSource;
    private int serialNumber;
    private boolean mIsSoftKeyBoardShowing;

    public static void start(Context context, BookSource data) {
        Intent intent = new Intent(context, EditSourceActivity.class);
        intent.putExtra(BOOK_SOURCE, data);
        context.startActivity(intent);
    }

    @Override
    protected void setupActivityComponent(ReaderComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    protected ActEditsourceBinding inflateView(LayoutInflater inflater) {
        return ActEditsourceBinding.inflate(inflater);
    }

    @Override
    protected void initToolBar() {
        if (mBookSource != null) {
            mTvTitle.setText("编辑书源");
        } else {
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
    protected void initData() {
        if (mBookSource != null) {

        } else {
            mBookSource = new BookSource();
        }
    }

    @Override
    protected void configViews() {
        initAdapter(EditSourceAdapter.class, false, false);
        setText(mBookSource);
        ClickUtils.click(binding.mEditFind, o -> {
            mIsEditFind = !mIsEditFind;
            mAdapter.clear();
            if (mIsEditFind) {
                binding.mEditFind.setText(R.string.back);
                mAdapter.addAll(findEditList);
            } else {
                binding.mEditFind.setText(R.string.edit_find);
                mAdapter.addAll(sourceEditList);
            }
        });
        binding.mCbSourceEnable.setChecked(mBookSource.getEnabled());

        binding.mRvPunctuation.setData(key -> {
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

    private void setText(BookSource BookSource) {
        sourceEditList.clear();
        findEditList.clear();
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closePopupWindow();
    }

    private BookSource getBookSource(boolean hasFind) {
        BookSource bookSource = new BookSource();
        return bookSource;
    }

    @Override
    public void showSaveBookResult() {
        ToastUtil.showLongToast(mContext, getResources().getString(R.string.success));
    }

    @Override
    public void showBookSource(BookSource data) {
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
        if (binding.mRvPunctuation != null && binding.mRvPunctuation.getVisibility() == View.VISIBLE) {
            return;
        }
        if (binding.mRvPunctuation != null & !this.isFinishing()) {
            binding.mRvPunctuation.setVisibility(View.VISIBLE);
        }
    }

    private void closePopupWindow() {
        if (binding.mRvPunctuation != null && binding.mRvPunctuation.getVisibility() == View.VISIBLE) {
            binding.mRvPunctuation.setVisibility(View.GONE);
        }
    }
}

 */
