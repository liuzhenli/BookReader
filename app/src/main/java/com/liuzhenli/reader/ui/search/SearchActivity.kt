package com.liuzhenli.reader.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import com.liuzhenli.common.BaseActivity
import com.micoredu.reader.databinding.ActivitySearchBinding

class SearchActivity : BaseActivity<ActivitySearchBinding>() {

    override fun inflateView(inflater: LayoutInflater?): ActivitySearchBinding {
        return ActivitySearchBinding.inflate(inflater!!)
    }

    override fun init(savedInstanceState: Bundle?) {
    }

}