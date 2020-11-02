package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.liuzhenli.reader.utils.ToastUtil;
import com.micoredu.readerlib.bean.SearchBookBean;
import com.micoredu.readerlib.bean.SearchHistoryBean;
import com.micoredu.readerlib.helper.DbHelper;
import com.microedu.reader.R;

import java.util.ArrayList;
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
    ImageView btnGeneralSearchClear;
    @BindView(R.id.tv_action_cancel)
    TextView tvActionCancel;
    @BindView(R.id.tv_general_search_history_title)
    TextView tvGeneralSearchHistoryTitle;
    @BindView(R.id.iv_clear_search_history)
    ImageView ivClearSearchHistory;
    @BindView(R.id.block_general_search_history_title)
    LinearLayout blockGeneralSearchHistoryTitle;
    @BindView(R.id.fl_general_search_history)
    FlowLayout flGeneralSearchHistory;
    @BindView(R.id.iv_back)
    View mViewBack;
    @BindView(R.id.view_search_history)
    View mViewSearchHistory;

    private List<SearchBookBean> bookList = new ArrayList<>();

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
        initAdapter(BookListAdapter.class, false, false);
        mRecyclerView.setVisibility(View.GONE);
        mViewBack.setOnClickListener(v -> finish());
        mPresenter.getSearchHistory();
        ClickUtils.click(tvActionCancel, o -> {
            String s = mEtSearch.getText().toString();
            if (TextUtils.isEmpty(s)) {
                ToastUtil.showCenter("请你输入搜索内容");
                return;
            }
            mPresenter.addToSearchHistory(SearchType.BOOK, s);
            mPresenter.stopSearch();
            bookList.clear();
            mPresenter.search(SearchType.BOOK, 0, s);
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
                    mPresenter.stopSearch();
                    mAdapter.clear();
                    mRecyclerView.setVisibility(View.GONE);
                    mViewSearchHistory.setVisibility(View.VISIBLE);
                }
            }
        });

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
            for (int i = 0; i < data.size(); i++) {
                TextView tvSearch = new TextView(mContext);
                tvSearch.setTextSize(12);
                tvSearch.setTextColor(getResources().getColor(R.color.text_color_99));
                tvSearch.setText(data.get(i).getContent());
                ClickUtils.click(tvSearch, o -> {
                    mEtSearch.setText(tvSearch.getText());
                    mEtSearch.setSelection(tvSearch.getText().length());
                    mPresenter.search(SearchType.BOOK, 0, tvSearch.getText().toString());
                });
                flGeneralSearchHistory.addView(tvSearch);
            }
        }
    }


    @Override
    public void showSearchResult(String key, List<SearchBookBean> searchResult) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mViewSearchHistory.setVisibility(View.GONE);
        if (mAdapter.getCount() == 0) {
            mAdapter.addAll(searchResult);
            return;
        }
        bookList.clear();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (searchResult == null || searchResult.size() == 0) {
                    return;
                }
                //添加数据
                ArrayList<SearchBookBean> allBooks = new ArrayList<>(mAdapter.getRealAllData());
                //搜索结果存入数据库
                DbHelper.getDaoSession().getSearchBookBeanDao().insertOrReplaceInTx(searchResult);

                //去重
                for (int i = 0; i < searchResult.size(); i++) {
                    boolean hasSameBook = false;
                    for (int j = 0; j < allBooks.size(); j++) {
                        //作者名和书名都相同,视为同一本书  同一本书的书源+1
                        if (TextUtils.equals(allBooks.get(j).getName(), searchResult.get(i).getName())
                                && TextUtils.equals(allBooks.get(j).getAuthor(), searchResult.get(i).getAuthor())) {
                            allBooks.get(j).addOriginUrl(searchResult.get(i).getTag());
                            hasSameBook = true;
                            break;
                        }
                    }

                    if (!hasSameBook) {
                        allBooks.add(searchResult.get(i));
                    }
                }


                //处理数据的逻辑:1.书名和搜索的词语相同的放最上面  2.作者和搜索词相同的其次  3.标签和搜索词相同
                if (allBooks.size() > 0) {
                    for (int i = 0; i < allBooks.size(); i++) {
                        if (TextUtils.equals(allBooks.get(i).getName(), key)) {
                            bookList.add(allBooks.get(i));
                            allBooks.remove(i);
                            i--;
                        }
                    }
                    for (int i = 0; i < allBooks.size(); i++) {
                        if (TextUtils.equals(allBooks.get(i).getAuthor(), key)) {
                            bookList.add(allBooks.get(i));
                            allBooks.remove(i);
                            i--;
                        }
                    }
                    bookList.addAll(allBooks);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mAdapter.getCount() != 0) {
                            mAdapter.clear();
                        }
                        mAdapter.addAll(bookList);
                        Log.e("111111", "booksize=" + bookList.size());
                        mAdapter.notifyDataSetChanged();

                    }
                });
            }
        };
        runnable.run();
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
}
