package com.liuzhenli.write.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.liuzhenli.common.widget.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.liuzhenli.write.R;
import com.liuzhenli.write.bean.WriteChapter;
import com.liuzhenli.write.databinding.ItemWriteChapterBinding;

/**
 * Description:WriteBook chapter adapter
 *
 * @author liuzhenli 2021/5/19
 * Email: 848808263@qq.com
 */
public class ChapterListAdapter extends RecyclerArrayAdapter<WriteChapter> {
    public ChapterListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_write_chapter);
    }

    class ViewHolder extends BaseViewHolder<WriteChapter> {
        ItemWriteChapterBinding mBinding;

        public ViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            mBinding = ItemWriteChapterBinding.bind(itemView);
        }

        @Override
        public void setData(WriteChapter item) {
            super.setData(item);
            mBinding.tvBookName.setText(item.getTitle());
        }
    }
}
