package com.micoredu.reader.ui.verify

import android.os.Bundle
import android.view.LayoutInflater
import com.micoredu.reader.BaseActivity
import com.microedu.lib.reader.databinding.ActivityTranslucenceBinding

class VerificationCodeActivity : BaseActivity<ActivityTranslucenceBinding>() {
    override fun inflateView(inflater: LayoutInflater?): ActivityTranslucenceBinding {
        return ActivityTranslucenceBinding.inflate(inflater!!)
    }

    override fun init(savedInstanceState: Bundle?) {
    }
}