package com.liuzhenli.reader.ui.discover.category

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.micoredu.reader.bean.SearchBook

data class CategoryState(
    val getBookList: Async<List<SearchBook>> = Uninitialized,
    val bookList: List<SearchBook> = listOf(),
    val hasMoreBooks: Boolean = true,
) : MavericksState