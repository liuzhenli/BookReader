package com.liuzhenli.reader.ui.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuzhenli.reader.service.DownloadService;
import com.liuzhenli.common.utils.image.ImageUtil;
import com.liuzhenli.common.widget.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.micoredu.readerlib.bean.DownloadBookBean;
import com.microedu.reader.R;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    class ViewHolder extends BaseViewHolder<DownloadBookBean> {
        @BindView(R.id.iv_download_book_cover)
        ImageView ivCover;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.mTvDownload)
        TextView tvDownload;
        @BindView(R.id.iv_delete)
        ImageView ivDel;

        public ViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(DownloadBookBean item) {
            super.setData(item);

            ivDel.getDrawable().mutate();
            ivDel.getDrawable().setColorFilter(mContext.getResources().getColor(R.color.tv_text_default), PorterDuff.Mode.SRC_ATOP);

            ImageUtil.setRoundedCornerImage(mContext, item.getCoverUrl(), R.drawable.book_cover, ivCover);
            if (item.getSuccessCount() > 0) {
                tvName.setText(String.format(Locale.getDefault(), "%s(正在下载)", item.getName()));
            } else {
                tvName.setText(String.format(Locale.getDefault(), "%s(等待下载)", item.getName()));
            }
            tvDownload.setText(mContext.getString(R.string.un_download, item.getDownloadCount() - item.getSuccessCount()));
            ivDel.setOnClickListener(view -> DownloadService.removeDownload(mContext, item.getNoteUrl()));
        }
    }
}
