package com.micoredu.reader.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.liuzhenli.common.utils.ClickUtils;
import com.microedu.lib.reader.R;
import com.micoredu.reader.bean.BookSource;
import com.microedu.lib.reader.databinding.ItemBookSourceBinding;
import com.liuzhenli.common.utils.L;
import com.liuzhenli.common.utils.ToastUtil;
import com.liuzhenli.common.utils.AppSharedPreferenceHelper;
import com.liuzhenli.common.widget.DialogUtil;
import com.liuzhenli.common.widget.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.micoredu.reader.ui.source.BookSourceActivity;
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
public class BookSourceAdapter extends RecyclerArrayAdapter<BookSource> {

    private int mSortType;
    private final BookSourceActivity mBookSourceActivity;

    public BookSourceAdapter(Context context) {
        super(context);
        mBookSourceActivity = (BookSourceActivity) context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookSourceViewHolder(parent, R.layout.item_book_source);
    }

    class BookSourceViewHolder extends BaseViewHolder<BookSource> {
        private final ItemBookSourceBinding binding;

        public BookSourceViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            binding = ItemBookSourceBinding.bind(itemView);
        }

        @Override
        public void setData(BookSource item) {
            super.setData(item);
            L.e(BookSourceAdapter.class.getName(), item.toString());
            binding.cbBookSourceCheck.setChecked(item.getEnabled());
            binding.tvSourceName.setText(item.getBookSourceName());
            binding.tvSourceGroupName.setText(item.getBookSourceGroup());
            binding.switchDiscover.setChecked(item.getEnabledExplore());

            //手动排序显示置顶
            if (mSortType != AppSharedPreferenceHelper.SortType.SORT_TYPE_HAND) {
                binding.viewMoveToTop.setVisibility(View.GONE);
            } else {
                binding.viewMoveToTop.setVisibility(View.VISIBLE);
            }
            //书源是否可用
            binding.cbBookSourceCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mObjects.get(getPosition()).setEnabled(isChecked);
                    item.setEnabled(isChecked);
                    //mBookSourceActivity.saveBookSource(item);
                    //是否可以选择推荐
                    binding.switchDiscover.setEnabled(isChecked);
                }
            });

            if (TextUtils.isEmpty(item.getExploreUrl())) {
                binding.switchDiscover.setVisibility(View.GONE);
            } else {
                binding.switchDiscover.setVisibility(View.VISIBLE);
            }
            //click hide
            ClickUtils.click(binding.ivBookSourceVisible,
                    o -> DialogUtil.showMessagePositiveDialog(mContext, "提示", String.format("是否删除书源:%s?", item.getBookSourceName()),
                            "取消", null, "确定", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    mObjects.remove(getPosition());
                                    notifyItemRemoved(getPosition());
                                    //mBookSourceActivity.deleteBookSource(item);
                                    ToastUtil.showToast("已删除");
                                }
                            }, true));
            //click show recommend
            ClickUtils.click(binding.switchDiscover, o -> {
                item.setEnabledExplore(binding.switchDiscover.isChecked());
                //mBookSourceActivity.saveBookSource(item);
            });

            //click to top
            ClickUtils.click(binding.viewMoveToTop, o -> {
                //mObjects = BookSourceManager.getAllBookSource();
                //改变顺序  置顶
                BookSource BookSource = mObjects.get(getPosition());
                mObjects.remove(BookSource);
                notifyItemRemoved(getPosition());
                mObjects.add(0, BookSource);
                notifyItemInserted(0);
                //如果是手动排序
                if (mSortType == AppSharedPreferenceHelper.SortType.SORT_TYPE_HAND) {
                    int maxWeight = mObjects.get(0).getWeight();
                    item.setWeight(maxWeight + 1);
                }
                //更改后的数据修改
                //mBookSourceActivity.saveBookSource(mObjects);
            });

            //整个条目的点击
            ClickUtils.click(itemView, o ->
                    binding.cbBookSourceCheck.setChecked(!binding.cbBookSourceCheck.isChecked()));
            //编辑书源
//            ClickUtils.click(binding.viewEditBookSource, o ->
                    //EditSourceActivity.start(mContext, item));
        }
    }


    public void setSortType(int sortType) {
        mSortType = sortType;
        notifyDataSetChanged();
    }


    public List<BookSource> getSelectedBookSource() {
        List<BookSource> selected = new ArrayList<>();
        List<BookSource> realAllData = getRealAllData();
        for (BookSource data : realAllData) {
            if (data.getEnabled()) {
                selected.add(data);
            }
        }
        return selected;
    }
}
