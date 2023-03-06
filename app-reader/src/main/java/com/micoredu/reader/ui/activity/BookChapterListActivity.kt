package com.micoredu.reader.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import com.liuzhenli.common.BaseApplication.Companion.instance
import com.liuzhenli.common.base.BaseTabActivity
import com.liuzhenli.common.utils.ClickUtils
import com.micoredu.reader.bean.Bookmark
import com.micoredu.reader.dao.AppDatabase.Companion.createDatabase
import com.micoredu.reader.model.ReadBook
import com.micoredu.reader.ui.bookmark.BookMarkFragment
import com.micoredu.reader.ui.bookmark.BookMarkFragment.Companion.getInstance
import com.micoredu.reader.ui.fragment.BookChapterListFragment.Companion.getInstance
import com.micoredu.reader.utils.ReadConfigManager
import com.microedu.lib.reader.R
import com.microedu.lib.reader.databinding.ActBookchapterlistBinding
import java.util.*

/**
 * Description:chapter list and bookmark list
 */
class BookChapterListActivity : BaseTabActivity<ActBookchapterlistBinding?>() {
    val bookShelf: ReadBook? = null

    private var mBookMarkList: List<Bookmark> = ArrayList()
    private var mBookMarkDesc = mutableListOf<Bookmark>()
    private var mIsBookMark = false
    private val mIsFromReadPage = false
    private var isAsc = true
    private var mBookUrl: String? = null
    override fun inflateView(inflater: LayoutInflater?): ActBookchapterlistBinding {
        return ActBookchapterlistBinding.inflate(inflater!!)
    }

    override fun init(savedInstanceState: Bundle?) {
        mBookUrl = intent.getStringExtra("bookUrl")
        mIsBookMark = intent.getBooleanExtra("isBookMark", false)
        super.init(savedInstanceState)

        if (bookShelf != null) {
            mBookMarkList = createDatabase(instance!!).bookmarkDao.getByBook(
                bookShelf.book!!.name, bookShelf.book!!.author
            )
            for (i in mBookMarkList.indices) {
                mBookMarkDesc.add(0, mBookMarkList[i])
            }
        }
        binding!!.icToolBar.tvToolbarRight.text = "倒序"
        ClickUtils.click(binding!!.icToolBar.tvToolbarRight) { o: Any? ->
            isAsc = !isAsc
            (mFragmentList[1] as BookMarkFragment).refreshData()
            if (isAsc) {
                binding!!.icToolBar.tvToolbarRight.text = "倒序"
            } else {
                binding!!.icToolBar.tvToolbarRight.text = "正序"
            }
        }
        binding!!.icToolBar.tvToolbarRight.visibility = View.VISIBLE
        if (bookShelf != null) {
            binding!!.icToolBar.tvToolbarTitle.text = bookShelf.book!!.name
        }
    }

    override fun createTabFragments(): List<Fragment> {
        return Arrays.asList<Fragment>(
            getInstance(
                mBookUrl!!
            ), getInstance(mIsFromReadPage)
        )
    }

    override fun createTabTitles(): List<String> {
        val titles = ArrayList<String>()
        titles.add("目录")
        titles.add("书签")
        return titles
    }


    override fun onResume() {
        super.onResume()
        //如果来自阅读页面,则需要改变一下颜色
        if (mIsFromReadPage) {
            changeTheme()
        }
    }

    private fun changeTheme() {
        mTabLayout.background = ReadConfigManager.getInstance().getTextBackground(this)
        if (ReadConfigManager.getInstance().isNightTheme) {
            binding!!.icToolBar.tvToolbarTitle.setBackgroundColor(resources.getColor(R.color.skin_night_reader_scene_bg_color))
            mTabLayout.setSelectedTabIndicatorColor(ReadConfigManager.getInstance().textColor)
        } else {
            mTabLayout.setSelectedTabIndicatorColor(ReadConfigManager.getInstance().textColor)
            binding!!.icToolBar.toolbar.setBackgroundColor(ReadConfigManager.getInstance().bgColor)
        }
        binding!!.mViewRoot.background = ReadConfigManager.getInstance().getTextBackground(this)
        mTabLayout.setTabTextColors(
            resources.getColor(R.color.text_color_99),
            ReadConfigManager.getInstance().textColor
        )
        setupSystemBar()
    }

    companion object {
        fun start(context: Context, bookUrl: String?, isBookMark: Boolean = false) {
            val intent = Intent(context, BookChapterListActivity::class.java)
            intent.putExtra("bookUrl", bookUrl)
            intent.putExtra("isBookMark", isBookMark)
            context.startActivity(intent)
        }
    }
}