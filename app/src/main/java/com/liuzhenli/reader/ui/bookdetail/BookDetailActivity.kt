package com.liuzhenli.reader.ui.bookdetail

import android.os.Bundle
import android.view.LayoutInflater
import com.airbnb.mvrx.asMavericksArgs
import com.liuzhenli.common.BaseActivity
import com.micoredu.reader.databinding.ActivityBookdetailBinding

class BookDetailActivity : BaseActivity<ActivityBookdetailBinding>() {

    override fun inflateView(inflater: LayoutInflater?): ActivityBookdetailBinding {
        return ActivityBookdetailBinding.inflate(inflater!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.frameLayout.getFragment<BookDetailFragment>().arguments =
            intent.extras?.asMavericksArgs()
        setContentView(binding.root)
    }

    override fun init(savedInstanceState: Bundle?) {
    }

}