package com.micoredu.reader.ui.fragment

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.*
import com.micoredu.reader.BaseFragment
import com.micoredu.reader.bean.BookChapter
import com.micoredu.reader.ui.adapter.BookChapterAdapter
import com.micoredu.reader.ui.models.itemChapterMenu
import com.microedu.lib.reader.R
import com.microedu.lib.reader.databinding.FragmentBookchapterlistBinding

data class BookChapterState(
    val queryChapter: Async<List<BookChapter>> = Uninitialized,
    val chapterList: List<BookChapter> = listOf()
) :
    MavericksState

class BookSourceController(viewModel: BookChapterViewModel) :
    TypedEpoxyController<BookChapterState>() {
    override fun buildModels(data: BookChapterState?) {
        data?.chapterList?.forEachIndexed { index, bookChapter ->
            itemChapterMenu {
                id("chapterId_${index}")
                source(bookChapter)
            }
        }
    }
}

class BookChapterListFragment : BaseFragment(R.layout.fragment_bookchapterlist), MavericksView {

    private var mBookUrl: String? = null
    val binding: FragmentBookchapterlistBinding by viewBinding(FragmentBookchapterlistBinding::bind)
    private val viewModel: BookChapterViewModel by fragmentViewModel()

    private val controller: BookSourceController by lazy {
        BookSourceController(viewModel)
    }

    override fun invalidate() = withState(viewModel) {
        controller.setData(it)
    }

    override fun init(savedInstanceState: Bundle?) {

        mBookUrl = arguments?.getString("bookUrl")

        viewModel.onAsync(BookChapterState::queryChapter, deliveryMode = uniqueOnly(), onSuccess = {

        })
        viewModel.queryChapter(mBookUrl!!)

    }


    companion object {
        @JvmStatic
        fun getInstance(bookUrl: String, position: Int): BookChapterListFragment {
            val instance = BookChapterListFragment()
            val bundle = Bundle()

            bundle.putString("bookUrl", bookUrl)
            bundle.putInt("position", position)
            instance.arguments = bundle
            return instance
        }
    }


}