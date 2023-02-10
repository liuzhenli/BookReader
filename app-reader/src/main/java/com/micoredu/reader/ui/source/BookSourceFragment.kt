package com.micoredu.reader.ui.source

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.*
import com.alibaba.android.arouter.launcher.ARouter
import com.liuzhenli.common.constant.ARouterConstants
import com.liuzhenli.common.utils.AppConfigManager
import com.liuzhenli.common.utils.IntentUtils
import com.liuzhenli.common.widget.DialogUtil
import com.micoredu.reader.BaseFragment
import com.microedu.lib.reader.R
import com.micoredu.reader.bean.BookSource
import com.microedu.lib.reader.databinding.FragmentBookSourceBinding
import com.micoredu.reader.help.source.SourceHelp
import com.micoredu.reader.ui.adapter.BookSourceFilterMenuAdapter
import com.micoredu.reader.ui.models.itemBookSource

/**
 * Description: Book Source List Page
 */
class BookSourceFragment : BaseFragment(R.layout.fragment_book_source),
    MavericksView {
    private lateinit var mFilterMenuAdapter: BookSourceFilterMenuAdapter
    val binding by viewBinding(FragmentBookSourceBinding::bind)
    private val mViewModel: BooksSourceViewModel by fragmentViewModel()

    private val controller: BookSourceController by lazy {
        BookSourceController(mViewModel)
    }

    override fun invalidate() = withState(mViewModel) {
        controller.setData(it)
    }

    override fun init(savedInstanceState: Bundle?) {
        binding.toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
        configMenuLayout()
        configDropDownMenu()

        mViewModel.onAsync(
            BookSourceState::getNetSources,
            deliveryMode = uniqueOnly(),
            onSuccess = {
                dismissProgress()
            },
            onFail = {
                dismissProgress()
                toast("获取失败${it.message}")
            })

        binding.recyclerView.setController(controller)
        mViewModel.fetchBookSource()
    }

    private fun configDropDownMenu() {
        mFilterMenuAdapter = BookSourceFilterMenuAdapter(requireContext())
        binding.mDropdownMenu.setMenuAdapter(mFilterMenuAdapter)
        updateSortMenu()
    }

    private fun updateSortMenu() {
        val groupList = mutableListOf<String>()
        groupList.addAll(SourceHelp.groupList)
        groupList.add(0, resources.getString(R.string.all));
        mFilterMenuAdapter.setGroupList(groupList)
        mFilterMenuAdapter.setMenuItemClickListener(object :
            BookSourceFilterMenuAdapter.MenuItemClickListener {
            override fun onSelectChange(index: Int) {
            }

            override fun onSortChange(index: Int) {
            }

            override fun onGroupChange(index: Int, groupName: String?) {
            }
        })
    }

    private fun configMenuLayout() {
        binding.toolbar.inflateMenu(R.menu.menu_book_source)
        binding.toolbar.setOnMenuItemClickListener { item: MenuItem ->
            val itemId = item.itemId
            //import default book source
            if (itemId == R.id.action_fast_import) {
                mViewModel.getNetSource(AppConfigManager.getDefaultBookSourceUrl())
            } else if (itemId == R.id.action_add_book_source) {
                startActivity(Intent(requireActivity(), EditSourceActivity::class.java))
            } else if (itemId == R.id.action_import_book_source_local) {
                IntentUtils.selectFileSys(
                    requireActivity(),
                    IntentUtils.IMPORT_BOOK_SOURCE_LOCAL
                )
                //import source form url
            } else if (itemId == R.id.action_import_book_source_online) {
                DialogUtil.showEditTextDialog(
                    requireActivity(),
                    resources.getString(R.string.import_book_source_on_line),
                    java.lang.String.format(
                        "%s://",
                        resources.getString(R.string.input_book_source_url)
                    ),
                    resources.getString(R.string.input_book_source_url)
                ) { s ->
                    showProgress()
                    mViewModel.getNetSource(s)
                }
                //二维码导入
            } else if (itemId == R.id.action_import_book_source_rwm) {
                ARouter.getInstance()
                    .build(ARouterConstants.ACT_QRCODE)
                    .navigation(requireActivity(), IntentUtils.IMPORT_BOOK_SOURCE_QRCODE)
            } else if (itemId == R.id.action_del_select) {
                val bookSources: List<BookSource> = mViewModel.getSelectedBookSource()
                if (bookSources.isNotEmpty()) {
                    DialogUtil.showMessagePositiveDialog(
                        requireActivity(),
                        resources.getString(R.string.dialog_title),
                        java.lang.String.format(
                            resources.getString(R.string.delete_selected_book_source),
                            bookSources.size
                        ),
                        resources.getString(R.string.cancel),
                        null,
                        resources.getString(R.string.ok),
                        { _, _ -> mViewModel.deleteSelectedSource(bookSources) },
                        true
                    )
                }
            } else if (itemId == R.id.action_check_book_source) {
                val selectedBookSource: List<BookSource> = listOf()
//                    SourceHelp.getSelectedBookSource()
                if (selectedBookSource.size === 0) {
                    toast(resources.getString(R.string.please_choose_book_source))
                } else {
                    mViewModel.checkBookSource(requireContext(), selectedBookSource)
                }
            } else if (itemId == R.id.action_share_wifi) {

            }
            false
        }
    }
}


data class BookSourceState(
    val sourceList: List<BookSource> = listOf(),
    val getLocalSource: Async<List<BookSource>> = Uninitialized,
    val getNetSources: Async<List<BookSource>> = Uninitialized
) : MavericksState

class BookSourceController(viewModel: BooksSourceViewModel) :
    TypedEpoxyController<BookSourceState>() {
    override fun buildModels(data: BookSourceState?) {
        data?.sourceList?.forEach { bookSource ->
            itemBookSource {
                id("bookSource")
                source(bookSource)
                itemClickListener(View.OnClickListener { })
                editClickListener(View.OnClickListener { })
                toTopClickListener(View.OnClickListener { })
                deleteClickListener(View.OnClickListener { })
                checkBoxChangeListener { _, isChecked ->
                    bookSource.enabled = isChecked
                    SourceHelp.update(bookSource)
                }
                switchChangeListener { _, isChecked ->
                    bookSource.enabledExplore = isChecked
                    SourceHelp.update(bookSource)
                }
            }
        }
    }
}

