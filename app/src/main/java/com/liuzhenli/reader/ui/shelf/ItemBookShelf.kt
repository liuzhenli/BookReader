package com.liuzhenli.reader.ui.shelf

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.*
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.liuzhenli.reader.ui.bookdetail.BookDetailActivity
import com.micoredu.reader.R
import com.micoredu.reader.bean.Book
import com.micoredu.reader.widgets.BookCover
import com.micoredu.reader.widgets.anima.RotateLoading
import com.micoredu.reader.widgets.text.BadgeView

/**
 * Description:book item view
 */

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ItemBookShelf @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {


    private val bookName: TextView
    private val author: TextView
    private val lastChapter: TextView
    private val intro: TextView
    private val root: View
    private val mIvCover: BookCover
    private val hasNew: FrameLayout
    private val unread: BadgeView
    private val loading: RotateLoading

    init {
        inflate(context, R.layout.item_bookshelf_list, this)
        orientation = VERTICAL
        bookName = findViewById(R.id.tv_name)
        author = findViewById(R.id.mTvAuthor)
        intro = findViewById(R.id.mTvRead)
        lastChapter = findViewById(R.id.tv_last)
        mIvCover = findViewById(R.id.mIvCover)
        hasNew = findViewById(R.id.fl_has_new)
        unread = findViewById(R.id.bv_unread)
        loading = findViewById(R.id.rl_loading)
        root = findViewById(R.id.cv_content)
    }


    var longClickListener: OnLongClickListener? = null
        @CallbackProp set


    @JvmOverloads
    @ModelProp
    fun book(book: Book) {
        if (book.origin.isNotEmpty()) {
            unread.visibility = View.VISIBLE;
            unread.text = String.format("%s", book.getUnreadChapterNum())
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
        intro.text = String.format("读至:%s", book.durChapterTitle ?: "章节名未知")
        mIvCover.setInfo(book.name, book.author, book.coverUrl)
        root.setOnClickListener {
            val intent =
                Intent(context, BookDetailActivity::class.java)
            intent.putExtra("book", book)
            intent.putExtra("author", book.author)
            intent.putExtra("bookUrl", book.bookUrl)
            intent.putExtra("name", book.name)
            context.startActivity(intent)
        }
        root.setOnLongClickListener {
            longClickListener?.onLongClick(this)!!
        }
        lastChapter.text = String.format("最新:%s", book.latestChapterTitle ?: "章节名未知")
    }


    private fun getAuthor(item: Book): String {
        return if (TextUtils.isEmpty(item.author)) {
            "佚名"
        } else {
            String.format("%s·著", item.author)
        }
    }
}