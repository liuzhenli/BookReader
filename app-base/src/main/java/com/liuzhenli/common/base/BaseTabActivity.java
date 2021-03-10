package com.liuzhenli.common.base;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.liuzhenli.common.R;
import com.liuzhenli.common.utils.UiUtils;


import java.util.List;

/**
 * describe: base TabActivity
 *
 * @author Liuzhenli on 2019-10-18 15:44
 */
public abstract class BaseTabActivity<T extends BaseContract.BasePresenter> extends BaseActivity<T> implements ViewPager.OnPageChangeListener {
    protected TabLayout mTabLayout;
    protected ViewPager mVp;
    protected TabFragmentPageAdapter tabFragmentPageAdapter;
    protected List<Fragment> mFragmentList;
    private List<String> mTitleList;

    private int mCurrentPage = 0;

    protected abstract List<Fragment> createTabFragments();

    protected abstract List<String> createTabTitles();

    @Override
    protected void configViews() {
        mTabLayout = findViewById(R.id.tab_tl_indicator);
        mVp = findViewById(R.id.tab_vp);
        setUpTabLayout();
        mVp.addOnPageChangeListener(this);
    }

    /***当前页的position*/
    protected int getCurrentPagePosition() {
        return mCurrentPage;
    }

    private void setUpTabLayout() {
        mFragmentList = createTabFragments();
        mTitleList = createTabTitles();
        checkParamsIsRight();
        tabFragmentPageAdapter = new TabFragmentPageAdapter(getSupportFragmentManager());
        mVp.setAdapter(tabFragmentPageAdapter);
        mVp.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mVp);
        //设置指示器宽度和文字同宽
        UiUtils.INSTANCE.setTabLayoutWidth(mTabLayout);
    }

    private void checkParamsIsRight() {
        if (mFragmentList == null || mTitleList == null) {
            throw new IllegalArgumentException("fragmentList or titleList doesn't have null");
        }

        if (mFragmentList.size() != mTitleList.size()) {
            throw new IllegalArgumentException("fragment and title size must equal");
        }
    }

    public class TabFragmentPageAdapter extends FragmentPagerAdapter {

        TabFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPage = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
