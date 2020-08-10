package com.micoredu.readerlib.content;


import com.liuzhenli.common.BaseApplication;
import com.micoredu.readerlib.R;

public class VipThrowable extends Throwable {

    private final static String tag = "VIP_THROWABLE";

    VipThrowable() {
        super(BaseApplication.getInstance().getString(R.string.donate_s));
    }
}
