package com.liuzhenli.reader.view.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.common.widget.dialog.FixHeightBottomSheetDialog;
import com.microedu.reader.R;

/**
 * Description:add wechat article dialog
 *
 * @author liuzhenli 2021/10/31
 * Email: 848808263@qq.com
 */
public class AddWxArticleDialog extends FixHeightBottomSheetDialog {
    public View.OnClickListener mOkClick;

    public AddWxArticleDialog(@NonNull Context context) {
        this(context, 0);
    }

    public AddWxArticleDialog(@NonNull Context context, int theme) {
        super(context, theme);
        init();
    }

    private void init() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_wx_article, null);
        View mViewOkButton = inflate.findViewById(R.id.view_copy_and_open);
        ClickUtils.click(mViewOkButton, o -> {
            if (mOkClick != null) {
                mOkClick.onClick(mViewOkButton);
            }

        });
        setContentView(inflate);
    }


    public AddWxArticleDialog setOkButtonClickListener(View.OnClickListener listener) {
        mOkClick = listener;
        return this;
    }
}
