package com.liuzhenli.reader.ui.discover.category

import com.airbnb.mvrx.MavericksViewModel
import com.micoredu.reader.bean.BookSource
import com.micoredu.reader.bean.SearchBook
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
        }.execute(Dispatchers.IO, retainValue = CategoryState::getBookList) { res ->
            val data = mutableListOf<SearchBook>()
            if (page != 1) {
                data.addAll(bookList)
            }
            val searchResult = res()
            if (searchResult?.isNotEmpty() == true && !data.containsAll(searchResult)) {
                data.addAll(searchResult)
            }
            copy(getBookList = res, bookList = data)
        }
    }


}