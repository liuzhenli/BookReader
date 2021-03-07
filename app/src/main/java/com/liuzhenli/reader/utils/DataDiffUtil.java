package com.liuzhenli.reader.utils;

import androidx.recyclerview.widget.DiffUtil;

import com.liuzhenli.common.widget.recyclerview.adapter.DefaultEventDelegate;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;

/**
 * 数据是否有变化
 * Created by liuzhenli on 2020/1/15.
 */

public class DataDiffUtil {
    public interface ItemSameCallBack<T> {
        boolean isItemSame(T oldItem, T newItem);

        boolean isContentSame(T oldItem, T newItem);
    }

    public static <T> void diffResult(RecyclerArrayAdapter<T> mAdapter, List<T> data, ItemSameCallBack<T> callBack) {

        DefaultEventDelegate eventDelegate = (DefaultEventDelegate) mAdapter.getEventDelegate();
        DefaultEventDelegate.EventFooter footer = eventDelegate.getFooter();
        int flag = footer.getFlag();
        if (flag == DefaultEventDelegate.EventFooter.ShowNoMore) {
            mAdapter.clear();
            mAdapter.addAll(data);
            return;
        }
        int headerCount = mAdapter.getHeaderCount();
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return headerCount + mAdapter.getAllData().size();
            }

            @Override
            public int getNewListSize() {
                return headerCount + data.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                if (oldItemPosition < headerCount)
                    return true;
                oldItemPosition = oldItemPosition - headerCount;
                newItemPosition = newItemPosition - headerCount;
                if (oldItemPosition != newItemPosition) {
                    return false;
                }
                T oldItem = mAdapter.getItem(oldItemPosition);
                T newItem = data.get(newItemPosition);
                return callBack.isItemSame(oldItem, newItem);
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                if (oldItemPosition < headerCount)
                    return true;
                oldItemPosition = oldItemPosition - headerCount;
                newItemPosition = newItemPosition - headerCount;
                if (oldItemPosition != newItemPosition) {
                    return false;
                }
                T oldItem = mAdapter.getItem(oldItemPosition);
                T newItem = data.get(newItemPosition);
                return callBack.isContentSame(oldItem, newItem);
            }

        }, true);

        diffResult.dispatchUpdatesTo(mAdapter);
        mAdapter.getRealAllData().clear();
        mAdapter.getRealAllData().addAll(data);
    }
}
