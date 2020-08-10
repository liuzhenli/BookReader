package com.liuzhenli.reader.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import com.liuzhenli.reader.ReaderApplication;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.base.rxlife.RxFragment;
import com.liuzhenli.reader.view.loading.CustomDialog;
import com.microedu.reader.R;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * @author Liuzhenli
 * @since 2019-07-06 17:18
 */
public abstract class BaseFragment<T extends BaseContract.BasePresenter> extends RxFragment {

    protected View parentView;
    protected FragmentActivity activity;
    protected LayoutInflater inflater;
    @Inject
    protected T mPresenter;

    protected Context mContext;
    public boolean isLoadFinish;
    public Toolbar mCommonToolbar;

    private CustomDialog dialog;
    public String TAG;

    @LayoutRes
    public abstract int getLayoutResId();

    protected abstract void setupActivityComponent(AppComponent appComponent);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        parentView = inflater.inflate(getLayoutResId(), container, false);
        activity = getSupportActivity();
        mContext = activity;
        TAG = activity.getClass().getName();
        isLoadFinish = false;
        this.inflater = inflater;
        return parentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        initVariable();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mCommonToolbar = view.findViewById(R.id.toolbar);
        if (mCommonToolbar != null) {
            initToolBar();
            if (activity instanceof AppCompatActivity) {
                AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
//                appCompatActivity.setSupportActionBar(mCommonToolbar);
//                appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

            }
        }
        setupActivityComponent(ReaderApplication.getInstance().getAppComponent());
        attachView();
        attachPresenter();
        initData();
        configViews();

        if (!hasCreateView && getUserVisibleHint()) {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
        }
    }

    private void attachPresenter() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    private void initToolBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            mCommonToolbar.getLayoutParams().height = getAppBarHeight();
//            mToolbar.setPadding(mToolbar.getPaddingLeft(),
//                    getStatusBarHeight(),
//                    mToolbar.getPaddingRight(),
//                    mToolbar.getPaddingBottom());
        }
    }

    public abstract void attachView();

    public abstract void initData();

    /**
     * 对各种控件进行设置、适配、填充数据
     */
    public abstract void configViews();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (FragmentActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.activity = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public FragmentActivity getSupportActivity() {
        return (FragmentActivity) super.getActivity();
    }

    public Context getApplicationContext() {
        return this.activity == null ? (getActivity() == null ? null : getActivity()
                .getApplicationContext()) : this.activity.getApplicationContext();
    }

//    protected LayoutInflater getLayoutInflater() {
//        return inflater;
//    }

    protected View getParentView() {
        return parentView;
    }

    public CustomDialog getDialog() {
        if (dialog == null) {
            dialog = new CustomDialog(mContext).instance(getActivity());
//            dialog.setCancelable(false);
        }
        return dialog;
    }

    public void hideDialog() {
        dismissDialog();
    }

    public void showDialog() {
        getDialog().show();
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    protected void gone(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    protected void visible(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    protected boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }


    /**
     * rootView是否初始化标志，防止回调函数在rootView为空的时候触发
     */
    private boolean hasCreateView;

    /**
     * 当前Fragment是否处于可见状态标志，防止因ViewPager的缓存机制而导致回调函数的触发
     */
    private boolean isFragmentVisible;

    /**
     * onCreateView()里返回的view，修饰为protected,所以子类继承该类时，在onCreateView里必须对该变量进行初始化
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (parentView == null) {
            return;
        }
        hasCreateView = true;
        if (isVisibleToUser) {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
            return;
        }
        if (isFragmentVisible) {
            onFragmentVisibleChange(false);
            isFragmentVisible = false;
        }
    }


    private void initVariable() {
        hasCreateView = false;
        isFragmentVisible = false;
    }

    protected void onFragmentVisibleChange(boolean isVisible) {

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
