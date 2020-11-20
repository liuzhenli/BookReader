package com.liuzhenli.reader.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.reader.view.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.reader.view.recyclerview.adapter.RecyclerArrayAdapter;
import com.microedu.reader.R;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Description:
 *
 * @author liuzhenli 2020/11/19
 * Email: 848808263@qq.com
 */
public class KeyboardTopView extends PopupWindow {
    private List<String> dataList;
    private CallBack mCallBack;

    public KeyboardTopView(Context context, List<String> dataList, CallBack mCallBack) {
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.dataList = dataList;
        this.mCallBack = mCallBack;
        init(context);
    }

    private void init(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_keybord_top, null);
        RecyclerView recyclerView = inflate.findViewById(R.id.keyboard_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(new RecyclerArrayAdapter<String>(context) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                TextView textView = new TextView(context);

                return new BaseViewHolder<String>(textView) {
                    @Override
                    public void setData(String item) {
                        super.setData(item);
                        textView.setText(item);
                        ClickUtils.click(textView, new Consumer() {
                            @Override
                            public void accept(Object o) throws Exception {
                                if (mCallBack != null) {
                                    mCallBack.onItemClick(item);
                                }
                            }
                        });
                    }
                };
            }
        });
    }

    public interface CallBack {
        void onItemClick(String key);
    }
}
