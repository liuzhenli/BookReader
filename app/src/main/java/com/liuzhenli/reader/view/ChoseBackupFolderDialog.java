package com.liuzhenli.reader.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liuzhenli.common.utils.DensityUtil;
import com.microedu.reader.R;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogView;


/**
 * Description:
 *
 * @author liuzhenli 2021/10/8
 * Email: 848808263@qq.com
 */
public class ChoseBackupFolderDialog extends QMUIDialog {

    public ChoseBackupFolderDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 带 CheckBox 的消息确认框 Builder
     */
    public static class ChoseBackupFolderDialogBuilder extends QMUIDialogBuilder<ChoseBackupFolderDialog.ChoseBackupFolderDialogBuilder> {

        private View.OnClickListener mOkClickListener;
        private View.OnClickListener mSelectClickListener;
        private View.OnClickListener mPreClickListener;
        private View btnOk;

        public ChoseBackupFolderDialogBuilder(Context context) {
            super(context);
        }

        @Nullable
        @Override
        protected View onCreateContent(@NonNull QMUIDialog dialog, @NonNull QMUIDialogView parent, @NonNull Context context) {
            mRootView.setMinWidth(DensityUtil.dip2px(context, 300));
            View root = LayoutInflater.from(context).inflate(R.layout.dialog_chose_backup_folder, parent, false);
            View btnSelectFolder = root.findViewById(R.id.rv_select_folder);
            btnOk = root.findViewById(R.id.rv_ok);
            View btnPre = root.findViewById(R.id.rv_pre_step);
            btnSelectFolder.setOnClickListener(v -> {
                if (mSelectClickListener != null) {
                    mSelectClickListener.onClick(v);
                }
            });
            btnOk.setOnClickListener(v -> {
                this.create().dismiss();
            });
            btnPre.setOnClickListener(v -> {
                btnOk.setEnabled(false);
                if (mPreClickListener != null) {
                    mPreClickListener.onClick(v);
                }
            });
            return root;
        }


        public ChoseBackupFolderDialogBuilder setSelectClickListener(View.OnClickListener listener) {
            this.mSelectClickListener = listener;
            return this;
        }

        public ChoseBackupFolderDialogBuilder setPreClickListener(View.OnClickListener listener) {
            this.mPreClickListener = listener;
            return this;
        }

        public ChoseBackupFolderDialogBuilder setOkClickListener(View.OnClickListener listener) {
            this.mOkClickListener = listener;
            return this;
        }

        public void setOkEnable(boolean enable) {
            btnOk.setEnabled(enable);
        }

    }


}
