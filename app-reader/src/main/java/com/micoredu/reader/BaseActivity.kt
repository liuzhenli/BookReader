package com.micoredu.reader

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.liuzhenli.common.theme.Theme
import com.liuzhenli.common.utils.applyTint
import com.liuzhenli.common.widget.dialog.LoadingDialog
import java.lang.ref.WeakReference


abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    private var mWeakReference: WeakReference<Activity>? = null

    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mWeakReference = WeakReference<Activity>(this)
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