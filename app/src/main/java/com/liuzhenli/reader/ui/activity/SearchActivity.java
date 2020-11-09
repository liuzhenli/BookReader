package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.internal.FlowLayout;
import com.liuzhenli.common.BitIntentDataManager;
import com.liuzhenli.common.constant.AppConstant;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.reader.base.BaseRvActivity;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.ui.adapter.BookListAdapter;
import com.liuzhenli.reader.ui.contract.SearchContract;
import com.liuzhenli.reader.ui.presenter.SearchPresenter;
import com.liuzhenli.reader.utils.DensityUtil;
import com.liuzhenli.reader.utils.SoftInputUtils;
import com.micoredu.readerlib.bean.SearchBookBean;
import com.micoredu.readerlib.bean.SearchHistoryBean;
import com.microedu.reader.R;
import com.qmuiteam.qmui.layout.QMUIButton;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

import static com.liuzhenli.common.BitIntentDataManager.DATA_KEY;

/**
 * Description:
 *
 * @author liuzhenli 2020/8/10
 * Email: 848808263@qq.com
 */
public class SearchActivity extends BaseRvActivity<SearchPresenter, SearchBookBean> implements SearchContract.View {


    public interface SearchType {
        int BOOK = 0;
    }

    @BindView(R.id.edit_general_search_content)
    EditText mEtSearch;
    @BindView(R.id.btn_general_search_clear)
    View btnGeneralSearchClear;
    @BindView(R.id.tv_action_cancel)
    TextView tvActionSearch;
    @BindView(R.id.tv_general_search_history_title)
    TextView tvGeneralSearchHistoryTitle;
    @BindView(R.id.iv_clear_search_history)
    ImageView ivClearSearchHistory;
    @BindView(R.id.fl_general_search_history)
    FlowLayout flGeneralSearchHistory;
    @BindView(R.id.iv_back)
    View mViewBack;
    @BindView(R.id.view_search_history)
    View mViewSearchHistory;
    /*** search book result view group*/
    @BindView(R.id.view_search_result_info)
    View mViewGroupSearchResult;
    @BindView(R.id.view_search_title_bar)
    View mVtitleBar;
    @BindView(R.id.view_stop_search)
    View mVStopSearch;
    @BindView(R.id.view_search_indicator)
    View mSearchIndicator;
    @BindView(R.id.tv_search_book_count)
    TextView mTvSearchBookCount;
    @BindView(R.id.view_search_bg)
    QMUIButton mEditBg;
    @BindView(R.id.btn_general_search_more)
    View mViewMore;
    private String mCurrentSearchKey;
    private QMUIPopup mMenu;

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    protected void initToolBar() {
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void configViews() {
        mEditBg.setRadius(DensityUtil.dip2px(mContext, 6));
        initAdapter(BookListAdapter.class, false, false);
        mViewBack.setOnClickListener(v -> finish());
        mPresenter.getSearchHistory();
        btnGeneralSearchClear.setVisibility(View.GONE);

        initMenu();
        //click search button
        ClickUtils.click(tvActionSearch, o -> {
            String s = mEtSearch.getText().toString();
            startSearch(s);
        });

        ClickUtils.click(ivClearSearchHistory, o -> {
            mPresenter.clearSearchHistory();
        });
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    btnGeneralSearchClear.setVisibility(View.GONE);
                    stopSearch();
                } else {
                    btnGeneralSearchClear.setVisibility(View.VISIBLE);
                }
            }
        });
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startSearch(mEtSearch.getText().toString());
                }
                return false;
            }
        });
        //click stop search button
        ClickUtils.click(mVStopSearch, o -> {
            if (mAdapter.getCount() == 0) {
                stopSearch();
            } else {
                mPresenter.stopSearch();
                mSearchIndicator.setVisibility(View.GONE);
                mVStopSearch.setVisibility(View.GONE);
            }
            mVtitleBar.setVisibility(View.VISIBLE);
        });

        //click delete search content
        ClickUtils.click(btnGeneralSearchClear, o -> {
            mEtSearch.setText("");
        });
        ClickUtils.click(mViewMore, o -> {
            mMenu.show(mViewMore);
        });
        mViewGroupSearchResult.setVisibility(View.GONE);

        mEtSearch.postDelayed(new Runnable() {
            @Override
            public void run() {
                mEtSearch.requestFocus();
                SoftInputUtils.showSoftInput(mContext, mEtSearch);
            }
        }, 500);
    }

    @Override
    public void onItemClick(int position) {
        SearchBookBean item = mAdapter.getItem(position);
        String dataKey = String.valueOf(System.currentTimeMillis());
        Intent intent = new Intent(mContext, BookDetailActivity.class);
        intent.putExtra(BookDetailActivity.OPEN_FROM, AppConstant.BookOpenFrom.OPEN_FROM_SEARCH);
        intent.putExtra(DATA_KEY, dataKey);
        BitIntentDataManager.getInstance().putData(dataKey, item);
        startActivityByAnim(intent, android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void addHistorySuccess() {
        mPresenter.getSearchHistory();
    }

    @Override
    public void showSearchHistory(List<SearchHistoryBean> data) {
        flGeneralSearchHistory.removeAllViews();
        if (data != null) {
            mViewSearchHistory.setVisibility(View.VISIBLE);
            for (int i = 0; i < data.size(); i++) {
                TextView tvSearch = new TextView(mContext);
                tvSearch.setTextSize(12);
                tvSearch.setTextColor(getResources().getColor(R.color.text_color_99));
                tvSearch.setText(data.get(i).getContent());
                tvSearch.setTag(data.get(i));
                ClickUtils.click(tvSearch, o -> {
                    mEtSearch.setText(tvSearch.getText());
                    mEtSearch.setSelection(tvSearch.getText().length());
                    startSearch(tvSearch.getText().toString());
                });

                ClickUtils.longClick(tvSearch, o -> {
                    flGeneralSearchHistory.removeView(tvSearch);
                    mPresenter.removeSearchHistoryItem((SearchHistoryBean) tvSearch.getTag());
                });
                flGeneralSearchHistory.addView(tvSearch);
            }
        }
    }


    @Override
    public void showSearchResult(String key, List<SearchBookBean> searchResult) {
        mTvSearchBookCount.setText(String.format("找到%s部相关书籍", searchResult.size()));
        mRecyclerView.setVisibility(View.VISIBLE);
        mViewGroupSearchResult.setVisibility(View.VISIBLE);
        mVtitleBar.setVisibility(View.GONE);
        mViewSearchHistory.setVisibility(View.GONE);
        if (mAdapter.getCount() == 0) {
            mAdapter.addAll(searchResult);
            return;
        }
        if (mAdapter.getCount() != 0) {
            mAdapter.clear();
        }
        mAdapter.addAll(searchResult);
    }

    @Override
    public void showError(Exception e) {

    }

    @Override
    public void complete() {

    }

    @Override
    public void onBackPressed() {
        mPresenter.stopSearch();
        super.onBackPressed();
    }

    @Override
    public void finish() {
        mPresenter.stopSearch();
        super.finish();
    }


    private void startSearch(String searchKey) {
        if (TextUtils.isEmpty(searchKey)) {
            return;
        }
        //如果当前搜索词相同,不需要重新搜索
        if (TextUtils.equals(mCurrentSearchKey, searchKey) && mPresenter.isSearching) {
            return;
        }
        SoftInputUtils.hideSoftInput(mContext, mEtSearch);
        mCurrentSearchKey = searchKey;
        mPresenter.addToSearchHistory(SearchType.BOOK, searchKey);

        mPresenter.stopSearch();
        if (mAdapter.getCount() > 0) {
            mAdapter.clear();
        }
        mPresenter.search(SearchType.BOOK, 0, searchKey, mAdapter);
        mViewGroupSearchResult.setVisibility(View.VISIBLE);
        mVtitleBar.setVisibility(View.GONE);
        mSearchIndicator.setVisibility(View.VISIBLE);
        mVStopSearch.setVisibility(View.VISIBLE);
        mViewSearchHistory.setVisibility(View.GONE);
        mTvSearchBookCount.setText(String.format("以\"%s\"为关键词进行搜索", mCurrentSearchKey));
    }

    private void stopSearch() {
        mPresenter.stopSearch();
        mAdapter.clear();
        mRecyclerView.setVisibility(View.GONE);
        mViewGroupSearchResult.setVisibility(View.GONE);
        mVtitleBar.setVisibility(View.VISIBLE);
        mPresenter.getSearchHistory();
    }


    private void initMenu() {
        List<String> data = Arrays.asList("书源管理", "全部书源");
        ArrayAdapter adapter = new ArrayAdapter(mContext, R.layout.item_menu_list, data);
        mMenu = QMUIPopups.listPopup(mContext,
                QMUIDisplayHelper.dp2px(mContext, 200),
                QMUIDisplayHelper.dp2px(mContext, 300),
                adapter,
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (mMenu != null) {
                            mMenu.dismiss();
                        }
                    }
                })
                .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                .preferredDirection(QMUIPopup.DIRECTION_TOP)
                .shadow(true)
                .offsetYIfTop(QMUIDisplayHelper.dp2px(mContext, 5))
                .skinManager(QMUISkinManager.defaultInstance(mContext))
                .onDismiss(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {

                    }
                });
    }
}
