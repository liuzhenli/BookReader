package com.liuzhenli.reader.ui.activity;

import android.Manifest;
import android.content.Intent;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.base.BaseActivity;
import com.liuzhenli.reader.observer.SampleProgressObserver;
import com.liuzhenli.reader.ui.adapter.MainTabAdapter;
import com.liuzhenli.reader.utils.PermissionUtil;
import com.liuzhenli.reader.utils.ToastUtil;
import com.liuzhenli.reader.view.NoAnimViewPager;
import com.microedu.reader.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author liuzhenli
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.view_pager_main)
    NoAnimViewPager mViewPager;
    @BindView(R.id.tab_layout_main)
    TabLayout mTabLayout;
    private int mCurrentPosition;

    @OnClick(R.id.tv_login_main)
    public void login() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_login_tab)
    public void tabTest() {
        Intent intent = new Intent(this, TabActivity.class);
        startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main_container;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initToolBar() {
        mTvTitle.setText(getResources().getStringArray(R.array.main_tab_names)[mCurrentPosition]);
    }

    @Override
    public void initData() {
        requestPermissions();
    }

    @Override
    public void configViews() {
        MainTabAdapter mainTabAdapter = new MainTabAdapter(mContext, getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(mainTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            mTabLayout.getTabAt(i).setCustomView(mainTabAdapter.getTabView(i));
        }
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mCurrentPosition = tab.getPosition();
                mTvTitle.setText(mainTabAdapter.getPageTitle(mCurrentPosition));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void requestPermissions() {
        PermissionUtil.requestPermission(this, new SampleProgressObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {

                } else {
                    ToastUtil.showCenter("权限被拒绝,拒绝权限将无法使用***功能！");
                }
            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

}
