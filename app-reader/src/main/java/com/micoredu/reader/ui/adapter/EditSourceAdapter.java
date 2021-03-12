package com.micoredu.reader.ui.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ViewGroup;

import com.micoredu.reader.R;
import com.micoredu.reader.bean.EditSource;
import com.liuzhenli.common.widget.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.micoredu.reader.databinding.ItemEditSourceBinding;


/**
 * Description:
 *
 * @author liuzhenli 2020/11/16
 * Email: 848808263@qq.com
 */
public class EditSourceAdapter extends RecyclerArrayAdapter<EditSource> {
    public EditSourceAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(parent, R.layout.item_edit_source);
    }

    public class ItemViewHolder extends BaseViewHolder<EditSource> {
        ItemEditSourceBinding inflate;

        public ItemViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            inflate = ItemEditSourceBinding.bind(itemView);
        }

        @Override
        public void setData(EditSource item) {
            super.setData(item);
            inflate.viewSourceItem.setText(mContext.getResources().getString(item.getHint()));
            if (TextUtils.isEmpty(item.getValue())) {
                inflate.etSourceItemContent.setText("");
            } else {
                inflate.etSourceItemContent.setText(item.getValue());
            }

            inflate.etSourceItemContent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    getRealAllData().get(getPosition()).setValue(s.toString());
                }
            });
        }
    }
}
