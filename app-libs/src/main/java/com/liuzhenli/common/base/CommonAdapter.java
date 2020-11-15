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
    protected Context mContext;
    private int mLayoutResId;

    public CommonAdapter(List<T> mData, Context mContext, int mLayoutResId) {
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mData = mData;
        this.mContext = mContext;
        this.mLayoutResId = mLayoutResId;
    }

    public void setList(List<T> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void addData(List<T> data) {
        if (mData != null) {
            this.mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void addToFirst(T t) {
        if (t == null) {
            return;
        }
        this.mData.add(0, t);
        notifyDataSetChanged();
    }

    public void addToFirst(List<T> list) {
        if (list == null) {
            return;
        }
        this.mData.addAll(0, list);
        notifyDataSetChanged();
    }


    /**
     * 添加数据到末尾，用于上拉加载等情况。<BR>
     * 不清楚原集合，添加到末尾。
     */
    public void addToLast(List<T> list) {
        if (list == null) {
            return;
        }
        this.mData.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 添加元素到集合末尾
     */
    public void addToLast(T t) {
        if (t == null) {
            return;
        }
        this.mData.add(t);
        notifyDataSetChanged();
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
