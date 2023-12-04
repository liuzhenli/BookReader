package com.liuzhenli.reader.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import com.liuzhenli.common.BaseActivity
import com.liuzhenli.common.BaseApplication
import com.liuzhenli.common.FileHelp
import com.liuzhenli.common.utils.AppSharedPreferenceHelper
import com.liuzhenli.common.utils.ClickUtils
import com.liuzhenli.common.utils.FileUtils
import com.liuzhenli.common.utils.FileUtils.deleteFileOrDirectory
import com.liuzhenli.reader.event.OnWebDavSetEvent
import com.liuzhenli.reader.ui.contract.SettingContract
import com.liuzhenli.reader.ui.database.DatabaseTableListActivity
import com.micoredu.reader.databinding.ActSettingBinding
import com.micoredu.reader.help.book.BookHelp.clearCache
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@SuppressLint("NonConstantResourceId")
class SettingActivity : BaseActivity<ActSettingBinding>(), SettingContract.View {
    override fun inflateView(inflater: LayoutInflater?): ActSettingBinding {
        return ActSettingBinding.inflate(inflater!!)
    }

    override fun init(savedInstanceState: Bundle?) {
        ClickUtils.click(binding.mViewClearCache) { o: Any? ->
            clearCache()
            deleteFileOrDirectory(mContext.cacheDir)
            FileUtils.deleteFile(FileHelp.getCachePath())
        }

        //backup
        ClickUtils.click(binding.viewSettingBackup) { o: Any? ->
            BackupSettingActivity.start(mContext)
        }
        ClickUtils.click(binding.mVFilePath) { o: Any? -> FilePathsListActivity.start(mContext) }
        ClickUtils.click(binding.mViewAppDatabase) { o: Any? ->
            DatabaseTableListActivity.start(
                mContext
            )
        }
        if (BaseApplication.isDebug) {
            binding.mViewAppDatabase.visibility = View.VISIBLE
        }
        // TODO mPresenter.getCacheSize();
        //check if webdav is set
        onWebDavSet(null)
    }

    override fun showCacheSize(size: String) {
        binding.mTvCacheSize.text = size
    }

    override fun showError(e: Exception) {}
    override fun complete() {}
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWebDavSet(event: OnWebDavSetEvent?) {
        if (TextUtils.isEmpty(AppSharedPreferenceHelper.getWebDavAccountName())
            || TextUtils.isEmpty(AppSharedPreferenceHelper.getWebDavAddPwd())
        ) {
            binding.viewWebDavIndicator.visibility = View.VISIBLE
        } else {
            if (event != null && event.isSuccess) {
                binding.viewWebDavIndicator.visibility = View.GONE
            } else {
                binding.viewWebDavIndicator.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SettingActivity::class.java)
            context.startActivity(intent)
        }
    }
}