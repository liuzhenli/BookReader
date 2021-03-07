package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.liuzhenli.common.base.BaseActivity;
import com.liuzhenli.common.base.BaseFragment;
import com.liuzhenli.common.AppComponent;
import com.liuzhenli.reader.ui.fragment.BookCategoryFragment;
import com.liuzhenli.reader.view.ScaleTransitionPagerTitleView;
import com.micoredu.readerlib.analyzerule.AnalyzeRule;
import com.micoredu.readerlib.bean.BookCategoryBean;
import com.micoredu.readerlib.bean.BookSourceBean;
import com.microedu.reader.R;
import com.microedu.reader.databinding.ActBooklistBinding;

import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptException;
import javax.script.SimpleBindings;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT;
import static com.liuzhenli.common.constant.AppConstant.SCRIPT_ENGINE;

/**
 * Description:书源站点 分类(左侧)+书籍(右侧)
 *
 * @author liuzhenli 2020/10/9
 * Email: 848808263@qq.com
 */
public class BookListActivity extends BaseActivity {
    public static final String BOOK_SOURCE_DATA = "book_source_data";

    private BookSourceBean mBookSource;
    private final ArrayList<BookCategoryBean> mBookCategory = new ArrayList<>();
    protected List<BaseFragment> mFragmentList = new ArrayList<>();
    private ActBooklistBinding binding;

    public static void start(Context context, BookSourceBean data) {
        Intent intent = new Intent(context, BookListActivity.class);
        intent.putExtra(BOOK_SOURCE_DATA, data);
        context.startActivity(intent);
    }

    @Override
    protected View bindContentView() {
        binding = ActBooklistBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    protected void initToolBar() {
        if (mBookSource != null) {
            mTvTitle.setText(mBookSource.getBookSourceName());
        }
    }

    @Override
    protected void initData() {
        mBookSource = (BookSourceBean) getIntent().getSerializableExtra(BOOK_SOURCE_DATA);


        String ruleFindUrl = mBookSource.getRuleFindUrl();
        if (TextUtils.isEmpty(ruleFindUrl)) {
            return;
        }
        if (ruleFindUrl.startsWith("<js>")) {
            try {
                String jsStr = ruleFindUrl.substring(4, ruleFindUrl.lastIndexOf("<"));
                SimpleBindings bindings = new SimpleBindings();
                bindings.put("java", new AnalyzeRule(null));
                bindings.put("baseUrl", ruleFindUrl);
                Object object = SCRIPT_ENGINE.eval(jsStr, bindings);
                ruleFindUrl = object.toString();

            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }
        //一个类目
        String[] findItem = ruleFindUrl.split("(&&|\n)+");
        for (String s : findItem) {
            String[] categoryInfo = s.split("::");
            mBookCategory.add(new BookCategoryBean(categoryInfo[0], categoryInfo[1]));
            String url = categoryInfo[1];
            String tag = mBookSource.getBookSourceUrl();
            mFragmentList.add(BookCategoryFragment.getInstance(url, tag, mBookSource.getBookSourceName()));
        }
    }

    @Override
    protected void configViews() {
        binding.mViewPager.setOffscreenPageLimit(10);
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), BEHAVIOR_SET_USER_VISIBLE_HINT) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mBookCategory.get(position).name;
            }
        };
        binding.mViewPager.setAdapter(fragmentPagerAdapter);

        CommonNavigator commonNavigator7 = new CommonNavigator(mContext);
        //这个控制左右滑动的时候,选中文字的位置,0.5表示在中间
        commonNavigator7.setScrollPivotX(0.5f);
        CommonNavigatorAdapter mCommonNavigationAdapter = new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                ScaleTransitionPagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                simplePagerTitleView.setText(mBookCategory.get(index).name);
                simplePagerTitleView.setTextSize(20);
                simplePagerTitleView.setNormalColor(getResources().getColor(R.color.text_color_99));
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.text_color_66));
                simplePagerTitleView.setOnClickListener(v -> binding.mViewPager.setCurrentItem(index));
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 4));
                indicator.setLineWidth(UIUtil.dip2px(context, 10));
                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(getResources().getColor(R.color.main_blue));
                return indicator;
            }
        };
        commonNavigator7.setAdapter(mCommonNavigationAdapter);
        binding.mIndicator.setNavigator(commonNavigator7);
        ViewPagerHelper.bind(binding.mIndicator, binding.mViewPager);
    }

}
