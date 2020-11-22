package com.liuzhenli.reader.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.liuzhenli.reader.base.BaseRvActivity;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.ui.adapter.BookSourceAdapter;
import com.liuzhenli.reader.ui.adapter.BookSourceFilterMenuAdapter;
import com.liuzhenli.reader.ui.contract.BookSourceContract;
import com.liuzhenli.reader.ui.presenter.BookSourcePresenter;
import com.liuzhenli.common.utils.AppConfigManager;
import com.liuzhenli.reader.view.filter.DropDownMenu;
import com.liuzhenli.reader.view.loading.DialogUtil;
import com.micoredu.readerlib.bean.BookSourceBean;
import com.micoredu.readerlib.model.BookSourceManager;
import com.microedu.reader.R;

import java.util.List;

import butterknife.BindView;

import static com.liuzhenli.common.utils.AppConfigManager.SortType.SORT_TYPE_AUTO;
import static com.liuzhenli.common.utils.AppConfigManager.SortType.SORT_TYPE_HAND;
import static com.liuzhenli.common.utils.AppConfigManager.SortType.SORT_TYPE_PINYIN;


/**
 * 书源列表
 *
 * @author liuzhenli 2020.8.10
 */
public class BookSourceActivity extends BaseRvActivity<BookSourcePresenter, BookSourceBean> implements BookSourceContract.View {

    private BookSourceFilterMenuAdapter mFilterMenuAdapter;

    @BindView(R.id.view_dropdown_menu)
    DropDownMenu mDropdownMenu;
    @BindView(R.id.et_book_source)
    EditText mEtSearchKey;
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
        mToolBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_add_book_source:
                    EditSourceActivity.start(mContext, null);
                    break;
                case R.id.action_import_book_source_local:
                    SearchActivity.start(mContext);
                    break;
                case R.id.action_import_book_source_online:

                    DialogUtil.showEditTextDialog(mContext, "网络导入", "请输入网址://", "请输入地址", new DialogUtil.DialogActionListener() {
                        @Override
                        public void onClick(String s) {
                            showDialog();
                            mPresenter.getNetSource(s);
                        }
                    });
                    break;
                case R.id.action_import_book_source_rwm:
                    SearchActivity.start(mContext);
                    break;
                case R.id.action_del_select:
                    mPresenter.deleteSelectedSource();
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
                mEtSearchKey.setText("enable");
                //反选
            } else if (index == 2) {
                for (int i = 0; i < mAdapter.getRealAllData().size(); i++) {
                    mAdapter.getRealAllData().get(i).setEnable(!mAdapter.getRealAllData().get(i).getEnable());
                    mAdapter.notifyItemChanged(i);
                }
            }

            mDropdownMenu.close();
        }

        /**
         * 排序
         * @param index index item index
         */
        @Override
        public void onSortChange(int index) {
            if (index == 0) {
                mSortType = SORT_TYPE_HAND;
            } else if (index == 1) {
                mSortType = SORT_TYPE_AUTO;
            } else if (index == 2) {
                mSortType = SORT_TYPE_PINYIN;
            }
            AppConfigManager.setBookSourceSortType(mSortType);
            mPresenter.getLocalBookSource("");
            mDropdownMenu.close();
        }

        /**
         * choose book source by group
         * @param index     index the item index
         * @param groupName the item name in the index
         */
        @Override
        public void onGroupChange(int index, String groupName) {
            if (index == 0) {
                mEtSearchKey.setText("");
            } else {
                mEtSearchKey.setText(groupName);
            }
            mDropdownMenu.close();
        }
    };

    @Override
    protected void configViews() {
        initAdapter(BookSourceAdapter.class, false, false);
        mPresenter.getLocalBookSource("");
        mFilterMenuAdapter = new BookSourceFilterMenuAdapter(mContext);
        updateSortMenu();
        mDropdownMenu.setMenuAdapter(mFilterMenuAdapter);
        mFilterMenuAdapter.setMenuItemClickListener(filterMenuClickListener);
        mEtSearchKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPresenter.getLocalBookSource(s.toString());
            }
        });

        mRecyclerView.setEmptyView(R.layout.empty_view_souce);
    }

    @Override
    public void showLocalBookSource(List<BookSourceBean> list) {
        BookSourceAdapter adapter = (BookSourceAdapter) mAdapter;
        adapter.setSortType(mSortType);
        mAdapter.clear();
        mAdapter.addAll(list);
        hideDialog();
    }

    @Override
    public void showAddNetSourceResult(List<BookSourceBean> list) {
        if (list != null && list.size() > 0) {
            showDialog();
            mPresenter.getLocalBookSource("");
            toast(String.format("成功导入%s个书源", list.size()));
        }

    }

    @Override
    public void shoDeleteBookSourceResult() {
        mPresenter.getLocalBookSource("");
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
