package com.liuzhenli.reader.ui.discover

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.micoredu.reader.bean.BookSource

data class DiscoverState(
    val title: String = "Hello Book",
    val queryBookSource: Async<List<BookSource>> = Uninitialized,
) : MavericksState