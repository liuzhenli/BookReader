package com.micoredu.reader.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.liuzhenli.common.widget.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.microedu.lib.reader.R;
import com.micoredu.reader.bean.BookChapter;
import com.microedu.lib.reader.databinding.ItemBookChapterBinding;
import com.micoredu.reader.utils.ReadConfigManager;


/**
 * Description:book item
 *
 * @author liuzhenli 2021/1/26
 * Email: 848808263@qq.com
 */
public class BookChapterAdapter extends RecyclerArrayAdapter<BookChapter> {

    private boolean isFromReadPage;

    public BookChapterAdapter(Context context, boolean isFromReadPage) {
        super(context);
        this.isFromReadPage = isFromReadPage;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookChapterItemAdapter(parent, R.layout.item_book_chapter);
    }


    public class BookChapterItemAdapter extends BaseViewHolder<BookChapter> {

        ItemBookChapterBinding binding;

        public BookChapterItemAdapter(ViewGroup parent, int resId) {
            super(parent, resId);
            binding = ItemBookChapterBinding.bind(itemView);
        }

        @Override
        public void setData(BookChapter item) {
            super.setData(item);
            if (TextUtils.isEmpty(item.getTitle())) {
                item.setTitle("章节未命名");
            }
            binding.tvBookChapterName.setText(item.getTitle());
            if (isFromReadPage) {
                binding.tvBookChapterName.setTextColor(ReadConfigManager.getInstance().getTextColor());
                binding.viewBookChapter.setBackground(ReadConfigManager.getInstance().getTextBackground(mContext));
            }
        }
    }
}
