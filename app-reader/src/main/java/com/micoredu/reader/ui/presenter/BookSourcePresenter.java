package com.micoredu.reader.ui.presenter;


/**
 * Description:BookSourceActivity Presenter
 *
 * @author liuzhenli 2020/11/9
 * Email: 848808263@qq.com
 */
/*
public class BookSourcePresenter extends RxPresenter<BookSourceContract.View> implements BookSourceContract.Presenter<BookSourceContract.View> {
    private ReaderApi mApi;

    @Inject
    public BookSourcePresenter(ReaderApi mApi) {
        this.mApi = mApi;
    }

    @Override
    public void getLocalBookSource(String key) {
        addSubscribe(RxUtil.subscribe(Observable.create(emitter -> {
            //获取全部书源
            if (TextUtils.isEmpty(key)) {
                emitter.onNext(SourceHelp.INSTANCE.getAllBookSource());
            } else if (TextUtils.equals("enable", key)) {
                emitter.onNext(SourceHelp.INSTANCE.getSelectedBookSource());
            } else {
                emitter.onNext(SourceHelp.INSTANCE.getSourceByKey(key));
            }
        }), new SampleProgressObserver<List<BookSource>>() {
            @Override
            public void onNext(@NotNull List<BookSource> list) {
                mView.showLocalBookSource(list);
            }
        }));
    }

    @Override
    public void setEnable(BookSource bookSource, boolean enable) {

    }

    @Override
    public void setTop(BookSource bookSource, boolean enable) {
        addSubscribe(RxUtil.subscribe(Observable.create(emitter -> {
            SourceHelp.INSTANCE.toTop(bookSource);
            //get all of the book sources
            emitter.onNext(SourceHelp.INSTANCE.getAllBookSource());
        }), new SampleProgressObserver<List<BookSource>>() {
            @Override
            public void onNext(List<BookSource> list) {
                mView.showLocalBookSource(list);
            }
        }));
    }

    @Override
    public void getNetSource(String url) {
        ApiManager.getInstance().setBookSource(url);
        addSubscribe(RxUtil.subscribe(mApi.getBookSource(""), new SampleProgressObserver<ResponseBody>(mView) {
            @Override
            public void onNext(@androidx.annotation.NonNull ResponseBody data) {
                configNetBookSource(data);
            }
        }));
    }

    @Override
    public void deleteSelectedSource(List<BookSource> BookSources) {
        addSubscribe(RxUtil.subscribe(Observable.create(emitter -> {
            if (BookSources == null || BookSources.size() == 0) {
                return;
            }
            for (BookSource BookSource : BookSources) {
                SourceHelp.INSTANCE.removeBookSource(BookSource);
            }
            emitter.onNext(BookSources.size());
        }), new SampleProgressObserver<Integer>() {
            @Override
            public void onNext(@androidx.annotation.NonNull Integer aBoolean) {
                mView.shoDeleteBookSourceResult();
            }
        }));
    }

    @Override
    public void checkBookSource(Context context, List<BookSource> selectedBookSource) {
        CheckSourceService.start(context, selectedBookSource);
    }

//
//   @param uri 1./storage/emulated/0/Documents/ShuFang/ShuFang/Backups/myBookSource.json
//              2.content://com.android.externalstorage.documents/document/primary:Documents/ShuFang/ShuFang/Backups/myBookSource.json
//
    @Override
    public void loadBookSourceFromFile(@NonNull Uri uri) {

        if (TextUtils.isEmpty(uri.getPath())) {
            mView.showError(new ApiException(1000, new Throwable(BaseApplication.Companion.getInstance().getString(R.string.read_file_error))));
            return;
        }
        String json;
        DocumentFile file;
        try {
            if (FileUtils.INSTANCE.isContentFile(uri)) {
                json = new String(DocumentUtil.readBytes(BaseApplication.Companion.getInstance(), uri), Charsets.UTF_8);
            } else {
                file = DocumentFile.fromFile(new File(uri.toString()));
                json = DocumentHelper.readString(file);
            }
        } catch (Exception e) {
            mView.showError(new ApiException(1000, new Throwable(BaseApplication.Companion.getInstance().getString(R.string.can_not_open))));
            return;
        }

        if (!isEmpty(json)) {
            importSource(json);
        } else {
            mView.showError(new ApiException(1000, new Throwable(BaseApplication.Companion.getInstance().getString(R.string.read_file_error))));
        }
    }


    public void importSource(String string) {

        Observable<List<BookSource>> observable = BookSourceManager.importSource(string);
        if (observable != null) {
            RxUtil.subscribe(observable, new SampleProgressObserver<List<BookSource>>(mView) {
                @Override
                public void onNext(@NonNull List<BookSource> bookSource) {
                    mView.showLocalBookSource(bookSource);
                }
            });
        } else {
            mView.showError(new ApiException(1000, new Throwable(BaseApplication.Companion.getInstance().getString(R.string.type_un_correct))));
        }
    }

    public void saveData(List<BookSource> data) {
        addSubscribe(RxUtil.subscribe(Observable.create(emitter -> {
            ThreadUtils.getInstance().getExecutorService().execute(() -> SourceHelp.INSTANCE.update(data));
            emitter.onNext(data == null ? 0 : data.size());
        }), new SampleProgressObserver<Integer>() {
            @Override
            public void onNext(@NotNull Integer aBoolean) {
            }
        }));

    }

    public void saveData(BookSource data) {
        ThreadUtils.getInstance().getExecutorService().execute(() -> SourceHelp.INSTANCE.update(data));
    }

    public void delData(BookSource data) {
        ThreadUtils.getInstance().getExecutorService().execute(() -> SourceHelp.INSTANCE.removeBookSource(data));
    }

    private void configNetBookSource(ResponseBody data) {
        Observable<List<BookSource>> listObservable = Observable.create(emitter -> {
            List<BookSource> bookSourceList = new ArrayList<>();
            byte[] bytes = new byte[0];
            try {
                bytes = data.bytes();
                String s = new String(bytes);
                if (StringUtils.isJsonArray(s)) {
                    bookSourceList = GsonUtils.parseJArray(s, BookSource.class);
                }
                //发现数据默认可见
                for (int i = 0; i < bookSourceList.size(); i++) {
                    if (!TextUtils.isEmpty(bookSourceList.get(i).getExploreUrl())) {
                        bookSourceList.get(i).setEnabledExplore(true);
                    }
                    //存入数据库
                    SourceHelp.INSTANCE.insertBookSource(bookSourceList.get(i));
                }
                //有发现项的书源
                emitter.onNext(bookSourceList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        addSubscribe(RxUtil.subscribe(listObservable, new SampleProgressObserver<List<BookSource>>() {
            @Override
            public void onNext(List<BookSource> bookSourceList) {
                mView.showAddNetSourceResult(bookSourceList);
            }
        }));
    }
}
*/
