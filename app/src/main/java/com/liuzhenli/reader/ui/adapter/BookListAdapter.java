package com.liuzhenli.reader.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuzhenli.common.utils.image.ImageUtil;
import com.liuzhenli.common.widget.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.micoredu.reader.bean.SearchBookBean;
import com.microedu.reader.R;
import com.microedu.reader.databinding.ItemBookListBinding;

/**
 * Description:
 *
 * @author liuzhenli 2020/10/9
 * Email: 848808263@qq.com
 */
public class BookListAdapter extends RecyclerArrayAdapter<SearchBookBean> {
    public BookListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookShelfHolder(parent, R.layout.item_book_list);
    }

    public class BookShelfHolder extends BaseViewHolder<SearchBookBean> {
        ItemBookListBinding binding;

        public BookShelfHolder(ViewGroup parent, int res) {
            super(parent, res);
            binding = ItemBookListBinding.bind(itemView);
        }

        @Override
        public void setData(SearchBookBean item) {
            super.setData(item);
            binding.tvName.setText(TextUtils.isEmpty(item.getName()) ? "[未知书名]" : item.getName());
            binding.tvBookSummary.setText(getSummary(item));
            //数据源的数量
            if (item.getOriginNum() > 1) {
                binding.bvUnread.setVisibility(View.VISIBLE);
                binding.bvUnread.setText(String.format("%s", item.getOriginNum()));
            } else {
                binding.bvUnread.setVisibility(View.GONE);
            }
            //作者
            if (!TextUtils.isEmpty(item.getKind())) {
                binding.tvAuthorInfo.setText(String.format("%s  %s", getAuthor(item), item.getKind()));
            } else {
                binding.tvAuthorInfo.setText(String.format("%s", getAuthor(item)));
            }
            ImageUtil.setRoundedCornerImage(mContext, item.getCoverUrl(), R.drawable.ic_book_cover_placeholder, R.drawable.book_cover, binding.mIvCover, 4);
        }

        private String getAuthor(SearchBookBean item) {
            return TextUtils.isEmpty(item.getAuthor()) ? "佚名" : String.format("%s·著", item.getAuthor());
        }
    }

    public String getSummary(SearchBookBean item) {
        if (item.getIntroduce() != null) {
            return item.getIntroduce();
        }
        if (item.getLastChapter() != null) {
            return item.getLastChapter();
        }
        return mBookSourceName;
    }

    private String mBookSourceName;

    public void setBookSourceName(String bookSourceName) {
        mBookSourceName = bookSourceName;
    }

}
