package com.liuzhenli.common.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Description:
 *
 * @author liuzhenli 2020/10/10
 * Email: 848808263@qq.com
 */
public class CommonViewHolder {
    private int mPosition;
    private final SparseArray<View> mViews;
    private View mConvertView;

    public CommonViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        mViews = new SparseArray<>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        this.mPosition = position;
        mConvertView.setTag(this);
    }

    public static CommonViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new CommonViewHolder(context, parent, layoutId, position);
        }
        CommonViewHolder commonViewHolder = (CommonViewHolder) convertView.getTag();
        commonViewHolder.setPosition(position);
        return commonViewHolder;
    }

    private void setPosition(int position) {
        this.mPosition = position;
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }
}
