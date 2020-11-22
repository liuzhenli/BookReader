package com.liuzhenli.reader.view.filter.adapter;

import android.content.Context;

import com.liuzhenli.common.base.CommonAdapter;
import com.liuzhenli.common.base.CommonViewHolder;
import com.liuzhenli.reader.view.filter.view.FilterCheckedTextView;
import com.microedu.reader.R;

import java.util.Calendar;
import java.util.List;

/**
 * 菜单条目适配器
 */
public abstract class SimpleTextAdapter<T> extends CommonAdapter<T> {

    public SimpleTextAdapter(List<T> list, Context context) {
        super(list, context, R.layout.lv_item_filter);
    }

    @Override
    public void convert(CommonViewHolder commonViewHolder, T itemData) {
        FilterCheckedTextView checkedTextView = commonViewHolder.getView(R.id.tv_item_filter);
        checkedTextView.setText(provideText(itemData));
        initCheckedTextView(checkedTextView);
    }


    public abstract String provideText(T t);

    protected void initCheckedTextView(FilterCheckedTextView checkedTextView) {
    }
}
