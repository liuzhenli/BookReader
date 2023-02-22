package com.liuzhenli.reader.ui.discover.category

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.micoredu.reader.bean.Book
import com.micoredu.reader.bean.BookSource
import com.micoredu.reader.bean.SearchBook
import com.micoredu.reader.bean.rule.ExploreKind

data class CategoryState(
    val getBookList: Async<List<SearchBook>> = Uninitialized,
) : MavericksState