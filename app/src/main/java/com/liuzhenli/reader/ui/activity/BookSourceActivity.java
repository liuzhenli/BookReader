package com.liuzhenli.reader.ui.activity;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.liuzhenli.common.constant.RxBusTag;
import com.liuzhenli.common.observer.MyObserver;
import com.liuzhenli.common.utils.FileUtils;
import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.reader.base.BaseBean;
import com.liuzhenli.reader.base.BaseRvActivity;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.service.CheckSourceService;
import com.liuzhenli.reader.service.ShareService;
import com.liuzhenli.reader.ui.adapter.BookSourceAdapter;
import com.liuzhenli.reader.ui.adapter.BookSourceFilterMenuAdapter;
import com.liuzhenli.reader.ui.contract.BookSourceContract;
import com.liuzhenli.reader.ui.presenter.BookSourcePresenter;
import com.liuzhenli.common.utils.AppSharedPreferenceHelper;
import com.liuzhenli.reader.utils.PermissionUtil;
import com.liuzhenli.reader.utils.filepicker.picker.FilePicker;
import com.liuzhenli.reader.view.filter.DropDownMenu;
import com.liuzhenli.reader.view.loading.DialogUtil;
import com.micoredu.readerlib.bean.BookSourceBean;
import com.micoredu.readerlib.model.BookSourceManager;
import com.microedu.reader.R;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;

import static com.liuzhenli.common.utils.AppSharedPreferenceHelper.SortType.SORT_TYPE_AUTO;
import static com.liuzhenli.common.utils.AppSharedPreferenceHelper.SortType.SORT_TYPE_HAND;
import static com.liuzhenli.common.utils.AppSharedPreferenceHelper.SortType.SORT_TYPE_PINYIN;


/**
 * 书源列表
 *
 * @author liuzhenli 2020.8.10
 */
public class BookSourceActivity extends BaseRvActivity<BookSourcePresenter, BookSourceBean> implements BookSourceContract.View {
    private final int IMPORT_BOOK_SOURCE = 1000;

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
                    PermissionUtil.requestPermission(BookSourceActivity.this, new MyObserver<Boolean>() {
                        @Override
                        public void onNext(@NonNull Boolean o) {
                            FilePicker filePicker = new FilePicker(BookSourceActivity.this, FilePicker.FILE);
                            filePicker.setBackgroundColor(getResources().getColor(R.color.background));
                            filePicker.setTopBackgroundColor(getResources().getColor(R.color.background));
                            filePicker.setAllowExtensions(getResources().getStringArray(R.array.text_suffix));
                            filePicker.setOnFilePickListener(s -> mPresenter.loadBookSourceFromFile(s));
                            filePicker.show();
                            filePicker.getSubmitButton().setText(R.string.sys_file_picker);
                            filePicker.getSubmitButton().setOnClickListener(view -> {
                                filePicker.dismiss();
                                selectFileSys();
                            });
                        }
                    }, Manifest.permission.READ_EXTERNAL_STORAGE);
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
                //二维码导入
                case R.id.action_import_book_source_rwm:

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
                    bookSourceBeans = ((BookSourceAdapter) mAdapter).getSelectedBookSource();
                    if (bookSourceBeans.size() > 0) {
                        ShareService.startThis(mContext, bookSourceBeans);
                    } else {
                        toast("没有选中书源哦");
                    }
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
            //发送消息,通知发现页
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
        toast("校验完成");
        mVCheckSource.setVisibility(View.GONE);
    }

    private void selectFileSys() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"text/*", "application/json"});
        intent.setType("*/*");//设置类型
        startActivityForResult(intent, IMPORT_BOOK_SOURCE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IMPORT_BOOK_SOURCE:
                    if (data != null && data.getData() != null) {
                        mPresenter.loadBookSourceFromFile(FileUtils.getPath(this, data.getData()));
                    }
                    break;
            }
        }
    }
}
