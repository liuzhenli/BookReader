package com.liuzhenli.reader.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.liuzhenli.reader.utils.image.ImageUtil;
import com.liuzhenli.reader.view.BadgeView;
import com.liuzhenli.reader.view.RotateLoading;
import com.liuzhenli.reader.view.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.reader.view.recyclerview.adapter.RecyclerArrayAdapter;
import com.micoredu.readerlib.bean.BookShelfBean;
import com.micoredu.readerlib.bean.SearchBookBean;
import com.microedu.reader.R;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        @BindView(R.id.iv_cover)
        ImageView mIvCover;
        @BindView(R.id.bv_unread)
        BadgeView mBvUnread;
        @BindView(R.id.rl_loading)
        RotateLoading mRlLoading;
        @BindView(R.id.fl_has_new)
        FrameLayout mFlHasNew;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_book_summary)
        TextView mTvSummary;
        @BindView(R.id.tv_author_info)
        TextView mTvAuthor;
        @BindView(R.id.vw_select)
        View mVwSelect;

        public BookShelfHolder(ViewGroup parent, int res) {
            super(parent, res);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(SearchBookBean item) {
            super.setData(item);
            mTvName.setText(item.getName() == null ? "[未知书名]" : item.getName());
            mTvSummary.setText(item.getIntroduce());
            mBvUnread.setText(item.getOriginNum()+"");
            mTvAuthor.setText(String.format("%s · %s", getAuthor(item), item.getKind()));
            ImageUtil.setImage(mContext, item.getCoverUrl(), R.drawable.book_cover, mIvCover);
        }

        private String getAuthor(SearchBookBean item) {
            return item.getAuthor() == null ? "佚名" : item.getAuthor();
        }
    }

}
