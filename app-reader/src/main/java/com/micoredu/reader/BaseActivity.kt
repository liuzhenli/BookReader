package com.micoredu.reader

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.liuzhenli.common.utils.printOnDebug
import com.liuzhenli.common.widget.dialog.LoadingDialog
import java.lang.ref.WeakReference


abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    private var mWeakReference: WeakReference<Activity>? = null

    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mWeakReference = WeakReference<Activity>(this)
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            }
//        } else {
//            try {
//                window?.setDecorFitsSystemWindows(false)
//                val controller = window?.insetsController
//                controller?.setSystemBarsAppearance(
//                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
//                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
//                )
//            } catch (e: Exception) {
//                e.printOnDebug()
//            }
//        }

        binding = inflateView(layoutInflater)
        setContentView(binding.root)
        init(savedInstanceState)
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

    open fun isInMultiWindow(): Boolean {
        return false
    }
}