package com.liuzhenli.reader.view.filter.typeview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.liuzhenli.common.base.CommonAdapter;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.reader.view.filter.interfaces.OnFilterItemClickListener;

import java.util.List;

/**
 * 单列ListView
 */
public class SingleListView<DATA> extends ListView implements AdapterView.OnItemClickListener {

    private CommonAdapter<DATA> mAdapter;
    private OnFilterItemClickListener<DATA> mOnItemClickListener;

    public SingleListView(Context context) {
        this(context, null);
    }

    public SingleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SingleListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        setDivider(null);
        setDividerHeight(0);
        setSelector(new ColorDrawable(Color.TRANSPARENT));

        setOnItemClickListener(this);
    }


    public SingleListView<DATA> adapter(CommonAdapter<DATA> adapter) {
        this.mAdapter = adapter;
        setAdapter(adapter);
        return this;
    }

    public SingleListView<DATA> onItemClick(OnFilterItemClickListener<DATA> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        return this;
    }

    public void setList(List<DATA> list, int checkedPosition) {
        mAdapter.setList(list);

        if (checkedPosition != -1) {
            setItemChecked(checkedPosition, true);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (ClickUtils.isFastDoubleClick()) {
            return;
        }
        DATA item = mAdapter.getItem(position);
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(item, position);
        }
    }


}
