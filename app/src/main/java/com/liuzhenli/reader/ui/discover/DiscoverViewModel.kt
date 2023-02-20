package com.liuzhenli.reader.ui.discover

import com.airbnb.mvrx.MavericksViewModel
import com.micoredu.reader.dao.appDb
import kotlinx.coroutines.Dispatchers

class DiscoverViewModel(initialState: DiscoverState) :
    MavericksViewModel<DiscoverState>(initialState) {

    fun queryBookSource() = withState {
        suspend {
            appDb.bookSourceDao.all
        }.execute(Dispatchers.IO, retainValue = DiscoverState::queryBookSource) {
            copy(queryBookSource = it)
        }
    }
}