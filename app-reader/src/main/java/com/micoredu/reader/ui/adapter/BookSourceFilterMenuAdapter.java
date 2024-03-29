package com.micoredu.reader.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.liuzhenli.common.utils.DensityUtil;
import com.liuzhenli.common.utils.AppSharedPreferenceHelper;
import com.liuzhenli.common.widget.filter.adapter.MenuAdapter;
import com.liuzhenli.common.widget.filter.adapter.SimpleTextAdapter;
import com.liuzhenli.common.widget.filter.typeview.SingleListView;
import com.liuzhenli.common.widget.filter.view.FilterCheckedTextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Description:
 *
 * @author liuzhenli 2020/11/12
 * Email: 848808263@qq.com
 */
public class BookSourceFilterMenuAdapter implements MenuAdapter {
    String[] menuTitles = {"选择", "排序", "分组"};
    private final int[] bottomMargins = {100, 100, 100};
    private final Context mContext;

    private final List<String> list1 = Arrays.asList("全选", "已选", "反选");
    private final List<String> list2 = Arrays.asList("手动", "智能", "音序");
    private List<String> groupList = Collections.singletonList("暂无分组");

    public BookSourceFilterMenuAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getMenuCount() {
        return menuTitles.length;
    }

    @Override
    public String getMenuTitle(int position) {
        return menuTitles == null ? "请选择" : menuTitles[position];
    }

    @Override
    public int getBottomMargin(int position) {
        return bottomMargins == null ? 0 : bottomMargins.length > position ? bottomMargins[position] : 0;
    }

    @Override
    public View getView(int position, FrameLayout parentContainer) {
        switch (position) {
            case 0:
                return createSingleListView(list1, 0, -1);
            case 1:
                return createSingleListView(list2, 1, AppSharedPreferenceHelper.getBookSourceSortType());
            case 2:
                return createSingleListView(groupList, 2, -1);
            default:
                break;
        }
        return null;
    }


    /**
     * @param list 数据
     * @param id   横向排列的index
     */
    private View createSingleListView(List<String> list, int id, int checkedItemIndex) {
        SingleListView<String> singleListView = new SingleListView<String>(mContext)
                .adapter(new SimpleTextAdapter<String>(null, mContext) {
                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @Override
                    public String provideText(String string) {
                        return string;
                    }

                    @Override
                    protected void initCheckedTextView(FilterCheckedTextView checkedTextView) {
                        checkedTextView.setCanCheck(id == 1);
                        int dp = DensityUtil.dip2px(mContext, 14);
                        checkedTextView.setPadding(dp, dp, 0, dp);
                    }
                })
                .onItemClick((item, position) -> {
                    if (mListener != null) {
                        if (id == 0) {
                            mListener.onSelectChange(position);
                        } else if (id == 1) {
                            mListener.onSortChange(position);
                        } else if (id == 2) {
                            mListener.onGroupChange(position, item);
                        }

                    }
                });
        //初始化数据  //默认不选中
        singleListView.setList(list, checkedItemIndex);
        return singleListView;
    }

    private MenuItemClickListener mListener;

    public void setMenuItemClickListener(MenuItemClickListener listener) {
        this.mListener = listener;
    }

    public void setGroupList(List<String> groupList) {
        this.groupList = groupList;
    }


    public interface MenuItemClickListener {

        /**
         * 选择
         *
         * @param index index
         */
        void onSelectChange(int index);

        /**
         * 排序
         *
         * @param index index
         */
        void onSortChange(int index);

        /**
         * 分组
         *
         * @param index     index
         * @param groupName the item name in the index
         */
        void onGroupChange(int index, String groupName);
    }
}


