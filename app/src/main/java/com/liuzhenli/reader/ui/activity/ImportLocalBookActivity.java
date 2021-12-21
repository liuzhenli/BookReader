package com.liuzhenli.reader.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;

import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.utils.AppSharedPreferenceHelper;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.common.base.BaseTabActivity;
import com.liuzhenli.common.utils.DeviceUtil;
import com.liuzhenli.common.utils.IntentUtils;
import com.liuzhenli.reader.DaggerReadBookComponent;
import com.liuzhenli.common.utils.filepicker.entity.FileItem;
import com.liuzhenli.reader.ui.contract.ImportLocalBookContract;
import com.liuzhenli.reader.ui.fragment.LocalFileFragment;
import com.liuzhenli.reader.ui.fragment.LocalTxtFragment;
import com.liuzhenli.reader.ui.presenter.ImportLocalBookPresenter;
import com.liuzhenli.common.utils.FileUtils;
import com.liuzhenli.common.utils.PermissionUtil;
import com.liuzhenli.common.utils.ToastUtil;
import com.liuzhenli.common.widget.DialogUtil;
import com.microedu.reader.R;
import com.microedu.reader.databinding.ActImportlocalbookBinding;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * describe:本地书导入页面
 * 智能导入,手机目录选择导入
 *
 * @author Liuzhenli on 2019-12-14 18:19
 */
public class ImportLocalBookActivity extends BaseTabActivity<ImportLocalBookPresenter, ActImportlocalbookBinding> implements ImportLocalBookContract.View {

    private static final int INTENT_CODE_IMPORT_BOOK_PATH = 110;
    private boolean mSelectAll;
    private QMUIDialog mOpenDirDialog;

    public static void start(Context context) {
        context.startActivity(new Intent(context, ImportLocalBookActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionUtil.requestPermission(this, new PermissionUtil.PermissionObserver() {
            @Override
            public void onGranted(List<String> permissions, boolean all) {
                if (!all) {
                    DialogUtil.showNoPermissionDialog(mContext, getResources().getString(R.string.please_grant_storage_permission));
                }
            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    protected ActImportlocalbookBinding inflateView(LayoutInflater inflater) {
        return ActImportlocalbookBinding.inflate(inflater);
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
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerReadBookComponent.builder().build().inject(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initToolBar() {
        mTvTitle.setText("导入本机书籍");
        mIvRight.setImageResource(R.drawable.ic_directory);
        if (DeviceUtil.isLaterQ()) {
            mIvRight.setVisibility(View.VISIBLE);
            ClickUtils.click(mIvRight, o -> openMobileDir());
        }
    }

    @Override
    protected void initData() {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void configViews() {
        super.configViews();
        //add to bookShelf
        ClickUtils.click(binding.mViewAddBookShelf, o -> {

            if (mFragmentList.get(getCurrentPagePosition()) instanceof LocalFileFragment) {
                LocalFileFragment fragment = (LocalFileFragment) (mFragmentList.get(getCurrentPagePosition()));
                List<FileItem> selectedBooks = fragment.getSelectedBooks();
                mPresenter.addToBookShelf(selectedBooks);
                fragment.notifyDataChanged();
            } else if (mFragmentList.get(getCurrentPagePosition()) instanceof LocalTxtFragment) {
                LocalTxtFragment fragment = (LocalTxtFragment) mFragmentList.get(getCurrentPagePosition());
                List<FileItem> selectedBooks = fragment.getSelectedBooks();
                mPresenter.addToBookShelf(selectedBooks);
                fragment.notifyDataChanged();
            }
        });
        ClickUtils.click(binding.mViewDeleteFile, o -> {
            if (mFragmentList.get(getCurrentPagePosition()) instanceof LocalFileFragment) {
                LocalFileFragment fragment = (LocalFileFragment) (mFragmentList.get(getCurrentPagePosition()));
                List<FileItem> selectedBooks = fragment.getSelectedBooks();
                for (FileItem selectedBook : selectedBooks) {
                    if (FileUtils.isContentFile(selectedBook.path)) {
                        DocumentFile documentFile = DocumentFile.fromSingleUri(mContext, Uri.parse(selectedBook.path));
                        if (documentFile != null) {
                            documentFile.delete();
                        }
                    } else {
                        FileUtils.deleteFile(new File(selectedBook.getPath()));
                    }
                }
                fragment.notifyDataChanged();
            } else if (mFragmentList.get(getCurrentPagePosition()) instanceof LocalTxtFragment) {

                DialogUtil.showMessagePositiveDialog(mContext, "提示", "是否从手机中删除该文件?", "取消", null, "删除", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        LocalTxtFragment fragment = (LocalTxtFragment) mFragmentList.get(getCurrentPagePosition());
                        List<FileItem> selectedBooks = fragment.getSelectedBooks();
                        for (FileItem selectedBook : selectedBooks) {
                            try {
                                if (FileUtils.isContentFile(selectedBook.path)) {
                                    DocumentFile documentFile = DocumentFile.fromSingleUri(mContext, Uri.parse(selectedBook.path));
                                    if (documentFile != null) {
                                        documentFile.delete();
                                    }
                                } else {
                                    FileUtils.deleteFile(new File(selectedBook.getPath()));
                                }
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


        ClickUtils.click(binding.mViewSelectAll, o -> {
            if (mFragmentList.get(getCurrentPagePosition()) instanceof LocalTxtFragment) {
                mSelectAll = !mSelectAll;
                LocalTxtFragment fragment = (LocalTxtFragment) mFragmentList.get(getCurrentPagePosition());
                List<FileItem> selectedBooks = fragment.getAllBooks();
                if (mSelectAll) {
                    binding.mViewSelectAll.setText("取消全选");
                } else {
                    binding.mViewSelectAll.setText("全选");
                }

                for (int i = 0; i < selectedBooks.size(); i++) {
                    selectedBooks.get(i).isSelected = mSelectAll;
                }
                fragment.notifyDataChanged();
            }
        });

        String path = AppSharedPreferenceHelper.getImportLocalBookPath();

        mOpenDirDialog = new QMUIDialog.MessageDialogBuilder(mContext)
                .setTitle("提示")
                .setMessage("您将选择一个文件夹,异书房将对该文件夹下的文件拥有读写权限")
                .setCanceledOnTouchOutside(true)
                .addAction(0, "取消", (dialog, index) -> {
                    dialog.dismiss();
                })
                .addAction(0, "确定", (dialog, index) -> {
                            dialog.dismiss();
                            IntentUtils.openMobileDir(ImportLocalBookActivity.this, INTENT_CODE_IMPORT_BOOK_PATH);
                        }
                )
                .create();

        if (TextUtils.isEmpty(path) && DeviceUtil.isLaterQ()) {
            openMobileDir();
        }
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        if (position == 0) {
            binding.mViewSelectAll.setVisibility(View.VISIBLE);
        } else {
            binding.mViewSelectAll.setVisibility(View.GONE);
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void openMobileDir() {
        mOpenDirDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_CODE_IMPORT_BOOK_PATH) {
            if (data == null || data.getData() == null) {
                return;
            }
            AppSharedPreferenceHelper.setImportLocalBookPath(data.getDataString());
            mContext.getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            for (Fragment fragment : mFragmentList) {
                if (fragment instanceof LocalTxtFragment) {
                    ((LocalTxtFragment) fragment).refreshData();
                } else if (fragment instanceof LocalFileFragment) {
                    ((LocalFileFragment) fragment).initRootDoc();
                }
            }

        }
    }
}

