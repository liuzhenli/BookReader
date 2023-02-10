package com.micoredu.reader.ui.history

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.mvrx.MavericksView
import com.microedu.lib.reader.R
import com.microedu.lib.reader.databinding.ActReadhistoryBinding

class ReadHistoryFragment : Fragment(R.layout.act_readhistory), MavericksView {
    private val binding: ActReadhistoryBinding by viewBinding()
    override fun invalidate() {
        binding.toolbar.tvToolbarTitle.text = resources.getString(R.string.read_history)
    }
}