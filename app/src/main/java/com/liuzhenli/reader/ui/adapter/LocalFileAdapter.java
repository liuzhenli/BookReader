package com.liuzhenli.reader.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuzhenli.greendao.BookShelfBeanDao;
import com.liuzhenli.common.utils.Constant;
import com.liuzhenli.common.widget.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.micoredu.readerlib.bean.BookShelfBean;
import com.micoredu.readerlib.helper.DbHelper;
import com.microedu.reader.R;
import com.microedu.reader.databinding.ItemLocalBinding;

import org.greenrobot.greendao.query.Query;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


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
        ItemLocalBinding inflate;

        public ViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            inflate = ItemLocalBinding.inflate(LayoutInflater.from(mContext));
        }

        @Override
        public void setData(Object item) {
            super.setData(item);
            HashMap hashMap = (HashMap) item;
            if (hashMap.get(Constant.FileAttr.IMAGE) != null && Objects.equals(hashMap.get(Constant.FileAttr.IMAGE), Constant.FileAttr.ZERO)) {
                inflate.imageLocalBook.setImageResource(R.drawable.dir);
            } else {
                inflate.imageLocalBook.setImageResource(R.drawable.txt);
            }
            inflate.name.setText(Objects.requireNonNull(hashMap.get(Constant.FileAttr.NAME)).toString());
            try {
                inflate.size.setText(Objects.requireNonNull(hashMap.get(Constant.FileAttr.SIZE)).toString());

            } catch (Exception e) {
                e.printStackTrace();
                inflate.size.setVisibility(View.GONE);
            }
            //处理文件选中的逻辑
            Query<BookShelfBean> build = DbHelper.getDaoSession().getBookShelfBeanDao().queryBuilder().where(BookShelfBeanDao.Properties.NoteUrl.eq(hashMap.get("file").toString())).build();
            List<BookShelfBean> list = build.list();
            int size = list.size();
            if (((File) hashMap.get(Constant.FileAttr.FILE)).isDirectory()) {
                inflate.cbLocalCheck.setVisibility(View.GONE);
                inflate.tvLocalImport.setVisibility(View.GONE);
            } else {
                inflate.cbLocalCheck.setVisibility(size == 0 ? View.VISIBLE : View.GONE);
                inflate.tvLocalImport.setVisibility(size == 0 ? View.GONE : View.VISIBLE);
                inflate.cbLocalCheck.setChecked((boolean) hashMap.get(Constant.FileAttr.CHECKED));
            }

        }
    }
}
