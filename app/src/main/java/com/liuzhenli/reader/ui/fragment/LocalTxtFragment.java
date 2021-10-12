package com.liuzhenli.reader.ui.fragment;

import android.Manifest;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;

import com.liuzhenli.common.base.BaseRVFragment;
import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.utils.AppSharedPreferenceHelper;
import com.liuzhenli.common.utils.DeviceUtil;
import com.liuzhenli.common.utils.FileUtils;
import com.liuzhenli.common.utils.PermissionUtil;
import com.liuzhenli.common.widget.DialogUtil;
import com.liuzhenli.reader.DaggerReadBookComponent;
import com.liuzhenli.common.utils.filepicker.entity.FileItem;
import com.liuzhenli.reader.ui.adapter.LocalTxtAdapter;
import com.liuzhenli.reader.ui.contract.LocalTxtContract;
import com.liuzhenli.reader.ui.presenter.LocalTxtPresenter;
import com.microedu.reader.R;
import com.microedu.reader.databinding.FragmentLocaltxtBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * describe: 导入书籍,文件
 *
 * @author Liuzhenli on 2019-12-15 10:06
 */
public class LocalTxtFragment extends BaseRVFragment<LocalTxtPresenter, FileItem> implements LocalTxtContract.View {

    private FragmentLocaltxtBinding inflate;

    @Override
    public View bindContentView(LayoutInflater inflater, ViewGroup container, boolean attachParent) {
        inflate = FragmentLocaltxtBinding.inflate(inflater, container, attachParent);
        return inflate.getRoot();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerReadBookComponent.builder().build().inject(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public void configViews() {
        initAdapter(LocalTxtAdapter.class, false, false, true);
        refreshData();
    }


    @Override
    public void showError(Exception e) {

    }

    @Override
    public void complete() {

    }

    @Override
    public void onItemClick(int position) {
        FileItem data = mAdapter.getItem(position);
        data.isSelected = !data.isSelected;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLocalTxt(List<FileItem> fileList) {
        hideDialog();
        if (mAdapter.getCount() > 0) {
            mAdapter.clear();
        }
        mAdapter.addAll(fileList);
        mAdapter.notifyDataSetChanged();
    }

    public List<FileItem> getSelectedBooks() {
        ArrayList<FileItem> bookFiles = new ArrayList<>();
        List<FileItem> data = mAdapter.getRealAllData();
        for (int i = 0; i < data.size(); i++) {
            FileItem item = data.get(i);
            if (item.isSelected) {
                bookFiles.add(item);
            }
        }
        return bookFiles;
    }

    public void notifyDataChanged() {
        mAdapter.notifyDataSetChanged();
    }

    public List<FileItem> getAllBooks() {
        return mAdapter.getRealAllData();
    }

    public synchronized void refreshData() {
        showDialog();
        if (mPresenter != null) {
            if (DeviceUtil.isLaterQ() && FileUtils.isContentFile(AppSharedPreferenceHelper.getImportLocalBookPath())) {
                mPresenter.getLocalTxt(this.getApplicationContext(), AppSharedPreferenceHelper.getImportLocalBookPath());
            } else {
                PermissionUtil.requestPermission(mContext, new PermissionUtil.PermissionObserver() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        mPresenter.getLocalTxt((FragmentActivity) mContext);
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        DialogUtil.showNoPermissionDialog(mContext, getResources().getString(R.string.please_grant_storage_permission));
                    }
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

}
