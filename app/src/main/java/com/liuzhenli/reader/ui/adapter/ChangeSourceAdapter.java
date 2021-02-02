package com.liuzhenli.reader.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liuzhenli.reader.view.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.reader.view.recyclerview.adapter.RecyclerArrayAdapter;
import com.micoredu.readerlib.bean.SearchBookBean;
import com.microedu.reader.R;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        @BindView(R.id.tv_book_source_site)
        TextView mTvBookSite;
        @BindView(R.id.tv_book_last_chapter)
        TextView mTvBookLastChapterName;

        @BindView(R.id.tv_is_current_selected)
        View mViewIsSelected;

        public ChangeSourceViewHolder(ViewGroup parent, int layoutResID) {
            super(parent, layoutResID);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(SearchBookBean item) {
            super.setData(item);
            mTvBookSite.setText(item.getOrigin());
            mTvBookLastChapterName.setText(item.getLastChapter());
            if (item.getIsCurrentSource()) {
                mViewIsSelected.setVisibility(View.VISIBLE);
            } else {
                mViewIsSelected.setVisibility(View.GONE);
            }
        }
    }
}
