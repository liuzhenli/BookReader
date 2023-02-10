package com.liuzhenli.common

import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.multidex.MultiDex
import com.airbnb.mvrx.Mavericks
import com.airbnb.mvrx.navigation.DefaultNavigationViewModelDelegateFactory
import com.liuzhenli.common.AppComponent
import com.liuzhenli.common.SharedPreferencesUtil
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager
import com.liuzhenli.common.BaseApplication
import com.liuzhenli.common.FileHelp
import com.hjq.permissions.XXPermissions
import com.liuzhenli.common.utils.ChannelUtil
import com.liuzhenli.common.constant.AppConstant
import com.tencent.mmkv.MMKV
import com.liuzhenli.common.DaggerAppComponent
import com.liuzhenli.common.module.AppModule
import com.liuzhenli.common.module.ApiModule
import com.alibaba.android.arouter.launcher.ARouter
import com.liuzhenli.common.utils.L
import java.io.File
import java.util.*

/**
 * describe:
 *
 * @author Liuzhenli on 2019-11-10 15:58
 */
open class BaseApplication : Application() {
    @JvmField
    protected var donateHb = false

    @JvmField
    var mVersionCode = 0

    @JvmField
    var mVersionName: String? = null

    @JvmField
    var mVersionChannel: String? = null
    var appComponent: AppComponent? = null
        private set

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        Mavericks.initialize(
            this,
            viewModelDelegateFactory = DefaultNavigationViewModelDelegateFactory()
        )
        initMMKV()
        SharedPreferencesUtil.init(this)
        QMUISwipeBackActivityManager.init(this)
        instance = this
        Companion.downloadPath =
            SharedPreferencesUtil.getInstance().getString(getString(R.string.pk_download_path), "")
        if (TextUtils.isEmpty(Companion.downloadPath) or (Companion.downloadPath == FileHelp.getCachePath())) {
            downloadPath = null
        }
        initAppVersion()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelId()
        }
        initComponent()
        initARouter()
        L.init()
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
        // 当前项目是否已经适配了分区存储的特性
        XXPermissions.setScopedStorage(true)
    }

    private fun initAppVersion() {
        try {
            val pi = packageManager.getPackageInfo(packageName, 0)
            mVersionCode = pi.versionCode
            mVersionName = pi.versionName
            mVersionChannel = ChannelUtil.getChannelName(this)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    /**
     * 设置下载地址
     */
    var downloadPath: String?
        get() = Companion.downloadPath
        set(path) {
            if (TextUtils.isEmpty(path)) {
                Companion.downloadPath = FileHelp.getFilesPath()
            } else {
                Companion.downloadPath = path
            }
            AppConstant.BOOK_CACHE_PATH =
                Companion.downloadPath + File.separator + "book_cache" + File.separator
            SharedPreferencesUtil.getInstance()
                .putString(getString(R.string.pk_download_path), path)
        }

    /**
     * 创建通知ID
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannelId() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        //用唯一的ID创建渠道对象
        val downloadChannel = NotificationChannel(
            channelIdDownload,
            getString(R.string.download_offline),
            NotificationManager.IMPORTANCE_LOW
        )
        //初始化channel
        downloadChannel.enableLights(false)
        downloadChannel.enableVibration(false)
        downloadChannel.setSound(null, null)

        //用唯一的ID创建渠道对象
        val readAloudChannel = NotificationChannel(
            channelIdReadAloud,
            getString(R.string.read_aloud),
            NotificationManager.IMPORTANCE_LOW
        )
        //初始化channel
        readAloudChannel.enableLights(false)
        readAloudChannel.enableVibration(false)
        readAloudChannel.setSound(null, null)

        //用唯一的ID创建渠道对象
        val webChannel = NotificationChannel(
            channelIdWeb,
            getString(R.string.web_service),
            NotificationManager.IMPORTANCE_LOW
        )
        //初始化channel
        webChannel.enableLights(false)
        webChannel.enableVibration(false)
        webChannel.setSound(null, null)

        //向notification manager 提交channel
        notificationManager?.createNotificationChannels(
            Arrays.asList(
                downloadChannel,
                readAloudChannel,
                webChannel
            )
        )
    }

    fun getDonateHb(): Boolean {
        return donateHb || BuildConfig.DEBUG
    }

    fun upDonateHb() {
        SharedPreferencesUtil.getInstance().putLong("DonateHb", System.currentTimeMillis())
        donateHb = true
    }

    fun upThemeStore() {}
    fun initNightTheme() {}
    private fun initMMKV() {
        val rootDir = MMKV.initialize(this)
    }

    private fun initComponent() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .apiModule(ApiModule())
            .build()
    }

    private fun initARouter() {
        if (isDebug) {
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        ARouter.getInstance().destroy()
    }

    companion object {
        @JvmField
        var isDebug = BuildConfig.DEBUG
        const val channelIdDownload = "channel_download"
        const val channelIdReadAloud = "channel_read_aloud"
        const val channelIdWeb = "channel_web"
        var instance: BaseApplication? = null
            private set
        private var downloadPath: String? = null
    }
}