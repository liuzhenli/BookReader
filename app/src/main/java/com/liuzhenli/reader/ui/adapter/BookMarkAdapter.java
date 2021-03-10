package com.liuzhenli.reader.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.liuzhenli.common.widget.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.micoredu.readerlib.bean.BookmarkBean;
import com.micoredu.readerlib.helper.ReadConfigManager;
import com.microedu.reader.R;
import com.microedu.reader.databinding.ItemBookMarkBinding;

/**
 * Description:bookmark item
 *
 * @author liuzhenli 2021/1/26
 * Email: 848808263@qq.com
 */
public class BookMarkAdapter extends RecyclerArrayAdapter<BookmarkBean> {
    boolean isFromReadPage;

    public BookMarkAdapter(Context context, boolean isFromReadPage) {
        super(context);
        this.isFromReadPage = isFromReadPage;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookChapterItemAdapter(parent, R.layout.item_book_mark);
    }


    public class BookChapterItemAdapter extends BaseViewHolder<BookmarkBean> {

        ItemBookMarkBinding binding;

        public BookChapterItemAdapter(ViewGroup parent, int resId) {
            super(parent, resId);
            binding = ItemBookMarkBinding.inflate(LayoutInflater.from(mContext));
        }

        @Override
        public void setData(BookmarkBean item) {
            super.setData(item);
            if (TextUtils.isEmpty(item.getChapterName())) {
                item.setChapterName("章节未命名");
            }
            binding.tvBookChapterName.setText(item.getChapterName());
            binding.tvBookChapterContent.setText(item.getContent());
            if (isFromReadPage) {
                binding.tvBookChapterName.setTextColor(ReadConfigManager.getInstance().getTextColor());
                binding.viewBookChapter.setBackground(ReadConfigManager.getInstance().getTextBackground(mContext));
            }
        }
    }
}
