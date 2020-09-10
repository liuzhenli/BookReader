package com.liuzhenli.reader.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.google.android.material.tabs.TabLayout;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.base.BaseActivity;
import com.liuzhenli.reader.observer.SampleProgressObserver;
import com.liuzhenli.reader.ui.adapter.MainTabAdapter;
import com.liuzhenli.reader.utils.Constant;
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
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawLayout;
    @BindView(R.id.material_menu)
    MaterialMenuView mMaterialMenu;
    @BindView(R.id.cl_main_left)
    ViewGroup mDrawLeft;

    @BindView(R.id.view_pager_main)
    NoAnimViewPager mViewPager;
    @BindView(R.id.tab_layout_main)
    TabLayout mTabLayout;
    @BindView(R.id.tv_main_draw_setting)
    View mViewSetting;
    @BindView(R.id.tv_main_feedback)
    View mViewFeedBack;
    @BindView(R.id.tv_qq_group)
    View mViewJoinQQGroup;
    /***关于**/
    @BindView(R.id.tv_main_draw_about)
    View mViewAbout;

    private int mCurrentPosition;

    @OnClick(R.id.tv_night_mode)
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
        //打开左侧的设置页面
        ClickUtils.click(mMaterialMenu, o -> {
            if (!mDrawLayout.isDrawerOpen(mDrawLeft)) {
                mDrawLayout.openDrawer(Gravity.LEFT, true);
            } else {
                mDrawLayout.closeDrawer(mDrawLeft);
            }

        });

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

        mDrawLeft.setOnClickListener(null);

        //意见反馈
        ClickUtils.click(mViewFeedBack, o -> {
            WebViewActivity.start(mContext, Constant.FEEDBACK);
        });
        //加qq群
        ClickUtils.click(mViewJoinQQGroup, o -> {
            joinQQGroup(Constant.QQGroup.QQ_1140723995);
        });

        //drawableLayout 滑动监听
        mDrawLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                mMaterialMenu.setTransformationOffset(MaterialMenuDrawable.AnimationState.BURGER_ARROW, 2 - slideOffset);
            }
        });

        //关于
        ClickUtils.click(mViewAbout, o -> {
            AboutActivity.start(mContext);
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

    /****************
     *
     * 发起添加群流程。群号：阅读①(1140723995) 的 key 为： py5-vU4j3y7mobTS3IkZMKKJAFbiKRgl
     * 调用 joinQQGroup(py5-vU4j3y7mobTS3IkZMKKJAFbiKRgl) 即可发起手Q客户端申请加群 阅读①(1140723995)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回false表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }


}
