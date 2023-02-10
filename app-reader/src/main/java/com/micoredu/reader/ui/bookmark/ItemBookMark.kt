package com.micoredu.reader.ui.bookmark

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.liuzhenli.common.widget.helper.KotlinEpoxyHolder
import com.microedu.lib.reader.R
import com.micoredu.reader.bean.Bookmark

abstract class ItemBookMark : EpoxyModelWithHolder<ItemViewHolder>() {

    override fun getDefaultLayout(): Int {
        return R.layout.item_book_mark
    }

    @EpoxyAttribute
    lateinit var bookMark: Bookmark

    @EpoxyAttribute
    lateinit var listener: () -> Unit

    override fun bind(holder: ItemViewHolder) {
        super.bind(holder)
        holder.tvChapterName.text = bookMark.chapterName
        holder.tvContent.text = bookMark.content
        holder.root.setOnClickListener {
            listener()
        }
    }

}

class ItemViewHolder : KotlinEpoxyHolder() {
    val tvChapterName by bind<TextView>(R.id.tv_book_chapter_name)
    val tvContent by bind<TextView>(R.id.tv_book_chapter_content)
    val root by bind<View>(R.id.view_book_chapter)
}