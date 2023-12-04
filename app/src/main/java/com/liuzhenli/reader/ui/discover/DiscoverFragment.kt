package com.liuzhenli.reader.ui.discover

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.liuzhenli.common.base.BaseListener
import com.liuzhenli.reader.ui.discover.category.BookCategoryFragment
import com.liuzhenli.reader.view.ScaleTransitionPagerTitleView
import com.micoredu.reader.BaseFragment
import com.micoredu.reader.R
import com.micoredu.reader.bean.BookSource
import com.micoredu.reader.bean.rule.ExploreKind
import com.micoredu.reader.constant.EventBus
import com.micoredu.reader.databinding.FragmentDiscoverBinding
import com.micoredu.reader.utils.postEvent
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator


class DiscoverFragment : BaseFragment(R.layout.fragment_discover),
    MavericksView {
    val binding by viewBinding(FragmentDiscoverBinding::bind)
    private val mViewModel: DiscoverViewModel by fragmentViewModel()

    private var fragmentsList = mutableListOf<BookCategoryFragment>()
    private var pagerAdapter: FragmentStateAdapter? = null
    private var navigatorAdapter: CommonNavigatorAdapter? = null
    private var mBookCategory = mutableListOf<ExploreKind>()

    override fun invalidate() = withState(mViewModel) {

    }


    override fun init(savedInstanceState: Bundle?) {
        mViewModel.onAsync(
            DiscoverState::queryBookSource,
            deliveryMode = uniqueOnly("DiscoverFragment_queryBookSource"),
            onSuccess = { list ->
                if (list.isNotEmpty()) {
                    val bookSources = list.filterNot { source ->
                        TextUtils.isEmpty(source.exploreUrl)
                    }
                    binding.mBookSourceView.setData(bookSources)
                    val bookSource = bookSources.shuffled().take(1)[0]
                    mViewModel.dealCategory(bookSource)
                    postEvent(EventBus.CHANGE_DISCOVER_TITLE, bookSource.bookSourceName)
                }

            },
            onFail = {})

        mViewModel.onAsync(
            DiscoverState::dealCategory,
            deliveryMode = uniqueOnly("DiscoverFragment_dealCategory"),
            onSuccess = {
                onBookSourceChange(it)
            },
            onFail = {})
        binding.mViewPager.offscreenPageLimit = 10
        pagerAdapter = object : FragmentStateAdapter(requireActivity()) {

            override fun getItemCount(): Int {
                return fragmentsList.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragmentsList[position]
            }

            override fun getItemId(position: Int): Long {
                return mBookCategory[position].hashCode().toLong()
            }

            override fun containsItem(itemId: Long): Boolean =
                mBookCategory.any { it.hashCode().toLong() == itemId }
        }

        binding.mViewPager.adapter = pagerAdapter
        val navigator = CommonNavigator(context)
        navigatorAdapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mBookCategory.size
            }

            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                val simplePagerTitleView = ScaleTransitionPagerTitleView(context)
                simplePagerTitleView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                simplePagerTitleView.text = mBookCategory[index].title.replace("\\s".toRegex(), "")
                simplePagerTitleView.textSize = 16f
                simplePagerTitleView.normalColor = resources.getColor(R.color.text_color_99)
                simplePagerTitleView.selectedColor = resources.getColor(R.color.text_color_66)
                simplePagerTitleView.setOnClickListener { v: View? ->
                    binding.mViewPager.setCurrentItem(index, false)
                }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context?): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineHeight = UIUtil.dip2px(context, 4.0).toFloat()
                indicator.lineWidth = UIUtil.dip2px(context, 10.0).toFloat()
                indicator.roundRadius = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.startInterpolator = AccelerateInterpolator()
                indicator.endInterpolator = DecelerateInterpolator(2.0f)
                indicator.setColors(resources.getColor(R.color.color_widget))
                return indicator
            }
        }
        navigator.adapter = navigatorAdapter
//        navigator.isReselectWhenLayout = false
        binding.mIndicator.navigator = navigator

        binding.mViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                binding.mIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.mIndicator.onPageSelected(position)
                binding.mIndicator.onPageScrolled(position, 0f, 0)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                binding.mIndicator.onPageScrollStateChanged(state)
            }
        })

        binding.mBookSourceView.setOnItemClick(object : BaseListener<BookSource> {
            override fun onResponse(bookSource: BookSource) {
                postEvent(EventBus.CHANGE_DISCOVER_TITLE, bookSource?.bookSourceName)
                mViewModel.dealCategory(bookSource!!)
            }
        })
        binding.mBookSourceView.visibility = View.GONE
        val tvText: TextView = binding.mViewEmpty.viewEmpty.findViewById(R.id.tv_empty_text)
        tvText.text = "暂无书源,搜索微信公众号:异书房,\n回复\"书源\"获取书源~"
        mViewModel.queryBookSource()
    }

    @Synchronized
    fun onBookSourceChange(list: List<ExploreKind>) {
        val categories = list.filterNot { TextUtils.isEmpty(it.url) }

        binding.mViewEmpty.viewEmpty.visibility = View.GONE

        fragmentsList.clear()
        mBookCategory.clear()

        mBookCategory.addAll(categories)
        for (s in categories) {
            val fragment = BookCategoryFragment()
            val bundle = Bundle()
            bundle.putSerializable("category", s)
            bundle.putSerializable("bookSourceUrl", mViewModel.bookSource?.bookSourceUrl)
            fragment.arguments = bundle
            fragmentsList.add(fragment)
        }
        pagerAdapter?.notifyDataSetChanged()
        navigatorAdapter?.notifyDataSetChanged()
        binding.mViewPager.currentItem = 0
    }


    fun configTitle() {
        if (binding.mBookSourceView.isClosed) {
            binding.mBookSourceView.show()
        } else {
            binding.mBookSourceView.close()
        }
    }
}