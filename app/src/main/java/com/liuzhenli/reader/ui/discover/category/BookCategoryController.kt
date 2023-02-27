package com.liuzhenli.reader.ui.discover.category

import com.airbnb.epoxy.TypedEpoxyController
import com.liuzhenli.reader.ui.discover.DiscoverState
import com.liuzhenli.reader.ui.search.itemBook

class BookCategoryController() : TypedEpoxyController<CategoryState>() {
    override fun buildModels(data: CategoryState?) {
        data?.bookList?.forEachIndexed { index, searchBook ->
            itemBook {
                id("book_category${index}")
                book(searchBook)
            }
        }
    }
}