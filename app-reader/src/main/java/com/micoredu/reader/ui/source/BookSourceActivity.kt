package com.micoredu.reader.ui.source

import android.os.Bundle
import android.view.LayoutInflater
import com.micoredu.reader.BaseActivity
import com.microedu.lib.reader.databinding.ActivityBooksourceBinding

class BookSourceActivity : BaseActivity<ActivityBooksourceBinding>() {

    override fun inflateView(inflater: LayoutInflater?): ActivityBooksourceBinding {
        return ActivityBooksourceBinding.inflate(inflater!!)
    }

    override fun init(savedInstanceState: Bundle?) {
    }

}