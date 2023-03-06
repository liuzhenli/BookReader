package com.micoredu.reader.ui.fragment

import com.airbnb.mvrx.MavericksViewModel
import com.micoredu.reader.bean.BookChapter
import com.micoredu.reader.dao.appDb
import kotlinx.coroutines.Dispatchers

class BookChapterViewModel(initialState: BookChapterState) :
    MavericksViewModel<BookChapterState>(initialState) {
    var chapterIndex: Int? = 0

    fun queryChapter(bookUrl: String) = withState {

        suspend { appDb.bookChapterDao.getChapterList(bookUrl) }.execute(
            Dispatchers.IO,
            retainValue = BookChapterState::queryChapter
        ) {
            val chapters = mutableListOf<BookChapter>()
            it()?.let { it1 -> chapters.addAll(it1) }
            copy(queryChapter = it, chapterList = chapters)
        }
    }

    fun queryBook(bookUrl: String) = withState {
        suspend { appDb.bookDao.getBook(bookUrl) }.execute(
            Dispatchers.IO,
            retainValue = BookChapterState::queryBook
        ) {
            chapterIndex = it()?.durChapterIndex
            copy(queryBook = it)
        }
    }
}