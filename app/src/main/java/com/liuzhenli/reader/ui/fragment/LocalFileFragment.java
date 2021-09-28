package com.liuzhenli.reader.ui.fragment;

import android.Manifest;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liuzhenli.common.base.BaseFragment;
import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.utils.AppSharedPreferenceHelper;
import com.liuzhenli.common.utils.Constant;
import com.liuzhenli.common.utils.DeviceUtil;
import com.liuzhenli.common.utils.FileUtils;
import com.liuzhenli.common.utils.PermissionUtil;
import com.liuzhenli.common.utils.ToastUtil;
import com.liuzhenli.reader.DaggerReadBookComponent;
import com.liuzhenli.common.utils.filepicker.entity.FileItem;
import com.liuzhenli.reader.ui.activity.ImportLocalBookActivity;
import com.liuzhenli.reader.ui.adapter.LocalFileAdapter;
import com.liuzhenli.reader.ui.contract.LocalFileContract;
import com.liuzhenli.reader.ui.presenter.LocalFilePresenter;
import com.liuzhenli.common.utils.filepicker.adapter.PathAdapter;
import com.microedu.reader.databinding.FragmentLocalfileBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * describe:导入书籍,文件夹
 *
 * @author Liuzhenli on 2019-12-15 10:06
 */
public class LocalFileFragment extends BaseFragment<LocalFilePresenter> implements LocalFileContract.View, PathAdapter.CallBack {

    private LocalFileAdapter mAdapter;

    private String sdPath = FileUtils.getSdCardPath();
    private String mPath = sdPath;

    /***
     * path indicator adapter
     */
    private PathAdapter pathAdapter = new PathAdapter();
    private FragmentLocalfileBinding inflate;
    private DocumentFile rootDoc;
    private List<DocumentFile> subDocs = new ArrayList<>();

    @Override
    public View bindContentView(LayoutInflater inflater, ViewGroup container, boolean attachParent) {
        inflate = FragmentLocalfileBinding.inflate(inflater, container, attachParent);
        return inflate.getRoot();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerReadBookComponent.builder().build().inject(this);
    }

    @Override
    public void attachView() {
        mPresenter.attachView(this);
    }


    @Override
    public void initData() {
        initRootDoc();
    }

    @Override
    public void configViews() {
        if (DeviceUtil.isLaterQ()) {
            inflate.viewPath.setVisibility(View.VISIBLE);
            inflate.rvPath.setVisibility(View.GONE);
        } else {
            inflate.rvPath.setVisibility(View.VISIBLE);
            inflate.viewPath.setVisibility(View.GONE);
        }
        inflate.rvPath.setLayoutManager(new LinearLayoutManager(mContext));
        inflate.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new LocalFileAdapter(mContext);
        inflate.recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(position -> {
            FileItem item = mAdapter.getItem(position);
            if (item.uri != null) {
                //文件夹
                if (FileUtils.isContentFile(item.uri)) {
                    if (TextUtils.equals(item.fileType, Constant.FileSuffix.DIRECTORY)) {
                        mPath = item.uri.toString();
                        subDocs.add(DocumentFile.fromTreeUri(mContext, item.uri));
                        updatePath();
                    } else {
                        boolean checked = item.isSelected;
                        mAdapter.getRealAllData().get(position).isSelected = !checked;
                    }
                } else {
                    if (TextUtils.equals(item.fileType, Constant.FileSuffix.DIRECTORY)) {
                        mPath = item.uri.getPath();
                        updatePath();
                    } else {
                        boolean checked = item.isSelected;
                        mAdapter.getRealAllData().get(position).isSelected = !checked;
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
        });
        inflate.rvPath.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        pathAdapter.setCallBack(this);
        initRootDoc();
        inflate.rvPath.setAdapter(pathAdapter);
        inflate.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!goBackDir()) {
                    ToastUtil.showToast("已经是根目录了");
                }
            }
        });
    }

    /**
     * go back to parent directory
     */
    private boolean goBackDir() {
        if (rootDoc == null) {
            if (!mPath.equals(sdPath)) {
                mPath = new File(mPath).getParent();
                updatePath();
                return true;
            }
            return false;
        }
        if (!subDocs.isEmpty()) {
            subDocs.remove(subDocs.size() - 1);
            updatePath();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void showDirectory(ArrayList<FileItem> data, File file) {
        dismissDialog();
        mAdapter.clear();
        mAdapter.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(Exception e) {
        dismissDialog();
    }

    @Override
    public void complete() {
        dismissDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPathClick(int position) {
        mPath = pathAdapter.getItem(position);
        updatePath();
    }

    public void refreshDocumentDirPath(DocumentFile rootDoc) {
        //当前目录
        StringBuilder path = new StringBuilder(rootDoc.getName() + File.separator);

        DocumentFile lastDoc = rootDoc;
        for (int i = 0; i < subDocs.size(); i++) {
            path.append(subDocs.get(i).getName()).append(File.separator);
            lastDoc = subDocs.get(i);
        }
        inflate.tvDirs.setText(path.toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            showDialog();
            mPresenter.getDirectory(lastDoc);
        }
    }

    public void refreshCurrentDirPath() {
        if (File.separator.equals(mPath)) {
            pathAdapter.updatePath(File.separator);
        } else {
            pathAdapter.updatePath(mPath);
        }
        showDialog();
        mPresenter.getDirectory(new File(mPath));
    }

    public List<FileItem> getSelectedBooks() {
        ArrayList<FileItem> bookFiles = new ArrayList<>();
        List<FileItem> data = mAdapter.getRealAllData();
        for (int i = 0; i < data.size(); i++) {
            FileItem item = data.get(i);
            if (!TextUtils.equals(item.fileType, Constant.FileAttr.DIRECTORY) && item.isSelected) {
                bookFiles.add(item);
            }
        }
        return bookFiles;
    }

    public void notifyDataChanged() {
        mAdapter.notifyDataSetChanged();
    }

    public void updatePath() {
        if (rootDoc != null) {
            refreshDocumentDirPath(rootDoc);
        } else {
            refreshCurrentDirPath();
        }
    }

    public void initRootDoc() {
        String path = AppSharedPreferenceHelper.getImportLocalBookPath();
        if (path == null) {
            ((ImportLocalBookActivity) activity).openMobileDir();
        } else if (FileUtils.isContentFile(path)) {
            Uri parse = Uri.parse(path);
            rootDoc = DocumentFile.fromTreeUri(mContext, parse);
            if (rootDoc == null) {
                ((ImportLocalBookActivity) activity).openMobileDir();
            } else {
                subDocs.clear();
                updatePath();
            }
        } else if (DeviceUtil.isLaterQ()) {
            ((ImportLocalBookActivity) activity).openMobileDir();
        } else {
            PermissionUtil.requestPermission(activity, new PermissionUtil.PermissionObserver() {
                @Override
                public void onGranted(List<String> permissions, boolean all) {
                    rootDoc = null;
                    subDocs.clear();
                    mPath = path;
                    updatePath();
                }
            }, Manifest.permission_group.STORAGE);
        }

    }
}
