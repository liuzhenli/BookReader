package com.liuzhenli.reader.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.text.TextUtils;

import com.micoredu.readerlib.helper.ReadConfigManager;
import com.micoredu.readerlib.page.PageLoader;

/**
 * Description:
 *
 * @author liuzhenli 2021/1/17
 * Email: 848808263@qq.com
 */
public class BatteryAndTimeChangeReceiver extends BroadcastReceiver {

    private final PageLoader mPageLoader;

    public BatteryAndTimeChangeReceiver(PageLoader pageLoader) {
        this.mPageLoader = pageLoader;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ReadConfigManager.getInstance().getHideStatusBar()) {
            if (TextUtils.equals(Intent.ACTION_TIME_TICK, intent.getAction())) {
                if (mPageLoader != null) {
                    mPageLoader.updateTime();
                }
            }

            if (TextUtils.equals(intent.getAction(), Intent.ACTION_BATTERY_CHANGED)) {
                if (mPageLoader != null) {
                    mPageLoader.updateBattery(intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0));
                }
            }
        }
    }

    public void register(Context context, BatteryAndTimeChangeReceiver receiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        context.registerReceiver(receiver, filter);
    }

}
