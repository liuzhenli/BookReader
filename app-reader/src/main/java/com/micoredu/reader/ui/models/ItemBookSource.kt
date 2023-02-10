package com.micoredu.reader.ui.models

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.microedu.lib.reader.R
import com.micoredu.reader.bean.BookSource

/**
 * Description:book source item view
 */

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ItemBookSource @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {


    private val textView: TextView
    private val checkBox: AppCompatCheckBox
    private val groupName: TextView
    private val switch: Switch
    private val editButton: View
    private val toTop: View
    private val delete: View
    private val root: View

    init {
        inflate(context, R.layout.item_book_source, this)
        orientation = VERTICAL
        textView = findViewById(R.id.tv_source_name)
        checkBox = findViewById(R.id.cb_book_source_check)
        groupName = findViewById(R.id.tv_source_group_name)
        switch = findViewById(R.id.switch_discover)
        editButton = findViewById(R.id.view_edit_book_source)
        toTop = findViewById(R.id.view_move_to_top)
        delete = findViewById(R.id.iv_book_source_visible)
        root = findViewById(R.id.item_root)
    }

    @JvmOverloads
    @ModelProp
    fun source(source: BookSource) {
        textView.text = source.bookSourceName
        checkBox.isChecked = source.enabled
        groupName.text = source.bookSourceGroup
        switch.isChecked = source.enabledExplore
    }

    var itemClickListener: View.OnClickListener? = null
        @CallbackProp set

    var editClickListener: View.OnClickListener? = null
        @CallbackProp set

    var toTopClickListener: View.OnClickListener? = null
        @CallbackProp set

    var deleteClickListener: View.OnClickListener? = null
        @CallbackProp set

    var checkBoxChangeListener: CompoundButton.OnCheckedChangeListener? = null
        @CallbackProp set

    var switchChangeListener: CompoundButton.OnCheckedChangeListener? = null
        @CallbackProp set

    @AfterPropsSet
    fun useProps() {
        // This is optional, and is called after the annotated properties above are set.
        // This is useful for using several properties in one method to guarantee they are all set first.
        root.setOnClickListener(itemClickListener)
        editButton.setOnClickListener(editClickListener)
        toTop.setOnClickListener(toTopClickListener)
        delete.setOnClickListener(deleteClickListener)
        checkBox.setOnCheckedChangeListener(checkBoxChangeListener)
        switch.setOnCheckedChangeListener(switchChangeListener)
    }

}