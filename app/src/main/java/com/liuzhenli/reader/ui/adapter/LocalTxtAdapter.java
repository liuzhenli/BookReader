package com.liuzhenli.reader.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuzhenli.greendao.BookShelfBeanDao;
import com.liuzhenli.reader.bean.LocalFileBean;
import com.liuzhenli.common.utils.Constant;
import com.liuzhenli.common.utils.FileUtils;
import com.liuzhenli.common.widget.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.micoredu.readerlib.bean.BookShelfBean;
import com.micoredu.readerlib.helper.DbHelper;
import com.microedu.reader.R;

import org.greenrobot.greendao.query.Query;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-12-15 10:38
 */
public class LocalTxtAdapter extends RecyclerArrayAdapter<LocalFileBean> {

    private SimpleDateFormat simpleDateFormat;

    public LocalTxtAdapter(Context context) {
        super(context);
        simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_local);
    }

    class ViewHolder extends BaseViewHolder<LocalFileBean> {
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
        public void setData(LocalFileBean item) {
            super.setData(item);
            //处理文件选中的逻辑
            Query<BookShelfBean> build = DbHelper.getDaoSession().getBookShelfBeanDao().queryBuilder().where(BookShelfBeanDao.Properties.NoteUrl.eq(item.file.toString())).build();
            List<BookShelfBean> list = build.list();
            int size = list.size();
            mName.setText(item.file.getName());
            if (item.fileType == null || item.fileType.equals(Constant.FileAttr.ZERO)) {
                mImageLocalBook.setImageResource(R.drawable.dir);
                mCbLocalCheck.setVisibility(View.GONE);
                mTvLocalImport.setVisibility(View.GONE);
                mSize.setText(item.fileCount);
            } else {
                mImageLocalBook.setImageResource(R.drawable.txt);
                mCbLocalCheck.setVisibility(size == 0 ? View.VISIBLE : View.GONE);
                mTvLocalImport.setVisibility(size == 0 ? View.GONE : View.VISIBLE);
                mCbLocalCheck.setChecked(item.isSelected);
                String fileSize = FileUtils.formatFileSizeToString(item.file.length());
                long data = item.file.lastModified();
                String time = simpleDateFormat.format(data);
                mSize.setText(String.format("%s     %s", time, fileSize));
            }
        }
    }
}
