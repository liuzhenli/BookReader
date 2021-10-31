package com.liuzhenli.reader.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.common.widget.dialog.FixHeightBottomSheetDialog;
import com.micoredu.reader.ui.presenter.BookSourcePresenter;
import com.microedu.reader.R;

/**
 * Description:
 *
 * @author liuzhenli 2021/10/31
 * Email: 848808263@qq.com
 */
public class ImportBookSourceDialog extends FixHeightBottomSheetDialog {
    public ImportBookSourceDialog(@NonNull Context context) {
        this(context, 0);
    }

    public ImportBookSourceDialog(@NonNull Context context, int theme) {
        super(context, theme);
        init();
    }

    private void init() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_import_book_source, null);
        View mViewOkButton = inflate.findViewById(R.id.view_import);
        View mViewCamera = inflate.findViewById(R.id.view_input_camera);
        EditText mEtSource = inflate.findViewById(R.id.et_input_book_source_url);
        EditText mViewInputLocal = inflate.findViewById(R.id.view_input_local);
        View mGetWxSource = inflate.findViewById(R.id.iv_get_source_from_wx);
        ClickUtils.click(mViewOkButton, o -> {
            if (mOkClick != null) {
                mOkClick.onClick(mViewOkButton);
            }

        });
        mEtSource.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mUserInput = s == null ? null : s.toString();
            }
        });
        ClickUtils.click(mViewCamera, o -> {
            if (mCameraClick != null) {
                mCameraClick.onClick(mViewCamera);
            }
        });
        ClickUtils.click(mViewInputLocal, o -> {
            if (mDirClick != null) {
                mDirClick.onClick(mViewInputLocal);
            }
        });

        ClickUtils.click(mGetWxSource, o -> {
            if (mWxClick != null) {
                mWxClick.onClick(mGetWxSource);
            }
        });
        setContentView(inflate);
    }

    public View.OnClickListener mCameraClick;
    public View.OnClickListener mDirClick;
    public View.OnClickListener mOkClick;
    public View.OnClickListener mWxClick;
    public String mUserInput;


    public ImportBookSourceDialog setCameraClickListener(View.OnClickListener listener) {
        mCameraClick = listener;
        return this;
    }

    public ImportBookSourceDialog setOkButtonClickListener(View.OnClickListener listener) {
        mOkClick = listener;
        return this;
    }

    public ImportBookSourceDialog setImportWxSource(View.OnClickListener listener) {
        mWxClick = listener;
        return this;
    }

    public ImportBookSourceDialog setDirClick(View.OnClickListener listener) {
        mDirClick = listener;
        return this;
    }

    public String getUserInput() {
        return mUserInput;
    }
}
