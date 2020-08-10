package com.liuzhenli.reader.ui.fragment;

import android.Manifest;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.liuzhenli.common.constant.AppConstant;
import com.liuzhenli.common.observer.MyObserver;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.reader.base.BaseFragment;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.observer.SampleProgressObserver;
import com.liuzhenli.reader.ui.activity.BookCatalogActivity;
import com.liuzhenli.reader.ui.activity.BookSourceActivity;
import com.liuzhenli.reader.ui.activity.ImportLocalBookActivity;
import com.liuzhenli.reader.ui.activity.WebViewActivity;
import com.liuzhenli.reader.utils.Constant;
import com.liuzhenli.reader.utils.PermissionUtil;
import com.liuzhenli.reader.utils.filepicker.picker.FilePicker;
import com.microedu.reader.R;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-11-09 22:28
 */
public class MeFragment extends BaseFragment {
    private final int IMPORT_SOURCE = 102;
    private final int REQUEST_QR = 202;

    @BindView(R.id.ll_import_local_books)
    LinearLayout mViewImportLocalBooks;
    @BindView(R.id.tv_feedback)
    View mViewFeedBack;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void attachView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void configViews() {
        ClickUtils.click(mViewImportLocalBooks, o -> {
            PermissionUtil.requestPermission(getActivity(), new MyObserver() {
                @Override
                public void onNext(Object o) {
                    ImportLocalBookActivity.start(mContext);
                }
            }, Manifest.permission.READ_EXTERNAL_STORAGE);
        });

        ClickUtils.click(mViewFeedBack, o -> {
            WebViewActivity.start(mContext, Constant.FEEDBACK);
        });
    }


    private void openFilePicker() {
        FilePicker filePicker = new FilePicker(getActivity(), FilePicker.FILE);
        filePicker.setBackgroundColor(getResources().getColor(R.color.background));
        filePicker.setTopBackgroundColor(getResources().getColor(R.color.background));
        filePicker.setAllowExtensions(getResources().getStringArray(R.array.text_suffix));
        filePicker.setOnFilePickListener(s -> {
            Logger.e("1111111" + s);
            //mPresenter.importBookSourceLocal(s)  ;
        });
        filePicker.show();
        filePicker.getSubmitButton().setText(R.string.sys_file_picker);
        filePicker.getSubmitButton().setOnClickListener(view -> {
            filePicker.dismiss();
            selectFileSys();
        });
    }

    private void selectFileSys() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //设置类型
        intent.setType("text/*");
        startActivityForResult(intent, IMPORT_SOURCE);
    }

}

