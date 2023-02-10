/*
package com.liuzhenli.reader.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.liuzhenli.common.utils.image.ImageUtil;
import com.liuzhenli.common.widget.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.micoredu.reader.R;
import com.micoredu.reader.bean.Book;
import com.micoredu.reader.databinding.ItemBookshelfListBinding;

*/
/**
 * describe:书架
 *
 * @author Liuzhenli on 2020-01-11 15:47
 *//*

public class BookShelfAdapter extends RecyclerArrayAdapter<Book> {


    public BookShelfAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder<Book> OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookShelfHolder(parent, R.layout.item_bookshelf_list);
    }

    public static class BookShelfHolder extends BaseViewHolder<Book> {
        ItemBookshelfListBinding binding;

        public BookShelfHolder(ViewGroup parent, int res) {
            super(parent, res);
            binding = ItemBookshelfListBinding.bind(itemView);
        }

        @Override
        public void setData(Book item) {
            super.setData(item);
            binding.tvName.setText(item.getBookInfoBean().getName() == null ? "[未知书名]" : item.getBookInfoBean().getName());
            binding.mTvAuthor.setText(String.format("%s 著", TextUtils.isEmpty(item.getBookInfoBean().getAuthor()) ? "佚名" : item.getBookInfoBean().getAuthor()));
            binding.mTvRead.setText(String.format("读至:%s", item.getTitle() == null ? "章节名未知" : item.getTitle()));
            binding.tvLast.setText(String.format("最新:%s", item.getLastChapterName() == null ? "章节名未知" : item.getLastChapterName()));
            if (item.getBookInfoBean() != null) {
                ImageUtil.setImage(mContext, item.getBookInfoBean().getCoverUrl(), R.drawable.book_cover, R.drawable.book_cover, binding.mIvCover);
            }
            if (item.isLoading()) {
                binding.bvUnread.setVisibility(View.GONE);
                binding.rlLoading.setVisibility(View.VISIBLE);
                binding.rlLoading.start();
            } else {
                binding.bvUnread.setVisibility(View.VISIBLE);
                //未读章节数
                binding.bvUnread.setBadgeCount(item.getUnreadChapterNum());
                //显示有(无)更新
                binding.bvUnread.setHighlight(item.getHasUpdate());
                //停止loading
                binding.rlLoading.setVisibility(View.GONE);
                binding.rlLoading.stop();
            }
        }
    }

}
*/
