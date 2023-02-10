package com.micoredu.reader.ui.history

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.liuzhenli.common.utils.TimeUtils
import com.liuzhenli.common.widget.helper.KotlinEpoxyHolder
import com.microedu.lib.reader.R
import com.micoredu.reader.bean.ReadRecord



abstract class ItemReadHistory : EpoxyModelWithHolder<ItemViewHolder>() {

    override fun getDefaultLayout(): Int {
        return R.layout.item_read_history
    }
    @EpoxyAttribute
    lateinit var bookMark: ReadRecord

    @EpoxyAttribute
    lateinit var listener: () -> Unit

    @EpoxyAttribute
    lateinit var longClickListener: () -> Unit

    override fun bind(holder: ItemViewHolder) {
        super.bind(holder)
        holder.tvBookName.text = bookMark.bookName
        holder.tvReadTime.text = String.format("累计阅读:%s", TimeUtils.formatToHour(bookMark.sumTime))
        holder.root.setOnClickListener {
            listener()
        }
//        holder.root.setOnLongClickListener {
//
//        }
    }

}

class ItemViewHolder : KotlinEpoxyHolder() {
    val tvBookName by bind<TextView>(R.id.tv_book_name)
    val tvReadTime by bind<TextView>(R.id.tv_read_time)
    val root by bind<View>(R.id.readHistoryRoot)
}