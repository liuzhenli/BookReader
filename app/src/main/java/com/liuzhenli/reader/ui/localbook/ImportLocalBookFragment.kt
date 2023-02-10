package com.liuzhenli.reader.ui.localbook

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.liuzhenli.reader.ui.search.SearchViewModel
import com.micoredu.reader.BaseFragment
import com.micoredu.reader.R
import com.micoredu.reader.databinding.ActImportlocalbookBinding

class ImportLocalBookFragment : BaseFragment(R.layout.act_importlocalbook),
    MavericksView {

    val binding by viewBinding(ActImportlocalbookBinding::bind)

    private val mViewModel: SearchViewModel by fragmentViewModel()

    override fun invalidate() = withState(mViewModel) {}

    override fun init(savedInstanceState: Bundle?) {

    }


}