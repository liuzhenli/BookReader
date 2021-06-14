package com.liuzhenli.write.ui.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.liuzhenli.common.constant.ARouterConstants;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.common.widget.DialogUtil;
import com.liuzhenli.write.R;
import com.liuzhenli.write.WriteBookComponent;
import com.liuzhenli.write.bean.WriteBook;
import com.liuzhenli.write.databinding.ActEditbookBinding;
import com.liuzhenli.write.helper.WriteBookHelper;
import com.liuzhenli.write.ui.WriteBaseActivity;
import com.liuzhenli.write.ui.contract.EditBookContract;
import com.liuzhenli.write.ui.presenter.EditBookPresenter;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import io.reactivex.functions.Consumer;

/**
 * Description:create new book or update book info.
 *
 * @author liuzhenli 2021/5/16
 * Email: 848808263@qq.com
 */
@Route(path = ARouterConstants.ACT_EDIT_BOOK_INFO)
public class EditBookInfoActivity extends WriteBaseActivity<EditBookPresenter> implements EditBookContract.View {
    public static final String WRITE_BOOK = "writeBook";
    private ActEditbookBinding mBinding;
    private WriteBook mBook;


    @Override
    protected View bindContentView() {
        mBinding = ActEditbookBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }

    @Override
    protected void setupActivityComponent(WriteBookComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    protected void initToolBar() {
        mTvRight.setText(getResources().getString(R.string.delete));
        if (mBook.getId() != null && mBook.getId() > 0) {
            mTvRight.setVisibility(View.VISIBLE);
            ClickUtils.click(mTvRight, o -> {
                DialogUtil.showMessagePositiveDialog(mContext,
                        getResources().getString(R.string.dialog_title),
                        getResources().getString(R.string.del_msg),
                        getResources().getString(R.string.dialog_cancel), null,
                        getResources().getString(R.string.dialog_confirm), new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                WriteBookHelper.deleteBook(mBook);
                                toast(getResources().getString(R.string.delete_success));
                                finish();
                            }
                        }, true);
            });
        }
    }

    @Override
    protected void initData() {
        mBook = (WriteBook) getIntent().getSerializableExtra(WRITE_BOOK);
        if (mBook == null || mBook.getId() == null || mBook.getId() < 1) {
            mBook = new WriteBook();
        }
    }

    @Override
    protected void configViews() {
        mBinding.etBookName.setText(mBook.getBookName());
        mBinding.etBookIntro.setText(mBook.getIntro());

        ClickUtils.click(mBinding.btnOk, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Editable mBookName = mBinding.etBookName.getText();
                Editable mBookIntro = mBinding.etBookIntro.getText();
                if (TextUtils.isEmpty(mBookName)) {
                    toast("请输入书名");
                    return;
                }
                mBook.setBookName(mBookName.toString().trim());
                mBook.setIntro(mBookIntro.toString());
                mPresenter.saveBooks(mBook);
            }
        });
    }

    @Override
    public void showSaveResult(long data) {
        toast("作品创建成功");
        finish();
    }

    @Override
    public void showError(Exception e) {

    }

    @Override
    public void complete() {

    }
}
