/*
package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.liuzhenli.common.BitIntentDataManager;
import com.liuzhenli.common.constant.AppConstant;
import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.common.base.BaseRvActivity;
import com.liuzhenli.reader.DaggerReadBookComponent;
import com.liuzhenli.reader.ui.adapter.BookListAdapter;
import com.liuzhenli.reader.ui.contract.SearchContract;
import com.liuzhenli.reader.ui.presenter.SearchPresenter;
import com.liuzhenli.common.utils.DensityUtil;
import com.liuzhenli.common.utils.SoftInputUtils;
import com.micoredu.reader.bean.SearchBook;
import com.micoredu.reader.ui.activity.BookSourceActivity;
import com.liuzhenli.common.widget.DialogUtil;
import com.micoredu.reader.R;
import com.micoredu.reader.databinding.ActivitySearchBinding;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;

import java.util.Arrays;
import java.util.List;

import static com.liuzhenli.common.BitIntentDataManager.DATA_KEY;

*/
/**
 * Description:
 *
 * @author liuzhenli 2020/8/10
 * Email: 848808263@qq.com
 *//*

public class SearchActivity extends BaseRvActivity<SearchPresenter, SearchBook, ActivitySearchBinding> implements SearchContract.View {


    public interface SearchType {
        int BOOK = 0;
    }

    private String mCurrentSearchKey;
    private QMUIPopup mMenu;

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected ActivitySearchBinding inflateView(LayoutInflater inflater) {
        return ActivitySearchBinding.inflate(inflater);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerReadBookComponent.builder().build().inject(this);
    }

    @Override
    protected void initToolBar() {
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void configViews() {
        binding.mEditBg.setRadius(DensityUtil.dip2px(mContext, 6));
        initAdapter(BookListAdapter.class, false, false);
        binding.mViewBack.setOnClickListener(v -> onBackPressed());
        mPresenter.getSearchHistory();
        binding.btnGeneralSearchClear.setVisibility(View.GONE);

        initMenu();
        //click search button
        ClickUtils.click(binding.tvActionSearch, o -> {
            mPresenter.checkBookSource();
        });

        ClickUtils.click(binding.ivClearSearchHistory, o -> {
            mPresenter.clearSearchHistory();
        });
        binding.mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    binding.btnGeneralSearchClear.setVisibility(View.GONE);
                    stopSearch();
                } else {
                    binding.btnGeneralSearchClear.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mPresenter.checkBookSource();
                }
                return false;
            }
        });
        //click stop search button
        ClickUtils.click(binding.mVStopSearch, o -> {
            if (mAdapter.getCount() == 0) {
                stopSearch();
            } else {
                mPresenter.stopSearch();
                binding.mSearchIndicator.setVisibility(View.GONE);
                binding.mVStopSearch.setVisibility(View.GONE);
            }
            binding.mViewTitleBar.setVisibility(View.VISIBLE);
        });

        //click delete search content
        ClickUtils.click(binding.btnGeneralSearchClear, o -> {
            binding.mEtSearch.setText("");
        });
        ClickUtils.click(binding.mViewMore, o -> {
            mMenu.show(binding.mViewMore);
        });
        binding.mViewGroupSearchResult.setVisibility(View.GONE);

        binding.mEtSearch.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.mEtSearch.requestFocus();
                SoftInputUtils.showSoftInput(mContext, binding.mEtSearch);
            }
        }, 500);
    }

    @Override
    public void onItemClick(int position) {
        SearchBook item = mAdapter.getItem(position);
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
        binding.flGeneralSearchHistory.removeAllViews();
        if (data != null) {
            binding.mViewSearchHistory.setVisibility(View.VISIBLE);
            for (int i = 0; i < data.size(); i++) {
                TextView tvSearch = new TextView(mContext);
                tvSearch.setTextSize(12);
                tvSearch.setTextColor(getResources().getColor(R.color.text_color_99));
                tvSearch.setText(data.get(i).getContent());
                tvSearch.setTag(data.get(i));
                ClickUtils.click(tvSearch, o -> {
                    binding.mEtSearch.setText(tvSearch.getText());
                    binding.mEtSearch.setSelection(tvSearch.getText().length());
                });

                ClickUtils.longClick(tvSearch, o -> {
                    binding.flGeneralSearchHistory.removeView(tvSearch);
                    mPresenter.removeSearchHistoryItem((SearchHistoryBean) tvSearch.getTag());
                });
                binding.flGeneralSearchHistory.addView(tvSearch);
            }
        }
    }


    @Override
    public void showSearchResult(String key, List<SearchBook> searchResult) {
        binding.mTvSearchBookCount.setText(String.format("找到%s部相关书籍", searchResult.size()));
        mRecyclerView.setVisibility(View.VISIBLE);
        binding.mViewGroupSearchResult.setVisibility(View.VISIBLE);
        binding.mViewTitleBar.setVisibility(View.GONE);
        binding.mViewSearchHistory.setVisibility(View.GONE);
        if (mAdapter.getCount() == 0) {
            mAdapter.addAll(searchResult);
        } else {
            mAdapter.setRealAllData(searchResult);
        }
    }

    @Override
    public void showCheckBookSourceResult(boolean noSourceAvailable) {
        if (noSourceAvailable) {
            showEmptyBookSourceDialog();
        } else {
            startSearch(binding.mEtSearch.getText().toString());
        }

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


    private void startSearch(String searchKey) {
        if (TextUtils.isEmpty(searchKey)) {
            toast("需要输入关键词");
            return;
        }
        //如果当前搜索词相同,不需要重新搜索
        if (TextUtils.equals(mCurrentSearchKey, searchKey) && mPresenter.isSearching) {
            return;
        }
        SoftInputUtils.hideSoftInput(mContext, binding.mEtSearch);
        mCurrentSearchKey = searchKey;
        mPresenter.addToSearchHistory(SearchType.BOOK, searchKey);

        mPresenter.stopSearch();
        if (mAdapter.getCount() > 0) {
            mAdapter.clear();
        }
        mPresenter.search(SearchType.BOOK, 0, searchKey, mAdapter);
        binding.mViewGroupSearchResult.setVisibility(View.VISIBLE);
        binding.mViewTitleBar.setVisibility(View.GONE);
        binding.mSearchIndicator.setVisibility(View.VISIBLE);
        binding.mVStopSearch.setVisibility(View.VISIBLE);
        binding.mViewSearchHistory.setVisibility(View.GONE);
        binding.mTvSearchBookCount.setText(String.format("正在搜\"%s\"", mCurrentSearchKey));
    }

    private void stopSearch() {
        mPresenter.stopSearch();
        mAdapter.clear();
        mRecyclerView.setVisibility(View.GONE);
        binding.mViewGroupSearchResult.setVisibility(View.GONE);
        binding.mViewTitleBar.setVisibility(View.VISIBLE);
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
                        switch (position) {
                            case 0:
                                BookSourceActivity.start(mContext);
                                break;
                            default:
                                break;
                        }
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

    */
/**
     * if no bookSource available, show
     *//*

    private void showEmptyBookSourceDialog() {
        DialogUtil.showOneButtonDialog(mContext, getResources().getString(R.string.dialog_title), getResources().getString(R.string.string_book_source_is_empty), new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.dismiss();
            }
        });
    }
}
*/
