package com.liuzhenli.reader.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuzhenli.common.widget.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.micoredu.readerlib.bean.SearchBookBean;
import com.microedu.reader.R;
import com.microedu.reader.databinding.ItemChangeSourceBinding;

/**
 * Description:
 *
 * @author liuzhenli 2021/1/31
 * Email: 848808263@qq.com
 */
public class ChangeSourceAdapter extends RecyclerArrayAdapter<SearchBookBean> {
    public ChangeSourceAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChangeSourceViewHolder(parent, R.layout.item_change_source);
    }

    public static class ChangeSourceViewHolder extends BaseViewHolder<SearchBookBean> {
        ItemChangeSourceBinding inflate;

        public ChangeSourceViewHolder(ViewGroup parent, int layoutResID) {
            super(parent, layoutResID);
            inflate = ItemChangeSourceBinding.inflate(LayoutInflater.from(mContext));
        }

        @Override
        public void setData(SearchBookBean item) {
            super.setData(item);
            inflate.tvBookSourceSite.setText(item.getOrigin());
            inflate.tvBookLastChapter.setText(item.getLastChapter());
            if (item.getIsCurrentSource()) {
                inflate.tvIsCurrentSelected.setVisibility(View.VISIBLE);
            } else {
                inflate.tvIsCurrentSelected.setVisibility(View.GONE);
            }
        }
    }
}
