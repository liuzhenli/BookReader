package com.liuzhenli.common.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.text.TextUtils;

import com.liuzhenli.common.R;
import com.liuzhenli.common.utils.PermissionUtil;
import com.liuzhenli.common.utils.ToastUtil;
import com.qmuiteam.qmui.skin.QMUISkinManager;
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
    public static void showNoPermissionDialog(Context cxt, String message) {
        new QMUIDialog.MessageDialogBuilder(cxt).setTitle(cxt.getResources().getString(R.string.dialog_title))
                .setCanceledOnTouchOutside(false)
                .setMessage(message)
                .addAction(new QMUIDialogAction(cxt.getResources().getString(R.string.ok), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        PermissionUtil.jumpPermissionPage(cxt);
                    }
                }))
                .create()
                .show();
    }

    public static void showOneButtonDialog(Context cxt, String title, String msg, QMUIDialogAction.ActionListener action) {
        new QMUIDialog.MessageDialogBuilder(cxt).setTitle(title)
                .setCanceledOnTouchOutside(false)
                .setMessage(msg)
                .addAction(new QMUIDialogAction(cxt.getResources().getString(R.string.ok), action))
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
     * @param cxt                    the cxt
     * @param title                  the dialog title
     * @param msg                    the content
     * @param noText                 cancel button text
     * @param nListener              cancel button click listener
     * @param yesText                confirm button text
     * @param yesListener            confirm button click listener
     * @param canceledOnTouchOutside true if the button is not cancelable
     */
    public static void showMessagePositiveDialog(Context cxt, String title, String msg, String noText,
                                                 QMUIDialogAction.ActionListener nListener, String yesText,
                                                 QMUIDialogAction.ActionListener yesListener, boolean canceledOnTouchOutside) {
        new QMUIDialog.MessageDialogBuilder(cxt)
                .setTitle(title)
                .setMessage(msg)
                .setCanceledOnTouchOutside(canceledOnTouchOutside)
                .addAction(0, noText, QMUIDialogAction.ACTION_PROP_NEUTRAL, (dialog, index) -> {
                    if (nListener != null) {
                        nListener.onClick(dialog, index);
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

    public static void showEditTextDialog(Context context, String title, String placeHolder,
                                          String toast, DialogActionListener actionListener) {
        showEditTextDialog(context, title, placeHolder, null, toast, actionListener);
    }

    public static void showEditTextDialog(Context context, String title, String placeHolder,
                                          String defaultText, String toast, DialogActionListener listener) {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(context);
        QMUIDialog qmuiDialog = builder.setTitle(title)
                .setSkinManager(QMUISkinManager.defaultInstance(context))
                .setPlaceholder(placeHolder)
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .setDefaultText(defaultText)
                .addAction(context.getResources().getString(R.string.cancel),
                        (dialog, index) -> dialog.dismiss())
                .addAction(context.getResources().getString(R.string.ok),
                        (dialog, index) -> {
                            CharSequence text = builder.getEditText().getText();
                            if (text != null && text.length() > 0) {
                                listener.onClick(text.toString());
                                dialog.dismiss();
                            } else if (!TextUtils.isEmpty(toast)) {
                                ToastUtil.showToast(toast);
                            } else {
                                dialog.dismiss();
                            }
                        }).create(mCurrentDialogStyle);
        qmuiDialog.show();
    }

    public interface DialogActionListener {
        void onClick(String s);
    }
}