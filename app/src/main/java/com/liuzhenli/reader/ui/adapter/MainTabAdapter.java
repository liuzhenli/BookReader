/*
package com.liuzhenli.reader.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.liuzhenli.common.base.BaseFragment;
import com.liuzhenli.reader.ui.fragment.DiscoverFragment;
import com.liuzhenli.reader.ui.fragment.BookShelfFragment;
import com.liuzhenli.write.ui.fragment.CreateBookFragment;
import com.micoredu.reader.R;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * describe: 主页tab
 *
 * @author Liuzhenli on 2019-11-09 22:27
 *//*

public class MainTabAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private String[] tabName;
    private List<BaseFragment> fragments;

    public MainTabAdapter(Context context, @NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        mContext = context;
        tabName = mContext.getResources().getStringArray(R.array.main_tab_names);
        fragments = new ArrayList<>();
        fragments.add(new BookShelfFragment());
        fragments.add(DiscoverFragment.getInstance());
        //     fragments.add(CreateBookFragment.getInstance());
//        fragments.add(new MeFragment());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return tabName.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabName[position];
    }


    public View getTabView(int position) {
        int[] tabIcon = {R.drawable.icon_main_tab_bookshelf,
                R.drawable.icon_main_tab_discover,
//                R.drawable.icon_main_tab_book_shop,
//                R.drawable.icon_main_tab_me
        };
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_main_tab_icon_view, null);
        ImageView iv = v.findViewById(R.id.iv_main_tab_icon);
        TextView tv = v.findViewById(R.id.tv_main_tab_name);
        iv.setImageResource(tabIcon[position]);
        tv.setText(tabName[position]);
        return v;
    }


}
*/
