package com.liuzhenli.reader.ui.discover

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.micoredu.reader.BaseFragment
import com.micoredu.reader.R
import com.micoredu.reader.databinding.FragmentBookshelfBinding

class DiscoverFragment : BaseFragment(R.layout.fragment_discover),
    MavericksView {
    val binding by viewBinding(FragmentBookshelfBinding::bind)
    private val mViewModel: DiscoverViewModel by fragmentViewModel()

    override fun invalidate() = withState(mViewModel) {}


    override fun init(savedInstanceState: Bundle?) {

    }

    fun configTitle() {


    }


}