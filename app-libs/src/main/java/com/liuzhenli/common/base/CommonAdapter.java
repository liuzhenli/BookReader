package com.liuzhenli.common.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Description:BaseAdapter 适用一个itemType  不给convertView设置LayoutParams
 *
 * @author liuzhenli 2020/10/9
 * Email: 848808263@qq.com
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    private List<T> mData;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int mLayoutResId;

    public CommonAdapter(List<T> mData, Context mContext, int mLayoutResId) {
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mData = mData;
        this.mContext = mContext;
        this.mLayoutResId = mLayoutResId;
    }

    public void updateData(List<T> data) {
        this.mData = data;
    }

    public void addData(List<T> data) {
        if (mData != null) {
            this.mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public T getItem(int position) {
        return null == mData ? null : mData.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonViewHolder commonViewHolder = CommonViewHolder.get(mContext, convertView, parent, mLayoutResId, position);
        convert(commonViewHolder, getItem(position));
        return commonViewHolder.getConvertView();
    }

    public abstract void convert(CommonViewHolder commonViewHolder, T itemData);
}
