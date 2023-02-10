package com.micoredu.reader.ui.bookmark

import com.airbnb.epoxy.TypedEpoxyController

class BookMarkController(private val viewModel: BookMarkViewModel) :
    TypedEpoxyController<BookmarkState>() {
    override fun buildModels(data: BookmarkState?) {
        data?.bookMarks?.forEach {
        }

    }
}