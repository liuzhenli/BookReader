package com.micoredu.reader.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.liuzhenli.common.utils.ClickUtils;
import com.micoredu.reader.R;
import com.micoredu.reader.databinding.ItemBookSourceBinding;
import com.micoredu.reader.ui.activity.BookSourceActivity;
import com.liuzhenli.common.utils.LogUtils;
import com.liuzhenli.common.utils.ToastUtil;
import com.liuzhenli.common.utils.AppSharedPreferenceHelper;
import com.micoredu.reader.ui.activity.EditSourceActivity;
import com.liuzhenli.common.widget.DialogUtil;
import com.liuzhenli.common.widget.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.micoredu.reader.bean.BookSourceBean;
import com.micoredu.reader.model.BookSourceManager;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2020/11/9
 * Email: 848808263@qq.com
 */
public class BookSourceAdapter extends RecyclerArrayAdapter<BookSourceBean> {


    BookSourceActivity mBookSourceActivity;

    public BookSourceAdapter(Context context) {
        super(context);
        mBookSourceActivity = (BookSourceActivity) context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookSourceViewHolder(parent, R.layout.item_book_source);
    }

    class BookSourceViewHolder extends BaseViewHolder<BookSourceBean> {
        private final ItemBookSourceBinding binding;

        public BookSourceViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            binding = ItemBookSourceBinding.bind(itemView);
        }

        @Override
        public void setData(BookSourceBean item) {
            super.setData(item);
            LogUtils.e(item.toString());
            binding.cbBookSourceCheck.setChecked(item.getEnable());
            binding.tvSourceName.setText(item.getBookSourceName());
            binding.tvSourceGroupName.setText(item.getBookSourceGroup());
            binding.viewShowRecommand.setChecked(item.getRuleFindEnable());

            //手动排序显示置顶
            if (mSortType != AppSharedPreferenceHelper.SortType.SORT_TYPE_HAND) {
                binding.viewMoveToTop.setVisibility(View.GONE);
            } else {
                binding.viewMoveToTop.setVisibility(View.VISIBLE);
            }

            binding.cbBookSourceCheck.setChecked(item.getEnable());

            //书源是否可用
            binding.cbBookSourceCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mObjects.get(getPosition()).setEnable(isChecked);
                    item.setEnable(isChecked);
                    mBookSourceActivity.saveBookSource(item);
                    //是否可以选择推荐
                    binding.viewShowRecommand.setEnabled(isChecked);
                }
            });

            if (TextUtils.isEmpty(item.getRuleFindUrl())) {
                binding.viewShowRecommand.setVisibility(View.GONE);
            } else {
                binding.viewShowRecommand.setVisibility(View.VISIBLE);
            }
            //click hide
            ClickUtils.click(binding.ivBookSourceVisible,
                    o -> DialogUtil.showMessagePositiveDialog(mContext, "提示", String.format("是否删除书源:%s?", item.getBookSourceName()),
                            "取消", null, "确定", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    mObjects.remove(getPosition());
                                    notifyItemRemoved(getPosition());
                                    mBookSourceActivity.deleteBookSource(item);
                                    ToastUtil.showToast("已删除");
                                }
                            }, true));
            //click show recommend
            ClickUtils.click(binding.viewShowRecommand, o -> {
                item.setRuleFindEnable(binding.viewShowRecommand.isChecked());
                mBookSourceActivity.saveBookSource(item);
            });

            //click to top
            ClickUtils.click(binding.viewMoveToTop, o -> {
                mObjects = BookSourceManager.getAllBookSource();
                //改变顺序  置顶
                BookSourceBean bookSourceBean = mObjects.get(getPosition());
                mObjects.remove(bookSourceBean);
                notifyItemRemoved(getPosition());
                mObjects.add(0, bookSourceBean);
                notifyItemInserted(0);
                //如果是手动排序
                if (mSortType == AppSharedPreferenceHelper.SortType.SORT_TYPE_AUTO) {
                    int maxWeight = mObjects.get(0).getWeight();
                    item.setWeight(maxWeight + 1);
                }
                //更改后的数据修改
                mBookSourceActivity.saveBookSource(mObjects);
            });

            //整个条目的点击
            ClickUtils.click(itemView, o ->
                    binding.cbBookSourceCheck.setChecked(!binding.cbBookSourceCheck.isChecked()));
            //编辑书源
            ClickUtils.click(binding.viewEditBookSource, o ->
                    EditSourceActivity.start(mContext, item));
        }
    }

    private int mSortType;

    public void setSortType(int sortType) {
        mSortType = sortType;
        notifyDataSetChanged();
    }


    public List<BookSourceBean> getSelectedBookSource() {
        List<BookSourceBean> selected = new ArrayList<>();
        List<BookSourceBean> realAllData = getRealAllData();
        for (BookSourceBean data : realAllData) {
            if (data.getEnable()) {
                selected.add(data);
            }
        }
        return selected;
    }
}
