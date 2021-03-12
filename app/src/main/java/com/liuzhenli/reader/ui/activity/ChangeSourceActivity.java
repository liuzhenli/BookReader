package com.liuzhenli.reader.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.liuzhenli.common.BitIntentDataManager;
import com.liuzhenli.common.constant.AppConstant;
import com.liuzhenli.common.constant.RxBusTag;
import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.common.utils.StringUtils;
import com.liuzhenli.greendao.SearchBookBeanDao;
import com.liuzhenli.common.base.BaseRvActivity;
import com.liuzhenli.reader.DaggerReadBookComponent;
import com.liuzhenli.reader.ui.adapter.ChangeSourceAdapter;
import com.liuzhenli.reader.ui.contract.ChangeSourceContract;
import com.liuzhenli.reader.ui.presenter.ChangeSourcePresenter;
import com.liuzhenli.reader.utils.DataDiffUtil;
import com.micoredu.reader.bean.BookShelfBean;
import com.micoredu.reader.bean.BookSourceBean;
import com.micoredu.reader.bean.SearchBookBean;
import com.micoredu.reader.helper.BookshelfHelper;
import com.micoredu.reader.helper.DbHelper;
import com.micoredu.reader.model.BookSourceManager;
import com.micoredu.reader.model.SearchBookModel;
import com.microedu.reader.databinding.ActChangesourceBinding;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.liuzhenli.common.BitIntentDataManager.DATA_KEY;

/**
 * 切换书源
 *
 * @author liuzhenli 2019.12.13
 */
public class ChangeSourceActivity extends BaseRvActivity<ChangeSourcePresenter, SearchBookBean> implements ChangeSourceContract.View {

    public static final String BOOK_SHELF = "book_shelf";
    private static Handler handler = new Handler(Looper.getMainLooper());

    private BookShelfBean mBookShelf;
    private String bookTag;
    private String bookName;
    private String bookAuthor;
    private int shelfLastChapter;
    private SearchBookModel searchBookModel;
    private ActChangesourceBinding binding;

