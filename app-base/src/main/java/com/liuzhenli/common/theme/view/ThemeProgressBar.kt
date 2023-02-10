package com.liuzhenli.common.theme.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ProgressBar
import com.liuzhenli.common.theme.accentColor
import com.liuzhenli.common.utils.applyTint

class ThemeProgressBar(context: Context, attrs: AttributeSet) : ProgressBar(context, attrs) {

    init {
        if (!isInEditMode) {
            applyTint(context.accentColor)
        }
    }
}