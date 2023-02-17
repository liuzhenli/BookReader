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
    val bookList: List<SearchBook> = listOf(),
    val searchWords: List<String> = listOf(),
    val searchStart: Boolean = false,
    val isFinishSearch: Boolean = false,
) :
    MavericksState {


    override fun hashCode(): Int {
        return hasBookSource.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SearchState

        if (hasBookSource != other.hasBookSource) return false
        if (ignore != other.ignore) return false
        if (getSearchWords != other.getSearchWords) return false
        if (checkBookSource != other.checkBookSource) return false
        if (bookList != other.bookList) return false
        if (searchWords != other.searchWords) return false
        if (searchStart != other.searchStart) return false
        if (isFinishSearch != other.isFinishSearch) return false

        return true
    }
}