package com.liuzhenli.reader.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liuzhenli.reader.view.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.reader.view.recyclerview.adapter.RecyclerArrayAdapter;
import com.micoredu.readerlib.bean.BookChapterBean;
import com.micoredu.readerlib.bean.BookmarkBean;
import com.micoredu.readerlib.helper.ReadConfigManager;
import com.microedu.reader.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description:bookmark item
 *
 * @author liuzhenli 2021/1/26
 * Email: 848808263@qq.com
 */
public class BookMarkAdapter extends RecyclerArrayAdapter<BookmarkBean> {

    public BookMarkAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookChapterItemAdapter(parent, R.layout.item_book_mark);
    }


    public class BookChapterItemAdapter extends BaseViewHolder<BookmarkBean> {
        @BindView(R.id.tv_book_chapter_name)
        TextView mTvBookChapterName;

        @BindView(R.id.tv_book_chapter_content)
        TextView mTvBookMarkContent;
        @BindView(R.id.view_book_chapter)
        View mVItemRoot;

        public BookChapterItemAdapter(ViewGroup parent, int resId) {
            super(parent, resId);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(BookmarkBean item) {
            super.setData(item);
            if (TextUtils.isEmpty(item.getChapterName())) {
                item.setChapterName("章节未命名");
            }
            mTvBookChapterName.setText(item.getChapterName());
            mTvBookMarkContent.setText(item.getContent());
            mTvBookChapterName.setTextColor(ReadConfigManager.getInstance().getTextColor());
            mVItemRoot.setBackground(ReadConfigManager.getInstance().getTextBackground(mContext));
        }
    }
}
