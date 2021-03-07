package com.liuzhenli.reader.ui.presenter;

import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.reader.network.Api;
import com.liuzhenli.common.observer.SampleProgressObserver;
import com.liuzhenli.reader.ui.contract.LocalFileContract;
import com.liuzhenli.common.utils.Constant;
import com.liuzhenli.common.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.observers.DisposableObserver;

import static com.liuzhenli.common.utils.Constant.Fileuffix.PDF;
import static com.liuzhenli.common.utils.Constant.Fileuffix.TET;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-12-15 10:29
 */
public class LocalFilePresenter extends RxPresenter<LocalFileContract.View> implements LocalFileContract.Presenter<LocalFileContract.View> {

    private Api mApi;

    @Inject
    public LocalFilePresenter(Api api) {
        this.mApi = api;
    }

    @Override
    public void getDirectory(File file) {
        DisposableObserver subscribe = RxUtil.subscribe(Observable
                .create((ObservableOnSubscribe<ArrayList<HashMap<String, Object>>>) subscriber -> {
                    //子线程
                    try {
                        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
                        File[] files = FileUtils.read(file);
                        int size = 0;
                        if (files != null) {
                            size = files.length;
                        }
                        data.clear();
                        try {
                            for (int i = 0; i < size; i++) {
                                HashMap<String, Object> map = new HashMap<String, Object>();
                                String fileName = files[i].getName();
                                // 文件
                                if (files[i].isDirectory()) {
                                    map.put(Constant.FileAttr.IMAGE, Constant.FileAttr.ZERO);
                                } else if (files[i].isFile()) {
                                    if (fileName.endsWith(TET)) {
                                        map.put(Constant.FileAttr.IMAGE, TET);
                                    } else if (fileName.endsWith(PDF)) {
                                        map.put("mBitmap", "pdf");
                                    } else if (fileName.endsWith(".epub")) {
                                        map.put("mBitmap", "epub");
                                    }
                                }
                                map.put(Constant.FileAttr.FILE, files[i]);
                                map.put(Constant.FileAttr.NAME, fileName);
                                map.put(Constant.FileAttr.CHECKED, false);
                                if (files[i].listFiles() != null) {// 文件夹非空
                                    map.put(Constant.FileAttr.SIZE, "(" + files[i].listFiles().length + ")");
                                } else if (files[i].isFile()) {// 是文件就不显示
                                    map.put(Constant.FileAttr.SIZE, files[i].length() + "");
                                } else {// 文件夹为空
                                    map.put(Constant.FileAttr.SIZE, "(0)");
                                }
                                data.add(map);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Collections.sort(data, (lhs, rhs) -> (((String) lhs.get(Constant.FileAttr.NAME)).toLowerCase()).compareTo(((String) rhs.get(Constant.FileAttr.NAME)).toLowerCase()));

                        List<HashMap<String, Object>> temp = new ArrayList<HashMap<String, Object>>();
                        Iterator<HashMap<String, Object>> list = data.iterator();
                        while (list.hasNext()) {
                            HashMap<String, Object> map = list.next();
                            String fileName = (String) map.get(Constant.FileAttr.NAME);
                            if (fileName != null && (TET.endsWith(fileName) || PDF.endsWith(fileName) || Constant.Fileuffix.EPUB.endsWith(fileName))) {
                                temp.add(map);
                                list.remove();
                            }
                        }
                        data.addAll(temp);
                        temp = null;
                        subscriber.onNext(data);
                        subscriber.onComplete();
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                }), new SampleProgressObserver<ArrayList<HashMap<String, Object>>>(mView) {
            @Override
            public void onNext(ArrayList<HashMap<String, Object>> data) {
                if (mView != null) {
                    mView.showDirectory(data, file);
                }
            }


        });
        addSubscribe(subscribe);
    }
}
