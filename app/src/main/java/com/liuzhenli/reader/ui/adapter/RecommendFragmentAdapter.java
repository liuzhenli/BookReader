package com.liuzhenli.reader.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liuzhenli.reader.view.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.reader.view.recyclerview.adapter.RecyclerArrayAdapter;
import com.micoredu.readerlib.bean.BookSourceBean;
import com.microedu.reader.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description:
 *
 * @author liuzhenli 2020/9/27
 * Email: 848808263@qq.com
 */
public class RecommendFragmentAdapter extends RecyclerArrayAdapter<BookSourceBean> {
    public RecommendFragmentAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(parent, R.layout.item_book_source);
    }

    class ItemViewHolder extends BaseViewHolder<BookSourceBean> {
        @BindView(R.id.tv_source_web_name)
        TextView mTvSite;

        public ItemViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(BookSourceBean item) {
            super.setData(item);
            mTvSite.setText(item.getBookSourceName());
        }
    }
}
