package com.liuzhenli.write.ui.adapter;

import android.view.ViewGroup;

import com.liuzhenli.common.widget.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.write.R;
import com.liuzhenli.write.bean.WriteBook;
import com.liuzhenli.write.databinding.ItemBannerBookBinding;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2021/5/16
 * Email: 848808263@qq.com
 */
public class CreateBookBannerAdapter extends BannerAdapter<WriteBook, CreateBookBannerAdapter.BannerViewHolder> {


    public CreateBookBannerAdapter(List<WriteBook> datas) {
        super(datas);
    }

    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new BannerViewHolder(parent, R.layout.item_banner_book);
    }

    @Override
    public void onBindView(BannerViewHolder holder, WriteBook data, int position, int size) {
        holder.setData(data);
    }


    class BannerViewHolder extends BaseViewHolder<WriteBook> {

        private final ItemBannerBookBinding mBinding;

        public BannerViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            mBinding = ItemBannerBookBinding.bind(itemView);
        }

        @Override
        public void setData(WriteBook item) {
            super.setData(item);
            mBinding.tvBookName.setText(item.bookName);
        }
    }
}
