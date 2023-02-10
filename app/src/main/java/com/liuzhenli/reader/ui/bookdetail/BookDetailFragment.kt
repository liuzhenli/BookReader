package com.liuzhenli.reader.ui.bookdetail

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.TextureView
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.mvrx.*
import com.liuzhenli.common.utils.AppLog
import com.liuzhenli.common.utils.ToastUtil
import com.liuzhenli.common.utils.image.ImageUtil
import com.micoredu.reader.ui.read.ReaderActivity
import com.micoredu.reader.BaseFragment
import com.micoredu.reader.R
import com.micoredu.reader.bean.Book
import com.micoredu.reader.bean.BookChapter
import com.micoredu.reader.databinding.ActBookdetailBinding
import com.micoredu.reader.help.book.isLocal

class BookDetailFragment : BaseFragment(R.layout.act_bookdetail),
    MavericksView {

    val binding by viewBinding(ActBookdetailBinding::bind)
    private val mViewModel: BookDetailViewModel by fragmentViewModel()
    private val args: Bundle by args()
    private var mChapterList: MutableList<BookChapter> = mutableListOf<BookChapter>()

    override fun invalidate() = withState(mViewModel) {

    }

    override fun init(savedInstanceState: Bundle?) {
        val book: Book? = args.getParcelable("book")
        mViewModel.initData(requireActivity().intent)
        binding.toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
        binding.viewBookInfo.mTvBookName.text = book?.name
        binding.viewBookInfo.mTvAuthor.text = book?.author
        binding.mTvRead.setOnClickListener {
            mViewModel.saveBook(book,"readBook")
        }
        setBookInfo(book)
        mViewModel.onAsync(
            BookDetailState::getBookChapter,
            deliveryMode = uniqueOnly(),
            onSuccess = {
                mChapterList.clear()
                mChapterList.addAll(it)
                binding.mTvChapterCount.text = String.format(
                    resources.getString(R.string.total_chapter_count),
                    mChapterList.size
                )
            },
            onFail = {
                AppLog.put("获取目录失败\n${it.localizedMessage}", it)
                ToastUtil.showToast(R.string.error_get_chapter_list)
            })

        mViewModel.onEach(BookDetailState::action, deliveryMode = UniqueOnly("action")){
            if (TextUtils.equals("readBook", it)) {
                val intent = Intent(context, ReaderActivity::class.java)
                intent.putExtra("bookUrl", book?.bookUrl)
                context?.startActivity(intent)
            }
        }
    }

    private fun setBookInfo(book: Book?) {
        if (book == null) {
            return
        }
        if (TextUtils.isEmpty(book.intro)) {
            book.intro = "2333"
        }
        binding.mTvDescription.text = Html.fromHtml(String.format("简介:\n%s", book.intro))
        binding.viewBookInfo.mTvBookName.text = book.name
        binding.viewBookInfo.mTvAuthor.text =
            String.format(
                "%s 著",
                book.author
            )
        binding.mTvChapterCount.text = String.format(
            resources.getString(R.string.total_chapter_count),
            mChapterList.size
        )
        binding.viewBookInfo.mTvBookSource.text = book.originName
        ImageUtil.setRoundedCornerImage(
            requireContext(),
            book.coverUrl,
            R.drawable.book_cover,
            binding.viewBookInfo.mIvCover,
            4
        )
        if (book.isLocal) {
            binding.mTvDownload.visibility = View.GONE
        }
    }
}