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
    val mSearchScope: SearchScope = SearchScope(AppConfig.searchScope)

    init {
        state = initialState
    }

    fun copySearchStart() = setState { copy(searchStart = true) }
    fun copySearchCancel() = setState { copy(searchStart = false) }
    fun copySearchFinish(isEmpty: Boolean) =
        setState { copy(searchStart = false, isFinishSearch = isEmpty) }

    fun copySearchSuccess(searchBooks: ArrayList<SearchBook>) =
        setState { copy(bookList = searchBooks, searchStart = true) }

    private val mSearchModel =
        SearchModel(viewModelScope, object : SearchModel.CallBack {

            override fun getSearchScope(): SearchScope {
                return mSearchScope
            }

            override fun onSearchStart() {
                copySearchStart()
            }

            override fun onSearchSuccess(searchBooks: ArrayList<SearchBook>) {
                copySearchSuccess(searchBooks)
            }

            override fun onSearchFinish(isEmpty: Boolean) {
                copySearchFinish(isEmpty)
            }

            override fun onSearchCancel() {
                copySearchCancel()
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

    fun clearSearchHistory() = withState {
        appDb.searchKeywordDao.deleteAll()
    }

    fun saveSearchKeyWords(key: String) = withState {
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

    fun stopSearch() = withState {
        mSearchModel.cancelSearch()
    }

    fun searchBook(key: String) = withState {
        if ((searchKey == key) || key.isNotEmpty()) {
            mSearchModel.cancelSearch()
            searchID = System.currentTimeMillis()
            searchKey = key
        }
        if (searchKey.isEmpty()) {
            return@withState
        }
        saveSearchKeyWords(key)
        setState { copy(bookList = listOf(), searchStart = false) }
        mSearchModel.search(searchID, searchKey)
    }

    fun removeSearchHistoryItem(word: SearchKeyword) = withState {
        appDb.searchKeywordDao.delete(word)
    }
}