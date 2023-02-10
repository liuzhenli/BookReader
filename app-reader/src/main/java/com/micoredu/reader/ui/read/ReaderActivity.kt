package com.micoredu.reader.ui.read

import android.os.Bundle
import android.view.LayoutInflater
import com.airbnb.mvrx.asMavericksArgs
import com.micoredu.reader.BaseActivity
import com.microedu.lib.reader.databinding.ActivityReaderBinding

/**
 * Description:reader page
 */
class ReaderActivity : BaseActivity<ActivityReaderBinding>(){
    override fun inflateView(inflater: LayoutInflater?): ActivityReaderBinding {
        return ActivityReaderBinding.inflate(inflater!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.frameLayout.getFragment<ReaderFragment>().arguments =
            intent.extras?.asMavericksArgs()
        setContentView(binding.root)
    }

    override fun init(savedInstanceState: Bundle?) {
    }
}