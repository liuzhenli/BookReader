package com.liuzhenli.reader.ui.discover.category

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.micoredu.reader.BaseFragment
import com.micoredu.reader.R
import com.micoredu.reader.bean.BookSource
import com.micoredu.reader.bean.rule.ExploreKind
import com.micoredu.reader.dao.appDb
import com.micoredu.reader.databinding.FragmentBooklistBinding

class BookCategoryFragment : BaseFragment(R.layout.fragment_booklist),
    MavericksView {
    val binding by viewBinding<FragmentBooklistBinding>()
    private val mViewModel: CategoryViewModel by fragmentViewModel()
    private var bookSource: BookSource? = null

    private val controller: BookCategoryController by lazy {
        BookCategoryController()
    }

    override fun init(savedInstanceState: Bundle?) {
        val categories = arguments?.getSerializable("category") as ExploreKind
        val sourceUrl = arguments?.getString("bookSourceUrl")
        if (sourceUrl != null) {
             bookSource = appDb.bookSourceDao.getBookSource(sourceUrl)
            if (bookSource != null) {
                categories.url?.let { mViewModel.getBookList(bookSource!!, it, 1) }
            }
        }
        binding.recyclerView.setController(controller)
    }

    override fun invalidate() = withState(mViewModel) {
        controller.setData(it)
    }

    fun getBookSource(): BookSource? {
        return bookSource
    }
}