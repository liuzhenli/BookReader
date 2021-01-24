package com.liuzhenli.reader.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.reader.ui.activity.BookSourceActivity;
import com.liuzhenli.reader.ui.activity.EditSourceActivity;
import com.liuzhenli.common.utils.LogUtils;
import com.liuzhenli.reader.utils.ToastUtil;
import com.liuzhenli.common.utils.AppSharedPreferenceHelper;
import com.liuzhenli.reader.view.loading.DialogUtil;
import com.liuzhenli.reader.view.recyclerview.adapter.BaseViewHolder;
import com.liuzhenli.reader.view.recyclerview.adapter.RecyclerArrayAdapter;
import com.micoredu.readerlib.bean.BookSourceBean;
import com.micoredu.readerlib.model.BookSourceManager;
import com.microedu.reader.R;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

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
        @BindView(R.id.cb_book_source_check)
        AppCompatCheckBox cbBookSourceCheck;
        @BindView(R.id.tv_source_name)
        TextView tvSourceName;
        @BindView(R.id.view_show_recommand)
        Switch viewShowRecommend;
        @BindView(R.id.view_move_to_top)
        View viewSetTop;
        @BindView(R.id.view_edit_book_source)
        View viewEditSource;
        @BindView(R.id.iv_book_source_visible)
        ImageView ivBookSourceVisible;
        @BindView(R.id.tv_source_group_name)
        TextView mTvGroup;

        public BookSourceViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(BookSourceBean item) {
            super.setData(item);
            LogUtils.e(item.toString());
            cbBookSourceCheck.setChecked(item.getEnable());
            tvSourceName.setText(item.getBookSourceName());
            mTvGroup.setText(item.getBookSourceGroup());
            viewShowRecommend.setChecked(item.getRuleFindEnable());

            //手动排序显示置顶
            if (mSortType != AppSharedPreferenceHelper.SortType.SORT_TYPE_HAND) {
                viewSetTop.setVisibility(View.GONE);
            } else {
                viewSetTop.setVisibility(View.VISIBLE);
            }

            cbBookSourceCheck.setChecked(item.getEnable());

            //书源是否可用
            cbBookSourceCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mObjects.get(getPosition()).setEnable(isChecked);
                    item.setEnable(isChecked);
                    mBookSourceActivity.saveBookSource(item);
                    //是否可以选择推荐
                    viewShowRecommend.setEnabled(isChecked);
                }
            });

            if (TextUtils.isEmpty(item.getRuleFindUrl())) {
                viewShowRecommend.setVisibility(View.GONE);
            } else {
                viewShowRecommend.setVisibility(View.VISIBLE);
            }
            //click hide
            ClickUtils.click(ivBookSourceVisible, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    DialogUtil.showMessagePositiveDialog(mContext, "提示", String.format("是否删除书源:%s?", item.getBookSourceName()),
                            "取消", null, "确定", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    mObjects.remove(getPosition());
                                    notifyItemRemoved(getPosition());
                                    mBookSourceActivity.deleteBookSource(item);
                                    ToastUtil.showToast("已删除");
                                }
                            }, true);
                }
            });
            //click show recommend
            ClickUtils.click(viewShowRecommend, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    item.setRuleFindEnable(viewShowRecommend.isChecked());
                    mBookSourceActivity.saveBookSource(item);
                }
            });

            //click to top
            ClickUtils.click(viewSetTop, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
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
                }
            });

            //整个条目的点击
            ClickUtils.click(itemView, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    cbBookSourceCheck.setChecked(!cbBookSourceCheck.isChecked());
                }
            });
            //编辑书源
            ClickUtils.click(viewEditSource, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    EditSourceActivity.start(mContext, item);
                }
            });
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
