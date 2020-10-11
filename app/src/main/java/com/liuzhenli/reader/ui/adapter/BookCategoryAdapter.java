package com.liuzhenli.reader.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.liuzhenli.common.base.CommonAdapter;
import com.liuzhenli.common.base.CommonViewHolder;
import com.micoredu.readerlib.bean.BookSourceBean;
import com.microedu.reader.R;

import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2020/10/9
 * Email: 848808263@qq.com
 */
public class BookCategoryAdapter extends CommonAdapter<BookSourceBean> {

    public BookCategoryAdapter(List<BookSourceBean> mData, Context mContext) {
        super(mData, mContext, R.layout.item_book_site);
    }

    @Override
    public void convert(CommonViewHolder commonViewHolder, BookSourceBean itemData) {
        TextView tvCategory = commonViewHolder.getView(R.id.tv_item_book_category);
        tvCategory.setText(itemData.getLastUpdateTime()+"");
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
