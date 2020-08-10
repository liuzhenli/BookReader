package com.liuzhenli.reader.view.loading;

import android.content.Context;
import android.content.DialogInterface;

import com.liuzhenli.reader.utils.PermissionUtil;
import com.microedu.reader.R;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

/**
 * Description:
 *
 * @author liuzhenli 2020/8/1
 * Email: 848808263@qq.com
 */
public class DialogUtil {

    private static int mCurrentDialogStyle = R.style.QMUI_Dialog;

    /**
     * 显示没有权限dialog 并跳转到权限设置界面
     */
    public static void showNoPermissionDialog(Context context, String message) {
        new QMUIDialog.MessageDialogBuilder(context).setTitle("提示")
                .setCanceledOnTouchOutside(false)
                .setMessage(message)
                .addAction(new QMUIDialogAction(context.getResources().getString(R.string.ok), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        PermissionUtil.jumpPermissionPage(context);
                    }
                }))
                .create()
                .show();
    }


    /**
     * single choice dialog with items
     *
     * @param context      context
     * @param items        items
     * @param listener     listener
     * @param checkedIndex default checked index
     */
    public static void sowSingleChoiceDialog(Context context, String[] items, DialogInterface.OnClickListener listener, int checkedIndex) {
        new QMUIDialog.CheckableDialogBuilder(context)
                .setCheckedIndex(checkedIndex)
                .addItems(items, (dialog, position) -> {
                    dialog.dismiss();
                    if (listener != null) {
                        listener.onClick(dialog, position);
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    /**
     * dialog with two buttons
     *
     * @param context                the context
     * @param title                  the dialog title
     * @param message                the content
     * @param noText                 cancel button text
     * @param noListener             cancel button click listener
     * @param yesText                confirm button text
     * @param yesListener            confirm button click listener
     * @param canceledOnTouchOutside true if the button is not cancelable
     */
    public static void showMessagePositiveDialog(Context context, String title, String message, String noText,
                                                 QMUIDialogAction.ActionListener noListener, String yesText, QMUIDialogAction.ActionListener yesListener, boolean canceledOnTouchOutside) {
        new QMUIDialog.MessageDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setCanceledOnTouchOutside(canceledOnTouchOutside)
                .addAction(0, noText, QMUIDialogAction.ACTION_PROP_NEUTRAL, (dialog, index) -> {
                    if (noListener != null) {
                        noListener.onClick(dialog, index);
                    }
                    dialog.dismiss();
                })
                .addAction(0, yesText, QMUIDialogAction.ACTION_PROP_POSITIVE, (dialog, index) -> {
                    if (yesListener != null) {
                        yesListener.onClick(dialog, index);
                    }
                    dialog.dismiss();
                })
                .create(mCurrentDialogStyle).show();
    }
}
