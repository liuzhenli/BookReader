package com.micoredu.reader;

import android.app.Application;
import android.content.res.Resources;

import com.liuzhenli.common.BaseApplication;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-11-10 14:57
 */
public class ReaderLibManager {
    public static Application getApplication() {
        return BaseApplication.Companion.getInstance();
    }

    public static Resources getAppResources() {
        return getApplication().getResources();
    }
}
