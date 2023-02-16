package com.liuzhenli.reader.ui.search

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.micoredu.reader.bean.BookSource
import com.micoredu.reader.bean.SearchBook
import com.micoredu.reader.bean.SearchKeyword

data class SearchState(
    val hasBookSource: Boolean = false,
    val ignore: Async<String> = Uninitialized,
    val getSearchWords: Async<List<SearchKeyword>> = Uninitialized,
    val checkBookSource: Async<List<BookSource>> = Uninitialized,
    val searchBook: Async<List<SearchBook>> = Uninitialized,
    val bookList: List<SearchBook> = listOf(),
    val searchWords: List<String> = listOf(),
    val searchStart: Boolean = false,
    val isFinishSearch: Boolean = false,
) :
    MavericksState