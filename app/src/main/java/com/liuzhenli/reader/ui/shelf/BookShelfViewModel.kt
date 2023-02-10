package com.liuzhenli.reader.ui.shelf

import com.airbnb.mvrx.MavericksViewModel
import com.liuzhenli.reader.ui.discover.ImportLocalBookState
import com.micoredu.reader.bean.Book
import com.micoredu.reader.dao.appDb
import com.micoredu.reader.help.book.BookHelp
import kotlinx.coroutines.Dispatchers

class BookShelfViewModel(initialState: BookShelfState) :
    MavericksViewModel<BookShelfState>(initialState) {

    fun queryBooks() = withState {
        suspend { appDb.bookDao.all }.execute(
            Dispatchers.IO,
            retainValue = BookShelfState::getBookList
        ) { res ->
            copy(getBookList = res)
        }
    }

    fun refreshBookShelf(books: List<Book>) = withState {

    }
}