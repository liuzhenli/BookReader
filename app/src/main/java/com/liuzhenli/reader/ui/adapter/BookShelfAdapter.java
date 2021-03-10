package com.liuzhenli.reader.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuzhenli.common.utils.image.ImageUtil;
import com.liuzhenli.common.widget.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.micoredu.readerlib.bean.BookShelfBean;
import com.microedu.reader.R;
import com.microedu.reader.databinding.ItemBookshelfListBinding;

/**
 * describe:书架
 *
 * @author Liuzhenli on 2020-01-11 15:47
 */
public class BookShelfAdapter extends RecyclerArrayAdapter<BookShelfBean> {


    public BookShelfAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder<BookShelfBean> OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookShelfHolder(parent, R.layout.item_bookshelf_list);
    }

    public static class BookShelfHolder extends BaseViewHolder<BookShelfBean> {
        ItemBookshelfListBinding binding;

        public BookShelfHolder(ViewGroup parent, int res) {
            super(parent, res);
            binding = ItemBookshelfListBinding.inflate(LayoutInflater.from(mContext));
        }

        @Override
        public void setData(BookShelfBean item) {
            super.setData(item);
            binding.tvName.setText(item.getBookInfoBean().getName() == null ? "[未知书名]" : item.getBookInfoBean().getName());
            binding.mTvAuthor.setText(String.format("%s 著", TextUtils.isEmpty(item.getBookInfoBean().getAuthor()) ? "佚名" : item.getBookInfoBean().getAuthor()));
            binding.mTvRead.setText(String.format("读至:%s", item.getDurChapterName() == null ? "章节名未知" : item.getDurChapterName()));
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
