package com.micoredu.reader

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.liuzhenli.common.utils.ToastUtil
import com.liuzhenli.common.widget.dialog.LoadingDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

/**
 * Description:BaseFragment
 */
abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId),
    CoroutineScope by MainScope() {
    var mPage = 1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
        observeLiveBus()
    }

    open fun observeLiveBus() {
    }

    protected abstract fun init(savedInstanceState: Bundle?)


    private var dialog: LoadingDialog? = null

    fun dismissProgress() {
        if (dialog?.isShowing == true) {
            dialog?.dismiss()
        }
    }

    fun showProgress() {
        if (dialog == null) {
            dialog = LoadingDialog(
                activity
            ).instance(activity)
        }
        if (dialog?.isShowing == false) {
            dialog?.show()
        }
    }

    fun toast(message: String) {
        ToastUtil.showToast(message)
    }

    fun finish() {
        activity?.finish()
    }

}