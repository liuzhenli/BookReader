package com.liuzhenli.reader.ui.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.liuzhenli.reader.bean.EditSource;
import com.liuzhenli.reader.view.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.reader.view.recyclerview.adapter.RecyclerArrayAdapter;
import com.microedu.reader.R;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        @BindView(R.id.et_source_item_content)
        EditText mEditContent;
        @BindView(R.id.view_source_item)
        TextView mTv;

        public ItemViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(EditSource item) {
            super.setData(item);
            mTv.setText(mContext.getResources().getString(item.getHint()));
            mEditContent.setText(item.getValue());
            mEditContent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    item.setValue(s.toString());
                }
            });
        }
    }
}
