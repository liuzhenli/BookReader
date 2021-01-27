package com.liuzhenli.reader.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.liuzhenli.common.BitIntentDataManager;
import com.liuzhenli.common.constant.AppConstant;
import com.liuzhenli.common.constant.RxBusTag;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.reader.base.BaseActivity;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.ui.contract.BookDetailContract;
import com.liuzhenli.reader.ui.presenter.BookDetailPresenter;
import com.liuzhenli.reader.utils.ToastUtil;
import com.liuzhenli.reader.utils.image.ImageUtil;
import com.micoredu.readerlib.bean.BookChapterBean;
import com.micoredu.readerlib.bean.BookInfoBean;
import com.micoredu.readerlib.bean.BookShelfBean;
import com.micoredu.readerlib.bean.SearchBookBean;
import com.micoredu.readerlib.helper.BookshelfHelper;
import com.micoredu.readerlib.helper.DbHelper;
import com.micoredu.readerlib.utils.bar.ImmersionBar;
import com.microedu.reader.R;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

import static com.liuzhenli.common.BitIntentDataManager.DATA_KEY;

/**
 * 书详情页面
 *
 * @author liuzhenli 2019.12.13
 */
public class BookDetailActivity extends BaseActivity<BookDetailPresenter> implements BookDetailContract.View {
    public static final String OPEN_FROM = "openFrom";
    @BindView(R.id.iv_cover)
    ImageView mIvCover;
    @BindView(R.id.tv_book_name)
    TextView mTvBookName;
    @BindView(R.id.tv_author)
    TextView mTvAuthor;
    /***书来源*/
    @BindView(R.id.tv_book_source)
    TextView mTvBookSource;
    /***章节数*/
    @BindView(R.id.tv_total_chapter_count)
    TextView mTvChapterCount;
    @BindView(R.id.tv_book_description)
    TextView mTvDescription;
    @BindView(R.id.tv_download)
    TextView mTvDownload;
    @BindView(R.id.tv_read)
    TextView mTvRead;
    @BindView(R.id.tv_add_to_bookshelf)
    TextView mTvAddToBookshelf;
    @BindView(R.id.tv_last_chapter_name)
    TextView mTvLastChapterName;
    @BindView(R.id.view_change_book_source)
    View mVChangeBookSource;
    @BindView(R.id.view_chapter_list)
    View mVChapterList;
    private List<BookChapterBean> mChapterList = new ArrayList<>();

    private int mOpenFrom;
    private SearchBookBean mSearchBook;
    private BookShelfBean mBookShelf;
    private boolean isInBookShelf = false;

