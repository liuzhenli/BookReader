package com.liuzhenli.reader.ui.discover.category

import com.airbnb.mvrx.MavericksViewModel
import com.micoredu.reader.bean.BookSource
import com.micoredu.reader.bean.SearchBook
import com.micoredu.reader.dao.appDb
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
        }.execute(Dispatchers.IO, retainValue = CategoryState::getBookList) { state ->
            val data = mutableListOf<SearchBook>()

            var hasMore = true
            if (state() == null || state()?.size == null || state()?.size!! == 0) {
                hasMore = false
            }

            if (page != 1) {
                data.addAll(bookList)
            }
            val searchResult = state()
            if (searchResult?.isNotEmpty() == true && !data.containsAll(searchResult)) {
                data.addAll(searchResult)
                appDb.searchBookDao.insert(*searchResult.toTypedArray())
            }
            copy(getBookList = state, bookList = data, hasMoreBooks = hasMore)
        }
    }


}