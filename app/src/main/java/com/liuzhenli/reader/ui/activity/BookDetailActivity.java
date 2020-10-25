package com.liuzhenli.reader.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuzhenli.common.BitIntentDataManager;
import com.liuzhenli.common.constant.AppConstant;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.reader.base.BaseActivity;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.ui.contract.BookDetailContract;
import com.liuzhenli.reader.ui.presenter.BookDetailPresenter;
import com.liuzhenli.reader.utils.filepicker.util.DateUtils;
import com.liuzhenli.reader.utils.image.ImageUtil;
import com.micoredu.readerlib.bean.BookChapterBean;
import com.micoredu.readerlib.bean.BookInfoBean;
import com.micoredu.readerlib.bean.BookShelfBean;
import com.micoredu.readerlib.bean.SearchBookBean;
import com.micoredu.readerlib.helper.BookshelfHelper;
import com.micoredu.readerlib.helper.DbHelper;
import com.microedu.reader.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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
    /***书分类*/
    @BindView(R.id.tv_category)
    TextView mTvCategory;
    /***章节数*/
    @BindView(R.id.tv_total_chapter_count)
    TextView mTvChapterCount;
    /***最后更新时间*/
    @BindView(R.id.tv_last_update_time)
    TextView mTvLastUpdateTime;
    @BindView(R.id.tv_book_description)
    TextView mTvDescription;
    @BindView(R.id.tv_net_matches)
    TextView tvNetMatches;
    @BindView(R.id.tv_download)
    TextView mTvDownload;
    @BindView(R.id.tv_read)
    TextView mTvRead;
    @BindView(R.id.tv_add_to_bookshelf)
    TextView mTvAddToBookshelf;
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
        mPresenter.getBookInfo(mBookShelf, isInBookShelf);
        ClickUtils.click(mTvAddToBookshelf, o -> {
            if (!isInBookShelf) {
                BookshelfHelper.saveBookToShelf(mBookShelf);
                if (mChapterList != null) {
                    DbHelper.getDaoSession().getBookChapterBeanDao().insertOrReplaceInTx(mChapterList);
                }
            }
        });
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
    }

    @Override
    public void showError(Exception e) {

    }

    @Override
    public void complete() {

    }

    @Override
    public void showBookInfo(BookInfoBean data, List<BookChapterBean> bookChapterBeans) {
        this.mChapterList = bookChapterBeans;
        setBookInfo(data);
    }

    private void setBookInfo(BookInfoBean book) {
        if (book == null) {
            return;
        }
        mTvDescription.setText(book.getIntroduce());
        mTvBookName.setText(book.getName());
        mTvAuthor.setText(book.getAuthor());
        mTvCategory.setText(book.getTag());
        mTvLastUpdateTime.setText(DateUtils.formatUpdateTime(book.getFinalRefreshData()));
        mTvChapterCount.setText(String.format(getResources().getString(R.string.total_chapter_count), mChapterList.size() + ""));
        ImageUtil.setImage(mContext, book.getCoverUrl(), R.drawable.book_cover, mIvCover);
    }

}
