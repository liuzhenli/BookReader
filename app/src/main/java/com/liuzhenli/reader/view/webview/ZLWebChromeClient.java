package com.liuzhenli.reader.view.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.liuzhenli.reader.ui.activity.WebViewActivity;
import com.liuzhenli.reader.utils.ToastUtil;


/**
 * @author liuzhenli
 */
public class ZLWebChromeClient extends WebChromeClient {

    private Context mContext;
    private String TAG;

    public ZLWebChromeClient(Context context) {
        mContext = context;
        TAG = mContext.getClass().getName();
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        if (mContext instanceof WebViewActivity) {
            WebViewActivity activity = (WebViewActivity) mContext;
            activity.mTvTitle.setText(title);
            activity.mTvTitle.setSelected(true);//开启走马灯(title);

        }
    }


    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {

        if (mContext instanceof WebViewActivity) {
            WebViewActivity activity = (WebViewActivity) mContext;
//            activity.mUploadMessageForAndroid5 = filePathCallback;
//            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
//            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
//            contentSelectionIntent.setType("mBitmap/*");
//            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
//            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
//            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.mUploadMessageForAndroid5 = filePathCallback;
                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("mBitmap/*");
                try {
                    Intent intent = fileChooserParams.createIntent();
                    intent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                    intent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                    activity.startActivityForResult(intent, 101);

                } catch (Exception e) {
                    ToastUtil.showCenter("文件资源打开失败!");
                }
            }
        }
        return true;

    }

    //扩展浏览器上传文件

    /**
     * 3.0++版本    For 4.1 to 4.4
     */
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        openFileChooserImpl(uploadMsg);
    }

    //3.0--版本
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        openFileChooserImpl(uploadMsg);
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        openFileChooserImpl(uploadMsg);
    }

    private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        if (mContext instanceof WebViewActivity) {
            WebViewActivity activity = (WebViewActivity) mContext;
            activity.mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("mBitmap/*");
            activity.startActivityForResult(Intent.createChooser(i, "File Chooser"), 1);
        }
    }


    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
    }
}
