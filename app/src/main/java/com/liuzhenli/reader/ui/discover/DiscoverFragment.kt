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
import androidx.fragment.app.FragmentPagerAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.hwangjr.rxbus.RxBus
import com.liuzhenli.common.constant.AppConstant
import com.liuzhenli.common.constant.RxBusTag
import com.liuzhenli.reader.bean.BookCategory
import com.liuzhenli.reader.ui.discover.category.BookCategoryFragment
import com.liuzhenli.reader.view.ScaleTransitionPagerTitleView
import com.micoredu.reader.BaseFragment
import com.micoredu.reader.R
import com.micoredu.reader.bean.BookSource
import com.micoredu.reader.bean.rule.ExploreKind
import com.micoredu.reader.databinding.FragmentDiscoverBinding
import com.micoredu.reader.help.source.exploreKinds
import com.micoredu.reader.model.analyzeRule.AnalyzeRule
import com.script.ScriptException
import com.script.SimpleBindings
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import okhttp3.internal.http.HTTP_GONE

class DiscoverFragment : BaseFragment(R.layout.fragment_discover),
    MavericksView {
    val binding by viewBinding(FragmentDiscoverBinding::bind)
    private val mViewModel: DiscoverViewModel by fragmentViewModel()

    private var fragmentsList = mutableListOf<Fragment>()
    private var pagerAdapter: FragmentPagerAdapter? = null
    private var navigatorAdapter: CommonNavigatorAdapter? = null
    private var mBookCategory = mutableListOf<ExploreKind>()
    override fun invalidate() = withState(mViewModel) {

    }

    override fun onResume() {
        super.onResume()
        mViewModel.queryBookSource()
    }

    override fun init(savedInstanceState: Bundle?) {

        mViewModel.onAsync(
            DiscoverState::queryBookSource,
            deliveryMode = uniqueOnly(),
            onSuccess = {
                mViewModel.dealCategory(it[3])
            },
            onFail = {})

        mViewModel.onAsync(
            DiscoverState::dealCategory,
            deliveryMode = uniqueOnly(),
            onSuccess = {
                onBookSourceChange(it)
            },
            onFail = {})
        binding.mViewPager.offscreenPageLimit = 10
        pagerAdapter =
            object : FragmentPagerAdapter(childFragmentManager, BEHAVIOR_SET_USER_VISIBLE_HINT) {
                override fun getCount(): Int {
                    return fragmentsList.size
                }

                override fun getItem(position: Int): Fragment {
                    return fragmentsList[position]
                }

                override fun getItemPosition(obj: Any): Int {
                    return if (fragmentsList.contains(obj)) {
                        // 如果当前 item 未被 remove，则返回 item 的真实 position
                        fragmentsList.indexOf(obj)
                    } else {
                        // 否则返回状态值 POSITION_NONE
                        POSITION_NONE;
                    }
                }
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
                simplePagerTitleView.text = mBookCategory[index].title
                simplePagerTitleView.textSize = 16f
                simplePagerTitleView.normalColor = resources.getColor(R.color.text_color_99)
                simplePagerTitleView.selectedColor = resources.getColor(R.color.text_color_66)
                simplePagerTitleView.setOnClickListener { v: View? ->
                    binding.mViewPager.currentItem = index
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
        binding.mIndicator.navigator = navigator
        ViewPagerHelper.bind(binding.mIndicator, binding.mViewPager)
        binding.mBookSourceView.setOnItemClick { booksource: BookSource? ->
            mViewModel.dealCategory(booksource!!)
        }
        binding.mBookSourceView.visibility = View.GONE
        val tvText: TextView = binding.mViewEmpty.viewEmpty.findViewById(R.id.tv_empty_text)
        tvText.text = "暂无书源,搜索微信公众号:异书房,\n回复\"书源\"获取书源~"
    }

    @Synchronized
    fun onBookSourceChange(categories: List<ExploreKind>) {

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
    }


}