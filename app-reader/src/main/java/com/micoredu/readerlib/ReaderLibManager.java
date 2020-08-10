package com.micoredu.readerlib;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import com.liuzhenli.common.BaseApplication;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-11-10 14:57
 */
public class ReaderLibManager {
    public static Application getApplication() {
        return BaseApplication.getInstance();
    }

    public static Resources getAppResources() {
        return getApplication().getResources();
    }
}
