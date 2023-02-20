package com.liuzhenli.reader.ui.shelf

import android.content.Context
import android.view.View
import com.airbnb.epoxy.TypedEpoxyController
import com.liuzhenli.reader.ui.discover.ImportLocalBookState

class BookShelfController(
    private var context: Context,
    private var longClickListener: View.OnLongClickListener
) : TypedEpoxyController<BookShelfState>() {


    override fun buildModels(data: BookShelfState?) {

        data?.bookList?.forEachIndexed { index, book ->
            itemBookShelf {
                id("itemBookShelf${index}")
                book(book)
                longClickListener(this@BookShelfController.longClickListener)
            }
        }
    }
}