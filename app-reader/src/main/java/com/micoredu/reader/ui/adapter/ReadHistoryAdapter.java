package com.micoredu.reader.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.liuzhenli.common.utils.TimeUtils;
import com.liuzhenli.common.widget.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.micoredu.reader.R;
import com.micoredu.reader.bean.ReadHistory;
import com.micoredu.reader.databinding.ItemReadHistoryBinding;

/**
 * Description:
 *
 * @author liuzhenli 2021/10/12
 * Email: 848808263@qq.com
 */
public class ReadHistoryAdapter extends RecyclerArrayAdapter<ReadHistory> {
    public ReadHistoryAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_read_history);
    }

    class ViewHolder extends BaseViewHolder<ReadHistory> {
        ItemReadHistoryBinding binding;

        public ViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            binding = ItemReadHistoryBinding.bind(itemView);
        }

        @Override
        public void setData(ReadHistory item) {
            super.setData(item);
            binding.tvBookName.setText(item.bookName);
            binding.tvReadTime.setText(String.format("累计阅读:%s", TimeUtils.formatToHour(item.sumTime)));
        }
    }
}
