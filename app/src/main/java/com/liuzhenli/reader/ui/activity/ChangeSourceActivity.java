//package com.liuzhenli.reader.ui.activity;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Handler;
//import android.os.Looper;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.View;
//
//import com.hwangjr.rxbus.annotation.Subscribe;
//import com.hwangjr.rxbus.annotation.Tag;
//import com.hwangjr.rxbus.thread.EventThread;
//import com.liuzhenli.common.BitIntentDataManager;
//import com.liuzhenli.common.constant.AppConstant;
//import com.liuzhenli.common.constant.RxBusTag;
//import com.liuzhenli.common.AppComponent;
//import com.liuzhenli.common.utils.ClickUtils;
//import com.liuzhenli.common.utils.StringUtils;
//import com.liuzhenli.common.base.BaseRvActivity;
//import com.liuzhenli.reader.DaggerReadBookComponent;
//import com.liuzhenli.reader.ui.adapter.ChangeSourceAdapter;
//import com.liuzhenli.reader.ui.contract.ChangeSourceContract;
//import com.liuzhenli.reader.ui.presenter.ChangeSourcePresenter;
//import com.liuzhenli.common.utils.DataDiffUtil;
//import com.micoredu.reader.bean.Book;
//import com.micoredu.reader.bean.SearchBook;
//import com.micoredu.reader.bean.SearchScope;
//import com.micoredu.reader.dao.AppDatabase;
//import com.micoredu.reader.databinding.ActChangesourceBinding;
//import com.micoredu.reader.help.book.BookHelp;
//import com.micoredu.reader.model.webBook.SearchModel;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Objects;
//
//import static com.liuzhenli.common.BitIntentDataManager.DATA_KEY;
//
//import androidx.annotation.NonNull;
//
//import kotlin.coroutines.CoroutineContext;
//import kotlinx.coroutines.CoroutineScope;
//import kotlinx.coroutines.Dispatchers;
//
///**
// * 切换书源
// *
// * @author liuzhenli 2019.12.13
// */
//public class ChangeSourceActivity extends BaseRvActivity<ChangeSourcePresenter, SearchBook, ActChangesourceBinding> implements ChangeSourceContract.View {
//
//    public static final String BOOK_SHELF = "book_shelf";
//    private static Handler handler = new Handler(Looper.getMainLooper());
//
//    private Book mBookShelf;
//    private String bookTag;
//    private String bookName;
//    private String bookAuthor;
//    private int shelfLastChapter;
//    private SearchModel searchBookModel;
//
//    public static void startForResult(Activity context, Book bookShelf, int requestCode) {
//        Intent intent = new Intent(context, ChangeSourceActivity.class);
//        String key = "changeSource" + System.currentTimeMillis();
//        intent.putExtra(BOOK_SHELF, key);
//        BitIntentDataManager.getInstance().putData(key, bookShelf);
//        context.startActivityForResult(intent, requestCode);
//    }
//
//    @Override
//    protected ActChangesourceBinding inflateView(LayoutInflater inflater) {
//        return ActChangesourceBinding.inflate(inflater);
//    }
//
//    @Override
//    protected void setupActivityComponent(AppComponent appComponent) {
//        DaggerReadBookComponent.builder().build().inject(this);
//    }
//
//    @Override
//    public void onRefresh() {
//        super.onRefresh();
//        mTvRight.post(new Runnable() {
//            @Override
//            public void run() {
//                mRecyclerView.setRefreshing(false);
//                mPresenter.getSearchBookInDatabase(mBookShelf, searchBookModel);
//            }
//        });
//    }
//
//    @Override
//    protected void initToolBar() {
//        mTvRight.setText("停止搜索");
//        mTvRight.setVisibility(View.VISIBLE);
//        ClickUtils.click(mTvRight, o -> {
//            binding.mSearchIndicator.setVisibility(View.GONE);
//            mTvRight.setVisibility(View.GONE);
//            searchBookModel.cancelSearch();
//        });
//        mTvTitle.setText(mBookShelf.getBookInfoBean().getName());
//    }
//
//    @Override
//    protected void initData() {
//        String key = getIntent().getStringExtra(BOOK_SHELF);
//        mBookShelf = (Book) BitIntentDataManager.getInstance().getData(key);
//        bookTag = mBookShelf.getCustomTag();
//        bookName = mBookShelf.getName();
//        bookAuthor = mBookShelf.getAuthor();
//        shelfLastChapter = BookHelp.guessChapterNum(mBookShelf.getLastChapterName());
//    }
//
//    @Override
//    protected void configViews() {
//        searchBookModel = new SearchModel((CoroutineScope) () -> Dispatchers.IO, new SearchModel.CallBack() {
//            @Override
//            public void onSearchCancel() {
//
//            }
//
//            @Override
//            public void onSearchFinish(boolean isEmpty) {
//                binding.mSearchIndicator.setVisibility(View.GONE);
//                mTvRight.setVisibility(View.GONE);
//
//            }
//
//            @Override
//            public void onSearchSuccess(@NonNull ArrayList<SearchBook> searchBooks) {
//                configSearchResult(searchBooks);
//            }
//
//            @Override
//            public void onSearchStart() {
//                binding.mSearchIndicator.setVisibility(View.VISIBLE);
//                mTvRight.setVisibility(View.VISIBLE);
//            }
//
//            @NonNull
//            @Override
//            public SearchScope getSearchScope() {
//                return null;
//            }
//        });
//
//        initAdapter(ChangeSourceAdapter.class, true, false);
//        binding.mEtBookName.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (StringUtils.isTrimEmpty(s.toString())) {
//                    List<SearchBook> searchBookBeans = AppDatabase.Companion.createDatabase(mContext).
//                            getSearchBookDao().getBookList(bookName, bookAuthor);
//                    mAdapter.clear();
//                    mAdapter.addAll(searchBookBeans);
//                } else {
//                    List<SearchBook> searchBookBeans = AppDatabase.Companion.createDatabase(mContext)
//                            .getSearchBookDao().getBookList(bookName, bookAuthor, s.toString());
//                    mAdapter.clear();
//                    mAdapter.addAll(searchBookBeans);
//                }
//            }
//        });
//        mPresenter.getSearchBookInDatabase(mBookShelf, searchBookModel);
//    }
//
//    @Override
//    public void showError(Exception e) {
//
//    }
//
//    @Override
//    public void complete() {
//
//    }
//
//    @Override
//    public void onItemClick(int position) {
//        SearchBook item = mAdapter.getItem(position);
//        String dataKey = String.valueOf(System.currentTimeMillis());
//        Intent intent = new Intent(mContext, BookDetailActivity.class);
//        intent.putExtra(BookDetailActivity.OPEN_FROM, AppConstant.BookOpenFrom.OPEN_FROM_SEARCH);
//        intent.putExtra(DATA_KEY, dataKey);
//        BitIntentDataManager.getInstance().putData(dataKey, item);
//        setResult(RESULT_OK, intent);
//        finish();
//    }
//
//    @Override
//    public void showSearchBook(List<SearchBook> books) {
//        if (mAdapter.getCount() > 0) {
//            DataDiffUtil.diffResult(mAdapter, books, new DataDiffUtil.ItemSameCallBack<SearchBook>() {
//                @Override
//                public boolean isItemSame(SearchBook oldItem, SearchBook newItem) {
//                    return TextUtils.equals(oldItem.getName(), newItem.getName()) && TextUtils.equals(oldItem.getAuthor(), newItem.getAuthor());
//                }
//
//                @Override
//                public boolean isContentSame(SearchBook oldItem, SearchBook newItem) {
//                    return TextUtils.equals(oldItem.getBookUrl(), newItem.getBookUrl()) && oldItem.getIsCurrentSource() == newItem.getIsCurrentSource();
//                }
//            });
//        } else {
//            mAdapter.addAll(books);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (searchBookModel != null) {
//            searchBookModel.onDestroy();
//        }
//    }
//
//    private synchronized void configSearchResult(List<SearchBook> value) {
//        if (value.size() > 0) {
//            Collections.sort(value, (o1, o2) -> mPresenter.compareSearchBooks(o1, o2));
//            for (SearchBook SearchBook : value) {
//                if (SearchBook.getName().equals(bookName)
//                        && (SearchBook.getAuthor().equals(bookAuthor) || TextUtils.isEmpty(SearchBook.getAuthor()) || TextUtils.isEmpty(bookAuthor))) {
//                    if (SearchBook.getTag().equals(bookTag)) {
//                        SearchBook.setIsCurrentSource(true);
//                    } else {
//                        SearchBook.setIsCurrentSource(false);
//                    }
//                    boolean saveBookSource = false;
//                    BookSource BookSource = BookSourceManager.getBookSourceByUrl(SearchBook.getTag());
//                    if (SearchBook.getSearchTime() < 60 && BookSource != null) {
//                        BookSource.increaseWeight(100 / (10 + SearchBook.getSearchTime()));
//                        saveBookSource = true;
//                    }
//                    if (shelfLastChapter > 0 && BookSource != null) {
//                        int lastChapter = BookHelp.guessChapterNum(SearchBook.getLastChapter());
//                        if (lastChapter > shelfLastChapter) {
//                            BookSource.increaseWeight(100);
//                            saveBookSource = true;
//                        }
//                    }
//                    if (saveBookSource) {
//                        AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().insertOrReplace(BookSource);
//                    }
//                    AppReaderDbHelper.getInstance().getDatabase().getSearchBookDao().insertOrReplace(SearchBook);
//                    if (StringUtils.isTrimEmpty(binding.mEtBookName.getText().toString()) || SearchBook.getOrigin().equals(binding.mEtBookName.getText().toString())) {
//                        handler.post(() -> mAdapter.add(SearchBook));
//                    }
//                    break;
//                }
//            }
//        }
//    }
//
//    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(RxBusTag.UP_SEARCH_BOOK)})
//    public void upSearchBook(SearchBook SearchBook) {
//        if (!Objects.equals(mBookShelf.getBookInfoBean().getName(), SearchBook.getName())
//                || !Objects.equals(mBookShelf.getBookInfoBean().getAuthor(), SearchBook.getAuthor())) {
//            return;
//        }
//        for (int i = 0; i < mAdapter.getRealAllData().size(); i++) {
//            if (mAdapter.getRealAllData().get(i).getTag().equals(SearchBook.getTag())
//                    && !mAdapter.getRealAllData().get(i).getLastChapter().equals(SearchBook.getLastChapter())) {
//                mAdapter.getRealAllData().get(i).setLastChapter(SearchBook.getLastChapter());
//                mAdapter.notifyItemChanged(i);
//            }
//        }
//    }
//}
