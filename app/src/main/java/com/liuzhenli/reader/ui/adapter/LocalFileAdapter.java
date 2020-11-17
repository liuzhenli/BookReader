package com.liuzhenli.reader.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuzhenli.greendao.BookShelfBeanDao;
import com.liuzhenli.common.utils.Constant;
import com.liuzhenli.reader.view.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.reader.view.recyclerview.adapter.RecyclerArrayAdapter;
import com.micoredu.readerlib.bean.BookShelfBean;
import com.micoredu.readerlib.helper.DbHelper;
import com.microedu.reader.R;

import org.greenrobot.greendao.query.Query;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-12-15 10:38
 */
public class LocalFileAdapter extends RecyclerArrayAdapter<HashMap<String, Object>> {


    public LocalFileAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_local);
    }

    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.image_local_book)
        ImageView mImageLocalBook;
        @BindView(R.id.name)
        TextView mName;
        @BindView(R.id.size)
        TextView mSize;
        @BindView(R.id.cb_local_check)
        CheckBox mCbLocalCheck;
        @BindView(R.id.tv_local_import)
        TextView mTvLocalImport;

        public ViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(Object item) {
            super.setData(item);
            HashMap hashMap = (HashMap) item;
            if (hashMap.get(Constant.FileAttr.IMAGE) != null && Objects.equals(hashMap.get(Constant.FileAttr.IMAGE), Constant.FileAttr.ZERO)) {
                mImageLocalBook.setImageResource(R.drawable.dir);
            } else {
                mImageLocalBook.setImageResource(R.drawable.txt);
            }
            mName.setText(Objects.requireNonNull(hashMap.get(Constant.FileAttr.NAME)).toString());
            try {
                mSize.setText(Objects.requireNonNull(hashMap.get(Constant.FileAttr.SIZE)).toString());

            } catch (Exception e) {
                e.printStackTrace();
                mSize.setVisibility(View.GONE);
            }
            //处理文件选中的逻辑
            Query<BookShelfBean> build = DbHelper.getDaoSession().getBookShelfBeanDao().queryBuilder().where(BookShelfBeanDao.Properties.NoteUrl.eq(hashMap.get("file").toString())).build();
            List<BookShelfBean> list = build.list();
            int size = list.size();
            if (((File) hashMap.get(Constant.FileAttr.FILE)).isDirectory()) {
                mCbLocalCheck.setVisibility(View.GONE);
                mTvLocalImport.setVisibility(View.GONE);
            } else {
                mCbLocalCheck.setVisibility(size == 0 ? View.VISIBLE : View.GONE);
                mTvLocalImport.setVisibility(size == 0 ? View.GONE : View.VISIBLE);
                mCbLocalCheck.setChecked((boolean) hashMap.get(Constant.FileAttr.CHECKED));
            }

        }
    }
}
