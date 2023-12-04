package com.liuzhenli.common

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.liuzhenli.common.theme.ColorUtils
import com.liuzhenli.common.theme.Theme
import com.liuzhenli.common.theme.backgroundColor
import com.liuzhenli.common.theme.primaryColor
import com.liuzhenli.common.utils.*
import com.liuzhenli.common.widget.dialog.LoadingDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import java.lang.ref.WeakReference


abstract class BaseActivity<VB : ViewBinding>(
    val fullScreen: Boolean = true,
    private val theme: Theme = Theme.Auto,
    private val toolBarTheme: Theme = Theme.Auto,
    private val transparent: Boolean = false,
    private val imageBg: Boolean = true
) : AppCompatActivity(), CoroutineScope by MainScope() {
    private var mWeakReference: WeakReference<Activity>? = null
    protected lateinit var binding: VB
    lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        mWeakReference = WeakReference<Activity>(this)
        window.decorView.disableAutoFill()
        initTheme()
        super.onCreate(savedInstanceState)
        mContext = this
        setupSystemBar()
        binding = inflateView(layoutInflater)
        setContentView(binding.root)
        init(savedInstanceState)
        observeLiveBus()
    }

    open fun observeLiveBus() {
    }

    open fun initTheme() {
        when (theme) {
            Theme.Transparent -> setTheme(R.style.AppTheme_Transparent)
            Theme.Dark -> {
                setTheme(R.style.AppTheme_Dark)
                window.decorView.applyBackgroundTint(backgroundColor)
            }

            Theme.Light -> {
                setTheme(R.style.AppTheme_Light)
                window.decorView.applyBackgroundTint(backgroundColor)
            }

            else -> {
                if (ColorUtils.isColorLight(primaryColor)) {
                    setTheme(R.style.AppTheme_Light)
                } else {
                    setTheme(R.style.AppTheme_Dark)
                }
                window.decorView.applyBackgroundTint(backgroundColor)
            }
        }
    }

    open fun setupSystemBar() {
        if (fullScreen && !isInMultiWindow) {
            fullScreen()
        }
        val isTransparentStatusBar = AppConfig.isTransparentStatusBar
        val statusBarColor = ThemeStore.statusBarColor(this, isTransparentStatusBar)
        setStatusBarColorAuto(statusBarColor, isTransparentStatusBar, fullScreen)
        if (toolBarTheme == Theme.Dark) {
            setLightStatusBar(false)
        } else if (toolBarTheme == Theme.Light) {
            setLightStatusBar(true)
        }
        upNavigationBarColor()
    }

    open fun upNavigationBarColor() {
        if (AppConfig.immNavigationBar) {
            setNavigationBarColorAuto(ThemeStore.navigationBarColor(this))
        } else {
            val nbColor = ColorUtils.darkenColor(ThemeStore.navigationBarColor(this))
            setNavigationBarColorAuto(nbColor)
        }
    }


    protected abstract fun inflateView(inflater: LayoutInflater?): VB

    protected abstract fun init(savedInstanceState: Bundle?)

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    private var dialog: LoadingDialog? = null

    fun dismissProgress() {
        if (dialog?.isShowing == true) {
            dialog?.dismiss()
        }
    }

    fun showProgress() {
        if (dialog == null) {
            dialog = LoadingDialog(
                this
            ).instance(this)
        }
        if (dialog?.isShowing == false) {
            dialog?.show()
        }
    }

    open fun handleApiException(throwable: Throwable) {
        dismissProgress()
    }


    open fun isStatusBarDarkMode(): Boolean {
        //true 状态栏黑色字体
        return false
    }

    val isInMultiWindow: Boolean
        get() {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                isInMultiWindowMode
            } else {
                false
            }
        }
}