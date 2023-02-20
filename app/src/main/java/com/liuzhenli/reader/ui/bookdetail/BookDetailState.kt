package com.liuzhenli.reader.ui.bookdetail

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.micoredu.reader.bean.Book
import com.micoredu.reader.bean.BookChapter

data class BookDetailState(
    val book: Book = Book(),
    val isInBookShelf: Boolean = false,
    val getBookInfo: Async<Book> = Uninitialized,
    val saveBook: Async<Any?> = Uninitialized,
    val upCoverByRule: Async<Any?> = Uninitialized,
    val addToBookShelf: Async<Boolean?> = Uninitialized,
    val removeFromBookshelf: Async<Boolean?> = Uninitialized,
    val getBookChapter: Async<List<BookChapter>> = Uninitialized,
    val chapterList: List<BookChapter> = listOf(),
    val action: String = "",
) :
    MavericksState