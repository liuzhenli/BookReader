package com.micoredu.reader.ui.source

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.liuzhenli.common.utils.ApiManager
import com.micoredu.reader.bean.BookSource
import com.micoredu.reader.dao.appDb
import com.micoredu.reader.help.source.SourceHelp
import com.micoredu.reader.net.RetrofitInstance
import kotlinx.coroutines.Dispatchers


class BooksSourceViewModel(state: BookSourceState) : MavericksViewModel<BookSourceState>(state) {
    fun fetchBookSource() = withState {
        if (it.getLocalSource is Loading) return@withState
        suspend { appDb.bookSourceDao.all }.execute(
            Dispatchers.IO,
            retainValue = BookSourceState::getLocalSource
        ) { result ->

            val bookSourceList = mutableListOf<BookSource>()
            if (result() != null && !sourceList.containsAll(result()!!.toList())) {
                bookSourceList.addAll(result()!!.toList())
            }
            copy(getLocalSource = result, sourceList = bookSourceList)
        }
    }

    fun getNetSource(url: String?) = withState { state ->
        ApiManager.getInstance().bookSource = url;
        if (state.getNetSources is Loading) return@withState
        suspend {
            RetrofitInstance.androidAPI.getBookSource()
        }.execute(Dispatchers.IO, retainValue = BookSourceState::getNetSources) { result ->
            val bookSourceList = mutableListOf<BookSource>()
            if (result() != null && !sourceList.containsAll(result()?.toList() ?: listOf())) {
                bookSourceList.addAll(result()!!.toList())
                SourceHelp.insertBookSource(*result()!!.toList().toTypedArray())
            }
            copy(getNetSources = result, sourceList = bookSourceList)
        }
    }

    fun checkBookSource(mContext: Any, selectedBookSource: List<BookSource>) {

    }

    fun deleteSelectedSource(bookSources: List<BookSource>) {

    }

    fun getSelectedBookSource(): List<BookSource> {
        return listOf()
    }
}
