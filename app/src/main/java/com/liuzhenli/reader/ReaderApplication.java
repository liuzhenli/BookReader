package com.liuzhenli.reader;


import androidx.annotation.Nullable;
import androidx.multidex.MultiDex;

import com.liuzhenli.common.BaseApplication;
import com.liuzhenli.common.SharedPreferencesUtil;
import com.liuzhenli.common.utils.AppFrontBackHelper;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.network.DaggerAppComponent;
import com.liuzhenli.reader.module.ApiModule;
import com.liuzhenli.reader.module.AppModule;
import com.liuzhenli.reader.utils.AppUtils;
import com.micoredu.readerlib.content.UpLastChapterModel;
import com.microedu.reader.BuildConfig;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;

import java.util.concurrent.TimeUnit;

/**
 * @author Liuzhenli
 * @since 2019-07-06 16:28
 */
public class ReaderApplication extends BaseApplication {
    public static boolean isDebug = BuildConfig.DEBUG;
    private static ReaderApplication mInstance;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        MultiDex.install(this);
        initComponent();
        AppUtils.init(this);
        AppFrontBackHelper.getInstance().register(this, new AppFrontBackHelper.OnAppStatusListener() {
            @Override
            public void onFront() {
                donateHb = System.currentTimeMillis() - SharedPreferencesUtil.getInstance().getLong("DonateHb", 0) <= TimeUnit.DAYS.toMillis(30);
            }

            @Override
            public void onBack() {
                UpLastChapterModel.destroy();
            }
        });

        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return BuildConfig.DEBUG;
            }
        });
        initMMKV();
    }

    private void initMMKV() {
        String initialize = MMKV.initialize(this);
    }

    public static ReaderApplication getInstance() {
        return mInstance;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    private void initComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule())
                .build();

    }
}
