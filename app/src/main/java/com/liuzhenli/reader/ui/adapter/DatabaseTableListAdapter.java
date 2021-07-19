package com.liuzhenli.reader.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liuzhenli.reader.bean.DatabaseTable;
import com.microedu.reader.R;

import java.util.ArrayList;
import java.util.List;

/**
 * table item
 *
 * @author liuzhenli
 * @date 2021.02.18
 */
public class DatabaseTableListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<DatabaseTable> dataset;

    public DatabaseTableListAdapter(Context ctx) {
        context = ctx;
        inflater = LayoutInflater.from(context);
    }

    public void setDataset(List<DatabaseTable> data) {
        if (dataset == null) {
            dataset = new ArrayList<>();
        } else {
            dataset.clear();
        }
        dataset.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataset == null ? 0 : dataset.size();
    }

    @Override
    public Object getItem(int position) {
        return dataset == null ? null : dataset.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemHolder holder = null;
        if (convertView == null) {
            holder = new ItemHolder();
            convertView = inflater.inflate(R.layout.item_database_table, null);
            holder.tvTableName = (TextView) convertView.findViewById(R.id.tv_diagnose_database_table_name);
            convertView.setTag(holder);
        } else {
            holder = (ItemHolder) convertView.getTag();
        }

        String tableName = dataset.get(position).tableName;
        String dbName = dataset.get(position).dbName;
        holder.tvTableName.setText(String.format("%s·(%s)", tableName, dbName));
        return convertView;
    }

    static class ItemHolder {
        TextView tvTableName;
    }

}
