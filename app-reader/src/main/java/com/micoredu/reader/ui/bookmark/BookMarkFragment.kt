package com.micoredu.reader.ui.bookmark

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.microedu.lib.reader.R
import com.microedu.lib.reader.databinding.FragmentBookmarkBinding
import com.micoredu.reader.ui.activity.BookChapterListActivity

class BookMarkFragment : Fragment(R.layout.fragment_bookmark), MavericksView {

    private val mViewModel: BookMarkViewModel by fragmentViewModel()
    private val binding: FragmentBookmarkBinding by viewBinding()
    private val controller: BookMarkController by lazy {
        BookMarkController(mViewModel)
    }


    private var mIsFromReadPage = false
    override fun invalidate() = withState(mViewModel) {
        controller.setData(it)
    }

    companion object {
        @JvmStatic
        fun getInstance(isFromReadPage: Boolean): BookMarkFragment {
            val instance = BookMarkFragment()
            val bundle = Bundle()
            bundle.putBoolean("isFromReadPage", isFromReadPage)
            instance.arguments = bundle
            return instance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mIsFromReadPage = arguments != null && requireArguments().getBoolean("isFromReadPage")


        binding.recyclerView.setRefreshListener {
            binding.recyclerView.setRefreshing(false)
        }
        binding.recyclerView.setLayoutManager(LinearLayoutManager(activity))


//        mAdapter = BookMarkAdapter(activity, mIsFromReadPage)
//        binding.recyclerView.adapter = mAdapter
//        val bookMarkList = parentActivity!!.bookMarkList
//        if (bookMarkList != null && bookMarkList.size > 0) {
//            binding.recyclerView.visibility = View.VISIBLE
//            inflate!!.empty.viewEmpty.visibility = View.GONE
//            mAdapter!!.addAll(bookMarkList)
//        } else {
//            binding.recyclerView.visibility = View.GONE
//            inflate!!.empty.viewEmpty.visibility = View.VISIBLE
//        }
//        mAdapter!!.setOnItemClickListener { position: Int ->
//            val item = mAdapter!!.getItem(position)
//            parentActivity!!.bookShelf.setFinalDate(System.currentTimeMillis())
//            BookHelp.saveBookToShelf(parentActivity!!.bookShelf)
//            val intent = Intent(context, ReaderActivity::class.java)
//            intent.putExtra(ReaderActivity.OPEN_FROM, Constant.BookOpenFrom.FROM_BOOKSHELF)
//            intent.putExtra(ReaderActivity.CHAPTER_ID, item.chapterIndex)
//            intent.putExtra(ReaderActivity.PROGRESS, item.getPageIndex())
//            val bookKey = System.currentTimeMillis().toString()
//            intent.putExtra(BitIntentDataManager.DATA_KEY, bookKey)
//            BitIntentDataManager.getInstance().putData(bookKey, parentActivity!!.bookShelf)
//            activity.startActivity(intent)
//        }
//        mAdapter!!.setOnItemLongClickListener { position: Int ->
//            DialogUtil.showMessagePositiveDialog(
//                activity,
//                "提示",
//                "是否删除该书签?",
//                "取消",
//                null,
//                "删除",
//                QMUIDialogAction.ActionListener { dialog, index ->
//                    BookHelp.delBookmark(mAdapter!!.getItem(position))
//                    mAdapter!!.remove(position)
//                    ToastUtil.showToast("书签已删除")
//                },
//                true
//            )
//            false
//        }
        return binding.root
    }

    private val parentActivity: BookChapterListActivity?
        private get() = getActivity() as BookChapterListActivity?

    fun refreshData() {

    }


}