package com.liuzhenli.reader.ui.discover.category

import com.airbnb.mvrx.MavericksViewModel
import com.micoredu.reader.bean.BookSource
import com.micoredu.reader.dao.appDb
import com.micoredu.reader.help.source.exploreKinds
import com.micoredu.reader.model.webBook.WebBook
import kotlinx.coroutines.Dispatchers

class CategoryViewModel(initialState: CategoryState) :
    MavericksViewModel<CategoryState>(initialState) {

    fun getBookList(
        source: BookSource,
        url: String, page: Int? = 1,
    ) = withState {
        suspend {

            WebBook.exploreBookAwait(source, url, page)
        }.execute(Dispatchers.IO, retainValue = CategoryState::getBookList) {
            copy(getBookList = it)
        }
    }


}