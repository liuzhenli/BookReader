package com.liuzhenli.reader.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import com.micoredu.reader.BaseActivity
import com.micoredu.reader.databinding.ActivitySearchBinding

class SearchActivity : BaseActivity<ActivitySearchBinding>() {

    override fun inflateView(inflater: LayoutInflater?): ActivitySearchBinding {
        return ActivitySearchBinding.inflate(inflater!!)
    }

    override fun init(savedInstanceState: Bundle?) {
    }

}