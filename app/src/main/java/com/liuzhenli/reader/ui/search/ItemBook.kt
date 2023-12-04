package com.liuzhenli.reader.ui.search

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.*
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.liuzhenli.reader.ui.bookdetail.BookDetailActivity
import com.microedu.lib.reader.R
import com.micoredu.reader.bean.SearchBook
import com.micoredu.reader.widgets.BookCover
import com.micoredu.reader.widgets.anima.RotateLoading
import com.micoredu.reader.widgets.text.BadgeView

/**
 * Description:book item view
 */

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ItemBook @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {


    private val bookName: TextView
    private val author: TextView
    private val intro: TextView
    private val root: View
    private val mIvCover: BookCover
    private val hasNew: FrameLayout
    private val unread: BadgeView
    private val loading: RotateLoading

    init {
        inflate(context, R.layout.item_book_list, this)
        orientation = VERTICAL
        bookName = findViewById(R.id.tv_name)
        author = findViewById(R.id.tv_author_info)
        intro = findViewById(R.id.tv_book_summary)
        mIvCover = findViewById(R.id.mIvCover)
        hasNew = findViewById(R.id.fl_has_new)
        unread = findViewById(R.id.bv_unread)
        loading = findViewById(R.id.rl_loading)
        root = findViewById(R.id.cv_content)
    }

    @JvmOverloads
    @ModelProp
    fun book(book: SearchBook) {

        //数据源的数量
        if (book.origins.size > 1) {
            unread.visibility = View.VISIBLE;
            unread.text = String.format("%s", book.origins.size);
        } else {
            unread.visibility = View.GONE;
        }
        //作者
        if (!TextUtils.isEmpty(book.kind)) {
            author.text = String.format("%s  %s", getAuthor(book), book.kind)
        } else {
            author.text = String.format("%s", getAuthor(book));
        }
        bookName.text = book.name
        author.text = book.author
        intro.text = getSummary(book)
        mIvCover.setInfo(book.name, book.author, book.coverUrl)
        root.setOnClickListener {
            val intent =
                Intent(context, BookDetailActivity::class.java)
            intent.putExtra("book", book.toBook())
            intent.putExtra("author", book.author)
            intent.putExtra("bookUrl", book.bookUrl)
            intent.putExtra("name", book.name)
            context.startActivity(intent)
        }

    }

    private fun getSummary(item: SearchBook): String {
        if (!TextUtils.isEmpty(item.intro)) {
            return item.intro?.replace("\n", "")?.trim() ?: ""
        }
        return item.getDisplayLastChapterTitle()
    }

    private fun getAuthor(item: SearchBook): String {
        return if (TextUtils.isEmpty(item.author)) {
            "佚名"
        } else {
            String.format("%s·著", item.author)
        }
    }
}