package com.liuzhenli.reader.ui.shelf

import com.airbnb.mvrx.MavericksViewModel
import com.micoredu.reader.bean.Book
import com.micoredu.reader.dao.appDb
import com.micoredu.reader.utils.cnCompare
import kotlinx.coroutines.Dispatchers

class BookShelfViewModel(initialState: BookShelfState) :
    MavericksViewModel<BookShelfState>(initialState) {

    var bookSort = 1
    fun queryBooks() = withState {
        suspend {
            var books = appDb.bookDao.all
            books = when (bookSort) {
                1 -> books.sortedByDescending { it.latestChapterTime }
                2 -> books.sortedWith { o1, o2 ->
                    o1.name.cnCompare(o2.name)
                }
                3 -> books.sortedBy { it.order }
                else -> books.sortedByDescending { it.durChapterTime }
            }
            books
        }.execute(
            Dispatchers.IO,
            retainValue = BookShelfState::getBookList
        ) { res ->
            var books = listOf<Book>()
            val b = res()
            if (b?.isNotEmpty() == true) {
                books = b
            }
            books.sortedBy { it.origin }
            copy(getBookList = res, bookList = books)
        }
    }

    fun refreshBookShelf(books: List<Book>) = withState {

    }
}