package com.liuzhenli.reader.ui.search

import android.content.Context
import android.content.Intent
import android.view.View
import com.airbnb.epoxy.TypedEpoxyController
import com.liuzhenli.reader.ui.bookdetail.BookDetailActivity

class SearchController(context: Context) : TypedEpoxyController<SearchState>() {

    var mContext = context;
    override fun buildModels(searchState: SearchState?) {
        try {
            searchState?.bookList?.forEachIndexed { index, book ->
                itemBook {
                    id("itemBookList_${index}")
                    book(book)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }
}