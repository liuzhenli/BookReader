package com.liuzhenli.reader.ui.search

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.liuzhenli.common.utils.AppConfig
import com.micoredu.reader.bean.SearchBook
import com.micoredu.reader.bean.SearchKeyword
import com.micoredu.reader.bean.SearchScope
import com.micoredu.reader.dao.appDb
import com.micoredu.reader.model.webBook.SearchModel
import kotlinx.coroutines.Dispatchers

class SearchViewModel(initialState: SearchState) :
    MavericksViewModel<SearchState>(initialState) {
    var state: SearchState
    private var searchKey = ""
    private var searchID = 0L
    val searchScope: SearchScope = SearchScope(AppConfig.searchScope)

    init {
        state = initialState
    }

    private val mSearchModel =
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


    fun getSearchHistory() = withState { it ->
        if (it.getSearchWords is Loading) return@withState
        suspend { appDb.searchKeywordDao.all }.execute(
            Dispatchers.IO,
            retainValue = SearchState::getSearchWords
        ) {
            copy(getSearchWords = it)
        }
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
        appDb.searchKeywordDao.deleteAll()
    }

    fun saveSearchKeyWords(key: String) {
        if (key.isNotEmpty()) {
            appDb.searchKeywordDao.insert(
                SearchKeyword(
                    word = key,
                    lastUseTime = System.currentTimeMillis(),
                    usage = (appDb.searchKeywordDao.get(key)?.usage ?: 0) + 1
                )
            )
        }
    }

    fun stopSearch() {
        mSearchModel.cancelSearch()
    }

    fun searchBook(key: String) {
        if ((searchKey == key) || key.isNotEmpty()) {
            mSearchModel.cancelSearch()
            searchID = System.currentTimeMillis()
            searchKey = key
        }
        if (searchKey.isEmpty()) {
            return
        }
        saveSearchKeyWords(key)
        mSearchModel.search(searchID, searchKey)
    }

    fun removeSearchHistoryItem(word: SearchKeyword) {
        appDb.searchKeywordDao.delete(word)
    }
}