    public static void startForResult(Activity context, BookShelfBean bookShelf, int requestCode) {
        Intent intent = new Intent(context, ChangeSourceActivity.class);
        String key = "changeSource" + System.currentTimeMillis();
        intent.putExtra(BOOK_SHELF, key);
        BitIntentDataManager.getInstance().putData(key, bookShelf);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected View bindContentView() {
        binding = ActChangesourceBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerReadBookComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mTvRight.post(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.setRefreshing(false);
                mPresenter.getSearchBookInDatabase(mBookShelf, searchBookModel);
            }
        });
    }

    @Override
    protected void initToolBar() {
        mTvRight.setText("停止搜索");
        mTvRight.setVisibility(View.VISIBLE);
        ClickUtils.click(mTvRight, o -> {
            binding.mSearchIndicator.setVisibility(View.GONE);
            mTvRight.setVisibility(View.GONE);
            searchBookModel.stopSearch();
        });
    }

    @Override
    protected void initData() {
        String key = getIntent().getStringExtra(BOOK_SHELF);
        mBookShelf = (BookShelfBean) BitIntentDataManager.getInstance().getData(key);
        bookTag = mBookShelf.getTag();
        bookName = mBookShelf.getBookInfoBean().getName();
        bookAuthor = mBookShelf.getBookInfoBean().getAuthor();
        shelfLastChapter = BookshelfHelper.guessChapterNum(mBookShelf.getLastChapterName());
        searchBookModel = new SearchBookModel(new SearchBookModel.OnSearchListener() {
            @Override
            public void refreshSearchBook() {
                binding.mSearchIndicator.setVisibility(View.VISIBLE);
                mTvRight.setVisibility(View.VISIBLE);
            }

            @Override
            public void refreshFinish(Boolean isAll) {
                binding.mSearchIndicator.setVisibility(View.GONE);
                mTvRight.setVisibility(View.GONE);
            }

            @Override
            public void loadMoreFinish(Boolean isAll) {
                binding.mSearchIndicator.setVisibility(View.GONE);
                mTvRight.setVisibility(View.GONE);
            }

            @Override
            public void loadMoreSearchBook(List<SearchBookBean> searchBookBeanList) {
                configSearchResult(searchBookBeanList);
            }

            @Override
            public void searchBookError(Throwable throwable) {
                binding.mSearchIndicator.setVisibility(View.GONE);
                mTvRight.setVisibility(View.GONE);
            }

            @Override
            public int getItemCount() {
                return 0;
            }
        });
        mTvTitle.setText(mBookShelf.getBookInfoBean().getName());

    }

    @Override
    protected void configViews() {
        initAdapter(ChangeSourceAdapter.class, true, false);
        binding.mEtBookName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.isTrimEmpty(s.toString())) {
                    List<SearchBookBean> searchBookBeans = DbHelper.getDaoSession().getSearchBookBeanDao().queryBuilder()
                            .where(SearchBookBeanDao.Properties.Name.eq(bookName), SearchBookBeanDao.Properties.Author.eq(bookAuthor))
                            .build().list();
                    mAdapter.clear();
                    mAdapter.addAll(searchBookBeans);
                } else {
                    List<SearchBookBean> searchBookBeans = DbHelper.getDaoSession().getSearchBookBeanDao().queryBuilder()
                            .where(SearchBookBeanDao.Properties.Name.eq(bookName), SearchBookBeanDao.Properties.Author.eq(bookAuthor),
                                    SearchBookBeanDao.Properties.Origin.like("%" + s.toString() + "%"))
                            .build().list();
                    mAdapter.clear();
                    mAdapter.addAll(searchBookBeans);
                }
            }
        });
        mPresenter.getSearchBookInDatabase(mBookShelf, searchBookModel);
    }

    @Override
    public void showError(Exception e) {

    }

    @Override
    public void complete() {

    }

    @Override
    public void onItemClick(int position) {
        SearchBookBean item = mAdapter.getItem(position);
        String dataKey = String.valueOf(System.currentTimeMillis());
        Intent intent = new Intent(mContext, BookDetailActivity.class);
        intent.putExtra(BookDetailActivity.OPEN_FROM, AppConstant.BookOpenFrom.OPEN_FROM_SEARCH);
        intent.putExtra(DATA_KEY, dataKey);
        BitIntentDataManager.getInstance().putData(dataKey, item);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void showSearchBook(List<SearchBookBean> books) {
        if (mAdapter.getCount() > 0) {
            DataDiffUtil.diffResult(mAdapter, books, new DataDiffUtil.ItemSameCallBack<SearchBookBean>() {
                @Override
                public boolean isItemSame(SearchBookBean oldItem, SearchBookBean newItem) {
                    return TextUtils.equals(oldItem.getName(), newItem.getName()) && TextUtils.equals(oldItem.getAuthor(), newItem.getAuthor());
                }

                @Override
                public boolean isContentSame(SearchBookBean oldItem, SearchBookBean newItem) {
                    return TextUtils.equals(oldItem.getNoteUrl(), newItem.getNoteUrl()) && oldItem.getIsCurrentSource() == newItem.getIsCurrentSource();
                }
            });
        } else {
            mAdapter.addAll(books);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (searchBookModel != null) {
            searchBookModel.onDestroy();
        }
    }

    private synchronized void configSearchResult(List<SearchBookBean> value) {
        if (value.size() > 0) {
            Collections.sort(value, (o1, o2) -> mPresenter.compareSearchBooks(o1, o2));
            for (SearchBookBean searchBookBean : value) {
                if (searchBookBean.getName().equals(bookName)
                        && (searchBookBean.getAuthor().equals(bookAuthor) || TextUtils.isEmpty(searchBookBean.getAuthor()) || TextUtils.isEmpty(bookAuthor))) {
                    if (searchBookBean.getTag().equals(bookTag)) {
                        searchBookBean.setIsCurrentSource(true);
                    } else {
                        searchBookBean.setIsCurrentSource(false);
                    }
                    boolean saveBookSource = false;
                    BookSourceBean bookSourceBean = BookSourceManager.getBookSourceByUrl(searchBookBean.getTag());
                    if (searchBookBean.getSearchTime() < 60 && bookSourceBean != null) {
                        bookSourceBean.increaseWeight(100 / (10 + searchBookBean.getSearchTime()));
                        saveBookSource = true;
                    }
                    if (shelfLastChapter > 0 && bookSourceBean != null) {
                        int lastChapter = BookshelfHelper.guessChapterNum(searchBookBean.getLastChapter());
                        if (lastChapter > shelfLastChapter) {
                            bookSourceBean.increaseWeight(100);
                            saveBookSource = true;
                        }
                    }
                    if (saveBookSource) {
                        DbHelper.getDaoSession().getBookSourceBeanDao().insertOrReplace(bookSourceBean);
                    }
                    DbHelper.getDaoSession().getSearchBookBeanDao().insertOrReplace(searchBookBean);
                    if (StringUtils.isTrimEmpty(binding.mEtBookName.getText().toString()) || searchBookBean.getOrigin().equals(binding.mEtBookName.getText().toString())) {
                        handler.post(() -> mAdapter.add(searchBookBean));
                    }
                    break;
                }
            }
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(RxBusTag.UP_SEARCH_BOOK)})
    public void upSearchBook(SearchBookBean searchBookBean) {
        if (!Objects.equals(mBookShelf.getBookInfoBean().getName(), searchBookBean.getName())
                || !Objects.equals(mBookShelf.getBookInfoBean().getAuthor(), searchBookBean.getAuthor())) {
            return;
        }
        for (int i = 0; i < mAdapter.getRealAllData().size(); i++) {
            if (mAdapter.getRealAllData().get(i).getTag().equals(searchBookBean.getTag())
                    && !mAdapter.getRealAllData().get(i).getLastChapter().equals(searchBookBean.getLastChapter())) {
                mAdapter.getRealAllData().get(i).setLastChapter(searchBookBean.getLastChapter());
                mAdapter.notifyItemChanged(i);
            }
        }
    }
}
