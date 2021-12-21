package com.liuzhenli.write.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.liuzhenli.common.base.BaseBean;
import com.liuzhenli.common.constant.ARouterConstants;
import com.liuzhenli.common.utils.L;
import com.liuzhenli.common.utils.ScreenUtils;
import com.liuzhenli.write.WriteBookComponent;
import com.liuzhenli.write.bean.WriteChapter;
import com.liuzhenli.write.data.WriteChapterDao;
import com.liuzhenli.write.databinding.ActWirtebookBinding;
import com.liuzhenli.write.helper.WriteDbHelper;
import com.liuzhenli.write.ui.WriteBaseActivity;
import com.liuzhenli.write.ui.contract.WriteBookContract;
import com.liuzhenli.write.ui.presenter.WriteBookPresenter;
import com.liuzhenli.write.util.WriteChapterManager;
import com.liuzhenli.write.util.WriteConstants;

/**
 * Description:
 *
 * @author liuzhenli 2021/1/20
 * Email: 848808263@qq.com
 */
@Route(path = ARouterConstants.ACT_WRITE_BOOK)
public class WriteBookActivity extends WriteBaseActivity<WriteBookPresenter,ActWirtebookBinding> implements WriteBookContract.View, ViewTreeObserver.OnGlobalLayoutListener {

    public static final String DATA = "chapterData";
    public static final String LOCAL_BOOK_ID = "local_book_id";
    public static final String MODE = "isCreate";

    private WriteChapter mChapter;
    private long mLocalBookId;
    private String mChapterPath;
    private WriteChapterDao mWriteChapterDao;
    private WriteChapterManager mWriteChapterManager;
    private String[] mHelpWords = {"@", "&", "|", "%", "/", ":", "[", "]", "(", ")", "{", "}", "<", ">", "\\", "$", "#", "!", "."};
    /*** true create false edit*/
    private boolean mIsCreate, mIsSoftKeyBoardShowing;

    public static void start(Context context) {
        Intent intent = new Intent(context, WriteBookActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void setupActivityComponent(WriteBookComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    protected ActWirtebookBinding inflateView(LayoutInflater inflater) {
        return ActWirtebookBinding.inflate(inflater);
    }

    @Override
    protected void initToolBar() {
         }

    @Override
    protected void initData() {
        mWriteChapterManager = new WriteChapterManager();
        mWriteChapterDao = WriteDbHelper.getInstance().getAppDatabase().getWriteChapterDao();
        mChapter = (WriteChapter) getIntent().getSerializableExtra(DATA);
        mLocalBookId = getIntent().getLongExtra(LOCAL_BOOK_ID, -1);
        mIsCreate = getIntent().getBooleanExtra(MODE, true);
        if (mChapter == null || mChapter.getId() == null || mChapter.getId() < 1) {
            createNewChapter();
        }
        mChapterPath = String.format("%s%s/%s", WriteConstants.PATH_WRITE_BOOK, mChapter.getLocalBookId(), mChapter.getId());
    }

    @Override
    protected void configViews() {
        mPresenter.getLocalData();
        if (!mIsCreate) {
            //fill title
            binding.etChapterTitle.setText(mChapter.getTitle());
            //file content
            binding.editView.setText(mWriteChapterManager.loadDraft(mChapterPath, false).mSpannableText);
        }

        binding.mRvPunctuation.setData(key -> {
            binding.editView.insertSymbol(key);
        }, mHelpWords);

        binding.editView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTvTitle.setText(String.format("%1$s字", binding.editView.getWordCount()));
            }
        });

        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void showError(Exception e) {

    }

    @Override
    public void complete() {

    }

    @Override
    public void showLocalData(BaseBean data) {

    }

    @Override
    public void showSaveChapterInfoResult(boolean data) {

    }

    @Override
    public void showAutoSaveDraftResult(boolean data) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        String title = binding.etChapterTitle.getText().toString();
        String content = binding.editView.getContent(title, System.currentTimeMillis(), null);
        mChapter.setTitle(title);
        //if content is not empty save
        if (!TextUtils.isEmpty(binding.editView.getText())) {
            mPresenter.saveChapterInfo(mChapter);
            mPresenter.autoSaveDraft(mChapter, content, mChapterPath);
            mWriteChapterManager.saveDraft(content, mChapterPath, null, null, false);
        } else {
            deleteChapter();
        }
    }


    private void createNewChapter() {
        mChapter.setCreateTime(System.currentTimeMillis());
        mChapter.setLocalBookId(mLocalBookId);
        mWriteChapterDao.insertOrReplace(mChapter);
        mChapter = mWriteChapterDao.getChapterByCreateTime(mChapter.getCreateTime());
        L.e("create chapter id = " + mChapter.getId() + "\nbookId" + mChapter.getLocalBookId() + "\n");
    }

    private void deleteChapter() {
        mWriteChapterDao.delete(mChapter);
    }

    @Override
    public void onGlobalLayout() {
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int screenHeight = ScreenUtils.getScreenHeight(this);
        int keyBordHeight = screenHeight - rect.bottom;
        if (keyBordHeight > screenHeight / 5) {
            mIsSoftKeyBoardShowing = true;
            showKeyboardTopPopupWindow();
        } else {
            mIsSoftKeyBoardShowing = false;
            closePopupWindow();
        }
    }

    private void showKeyboardTopPopupWindow() {
        if (isFinishing()) {
            return;
        }
        if (binding.mRvPunctuation.getVisibility() == View.VISIBLE) {
            return;
        }
        if (!this.isFinishing()) {
            binding.mRvPunctuation.setVisibility(View.VISIBLE);
            binding.etChapterTitle.setVisibility(View.GONE);
        }
    }

    private void closePopupWindow() {
        if (binding.mRvPunctuation.getVisibility() == View.VISIBLE) {
            binding.mRvPunctuation.setVisibility(View.GONE);
            binding.etChapterTitle.setVisibility(View.VISIBLE);
        }
    }
}
