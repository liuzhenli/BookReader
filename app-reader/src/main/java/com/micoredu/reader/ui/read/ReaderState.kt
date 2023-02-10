package com.micoredu.reader.ui.read

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.micoredu.reader.bean.BookChapter
import com.micoredu.reader.bean.BookProgress

/**
 * Description:reader fragment state
 */
data class ReaderState(
    val flag: Boolean = false,
    val getBookChapter: Async<List<BookChapter>> = Uninitialized,
    val syncBookProgress: Async<BookProgress?> = Uninitialized,
    val changeBookSource: Async<Any?> = Uninitialized,
    val autoChangeBookSource: Async<Any?> = Uninitialized,
    val chapterList: List<BookChapter> = listOf(),
    val removeFromBookshelf: Async<Boolean> = Uninitialized,
    val upBookSource: Async<Boolean> = Uninitialized,
    val refreshContentDur: Async<Any?> = Uninitialized,
    val refreshContentAfter: Async<Any> = Uninitialized,
    val voidRequest: Async<Any> = Uninitialized,
    val saveContent: Async<Any?> = Uninitialized,
    val reverseContent: Async<Any?> = Uninitialized,
    val refreshImage: Async<Any?> = Uninitialized,
    val saveImage: Async<Any?> = Uninitialized,
    val replaceRuleChanged: Async<Any?> = Uninitialized,
    val disableSource: Async<Any?> = Uninitialized,
) : MavericksState