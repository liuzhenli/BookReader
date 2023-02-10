package com.micoredu.reader.ui.cache

import android.os.Bundle
import android.view.LayoutInflater
import com.micoredu.reader.BaseActivity
import com.microedu.lib.reader.databinding.ActivityCacheBinding

class CacheActivity : BaseActivity<ActivityCacheBinding>() {
    override fun inflateView(inflater: LayoutInflater?): ActivityCacheBinding {
        return ActivityCacheBinding.inflate(inflater!!)
    }

    override fun init(savedInstanceState: Bundle?) {
    }
}