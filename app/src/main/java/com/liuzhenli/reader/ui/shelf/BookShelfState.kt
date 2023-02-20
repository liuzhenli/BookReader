package com.liuzhenli.reader.ui.shelf

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.micoredu.reader.bean.Book

data class BookShelfState(
    val title: String = "Hello Book",
    val bookList: List<Book> = listOf(),
    val getBookList: Async<List<Book>> = Uninitialized,
    val removeFromBookShelf: Async<Any> = Uninitialized,
    val startRefreshBookShelf: Async<Any> = Uninitialized,
    val updateBookInfo: Async<Any> = Uninitialized,
    val saveBookToShelf: Async<Any> = Uninitialized,
) : MavericksState