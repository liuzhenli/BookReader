package com.liuzhenli.reader.ui.discover

import com.airbnb.mvrx.MavericksViewModel
import com.micoredu.reader.bean.Book
import com.micoredu.reader.bean.BookSource
import com.micoredu.reader.dao.appDb
import com.micoredu.reader.help.source.exploreKinds
import kotlinx.coroutines.Dispatchers

class DiscoverViewModel(initialState: DiscoverState) :
    MavericksViewModel<DiscoverState>(initialState) {
    var bookSource: BookSource? = null
    fun queryBookSource() = withState {
        suspend {
            appDb.bookSourceDao.all
        }.execute(Dispatchers.IO, retainValue = DiscoverState::queryBookSource) {
            copy(queryBookSource = it)
        }
    }

    fun dealCategory(bookSource: BookSource) = withState {
        this.bookSource = bookSource
        suspend {
            bookSource.exploreKinds()
        }.execute(Dispatchers.IO, retainValue = DiscoverState::dealCategory) {
            copy(dealCategory = it)
        }
    }

}