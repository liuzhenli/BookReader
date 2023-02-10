package com.micoredu.reader.ui.source

import android.os.Bundle
import android.view.LayoutInflater
import com.micoredu.reader.BaseActivity
import com.microedu.lib.reader.databinding.ActivityEditsourceBinding

class EditSourceActivity : BaseActivity<ActivityEditsourceBinding>() {

    override fun inflateView(inflater: LayoutInflater?): ActivityEditsourceBinding {
        return ActivityEditsourceBinding.inflate(inflater!!)
    }

    override fun init(savedInstanceState: Bundle?) {
    }

}