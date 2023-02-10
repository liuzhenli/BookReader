package com.micoredu.reader.ui.activity;




/**
 * Book source list page
 *
 * @author liuzhenli 2020.8.10
 */
/*
public class BookSourceActivity extends ReaderBaseRVActivity<BookSourcePresenter, BookSource, ActivityBookSourceBinding> implements BookSourceContract.View {


    private BookSourceFilterMenuAdapter mFilterMenuAdapter;
    //
    //book source order type see AppSharedPreferenceHelper.SortType
    //
    private int mSortType;

    public static void start(Context context) {
        Intent intent = new Intent(context, BookSourceActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void setupActivityComponent(ReaderComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    protected ActivityBookSourceBinding inflateView(LayoutInflater inflater) {
        return ActivityBookSourceBinding.inflate(inflater);
    }

    @Override
    protected void initToolBar() {
        String defaultBookSourceUrl = AppConfigManager.INSTANCE.getDefaultBookSourceUrl();
        mToolBar.inflateMenu(R.menu.menu_book_source);
        if (TextUtils.isEmpty(defaultBookSourceUrl)) {
            mToolBar.getMenu().findItem(R.id.action_fast_import).setVisible(false);
        }
        mToolBar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_fast_import) {
                showDialog();
                mPresenter.getNetSource(AppConfigManager.INSTANCE.getDefaultBookSourceUrl());
            } else if (itemId == R.id.action_add_book_source) {
                EditSourceActivity.start(mContext, null);
            } else if (itemId == R.id.action_import_book_source_local) {
                IntentUtils.selectFileSys(BookSourceActivity.this, IntentUtils.IMPORT_BOOK_SOURCE_LOCAL);
            } else if (itemId == R.id.action_import_book_source_online) {
                DialogUtil.showEditTextDialog(this, getResources().getString(R.string.import_book_source_on_line),
                        String.format("%s://", getResources().getString(R.string.input_book_source_url)),
                        getResources().getString(R.string.input_book_source_url), new DialogUtil.DialogActionListener() {
                            @Override
                            public void onClick(String s) {
                                showDialog();
                                mPresenter.getNetSource(s);
                            }
                        });
                //二维码导入
            } else if (itemId == R.id.action_import_book_source_rwm) {
                ARouter.getInstance()
                        .build(ARouterConstants.ACT_QRCODE)
                        .navigation(this, IntentUtils.IMPORT_BOOK_SOURCE_QRCODE);
            } else if (itemId == R.id.action_del_select) {
                List<BookSource> BookSources = ((BookSourceAdapter) mAdapter).getSelectedBookSource();
                if (BookSources.size() > 0) {
                    DialogUtil.showMessagePositiveDialog(mContext, getResources().getString(R.string.dialog_title),
                            String.format(getResources().getString(R.string.delete_selected_book_source), BookSources.size()),
                            getResources().getString(R.string.cancel), null, getResources().getString(R.string.ok), (dialog, index) -> {
                                mPresenter.deleteSelectedSource(BookSources);
                            }, true);
                }
            } else if (itemId == R.id.action_check_book_source) {
                List<BookSource> selectedBookSource = SourceHelp.INSTANCE.getSelectedBookSource();
                if (selectedBookSource == null || selectedBookSource.size() == 0) {
                    toast(getResources().getString(R.string.please_choose_book_source));
                } else {
                    mPresenter.checkBookSource(mContext, selectedBookSource);
                }
            } else if (itemId == R.id.action_share_wifi) {
                List<BookSource> BookSources;
                BookSources = ((BookSourceAdapter) mAdapter).getSelectedBookSource();
                if (BookSources.size() > 0) {
                    ShareService.startThis(mContext, BookSources);
                } else {
                    toast(getResources().getString(R.string.no_book_source_selected));
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
                    mAdapter.getRealAllData().get(i).setEnabled(true);
                    mAdapter.notifyItemChanged(i);
                }
                //已选
            } else if (index == 1) {
                binding.mEtSearchKey.setText("enable");
                binding.mEtSearchKey.setSelection(binding.mEtSearchKey.length());
                //反选
            } else if (index == 2) {
                for (int i = 0; i < mAdapter.getRealAllData().size(); i++) {
                    mAdapter.getRealAllData().get(i).setEnabled(!mAdapter.getRealAllData().get(i).getEnabled());
                    mAdapter.notifyItemChanged(i);
                }
            }
            mPresenter.saveData(mAdapter.getAllData());
            binding.mDropdownMenu.close();
        }

        //
        //排序
        //@param index index item index
        //
        @RequiresApi(api = Build.VERSION_CODES.N)
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

        //
        //choose book source by group
        //@param index     index the item index
        //@param groupName the item name in the index
        //
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
            mTvEmptyText.setText(getResources().getString(R.string.no_book_source_tips));
        }
        binding.mTvStopCheck.setOnClickListener(v -> CheckSourceService.stop(mContext));
    }

    @Override
    public void showLocalBookSource(List<BookSource> list) {
        BookSourceAdapter adapter = (BookSourceAdapter) mAdapter;
        adapter.setSortType(mSortType);
        mAdapter.clear();
        mAdapter.addAll(list);
        hideDialog();
    }

    @Override
    public void showAddNetSourceResult(List<BookSource> list) {
        if (list != null && list.size() > 0) {
            showDialog();
            mPresenter.getLocalBookSource("");
            toast(String.format(getResources().getString(R.string.input_book_source_url_success), list.size()));
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

    public void saveBookSource(List<BookSource> BookSourceList) {
        mPresenter.saveData(BookSourceList);
    }

    public void saveBookSource(BookSource bookSource) {
        mPresenter.saveData(bookSource);
    }

    public void deleteBookSource(BookSource bookSource) {
        mPresenter.delData(bookSource);
    }


    private void updateSortMenu() {
        List<String> groupList = SourceHelp.INSTANCE.getGroupList();
        groupList.add(0, getResources().getString(R.string.all));
        mFilterMenuAdapter.setGroupList(groupList);
    }


    ///检测书源是否可用发来的消息
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IntentUtils.IMPORT_BOOK_SOURCE_LOCAL) {
                if (data != null && data.getData() != null) {
                    mPresenter.loadBookSourceFromFile(data.getData());
                }
            } else if (requestCode == IntentUtils.IMPORT_BOOK_SOURCE_QRCODE) {
                if (data != null) {
                    String result = data.getStringExtra("result");
                    //如果是http开头,访问网络
                    if (result != null) {
                        showDialog();
                        mPresenter.importSource(result);
                    } else {
                        ToastUtil.showToast(getResources().getString(R.string.type_un_correct));
                    }
                }
            }
        }
    }
}
*/
