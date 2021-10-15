package com.liuzhenli.reader.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liuzhenli.common.utils.AppSharedPreferenceHelper;
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
        private View btnPre;
        private TextView tvDes;
        private TextView tvOKDes;
        private View btnSelectFolder;

        public ChoseBackupFolderDialogBuilder(Context context) {
            super(context);
        }

        @Nullable
        @Override
        protected View onCreateContent(@NonNull QMUIDialog dialog, @NonNull QMUIDialogView parent, @NonNull Context context) {
            mRootView.setMinWidth(DensityUtil.dip2px(context, 290));
            View root = LayoutInflater.from(context).inflate(R.layout.dialog_chose_backup_folder, parent, false);
            btnSelectFolder = root.findViewById(R.id.rv_select_folder);
            btnOk = root.findViewById(R.id.rv_ok);
            btnPre = root.findViewById(R.id.rv_pre_step);
            tvDes = root.findViewById(R.id.tv_description);
            tvOKDes = root.findViewById(R.id.tv_select_ok_des);
            btnSelectFolder.setOnClickListener(v -> {
                if (mSelectClickListener != null) {
                    mSelectClickListener.onClick(v);
                }
            });
            btnOk.setOnClickListener(v -> {
                if (mOkClickListener != null) {
                    mOkClickListener.onClick(v);
                }
            });
            btnPre.setOnClickListener(v -> {
                setFolderEnable(false);
            });
            setFolderEnable(false);
            return wrapWithScroll(root);
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

        /**
         * user selected folder
         *
         * @param enable true if selected
         */
        public void setFolderEnable(boolean enable) {
            //用户已经选择了文件夹
            if (enable) {
                btnOk.setVisibility(View.VISIBLE);
                btnPre.setVisibility(View.VISIBLE);
                btnSelectFolder.setVisibility(View.GONE);
                tvOKDes.setVisibility(View.VISIBLE);
                String backupPath = AppSharedPreferenceHelper.getBackupPath(null);
                tvDes.setText(String.format(getBaseContext().getString(R.string.tv_select_backup_folder_name), backupPath));
                tvOKDes.setText(String.format(getBaseContext().getString(R.string.tv_select_backup_folder_name_confirm), backupPath + "/ShuFang/"));
                //未选择文件夹
            } else {
                btnOk.setVisibility(View.GONE);
                btnPre.setVisibility(View.GONE);
                btnSelectFolder.setVisibility(View.VISIBLE);
                tvOKDes.setVisibility(View.GONE);

                tvDes.setText(R.string.tv_select_backup_folder_intro);
            }
        }

    }


}
