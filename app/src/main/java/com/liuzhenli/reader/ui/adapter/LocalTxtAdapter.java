/*
package com.liuzhenli.reader.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.liuzhenli.common.utils.filepicker.entity.FileItem;
import com.liuzhenli.common.utils.Constant;
import com.liuzhenli.common.utils.FileUtils;
import com.liuzhenli.common.widget.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.micoredu.reader.R;
import com.micoredu.reader.databinding.ItemLocalBinding;
import java.text.SimpleDateFormat;
import java.util.List;

*/
/**
 * describe:
 *
 * @author Liuzhenli on 2019-12-15 10:38
 *//*

public class LocalTxtAdapter extends RecyclerArrayAdapter<FileItem> {

    private SimpleDateFormat simpleDateFormat;

    public LocalTxtAdapter(Context context) {
        super(context);
        simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_local);
    }

    class ViewHolder extends BaseViewHolder<FileItem> {

        ItemLocalBinding inflate;

        public ViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            inflate = ItemLocalBinding.bind(itemView);
        }

        @Override
        public void setData(FileItem item) {
            super.setData(item);
            //处理文件选中的逻辑 查看数据库中有没有这本书,如果有则显示已经在书架中
            List<Book> list= AppReaderDbHelper.getInstance().getDatabase().getBookShelfDao().getByNoteUrl(item.path);
            int size = list.size();
            inflate.name.setText(item.name);
            if (item.fileType == null || item.fileType.equals(Constant.FileAttr.DIRECTORY)) {
                inflate.imageLocalBook.setImageResource(R.drawable.dir);
                inflate.cbLocalCheck.setVisibility(View.GONE);
                inflate.tvLocalImport.setVisibility(View.GONE);
                inflate.size.setText(item.fileCount);
            } else {
                inflate.imageLocalBook.setImageResource(R.drawable.txt);
                inflate.cbLocalCheck.setVisibility(size == 0 ? View.VISIBLE : View.GONE);
                inflate.tvLocalImport.setVisibility(size == 0 ? View.GONE : View.VISIBLE);
                inflate.cbLocalCheck.setChecked(item.isSelected);
                String fileSize = FileUtils.formatFileSizeToString(item.size);
                long data = item.time.getTime();
                String time = simpleDateFormat.format(data);
                inflate.size.setText(String.format("%s     %s", time, fileSize));
            }
        }
    }
}
*/
