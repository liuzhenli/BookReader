package com.liuzhenli.reader.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.liuzhenli.reader.base.BaseRvActivity;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.ui.adapter.BookSourceAdapter;
import com.liuzhenli.reader.ui.adapter.BookSourceFilterMenuAdapter;
import com.liuzhenli.reader.ui.contract.BookSourceContract;
import com.liuzhenli.reader.ui.presenter.BookSourcePresenter;
import com.liuzhenli.reader.utils.face.AppConfigManager;
import com.liuzhenli.reader.view.filter.DropDownMenu;
import com.micoredu.readerlib.bean.BookSourceBean;
import com.micoredu.readerlib.model.BookSourceManager;
import com.microedu.reader.R;

import java.util.List;

import butterknife.BindView;

import static com.liuzhenli.reader.utils.face.AppConfigManager.SortType.SORT_TYPE_AUTO;
import static com.liuzhenli.reader.utils.face.AppConfigManager.SortType.SORT_TYPE_HAND;
import static com.liuzhenli.reader.utils.face.AppConfigManager.SortType.SORT_TYPE_PINYIN;


/**
 * 书源列表
 *
 * @author liuzhenli 2020.8.10
 */
public class BookSourceActivity extends BaseRvActivity<BookSourcePresenter, BookSourceBean> implements BookSourceContract.View {

    private BookSourceFilterMenuAdapter mFilterMenuAdapter;

    @BindView(R.id.view_dropdown_menu)
    DropDownMenu mDropdownMenu;
    /***书源排序方式*/
    private int mSortType;

    public static void start(Context context) {
        Intent intent = new Intent(context, BookSourceActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_source;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    protected void initToolBar() {
        mToolBar.inflateMenu(R.menu.menu_book_source);
        mToolBar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_more));
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add_book_source:
                        EditSourceActivity.start(mContext, null);
                        break;
                    case R.id.action_import_book_source_local:
                        SearchActivity.start(mContext);
                        break;
                    case R.id.action_import_book_source_online:
                        SearchActivity.start(mContext);
                        break;
                    case R.id.action_import_book_source_rwm:
                        SearchActivity.start(mContext);
                        break;
                    case R.id.action_del_select:
                        SearchActivity.start(mContext);
                        break;
                    case R.id.action_check_book_source:
                        SearchActivity.start(mContext);
                        break;
                    case R.id.action_share_wifi:
                        SearchActivity.start(mContext);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        mSortType = AppConfigManager.getBookSourceSortType();
    }

    private BookSourceFilterMenuAdapter.MenuItemClickListener filterMenuClickListener = new BookSourceFilterMenuAdapter.MenuItemClickListener() {
        @Override
        public void onSelectChange(int index) {
            //全选
            if (index == 0) {
                for (int i = 0; i < mAdapter.getRealAllData().size(); i++) {
                    mAdapter.getRealAllData().get(i).setEnable(true);
                    mAdapter.notifyItemChanged(i);
                }
                //已选
            } else if (index == 1) {
                List<BookSourceBean> selectedBookSource = BookSourceManager.getSelectedBookSource();
                mAdapter.clear();
                mAdapter.addAll(selectedBookSource);
                //反选
            } else if (index == 2) {
                for (int i = 0; i < mAdapter.getRealAllData().size(); i++) {
                    mAdapter.getRealAllData().get(i).setEnable(!mAdapter.getRealAllData().get(i).getEnable());
                    mAdapter.notifyItemChanged(i);
                }
            }

            mDropdownMenu.close();
        }

        @Override
        public void onSortChange(int index) {
            BookSourceAdapter adapter = (BookSourceAdapter) mAdapter;
            if (index == 0) {
                mSortType = SORT_TYPE_HAND;
            } else if (index == 1) {
                mSortType = SORT_TYPE_AUTO;
            } else if (index == 2) {
                mSortType = SORT_TYPE_PINYIN;
            }
            adapter.setSortType(mSortType);
            AppConfigManager.setBookSourceSortType(mSortType);
            mDropdownMenu.close();
            mAdapter.clear();
            mAdapter.addAll(BookSourceManager.getAllBookSource());
        }

        @Override
        public void onGroupChange(int index, String groupName) {
            List<BookSourceBean> enableSourceByGroup;
            if (index == 0) {
                enableSourceByGroup = BookSourceManager.getAllBookSource();
            } else {
                enableSourceByGroup = BookSourceManager.getEnableSourceByGroup(groupName);
            }
            mAdapter.clear();
            mAdapter.addAll(enableSourceByGroup);
            mDropdownMenu.close();
        }
    };

    @Override
    protected void configViews() {
        initAdapter(BookSourceAdapter.class, false, false);
        mPresenter.getLocalBookSource();
        mFilterMenuAdapter = new BookSourceFilterMenuAdapter(mContext);
        updateSortMenu();
        mDropdownMenu.setMenuAdapter(mFilterMenuAdapter);
        mFilterMenuAdapter.setMenuItemClickListener(filterMenuClickListener);
    }

    @Override
    public void showLocalBookSource(List<BookSourceBean> list) {
        mAdapter.clear();
        mAdapter.addAll(list);
    }

    @Override
    public void showError(Exception e) {

    }

    @Override
    public void complete() {

    }

    @Override
    public void onItemClick(int position) {
    }

    public void saveBookSource(List<BookSourceBean> bookSourceBeanList) {
        mPresenter.saveData(bookSourceBeanList);
    }

    public void saveBookSource(BookSourceBean bookSource) {
        mPresenter.saveData(bookSource);
    }

    public void deleteBookSource(BookSourceBean bookSource) {
        mPresenter.delData(bookSource);
    }


    private void updateSortMenu() {
        List<String> groupList = BookSourceManager.getGroupList();
        groupList.add(0, "全部");
        mFilterMenuAdapter.setGroupList(groupList);
    }

}
