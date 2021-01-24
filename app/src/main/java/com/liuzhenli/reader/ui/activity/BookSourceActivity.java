package com.liuzhenli.reader.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.liuzhenli.common.constant.RxBusTag;
import com.liuzhenli.reader.base.BaseBean;
import com.liuzhenli.reader.base.BaseRvActivity;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.service.CheckSourceService;
import com.liuzhenli.reader.ui.adapter.BookSourceAdapter;
import com.liuzhenli.reader.ui.adapter.BookSourceFilterMenuAdapter;
import com.liuzhenli.reader.ui.contract.BookSourceContract;
import com.liuzhenli.reader.ui.presenter.BookSourcePresenter;
import com.liuzhenli.common.utils.AppSharedPreferenceHelper;
import com.liuzhenli.reader.view.filter.DropDownMenu;
import com.liuzhenli.reader.view.loading.DialogUtil;
import com.micoredu.readerlib.bean.BookSourceBean;
import com.micoredu.readerlib.model.BookSourceManager;
import com.microedu.reader.R;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.List;

import butterknife.BindView;

import static com.liuzhenli.common.utils.AppSharedPreferenceHelper.SortType.SORT_TYPE_AUTO;
import static com.liuzhenli.common.utils.AppSharedPreferenceHelper.SortType.SORT_TYPE_HAND;
import static com.liuzhenli.common.utils.AppSharedPreferenceHelper.SortType.SORT_TYPE_PINYIN;


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
    @BindView(R.id.view_check_source_info)
    View mVCheckSource;
    @BindView(R.id.tv_book_source_check_progress)
    TextView mTvCheckProgress;
    @BindView(R.id.tv_stop_check)
    TextView mTvStopCheck;
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
                //删除选中
                case R.id.action_del_select:
                    List<BookSourceBean> bookSourceBeans = ((BookSourceAdapter) mAdapter).getSelectedBookSource();
                    if (bookSourceBeans.size() > 0) {
                        DialogUtil.showMessagePositiveDialog(mContext, "提示",
                                String.format("您共选中%s个书源,是否删除?", bookSourceBeans.size()),
                                "取消", null, "确定", (dialog, index) -> {
                                    mPresenter.deleteSelectedSource(bookSourceBeans);
                                }, true);
                    }
                    break;
                case R.id.action_check_book_source:
                    List<BookSourceBean> selectedBookSource = BookSourceManager.getSelectedBookSource();
                    if (selectedBookSource == null || selectedBookSource.size() == 0) {
                        toast("请选择书源~");
                    } else {
                        mPresenter.checkBookSource(mContext, selectedBookSource);
                    }
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
        mSortType = AppSharedPreferenceHelper.getBookSourceSortType();
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
            mPresenter.saveData(mAdapter.getAllData());
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
            AppSharedPreferenceHelper.setBookSourceSortType(mSortType);
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
        if (mRecyclerView.getEmptyView() != null) {
            TextView mTvEmptyText = mRecyclerView.getEmptyView().findViewById(R.id.tv_empty_text);
            mTvEmptyText.setText("暂无书源,搜索微信公众号:异书房,\n回复\"书源\"获取书源~");
        }
        mTvStopCheck.setOnClickListener(v -> {
            CheckSourceService.stop(mContext);
        });
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
        mPresenter.getLocalBookSource(mEtSearchKey.getText().toString());
    }

    @Override
    public void showCheckBookSourceResult(BaseBean data) {

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
    public void onItemClick(int position) {
    }

    @Override
    public void onBackPressed() {
        if (TextUtils.isEmpty(mEtSearchKey.getText())) {
            super.onBackPressed();
        } else {
            mEtSearchKey.setText("");
        }
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


    /****检测书源是否可用发来的消息*/
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(RxBusTag.CHECK_SOURCE_STATE)})
    public void onSourceCheckChanged(String msg) {
        mVCheckSource.setVisibility(View.VISIBLE);
        mTvCheckProgress.setText(msg);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(RxBusTag.CHECK_SOURCE_FINISH)})
    public void onStopCheckService(String msg) {
        mVCheckSource.setVisibility(View.GONE);
    }

}
