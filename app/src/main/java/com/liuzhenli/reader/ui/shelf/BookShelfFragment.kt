package com.liuzhenli.reader.ui.shelf

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.micoredu.reader.BaseFragment
import com.micoredu.reader.R
import com.micoredu.reader.databinding.FragmentBookshelfBinding

class BookShelfFragment : BaseFragment(R.layout.fragment_bookshelf),
    MavericksView {
    val binding by viewBinding(FragmentBookshelfBinding::bind)
    private val mViewModel: BookShelfViewModel by fragmentViewModel()
    private val controller: BookShelfController by lazy {
        BookShelfController(
            longClickListener = {
                toast("long click item ")
                true
            },
            context = requireContext()
        )
    }

    override fun invalidate() = withState(mViewModel) {
        controller.setData(it)
    }

    override fun init(savedInstanceState: Bundle?) {
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.setController(controller)
    }

    override fun onResume() {
        super.onResume()
        mViewModel.queryBooks()
    }


}