package com.micoredu.reader.ui.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.ViewGroup;

import com.micoredu.reader.R;
import com.micoredu.reader.databinding.ItemDownloadBinding;
import com.micoredu.reader.service.DownloadService;
import com.liuzhenli.common.utils.image.ImageUtil;
import com.liuzhenli.common.widget.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.micoredu.reader.bean.DownloadBookBean;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-12-15 10:38
 */
public class DownloadAdapter extends RecyclerArrayAdapter<DownloadBookBean> {

    private SimpleDateFormat simpleDateFormat;

    public DownloadAdapter(Context context) {
        super(context);
        simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_download);
    }

    @Override
    public void OnBindViewHolder(BaseViewHolder holder, int position) {
        super.OnBindViewHolder(holder, position);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    static class ViewHolder extends BaseViewHolder<DownloadBookBean> {
        ItemDownloadBinding inflate;

        public ViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            inflate = ItemDownloadBinding.bind(itemView);
        }

        @Override
        public void setData(DownloadBookBean item) {
            super.setData(item);

            inflate.ivDelete.getDrawable().mutate();
            inflate.ivDelete.getDrawable().setColorFilter(mContext.getResources().getColor(R.color.tv_text_default), PorterDuff.Mode.SRC_ATOP);

            ImageUtil.setRoundedCornerImage(mContext, item.getCoverUrl(), R.drawable.book_cover, inflate.ivDownloadBookCover);
            if (item.getSuccessCount() > 0) {
                inflate.tvName.setText(String.format(Locale.getDefault(), "%s(正在下载)", item.getName()));
            } else {
                inflate.tvName.setText(String.format(Locale.getDefault(), "%s(等待下载)", item.getName()));
            }
            inflate.mTvDownload.setText(mContext.getString(R.string.un_download, item.getDownloadCount() - item.getSuccessCount()));
            inflate.ivDelete.setOnClickListener(view -> DownloadService.removeDownload(mContext, item.getNoteUrl()));
        }
    }
}
