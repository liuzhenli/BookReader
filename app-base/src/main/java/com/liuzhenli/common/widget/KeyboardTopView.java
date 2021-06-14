package com.liuzhenli.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liuzhenli.common.R;
import com.liuzhenli.common.widget.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2020/11/19
 * Email: 848808263@qq.com
 */
public class KeyboardTopView extends FrameLayout {
    private List<String> dataList;
    private CallBack mCallBack;
    private String[] helpWords = {"@", "&", "|", "%", "/", ":", "[", "]", "(", ")", "{", "}", "<", ">", "\\", "$", "#", "!", ".",
            "href", "src", "textNodes", "xpath", "json", "css", "id", "class", "tag"};
    private RecyclerArrayAdapter<String> mAdapter;

    public KeyboardTopView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public KeyboardTopView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KeyboardTopView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setData(CallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    public void setData(CallBack mCallBack, String[] helpWords) {
        this.mCallBack = mCallBack;
        this.helpWords = helpWords;
        dataList = Arrays.asList(helpWords);
        mAdapter.setRealAllData(dataList);
    }

    private void init(Context context) {
        dataList = Arrays.asList(helpWords);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_keybord_top, this);
        RecyclerView recyclerView = view.findViewById(R.id.keyboard_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        mAdapter = new RecyclerArrayAdapter<String>(context, dataList) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(context).inflate(R.layout.item_dot, parent, false);
                TextView textView = view.findViewById(R.id.text_view);
                return new BaseViewHolder<String>(view) {
                    @Override
                    public void setData(String item) {
                        super.setData(item);
                        textView.setText(item);
                        textView.setOnClickListener(v -> {
                            if (mCallBack != null) {
                                mCallBack.onItemClick(item);
                            }
                        });
                    }
                };
            }
        };
        recyclerView.setAdapter(mAdapter);
    }

    public interface CallBack {
        void onItemClick(String key);
    }
}
