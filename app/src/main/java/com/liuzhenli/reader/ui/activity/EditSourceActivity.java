package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;

import com.liuzhenli.reader.base.BaseActivity;
import com.liuzhenli.reader.base.BaseRvActivity;
import com.liuzhenli.reader.bean.EditSource;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.ui.adapter.EditSourceAdapter;
import com.liuzhenli.reader.ui.contract.EditSourceContract;
import com.liuzhenli.reader.ui.presenter.EditSourcePresenter;
import com.micoredu.readerlib.bean.BookSourceBean;
import com.microedu.reader.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2020/11/16
 * Email: 848808263@qq.com
 */
public class EditSourceActivity extends BaseRvActivity<EditSourcePresenter, EditSource> implements EditSourceContract.View {

    public static final String BOOK_SOURCE = "book_source";

    private List<EditSource> sourceEditList = new ArrayList<>();
    private List<EditSource> findEditList = new ArrayList<>();

    private BookSourceBean mBookSource;

    public static void start(Context context, BookSourceBean data) {
        Intent intent = new Intent(context, EditSourceActivity.class);
        intent.putExtra(BOOK_SOURCE, data);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_editsource;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initToolBar() {

    }

    @Override
    protected void initData() {
        mBookSource = (BookSourceBean) getIntent().getSerializableExtra(BOOK_SOURCE);


    }

    @Override
    protected void configViews() {
        initAdapter(EditSourceAdapter.class, false, false);
        setText(mBookSource);
    }

    @Override
    public void showError(Exception e) {

    }

    @Override
    public void complete() {

    }

    private void setText(BookSourceBean bookSourceBean) {
        sourceEditList.clear();
        findEditList.clear();
        mAdapter.notifyDataSetChanged();
        sourceEditList.add(new EditSource("bookSourceUrl", bookSourceBean.getBookSourceUrl(), R.string.book_source_url));
        sourceEditList.add(new EditSource("bookSourceName", bookSourceBean.getBookSourceName(), R.string.book_source_name));
        sourceEditList.add(new EditSource("bookSourceGroup", bookSourceBean.getBookSourceGroup(), R.string.book_source_group));
        sourceEditList.add(new EditSource("loginUrl", bookSourceBean.getLoginUrl(), R.string.book_source_login_url));
        //搜索
        sourceEditList.add(new EditSource("ruleSearchUrl", bookSourceBean.getRuleSearchUrl(), R.string.rule_search_url));
        sourceEditList.add(new EditSource("ruleSearchList", bookSourceBean.getRuleSearchList(), R.string.rule_search_list));
        sourceEditList.add(new EditSource("ruleSearchName", bookSourceBean.getRuleSearchName(), R.string.rule_search_name));
        sourceEditList.add(new EditSource("ruleSearchAuthor", bookSourceBean.getRuleSearchAuthor(), R.string.rule_search_author));
        sourceEditList.add(new EditSource("ruleSearchKind", bookSourceBean.getRuleSearchKind(), R.string.rule_search_kind));
        sourceEditList.add(new EditSource("ruleSearchLastChapter", bookSourceBean.getRuleSearchLastChapter(), R.string.rule_search_last_chapter));
        sourceEditList.add(new EditSource("ruleSearchIntroduce", bookSourceBean.getRuleSearchIntroduce(), R.string.rule_search_introduce));
        sourceEditList.add(new EditSource("ruleSearchCoverUrl", bookSourceBean.getRuleSearchCoverUrl(), R.string.rule_search_cover_url));
        sourceEditList.add(new EditSource("ruleSearchNoteUrl", bookSourceBean.getRuleSearchNoteUrl(), R.string.rule_search_note_url));
        //详情页
        sourceEditList.add(new EditSource("ruleBookUrlPattern", bookSourceBean.getRuleBookUrlPattern(), R.string.book_url_pattern));
        sourceEditList.add(new EditSource("ruleBookInfoInit", bookSourceBean.getRuleBookInfoInit(), R.string.rule_book_info_init));
        sourceEditList.add(new EditSource("ruleBookName", bookSourceBean.getRuleBookName(), R.string.rule_book_name));
        sourceEditList.add(new EditSource("ruleBookAuthor", bookSourceBean.getRuleBookAuthor(), R.string.rule_book_author));
        sourceEditList.add(new EditSource("ruleCoverUrl", bookSourceBean.getRuleCoverUrl(), R.string.rule_cover_url));
        sourceEditList.add(new EditSource("ruleIntroduce", bookSourceBean.getRuleIntroduce(), R.string.rule_introduce));
        sourceEditList.add(new EditSource("ruleBookKind", bookSourceBean.getRuleBookKind(), R.string.rule_book_kind));
        sourceEditList.add(new EditSource("ruleBookLastChapter", bookSourceBean.getRuleBookLastChapter(), R.string.rule_book_last_chapter));
        sourceEditList.add(new EditSource("ruleChapterUrl", bookSourceBean.getRuleChapterUrl(), R.string.rule_chapter_list_url));
        //目录页
        sourceEditList.add(new EditSource("ruleChapterUrlNext", bookSourceBean.getRuleChapterUrlNext(), R.string.rule_chapter_list_url_next));
        sourceEditList.add(new EditSource("ruleChapterList", bookSourceBean.getRuleChapterList(), R.string.rule_chapter_list));
        sourceEditList.add(new EditSource("ruleChapterName", bookSourceBean.getRuleChapterName(), R.string.rule_chapter_name));
        sourceEditList.add(new EditSource("ruleContentUrl", bookSourceBean.getRuleContentUrl(), R.string.rule_content_url));
        //正文页
        sourceEditList.add(new EditSource("ruleContentUrlNext", bookSourceBean.getRuleContentUrlNext(), R.string.rule_content_url_next));
        sourceEditList.add(new EditSource("ruleBookContent", bookSourceBean.getRuleBookContent(), R.string.rule_book_content));
        //sourceEditList.add(new EditSource("ruleBookContentReplace", bookSourceBean.getRuleBookContentReplace(), R.string.rule_book_content_replace));
        sourceEditList.add(new EditSource("httpUserAgent", bookSourceBean.getHttpUserAgent(), R.string.source_user_agent));

        //发现
        findEditList.add(new EditSource("ruleFindUrl", bookSourceBean.getRuleFindUrl(), R.string.rule_find_url));
        findEditList.add(new EditSource("ruleFindList", bookSourceBean.getRuleFindList(), R.string.rule_find_list));
        findEditList.add(new EditSource("ruleFindName", bookSourceBean.getRuleFindName(), R.string.rule_find_name));
        findEditList.add(new EditSource("ruleFindAuthor", bookSourceBean.getRuleFindAuthor(), R.string.rule_find_author));
        findEditList.add(new EditSource("ruleFindKind", bookSourceBean.getRuleFindKind(), R.string.rule_find_kind));
        findEditList.add(new EditSource("ruleFindIntroduce", bookSourceBean.getRuleFindIntroduce(), R.string.rule_find_introduce));
        findEditList.add(new EditSource("ruleFindLastChapter", bookSourceBean.getRuleFindLastChapter(), R.string.rule_find_last_chapter));
        findEditList.add(new EditSource("ruleFindCoverUrl", bookSourceBean.getRuleFindCoverUrl(), R.string.rule_find_cover_url));
        findEditList.add(new EditSource("ruleFindNoteUrl", bookSourceBean.getRuleFindNoteUrl(), R.string.rule_find_note_url));

        mAdapter.addAll(sourceEditList);
    }

    @Override
    public void onItemClick(int position) {

    }
}
