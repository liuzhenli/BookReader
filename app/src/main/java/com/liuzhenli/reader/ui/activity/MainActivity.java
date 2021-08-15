package com.liuzhenli.reader.ui.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.google.android.material.tabs.TabLayout;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.liuzhenli.common.constant.ARouterConstants;
import com.liuzhenli.common.constant.RxBusTag;
import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.common.utils.FillContentUtil;
import com.liuzhenli.common.base.BaseActivity;
import com.liuzhenli.reader.DaggerReadBookComponent;
import com.liuzhenli.reader.ui.adapter.MainTabAdapter;
import com.liuzhenli.common.utils.Constant;
import com.liuzhenli.reader.ui.contract.MainContract;
import com.liuzhenli.reader.ui.fragment.DiscoverFragment;
import com.liuzhenli.reader.ui.presenter.MainPresenter;
import com.liuzhenli.reader.utils.JumpToLastPageUtil;
import com.micoredu.reader.bean.BookSourceBean;
import com.micoredu.reader.ui.activity.BookSourceActivity;
import com.liuzhenli.reader.utils.storage.Backup;
import com.microedu.reader.R;
import com.microedu.reader.databinding.ActivityMainContainerBinding;

/**
 * @author liuzhenli
 */
@Route(path = ARouterConstants.ACT_MAIN)
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {
    private int mCurrentPosition;
    private MainTabAdapter mainTabAdapter;
    /***发现页面的书源名字*/
    private String mDiscoverBookSourceName;
    private ActivityMainContainerBinding inflate;

    @Override
    protected View bindContentView() {
        inflate = ActivityMainContainerBinding.inflate(getLayoutInflater());
        return inflate.getRoot();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerReadBookComponent.builder().build().inject(this);
    }

    @Override
    public void initToolBar() {
        mToolBar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_more));
        FillContentUtil.setText(mTvTitle, getResources().getStringArray(R.array.main_tab_names)[mCurrentPosition]);
        mTvTitle.requestFocus();
        mTvTitle.setOnClickListener(v -> {
            if (mCurrentPosition == 1) {
                DiscoverFragment fragment = (DiscoverFragment) mainTabAdapter.getItem(mCurrentPosition);
                fragment.showBookSourceView();
            }
        });
    }

    @Override
    public void initData() {
        requestPermissions();
    }

    @Override
    public void configViews() {
        setMenu();
        mainTabAdapter = new MainTabAdapter(mContext, getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        inflate.viewMain.mViewPager.setAdapter(mainTabAdapter);
        inflate.viewMain.mTabLayout.setupWithViewPager(inflate.viewMain.mViewPager);
        //打开左侧的设置页面
        ClickUtils.click(inflate.mMaterialMenu, o -> {
            if (!inflate.mDrawLayout.isDrawerOpen(inflate.viewMainLeft.mDrawLeft)) {
                inflate.mDrawLayout.openDrawer(Gravity.LEFT, true);
            } else {
                inflate.mDrawLayout.closeDrawer(inflate.viewMainLeft.mDrawLeft);
            }

        });

        for (int i = 0; i < inflate.viewMain.mTabLayout.getTabCount(); i++) {
            if (inflate.viewMain.mTabLayout.getTabAt(i) != null) {
                inflate.viewMain.mTabLayout.getTabAt(i).setCustomView(mainTabAdapter.getTabView(i));
            }
        }
        inflate.viewMain.mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mCurrentPosition = tab.getPosition();
                if (mCurrentPosition == 1 && !TextUtils.isEmpty(mDiscoverBookSourceName)) {
                    FillContentUtil.setTextDrawable(mTvTitle, mDiscoverBookSourceName, getResources()
                            .getDrawable(R.drawable.ic_down_filled), FillContentUtil.Place.right);
                } else {
                    FillContentUtil.setText(mTvTitle, mainTabAdapter.getPageTitle(mCurrentPosition));
                }
                setMenu();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        inflate.viewMainLeft.mDrawLeft.setOnClickListener(null);

        //feedback
        ClickUtils.click(inflate.viewMainLeft.mViewFeedBack, o -> {
            WebViewActivity.start(mContext, Constant.FEEDBACK);
        });

        //manage book source
        ClickUtils.click(inflate.viewMainLeft.mViewBookSourceManager, o -> {
            BookSourceActivity.start(mContext);
        });

        //drawableLayout on draw listener
        inflate.mDrawLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                inflate.mMaterialMenu.setTransformationOffset(MaterialMenuDrawable.AnimationState.BURGER_ARROW, 2 - slideOffset);
            }
        });
        //setting
        ClickUtils.click(inflate.viewMainLeft.mViewSetting, o -> {
            SettingActivity.start(mContext);
        });
        //about page
        ClickUtils.click(inflate.viewMainLeft.mViewAbout, o -> {
            AboutActivity.start(mContext);
        });
        //donate
        ClickUtils.click(inflate.viewMainLeft.mViewDonate, o -> {
        });
        inflate.viewMainLeft.mViewDonate.setVisibility(View.GONE);
        inflate.viewMain.mViewPager.setOffscreenPageLimit(4);
        inflate.viewMainLeft.mViewNightMode.setVisibility(View.GONE);

        JumpToLastPageUtil.openLastPage(mContext);

        mPresenter.checkWebDav();
    }

    private void setMenu() {
        mToolBar.getMenu().clear();
        switch (mCurrentPosition) {
            case 0:
                mToolBar.inflateMenu(R.menu.menu_main);
                mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_search:
                                SearchActivity.start(mContext);
                                break;
                            case R.id.item_add_from_local:
                                ImportLocalBookActivity.start(mContext);
                                break;
                            case R.id.item_arrange_bookshelf:
                                ImportLocalBookActivity.start(mContext);
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                break;
            case 1:
                mToolBar.inflateMenu(R.menu.menu_discover);
                mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_search:
                                SearchActivity.start(mContext);
                                break;
                            case R.id.item_arrange_book_source:
                                BookSourceActivity.start(mContext);
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                break;
            default:
                break;
        }

    }

    private void requestPermissions() {
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(RxBusTag.CHANGE_DISCOVER_BOOK_SOURCE)})
    public void onBookSourceChange(BookSourceBean book) {
        if (book != null) {
            mDiscoverBookSourceName = book.getBookSourceName();
        }
        if (mCurrentPosition == 1) {
            FillContentUtil.setTextDrawable(mTvTitle, mDiscoverBookSourceName, getResources().getDrawable(R.drawable.ic_down_filled), FillContentUtil.Place.right);
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(RxBusTag.RECREATE)})
    public void onRecreateEvent(Boolean recreate) {
        recreate();
    }

    @Override
    public void onBackPressed() {
        if (mCurrentPosition == 1) {
            DiscoverFragment fragment = (DiscoverFragment) mainTabAdapter.getItem(mCurrentPosition);
            if (fragment.isShowBookSourceMenu()) {
                fragment.dismissBookSourceMenu();
            } else {
                moveTaskToBack(true);
                //super.onBackPressed();
            }
        } else {
            moveTaskToBack(true);
            //super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Backup.INSTANCE.autoBack();
    }

    @Override
    public void showWebDavResult(boolean isSet) {
        Log.e(TAG, "web dav ---: " + isSet);
    }

    @Override
    public void showError(Exception e) {

    }

    @Override
    public void complete() {

    }
}
