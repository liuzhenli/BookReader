package com.micoredu.reader.ui.models

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.micoredu.reader.bean.BookChapter
import com.microedu.lib.reader.R
import org.jetbrains.anko.textColor

/**
 * Description:book source item view
 */

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ItemChapterMenu @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {


    private var currentIndex: Int? = -1
    private val textView: TextView

    private val root: View

    init {
        inflate(context, R.layout.item_book_chapter, this)
        orientation = VERTICAL
        textView = findViewById(R.id.tv_book_chapter_name)
        root = findViewById(R.id.view_book_chapter)
    }

    @JvmOverloads
    @ModelProp
    fun setCurrentChapterIndex(currentIndex: Int?) {
        this.currentIndex = currentIndex
    }

    @JvmOverloads
    @ModelProp
    fun source(source: BookChapter) {
        textView.text = source.title
        if (currentIndex == source.index) {
            textView.textColor = resources.getColor(R.color.color_widget)
        } else {
            textView.textColor = resources.getColor(R.color.text_color_66)
        }
    }

    var itemClickListener: View.OnClickListener? = null
        @CallbackProp set


    @AfterPropsSet
    fun useProps() {
        // This is optional, and is called after the annotated properties above are set.
        // This is useful for using several properties in one method to guarantee they are all set first.
        root.setOnClickListener(itemClickListener)
    }

}