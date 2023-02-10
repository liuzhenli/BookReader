package com.micoredu.reader.ui.source

import android.os.Bundle
import android.view.LayoutInflater
import com.alibaba.android.arouter.facade.annotation.Route
import com.liuzhenli.common.constant.ARouterConstants
import com.micoredu.reader.BaseActivity
import com.microedu.lib.reader.databinding.ActivityBooksourceBinding
@Route(path = ARouterConstants.ACT_BOOK_SOURCE)
class BookSourceActivity : BaseActivity<ActivityBooksourceBinding>() {

    override fun inflateView(inflater: LayoutInflater?): ActivityBooksourceBinding {
        return ActivityBooksourceBinding.inflate(inflater!!)
    }

    override fun init(savedInstanceState: Bundle?) {
    }

}