package com.micoredu.reader.ui.bookmark

import com.airbnb.mvrx.MavericksState
import com.micoredu.reader.bean.Bookmark

data class BookmarkState(val bookMarks: List<Bookmark>) : MavericksState