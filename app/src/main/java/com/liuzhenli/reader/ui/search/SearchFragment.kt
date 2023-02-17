package com.liuzhenli.reader.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.liuzhenli.common.utils.DensityUtil
import com.liuzhenli.common.utils.SoftInputUtils
import com.micoredu.reader.BaseFragment
import com.micoredu.reader.R
import com.micoredu.reader.databinding.FragmentSearchBinding
import com.micoredu.reader.ui.source.BookSourceActivity
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.popup.QMUIPopup
import com.qmuiteam.qmui.widget.popup.QMUIPopups

class SearchFragment : BaseFragment(R.layout.fragment_search),
    MavericksView {
    val binding by viewBinding(FragmentSearchBinding::bind)
    private val mViewModel: SearchViewModel by fragmentViewModel()
    private lateinit var mMenu: QMUIPopup


    private val controller: SearchController by lazy {
        SearchController(requireContext())
    }

    override fun invalidate() = withState(mViewModel) {
        if (it.searchStart) {
            binding.mTvSearchBookCount.text = String.format("找到%s部相关书籍", it.bookList.size)
            binding.recyclerView.visibility = View.VISIBLE
            binding.mViewGroupSearchResult.visibility = View.VISIBLE
            binding.mViewTitleBar.visibility = View.GONE
            binding.mViewSearchHistory.visibility = View.GONE

            binding.mSearchIndicator.visibility = View.VISIBLE
            binding.mVStopSearch.visibility = View.VISIBLE
        }
        controller.setData(it)
    }

    override fun init(savedInstanceState: Bundle?) {
        binding.mEditBg.radius = DensityUtil.dip2px(requireContext(), 6F)
        binding.mViewBack.setOnClickListener { requireActivity().onBackPressed() }

        binding.btnGeneralSearchClear.visibility = View.GONE

        initMenu()

        mViewModel.onAsync(
            SearchState::checkBookSource,
            deliveryMode = uniqueOnly("checkBookSource"),
            onSuccess = {
                if (it.isNotEmpty()) {
                    binding.mViewSearchHistory.visibility = View.GONE
                    binding.mViewGroupSearchResult.visibility = View.VISIBLE
                    mViewModel.searchBook(binding.mEtSearch.text.toString().trim())
                } else {
                    mViewModel.stopSearch()
                    toast("no book source")
                }
            },
            onFail = { toast("no book source") })

        mViewModel.onAsync(
            SearchState::getSearchWords,
            deliveryMode = uniqueOnly("searchWordsHistory"),
            onSuccess = { res ->
                binding.flGeneralSearchHistory.removeAllViews()
                if (res.isNotEmpty()) {
                    binding.mViewSearchHistory.visibility = View.VISIBLE;
                    res.forEach { word ->
                        val tvSearch = TextView(activity)
                        tvSearch.textSize = 12f
                        tvSearch.setTextColor(resources.getColor(R.color.text_color_99))
                        tvSearch.text = word.word
                        tvSearch.tag = word.word
                        tvSearch.setOnClickListener {
                            binding.mEtSearch.setText(tvSearch.text)
                            binding.mEtSearch.setSelection(tvSearch.text.length)
                        }
                        tvSearch.setOnLongClickListener {
                            binding.flGeneralSearchHistory.removeView(tvSearch)
                            mViewModel.removeSearchHistoryItem(word)
                            true
                        }

                        binding.flGeneralSearchHistory.addView(tvSearch);
                    }
                }
            })

        binding.mEtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (TextUtils.isEmpty(s.toString())) {
                    binding.btnGeneralSearchClear.visibility = View.GONE;
                    stopSearch();
                } else {
                    binding.btnGeneralSearchClear.visibility = View.VISIBLE;
                }
            }
        });

        //click stop search
        binding.mVStopSearch.setOnClickListener {
            if (controller.adapter.itemCount == 0) {
                stopSearch()
            } else {
                mViewModel.stopSearch()
                binding.mSearchIndicator.visibility = View.GONE
                binding.mVStopSearch.visibility = View.GONE
            }
            binding.mViewTitleBar.visibility = View.VISIBLE
        }

        //click search button, check if the book source is avaliable
        binding.tvActionSearch.setOnClickListener {
            mViewModel.checkBookSource()
        }
        binding.ivClearSearchHistory.setOnClickListener {
            binding.flGeneralSearchHistory.removeAllViews()
            mViewModel.clearSearchHistory()
        }

        binding.recyclerView.setController(controller)

        binding.mEtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (TextUtils.isEmpty(s.toString())) {
                    binding.btnGeneralSearchClear.visibility = View.GONE
                    stopSearch()
                } else {
                    binding.btnGeneralSearchClear.visibility = View.VISIBLE
                }
            }
        })

        binding.mEtSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mViewModel.checkBookSource()
            }
            false
        }

        //click delete search content
        binding.btnGeneralSearchClear.setOnClickListener {
            binding.mEtSearch.setText("")
        }

        binding.mViewMore.setOnClickListener {
            mMenu.show(binding.mViewMore)
        }


        binding.mViewGroupSearchResult.visibility = View.GONE

        binding.mEtSearch.postDelayed({
            binding.mEtSearch.requestFocus()
            SoftInputUtils.showSoftInput(requireContext(), binding.mEtSearch)
        }, 500)

        mViewModel.getSearchHistory()
    }

    private fun stopSearch() {
        mViewModel.stopSearch()
        controller.setData(null)
        binding.recyclerView.visibility = View.GONE
        binding.mViewGroupSearchResult.visibility = View.GONE
        binding.mViewTitleBar.visibility = View.VISIBLE
        mViewModel.getSearchHistory()
    }

    private fun initMenu() {
        val data = listOf("书源管理", "全部书源")
        val adapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(requireContext(), R.layout.item_menu_list, data)
        mMenu = QMUIPopups.listPopup(
            requireContext(),
            QMUIDisplayHelper.dp2px(requireContext(), 200),
            QMUIDisplayHelper.dp2px(requireContext(), 300),
            adapter
        ) { _, _, position, _ ->
            when (position) {
                0 -> startActivity(Intent(requireActivity(), BookSourceActivity::class.java))
                else -> {}
            }
            mMenu.dismiss()
        }
            .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
            .preferredDirection(QMUIPopup.DIRECTION_TOP)
            .shadow(true)
            .offsetYIfTop(QMUIDisplayHelper.dp2px(requireContext(), 5))
            .skinManager(QMUISkinManager.defaultInstance(requireContext()))
            .onDismiss { }

    }
}