package com.liuzhenli.reader.ui.discover.category

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import com.micoredu.reader.BaseFragment
import com.micoredu.reader.R
import com.micoredu.reader.bean.rule.ExploreKind
import com.micoredu.reader.dao.appDb
import com.micoredu.reader.databinding.FragmentBooklistBinding

class BookCategoryFragment : BaseFragment(R.layout.fragment_booklist),
    MavericksView {
    val binding by viewBinding<FragmentBooklistBinding>()
    private val mViewModel: CategoryViewModel by fragmentViewModel()

    override fun init(savedInstanceState: Bundle?) {
        val categories = arguments?.getSerializable("category") as ExploreKind
        val sourceUrl = arguments?.getString("bookSourceUrl")
        if (sourceUrl != null) {
            val bookSource = appDb.bookSourceDao.getBookSource(sourceUrl)
            if (bookSource != null) {
                categories.url?.let { mViewModel.getBookList(bookSource, it, 1) }
            }
        }


        mViewModel.onAsync(CategoryState::getBookList, deliveryMode = uniqueOnly(), onSuccess = {


        })
    }

    override fun invalidate() {
    }
}