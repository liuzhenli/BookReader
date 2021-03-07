package com.liuzhenli.reader.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.liuzhenli.common.widget.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.micoredu.readerlib.bean.BookSourceBean;
import com.microedu.reader.R;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description:
 *
 * @author liuzhenli 2020/9/27
 * Email: 848808263@qq.com
 */
public class BookSourceViewAdapter extends RecyclerArrayAdapter<BookSourceBean> {
    public BookSourceViewAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(parent, R.layout.item_book_site);
    }

    class ItemViewHolder extends BaseViewHolder<BookSourceBean> {
        @BindView(R.id.tv_source_web_name)
        QMUIRoundButton mTvSite;

        public ItemViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(BookSourceBean item) {
            super.setData(item);
            mTvSite.setText(item.getBookSourceName());
            mTvSite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.callOnClick();
                }
            });
        }
    }
}
