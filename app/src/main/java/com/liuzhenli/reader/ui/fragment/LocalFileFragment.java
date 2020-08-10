package com.liuzhenli.reader.ui.fragment;

import android.os.Environment;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liuzhenli.reader.base.BaseFragment;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.ui.adapter.LocalFileAdapter;
import com.liuzhenli.reader.ui.contract.LocalFileContract;
import com.liuzhenli.reader.ui.presenter.LocalFilePresenter;
import com.liuzhenli.reader.utils.Constant;
import com.liuzhenli.reader.utils.ToastUtil;
import com.liuzhenli.reader.utils.filepicker.adapter.PathAdapter;
import com.liuzhenli.reader.view.recyclerview.EasyRecyclerView;
import com.microedu.reader.R;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import butterknife.BindView;

/**
 * describe:
 * <p>
 * 导入书籍,文件夹
 *
 * @author Liuzhenli on 2019-12-15 10:06
 */
public class LocalFileFragment extends BaseFragment<LocalFilePresenter> implements LocalFileContract.View, PathAdapter.CallBack {
    @BindView(R.id.rv_path)
    RecyclerView mFilePathView;
    @BindView(R.id.recyclerView)
    EasyRecyclerView mRecyclerView;

    private LocalFileAdapter mAdapter;
    /*** 初始文件路径***/
    private File rootDir;
    private PathAdapter pathAdapter = new PathAdapter();

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_localfile;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public void attachView() {

    }


    @Override
    public void initData() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            rootDir = Environment.getExternalStorageDirectory();
        } else {
            rootDir = Environment.getRootDirectory();
        }
        //读取本地书 // mnt/sdcard
        mPresenter.getDirectory(rootDir);
    }

    @Override
    public void configViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new LocalFileAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(position -> {
            HashMap<String, Object> item = mAdapter.getItem(position);
            rootDir = (File) item.get(Constant.FileAttr.FILE);
            if (rootDir != null && rootDir.isDirectory()) {
                refreshCurrentDirPath(rootDir.getPath());
                //当前文件的路径
            } else if (rootDir != null) {
                //已经获取到文件的路径  设置选中状态
                ToastUtil.showCenter(rootDir.getAbsolutePath());
                boolean checked = (boolean) item.get(Constant.FileAttr.CHECKED);
                mAdapter.getRealAllData().get(position).put(Constant.FileAttr.CHECKED, !checked);
            }
            mAdapter.notifyDataSetChanged();
        });
        mFilePathView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        pathAdapter.setCallBack(this);
        refreshCurrentDirPath(rootDir.getPath());
        mFilePathView.setAdapter(pathAdapter);
    }

    @Override
    public void showDirectory(ArrayList<HashMap<String, Object>> data, File file) {
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
        refreshCurrentDirPath(pathAdapter.getItem(position));
    }

    private void refreshCurrentDirPath(String currentPath) {
        if (File.separator.equals(currentPath)) {
            pathAdapter.updatePath(File.separator);
        } else {
            pathAdapter.updatePath(currentPath);
        }
        mPresenter.getDirectory(new File(currentPath));
    }

    public List<File> getSelectedBooks() {
        ArrayList<File> bookFiles = new ArrayList<>();
        List<HashMap<String, Object>> data = mAdapter.getRealAllData();
        for (int i = 0; i < data.size(); i++) {
            HashMap<String, Object> item = data.get(i);
            rootDir = (File) item.get(Constant.FileAttr.FILE);
            if (rootDir != null && rootDir.isFile() && (boolean) item.get(Constant.FileAttr.CHECKED)) {
                bookFiles.add(rootDir);
                Logger.e(rootDir.getAbsolutePath());
            }
        }
        return bookFiles;
    }

    public void notifyDataChanged() {
        mAdapter.notifyDataSetChanged();
    }
}
