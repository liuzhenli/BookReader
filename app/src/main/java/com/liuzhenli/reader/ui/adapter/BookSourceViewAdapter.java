package com.liuzhenli.reader.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.liuzhenli.common.widget.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.micoredu.reader.R;
import com.micoredu.reader.bean.BookSource;
import com.micoredu.reader.databinding.ItemBookSiteBinding;

/**
 * Description:
 *
 * @author liuzhenli 2020/9/27
 * Email: 848808263@qq.com
 */
public class BookSourceViewAdapter extends RecyclerArrayAdapter<BookSource> {
    public BookSourceViewAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(parent, R.layout.item_book_site);
    }

    static class ItemViewHolder extends BaseViewHolder<BookSource> {
        ItemBookSiteBinding binding;

        public ItemViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            binding = ItemBookSiteBinding.bind(itemView);
        }

        @Override
        public void setData(BookSource item) {
            super.setData(item);
            binding.tvSourceWebName.setText(item.getBookSourceName());
            binding.tvSourceWebName.setOnClickListener(v -> itemView.callOnClick());
        }
    }
}
