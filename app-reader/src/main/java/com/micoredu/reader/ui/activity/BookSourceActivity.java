package com.micoredu.reader.ui.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.liuzhenli.common.base.BaseRvActivity;
import com.liuzhenli.common.constant.RxBusTag;
import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.observer.MyObserver;
import com.liuzhenli.common.utils.FileUtils;
import com.liuzhenli.common.base.BaseBean;
import com.liuzhenli.common.utils.AppSharedPreferenceHelper;
import com.liuzhenli.common.utils.PermissionUtil;
import com.liuzhenli.common.utils.picker.FilePicker;
import com.micoredu.reader.DaggerReaderComponent;
import com.micoredu.reader.R;
import com.micoredu.reader.ReaderApiModule;
import com.micoredu.reader.databinding.ActivityBookSourceBinding;
import com.micoredu.reader.service.CheckSourceService;
import com.micoredu.reader.service.ShareService;
import com.micoredu.reader.ui.adapter.BookSourceAdapter;
import com.micoredu.reader.ui.adapter.BookSourceFilterMenuAdapter;
import com.micoredu.reader.ui.contract.BookSourceContract;
import com.micoredu.reader.ui.presenter.BookSourcePresenter;
import com.micoredu.reader.widgets.DialogUtil;
import com.micoredu.reader.bean.BookSourceBean;
import com.micoredu.reader.model.BookSourceManager;

import java.util.List;

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
    /***书源排序方式*/
    private int mSortType;
    private ActivityBookSourceBinding binding;

    public static void start(Context context) {
        Intent intent = new Intent(context, BookSourceActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected View bindContentView() {
        binding = ActivityBookSourceBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerReaderComponent.builder().readerApiModule(new ReaderApiModule()).
                build().inject(this);
    }

    @Override
    protected void initToolBar() {
        mToolBar.inflateMenu(R.menu.menu_book_source);
        mToolBar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_add_book_source) {
                EditSourceActivity.start(mContext, null);
            } else if (itemId == R.id.action_import_book_source_local) {
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
            } else if (itemId == R.id.action_import_book_source_online) {
                DialogUtil.showEditTextDialog(mContext, "网络导入", "请输入网址://", "请输入地址", new DialogUtil.DialogActionListener() {
                    @Override
                    public void onClick(String s) {
                        showDialog();
                        mPresenter.getNetSource(s);
                    }
                });
                //二维码导入
            } else if (itemId == R.id.action_import_book_source_rwm) {//删除选中
            } else if (itemId == R.id.action_del_select) {
                List<BookSourceBean> bookSourceBeans = ((BookSourceAdapter) mAdapter).getSelectedBookSource();
                if (bookSourceBeans.size() > 0) {
                    DialogUtil.showMessagePositiveDialog(mContext, "提示",
                            String.format("您共选中%s个书源,是否删除?", bookSourceBeans.size()),
                            "取消", null, "确定", (dialog, index) -> {
                                mPresenter.deleteSelectedSource(bookSourceBeans);
                            }, true);
                }
            } else if (itemId == R.id.action_check_book_source) {
                List<BookSourceBean> selectedBookSource = BookSourceManager.getSelectedBookSource();
                if (selectedBookSource == null || selectedBookSource.size() == 0) {
                    toast("请选择书源~");
                } else {
                    mPresenter.checkBookSource(mContext, selectedBookSource);
                }
            } else if (itemId == R.id.action_share_wifi) {
                List<BookSourceBean> bookSourceBeans;
                bookSourceBeans = ((BookSourceAdapter) mAdapter).getSelectedBookSource();
                if (bookSourceBeans.size() > 0) {
                    ShareService.startThis(mContext, bookSourceBeans);
                } else {
                    toast("没有选中书源哦");
                }
            }
            return false;
        });
    }

    @Override
    protected void initData() {
        mSortType = AppSharedPreferenceHelper.getBookSourceSortType();
    }

    private final BookSourceFilterMenuAdapter.MenuItemClickListener filterMenuClickListener
            = new BookSourceFilterMenuAdapter.MenuItemClickListener() {
        @SuppressLint("SetTextI18n")
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
                binding.mEtSearchKey.setText("enable");
                //反选
            } else if (index == 2) {
                for (int i = 0; i < mAdapter.getRealAllData().size(); i++) {
                    mAdapter.getRealAllData().get(i).setEnable(!mAdapter.getRealAllData().get(i).getEnable());
                    mAdapter.notifyItemChanged(i);
                }
            }
            mPresenter.saveData(mAdapter.getAllData());
            binding.mDropdownMenu.close();
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
            binding.mDropdownMenu.close();
        }

        /**
         * choose book source by group
         * @param index     index the item index
         * @param groupName the item name in the index
         */
        @Override
        public void onGroupChange(int index, String groupName) {
            if (index == 0) {
                binding.mEtSearchKey.setText("");
            } else {
                binding.mEtSearchKey.setText(groupName);
            }
            binding.mDropdownMenu.close();
        }
    };

    @Override
    protected void configViews() {
        initAdapter(BookSourceAdapter.class, false, false);
        mPresenter.getLocalBookSource("");
        mFilterMenuAdapter = new BookSourceFilterMenuAdapter(mContext);
        updateSortMenu();
        binding.mDropdownMenu.setMenuAdapter(mFilterMenuAdapter);
        mFilterMenuAdapter.setMenuItemClickListener(filterMenuClickListener);
        binding.mEtSearchKey.addTextChangedListener(new TextWatcher() {
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
        binding.mTvStopCheck.setOnClickListener(v -> CheckSourceService.stop(mContext));
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
        mPresenter.getLocalBookSource(binding.mEtSearchKey.getText().toString());
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
        if (TextUtils.isEmpty(binding.mEtSearchKey.getText())) {
            super.onBackPressed();
        } else {
            binding.mEtSearchKey.setText("");
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
        binding.mVCheckSource.setVisibility(View.VISIBLE);
        binding.mTvCheckProgress.setText(msg);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(RxBusTag.CHECK_SOURCE_FINISH)})
    public void onStopCheckService(String msg) {
        toast("校验完成");
        binding.mVCheckSource.setVisibility(View.GONE);
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
            if (requestCode == IMPORT_BOOK_SOURCE) {
                if (data != null && data.getData() != null) {
                    mPresenter.loadBookSourceFromFile(FileUtils.getPath(this, data.getData()));
                }
            }
        }
    }
}
