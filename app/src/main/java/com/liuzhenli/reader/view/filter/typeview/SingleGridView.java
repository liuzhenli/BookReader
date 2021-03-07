package com.liuzhenli.reader.view.filter.typeview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.liuzhenli.common.base.CommonAdapter;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.common.utils.DensityUtil;
import com.liuzhenli.reader.view.filter.interfaces.OnFilterItemClickListener;

import java.util.List;

/**
 * description:
 */
public class SingleGridView<DATA> extends GridView implements AdapterView.OnItemClickListener {

    private CommonAdapter<DATA> mAdapter;
    private OnFilterItemClickListener<DATA> mOnItemClickListener;

    public SingleGridView(Context context) {
        this(context, null);
    }

    public SingleGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SingleGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        setSelector(new ColorDrawable(Color.TRANSPARENT));
        setNumColumns(3);
        setBackgroundColor(Color.WHITE);
        setSmoothScrollbarEnabled(false);


        int dp = DensityUtil.dip2px(context, 15);

        setVerticalSpacing(dp);
        setHorizontalSpacing(dp);
        setPadding(dp, dp, dp, dp);

        setOnItemClickListener(this);
    }

    public SingleGridView<DATA> adapter(CommonAdapter<DATA> adapter) {
        this.mAdapter = adapter;
        setAdapter(adapter);
        return this;
    }

    public SingleGridView<DATA> onItemClick(OnFilterItemClickListener<DATA> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        return this;
    }


    public void setList(List<DATA> list, int checkedPositoin) {
        mAdapter.setList(list);

        if (checkedPositoin != -1) {
            setItemChecked(checkedPositoin, true);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (ClickUtils.isFastDoubleClick()) {
            return;
        }

        DATA item = mAdapter.getItem(position);

        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(item,position);
        }
    }


}
