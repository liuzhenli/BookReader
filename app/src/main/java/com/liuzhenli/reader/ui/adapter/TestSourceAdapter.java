package com.liuzhenli.reader.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.liuzhenli.common.widget.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.microedu.reader.R;
import com.microedu.reader.databinding.ItemTestSourceBinding;

/**
 * Description:
 *
 * @author liuzhenli 2020/11/18
 * Email: 848808263@qq.com
 */
public class TestSourceAdapter extends RecyclerArrayAdapter<String> {
    public TestSourceAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_test_source);
    }

    static class ViewHolder extends BaseViewHolder<String> {
        ItemTestSourceBinding inflate;

        public ViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            inflate = ItemTestSourceBinding.inflate(LayoutInflater.from(mContext));
        }

        @Override
        public void setData(String item) {
            super.setData(item);
            inflate.tvTesting.setText(item);
        }
    }
}
