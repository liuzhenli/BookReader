package com.liuzhenli.reader.ui.bookdetail

import android.content.Intent
import android.text.TextUtils
import android.util.Log
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.liuzhenli.common.utils.ToastUtil
import com.liuzhenli.reader.ui.shelf.BookShelfState
import com.micoredu.reader.R
import com.micoredu.reader.bean.Book
import com.micoredu.reader.bean.BookChapter
import com.micoredu.reader.bean.BookSource
import com.micoredu.reader.dao.appDb
import com.micoredu.reader.help.book.BookHelp
import com.micoredu.reader.help.book.isLocal
import com.micoredu.reader.help.book.isSameNameAuthor
import com.micoredu.reader.model.BookCover
import com.micoredu.reader.model.ReadBook
import com.micoredu.reader.model.localBook.LocalBook
import com.micoredu.reader.model.webBook.WebBook
import kotlinx.coroutines.Dispatchers

class BookDetailViewModel(initialState: BookDetailState) :
    MavericksViewModel<BookDetailState>(initialState) {
    var state: BookDetailState
    var bookSource: BookSource? = null
    var isImportBookOnLine = false
    var inBookshelf = false
    private lateinit var bookName: String
    private lateinit var authorName: String

    init {
        state = initialState
    }

    fun initData(intent: Intent) {
         bookName = intent.getStringExtra("name") ?: ""
         authorName = intent.getStringExtra("author") ?: ""
        val bookUrl = intent.getStringExtra("bookUrl") ?: ""

        appDb.bookDao.getBook(bookName, authorName)?.let {
            inBookshelf = true
            upBook(it)
            return
        }
        if (bookUrl.isNotBlank()) {
            appDb.searchBookDao.getSearchBook(bookUrl)?.toBook()?.let {
                upBook(it)
                return
            }
        }
        appDb.searchBookDao.getFirstByNameAuthor(bookName, authorName)?.toBook()?.let {
            upBook(it)
            return
        }
    }

    private fun loadBookInfo(
        book: Book,
        canReName: Boolean = true,
    ) = withState { bookSourceState ->
        if (bookSourceState.getBookInfo is Loading) {
            return@withState
        }
        bookSource?.let { bookSource ->
            suspend {
                WebBook.getBookInfoAwait(bookSource, book, canReName = canReName)
            }.execute(
                Dispatchers.IO,
                retainValue = BookDetailState::getBookInfo
            ) {
                if (isImportBookOnLine) {
                    appDb.searchBookDao.update(book.toSearchBook())
                }
                if (bookSourceState.isInBookShelf) {
                    appDb.bookDao.update(book)
                }
                loadChapter(book)
                copy(getBookInfo = it, isInBookShelf = inBookshelf)
            }
        }


    }

    private fun loadChapter(book: Book) = withState { it ->
        if (it.getBookChapter is Loading) return@withState
        if (book.isLocal) {
            suspend { LocalBook.getChapterList(book) }.execute(
                Dispatchers.IO,
                retainValue = BookDetailState::getBookChapter
            ) {
                appDb.bookDao.update(book)
                appDb.bookChapterDao.delByBook(book.bookUrl)
                appDb.bookChapterDao.insert(*chapterList.toTypedArray())
                copy(getBookChapter = it, isInBookShelf = inBookshelf)
            }
        } else if (isImportBookOnLine) {
            setState { copy(chapterList = emptyList<BookChapter>()) }
        } else {
            bookSource?.let { bookSource ->
                val oldBook = book.copy()
                if (TextUtils.isEmpty(book.tocUrl)) {
                    return@withState
                }
                suspend {
                    WebBook.getChapterListAwait(bookSource, book, true).getOrThrow()
                }.execute(
                    Dispatchers.IO,
                    retainValue = BookDetailState::getBookChapter
                ) { result ->
                    if (inBookshelf) {
                        if (oldBook.bookUrl == book.bookUrl) {
                            appDb.bookDao.update(book)
                        } else {
                            appDb.bookDao.insert(book)
                            BookHelp.updateCacheFolder(oldBook, book)
                        }
                        appDb.bookChapterDao.delByBook(oldBook.bookUrl)
                        result()?.toTypedArray()?.let { appDb.bookChapterDao.insert(*it) }
                        if (book.isSameNameAuthor(ReadBook.book)) {
                            ReadBook.book = book
                            ReadBook.chapterSize = book.totalChapterNum
                        }
                    }
                    copy(getBookChapter = result, isInBookShelf = inBookshelf)
                }

            } ?: let {
                ToastUtil.showToast(R.string.error_no_source)
            }
        }

    }

    fun upBook(intent: Intent) {
        val name = intent.getStringExtra("name") ?: ""
        val author = intent.getStringExtra("author") ?: ""
        appDb.bookDao.getBook(name, author)?.let { book ->
            upBook(book)
        }
    }

    private fun upBook(book: Book) {
        /// bookData.postValue(book)
        upCoverByRule(book)
        bookSource = if (book.isLocal) null else
            appDb.bookSourceDao.getBookSource(book.origin)
        if (book.tocUrl.isEmpty()) {
            loadBookInfo(book)
        } else if (isImportBookOnLine) {
            //chapterListData.postValue(emptyList())
        } else {
            val chapterList = appDb.bookChapterDao.getChapterList(book.bookUrl)
            if (chapterList.isNotEmpty()) {
                //chapterListData.postValue(chapterList)
            } else {
                loadChapter(book)
            }
        }
    }


    private fun upCoverByRule(book: Book) = withState { it ->
        if (it.upCoverByRule is Loading) return@withState
        suspend {
            if (book.customCoverUrl.isNullOrBlank()) {
                BookCover.searchCover(book)?.let { coverUrl ->
                    book.customCoverUrl = coverUrl
                    // bookData.postValue(book)
                    if (inBookshelf) {
                        saveBook(book)
                    }
                }
            }
        }.execute(Dispatchers.IO, BookDetailState::upCoverByRule) {
            copy(upCoverByRule = it)
        }
    }

    fun saveBook(book: Book?) = withState { it ->
        if (it.saveBook is Loading) return@withState
        suspend {
            if (book?.order == 0) {
                book.order = appDb.bookDao.minOrder - 1
            }
            appDb.bookDao.getBook(book?.name!!, book.author)?.let {
                book.durChapterPos = it.durChapterPos
                book.durChapterTitle = it.durChapterTitle
            }
            book.save()
            if (ReadBook.book?.name == book.name && ReadBook.book?.author == book.author) {
                ReadBook.book = book
            }
        }.execute(Dispatchers.IO, BookDetailState::saveBook) {
            copy(saveBook = it)
        }
    }

    fun addBookShelf(book: Book?) = withState { state ->
        if (state.saveBook is Loading) return@withState
        suspend {
            if (book?.order == 0) {
                book.order = appDb.bookDao.minOrder - 1
            }
            appDb.bookDao.getBook(book?.name!!, book.author)?.let { it ->
                book.durChapterPos = it.durChapterPos
                book.durChapterTitle = it.durChapterTitle

            }
            book.save()
            inBookshelf = true
            inBookshelf
        }.execute(Dispatchers.IO, retainValue = BookDetailState::addToBookShelf) {
            copy(isInBookShelf = inBookshelf)
        }
    }

    fun removeFromBookShelf(book: Book?) = withState { state ->
        if (state.removeFromBookshelf is Loading) return@withState
        suspend {
            book?.delete()
            inBookshelf = false
            inBookshelf
        }.execute(Dispatchers.IO, retainValue = BookDetailState::removeFromBookshelf) {
            copy(isInBookShelf = inBookshelf)
        }
    }

    fun checkBookIsInBookShelf() = withState {
        suspend {
            appDb.bookDao.getBook(bookName, authorName)!=null
        }.execute(Dispatchers.IO, retainValue = BookDetailState::checkInBookShelf){
            copy(isInBookShelf = it()?:false)
        }
    }
}