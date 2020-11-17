package com.liuzhenli.reader.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.liuzhenli.common.observer.MyObserver;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.reader.base.BaseTabActivity;
import com.liuzhenli.reader.bean.LocalFileBean;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.ui.contract.ImportLocalBookContract;
import com.liuzhenli.reader.ui.fragment.LocalFileFragment;
import com.liuzhenli.reader.ui.fragment.LocalTxtFragment;
import com.liuzhenli.reader.ui.presenter.ImportLocalBookPresenter;
import com.liuzhenli.common.utils.FileUtils;
import com.liuzhenli.reader.utils.PermissionUtil;
import com.liuzhenli.reader.utils.ToastUtil;
import com.liuzhenli.reader.view.loading.DialogUtil;
import com.microedu.reader.R;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * describe:本地书导入页面
 * 智能导入,手机目录选择导入
 *
 * @author Liuzhenli on 2019-12-14 18:19
 */
public class ImportLocalBookActivity extends BaseTabActivity<ImportLocalBookPresenter> implements ImportLocalBookContract.View {
    @BindView(R.id.view_add_book_shelf)
    TextView mViewAddBookShelf;
    @BindView(R.id.view_delete_file)
    TextView mViewDeleteFile;
    @BindView(R.id.view_select_all)
    TextView mViewSelectAll;
    private boolean mSelectAll;

    public static void start(Context context) {
        context.startActivity(new Intent(context, ImportLocalBookActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionUtil.requestPermission(this, new MyObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                if (!aBoolean) {
                    DialogUtil.showNoPermissionDialog(mContext, getResources().getString(R.string.please_grant_storage_permission));
                }
            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    protected List<Fragment> createTabFragments() {
        LocalTxtFragment localTxtFragment = new LocalTxtFragment();
        LocalFileFragment localFileFragment = new LocalFileFragment();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(localTxtFragment);
        fragments.add(localFileFragment);
        return fragments;
    }

    @Override
    protected List<String> createTabTitles() {
        ArrayList<String> titles = new ArrayList<>();
        titles.add("智能导入");
        titles.add("手机目录");
        return titles;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_importlocalbook;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    protected void initToolBar() {
        mTvTitle.setText("导入本地书籍");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void configViews() {
        super.configViews();
        ClickUtils.click(mViewAddBookShelf, o -> {

            if (mFragmentList.get(getCurrentPagePosition()) instanceof LocalFileFragment) {
                LocalFileFragment fragment = (LocalFileFragment) (mFragmentList.get(getCurrentPagePosition()));
                List<File> selectedBooks = fragment.getSelectedBooks();
                mPresenter.addToBookShelf(selectedBooks);
                fragment.notifyDataChanged();
            } else if (mFragmentList.get(getCurrentPagePosition()) instanceof LocalTxtFragment) {
                LocalTxtFragment fragment = (LocalTxtFragment) mFragmentList.get(getCurrentPagePosition());
                List<File> selectedBooks = fragment.getSelectedBooks();
                mPresenter.addToBookShelf(selectedBooks);
                fragment.notifyDataChanged();
            }
        });
        ClickUtils.click(mViewDeleteFile, o -> {
            if (mFragmentList.get(getCurrentPagePosition()) instanceof LocalFileFragment) {
                LocalFileFragment fragment = (LocalFileFragment) (mFragmentList.get(getCurrentPagePosition()));
                List<File> selectedBooks = fragment.getSelectedBooks();
                for (File selectedBook : selectedBooks) {
                    FileUtils.deleteFile(selectedBook);
                }
                fragment.notifyDataChanged();
            } else if (mFragmentList.get(getCurrentPagePosition()) instanceof LocalTxtFragment) {

                DialogUtil.showMessagePositiveDialog(mContext, "提示", "是否从手机中删除该文件?", "取消", null, "删除", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        LocalTxtFragment fragment = (LocalTxtFragment) mFragmentList.get(getCurrentPagePosition());
                        List<File> selectedBooks = fragment.getSelectedBooks();
                        for (File selectedBook : selectedBooks) {
                            try {
                                FileUtils.deleteFile(selectedBook);
                                fragment.notifyDataChanged();
                                ToastUtil.showToast("已经删除");
                                fragment.refreshData();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, true);

            }
        });


        ClickUtils.click(mViewSelectAll, o -> {
            if (mFragmentList.get(getCurrentPagePosition()) instanceof LocalTxtFragment) {
                mSelectAll = !mSelectAll;
                LocalTxtFragment fragment = (LocalTxtFragment) mFragmentList.get(getCurrentPagePosition());
                List<LocalFileBean> selectedBooks = fragment.getAllBooks();
                if (mSelectAll) {
                    mViewSelectAll.setText("取消全选");
                } else {
                    mViewSelectAll.setText("全选");
                }

                for (int i = 0; i < selectedBooks.size(); i++) {
                    selectedBooks.get(i).isSelected = mSelectAll;
                }
                fragment.notifyDataChanged();
            }
        });
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        if (position == 0) {
            mViewSelectAll.setVisibility(View.VISIBLE);
        } else {
            mViewSelectAll.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void complete() {

    }

    @Override
    public void showAddResult() {

    }
}

