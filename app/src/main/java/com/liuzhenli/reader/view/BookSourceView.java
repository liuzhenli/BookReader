package com.liuzhenli.reader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.liuzhenli.reader.ui.adapter.BookSourceViewAdapter;
import com.liuzhenli.reader.view.recyclerview.EasyRecyclerView;
import com.liuzhenli.reader.view.recyclerview.adapter.RecyclerArrayAdapter;
import com.micoredu.readerlib.bean.BookSourceBean;
import com.microedu.reader.R;

import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2020/11/29
 * Email: 848808263@qq.com
 */
public class BookSourceView extends FrameLayout {

    private BookSourceViewAdapter adapter;

    public BookSourceView(@NonNull Context context) {
        this(context, null);
    }

    public BookSourceView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookSourceView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        adapter = new BookSourceViewAdapter(getContext());
        ViewGroup mRoot = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.view_book_source, this);
        EasyRecyclerView mRecyclerView = mRoot.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (onItemClick != null) {
                    onItemClick.onItemClick(adapter.getItem(position));
                }
                setVisibility(GONE);
            }
        });
    }

    public void setData(List<BookSourceBean> data) {
        adapter.clear();
        adapter.addAll(data);
        adapter.notifyDataSetChanged();
    }

    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void onItemClick(BookSourceBean bookSourceBean);
    }
}