    @Override
    protected int getLayoutId() {
        return R.layout.act_bookdetail;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    protected void initToolBar() {
        mTvTitle.setText("书详情");
    }

    @Override
    protected void initData() {

        if (mOpenFrom == AppConstant.BookOpenFrom.FROM_BOOKSHELF) {
            mBookShelf = (BookShelfBean) BitIntentDataManager.getInstance().getData(DATA_KEY);
            if (mBookShelf == null) {
                String noteUrl = getIntent().getStringExtra("noteUrl");
                if (!TextUtils.isEmpty(noteUrl)) {
                    mBookShelf = BookshelfHelper.getBook(noteUrl);
                }
            }
            if (mBookShelf == null) {
                finish();
                return;
            }
            isInBookShelf = true;
            mSearchBook = new SearchBookBean();
            mSearchBook.setNoteUrl(mBookShelf.getNoteUrl());
            mSearchBook.setTag(mBookShelf.getBookInfoBean().getTag());
            setBookInfo(mBookShelf.getBookInfoBean());
        } else {
            String key = getIntent().getStringExtra(DATA_KEY);
            if (!TextUtils.isEmpty(key)) {
                mSearchBook = (SearchBookBean) BitIntentDataManager.getInstance().getData(key);
                if (mSearchBook != null) {
                    isInBookShelf = BookshelfHelper.isInBookShelf(mSearchBook.getNoteUrl());
                    mBookShelf = BookshelfHelper.getBookFromSearchBook(mSearchBook);
                }
            }
        }
    }

    @Override
    protected void configViews() {
        //add to book shelf or remove from bookshelf
        ClickUtils.click(mTvAddToBookshelf, o -> {
            if (!isInBookShelf) {
                BookshelfHelper.saveBookToShelf(mBookShelf);
                if (mChapterList != null) {
                    DbHelper.getDaoSession().getBookChapterBeanDao().insertOrReplaceInTx(mChapterList);
                }
                ToastUtil.showToast("已加入书架");
            } else {
                BookshelfHelper.removeFromBookShelf(mBookShelf);
                ToastUtil.showToast("已从书架中移除");
            }
            setIsInBookShelf(!isInBookShelf);
        });
        //read book button click
        ClickUtils.click(mTvRead, o -> {
            Intent intent = new Intent(BookDetailActivity.this, ReaderActivity.class);
            intent.putExtra("openFrom", AppConstant.BookOpenFrom.OPEN_FROM_APP);
            intent.putExtra("inBookshelf", isInBookShelf);
            String key = String.valueOf(System.currentTimeMillis());
            String bookKey = "book" + key;
            intent.putExtra(DATA_KEY, bookKey);
            BitIntentDataManager.getInstance().putData(bookKey, mBookShelf.clone());
            startActivityByAnim(intent, android.R.anim.fade_in, android.R.anim.fade_out);
        });
        if (mBookShelf != null && mBookShelf.getBookInfoBean() != null) {
            mTvBookName.setText(mBookShelf.getBookInfoBean().getName());
            mTvAuthor.setText(String.format("%s 著", mBookShelf.getBookInfoBean().getAuthor()));
            //来源网站
            mTvBookSource.setText(mBookShelf.getBookInfoBean().getOrigin());
            mTvLastChapterName.setText(mSearchBook.getLastChapter());
            mTvDescription.setText(mBookShelf.getBookInfoBean().getIntroduce());
            ImageUtil.setRoundedCornerImage(mContext, mBookShelf.getBookInfoBean().getCoverUrl(), R.drawable.book_cover, mIvCover, 4);
        }
        mPresenter.getBookInfo(mBookShelf, isInBookShelf);
        showDialog();
        setIsInBookShelf(isInBookShelf);

        ClickUtils.click(mVChangeBookSource, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {

            }
        });

        ClickUtils.click(mVChapterList, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                BookChapterListActivity.start(mContext,mBookShelf,mChapterList);
            }
        });
    }

    @Override
    public void showError(Exception e) {
        hideDialog();
    }

    @Override
    public void complete() {
        hideDialog();
    }

    @Override
    public void showBookInfo(BookInfoBean data, List<BookChapterBean> bookChapterBeans) {
        this.mChapterList = bookChapterBeans;
        setBookInfo(data);
        hideDialog();
    }

    private void setBookInfo(BookInfoBean book) {
        if (book == null) {
            return;
        }
        mTvDescription.setText(book.getIntroduce());
        mTvBookName.setText(book.getName());
        mTvAuthor.setText(String.format("%s 著", mBookShelf.getBookInfoBean().getAuthor()));
        mTvChapterCount.setText(String.format(getResources().getString(R.string.total_chapter_count), mChapterList.size() + ""));
        ImageUtil.setRoundedCornerImage(mContext, book.getCoverUrl(), R.drawable.book_cover, mIvCover, 4);
    }

    private void setIsInBookShelf(boolean isInBookShelf) {
        if (isInBookShelf) {
            mTvAddToBookshelf.setText("移除书架");
        } else {
            mTvAddToBookshelf.setText("加入书架");
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(RxBusTag.UPDATE_BOOK_PROGRESS)})
    public void onBookAddToBookShelf(BookShelfBean bookShelfBean) {
        if (TextUtils.equals(mSearchBook.getNoteUrl(), bookShelfBean.getNoteUrl())) {
            isInBookShelf = true;
            mTvAddToBookshelf.setText("移除书架");
        }
    }

    /**
     * 沉浸状态栏
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarColor(R.color.book_index_bg_color);
        mImmersionBar.statusBarDarkFont(false);
        mImmersionBar.fitsSystemWindows(true);
        mImmersionBar.init();
    }
}
