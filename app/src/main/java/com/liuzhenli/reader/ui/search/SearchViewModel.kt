package com.liuzhenli.reader.ui.search

import com.airbnb.mvrx.MavericksViewModel
import com.liuzhenli.common.utils.AppConfig
import com.liuzhenli.reader.ui.discover.ImportLocalBookState
import com.micoredu.reader.bean.SearchBook
import com.micoredu.reader.bean.SearchScope
import com.micoredu.reader.dao.appDb
import com.micoredu.reader.model.webBook.SearchModel
import com.micoredu.reader.model.webBook.WebBook
import com.micoredu.reader.ui.source.BookSourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

class SearchViewModel(initialState: SearchState) :
    MavericksViewModel<SearchState>(initialState) {
    var state: SearchState
    private var searchKey = ""
    private var searchID = 0L
    val searchScope: SearchScope = SearchScope(AppConfig.searchScope)

    init {
        state = initialState
    }

    private val searchModel =
        SearchModel(viewModelScope, object : SearchModel.CallBack {

            override fun getSearchScope(): SearchScope {
                return searchScope
            }

            override fun onSearchStart() {
                suspend { true }.execute(
                    Dispatchers.IO,
                    retainValue = SearchState::ignore
                ) {
                    copy(searchStart = true)
                }
            }

            override fun onSearchSuccess(searchBooks: ArrayList<SearchBook>) {
                suspend { searchBooks }.execute(
                    Dispatchers.IO,
                    retainValue = SearchState::searchBook
                ) { result ->
                    copy(bookList = searchBooks, searchBook = result)
                }
            }

            override fun onSearchFinish(isEmpty: Boolean) {
                suspend { true }.execute(
                    Dispatchers.IO,
                    retainValue = SearchState::ignore
                ) {
                    copy(searchStart = true, isFinishSearch = isEmpty)
                }
            }

            override fun onSearchCancel() {
                suspend { true }.execute(
                    Dispatchers.IO,
                    retainValue = SearchState::checkBookSource
                ) {
                    copy(searchStart = false)
                }
            }
        })


    fun getSearchHistory() {

    }

    fun checkBookSource() = withState {
        suspend { appDb.bookSourceDao.all }.execute(
            Dispatchers.IO,
            retainValue = SearchState::checkBookSource
        ) { result ->
            val size = result()?.size
            copy(hasBookSource = size != 0, checkBookSource = result)
        }
    }

    fun clearSearchHistory() {
    }

    fun stopSearch() {
        searchModel.cancelSearch()
    }

    fun searchBook(key: String) {
        if ((searchKey == key) || key.isNotEmpty()) {
            searchModel.cancelSearch()
            searchID = System.currentTimeMillis()
            searchKey = key
        }
        if (searchKey.isEmpty()) {
            return
        }
        searchModel.search(searchID, searchKey)
    }
}