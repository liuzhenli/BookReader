package com.liuzhenli.reader.ui.database

import android.content.Intent
import android.database.Cursor
import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.AdapterView
import androidx.sqlite.db.SupportSQLiteDatabase
import com.liuzhenli.common.BaseActivity
import com.liuzhenli.common.utils.ToastUtil
import com.liuzhenli.reader.ui.adapter.DatabaseTableDataGridAdapter
import com.liuzhenli.write.helper.WriteDbHelper
import com.micoredu.reader.dao.AppDatabase
import com.micoredu.reader.databinding.ActivityDiagnoseDatabaseTableRecordBinding
import net.lucode.hackware.magicindicator.buildins.UIUtil

/**
 * view database table records
 */
class DatabaseTableRecordsActivity : BaseActivity<ActivityDiagnoseDatabaseTableRecordBinding>() {
    private var tableDataAdapter: DatabaseTableDataGridAdapter? = null
    private var tableName: String? = null
    private var dbName: String? = null
    override fun inflateView(inflater: LayoutInflater?): ActivityDiagnoseDatabaseTableRecordBinding {
        return ActivityDiagnoseDatabaseTableRecordBinding.inflate(
            inflater!!
        )
    }

    override fun init(savedInstanceState: Bundle?) {
        tableName = intent.getStringExtra(EXTRA_TABLE_NAME)
        dbName = intent.getStringExtra(EXTRA_DB_NAME)
        if (!TextUtils.isEmpty(tableName)) {
            //mTvTitle.setText(tableName);
        }
        tableDataAdapter = DatabaseTableDataGridAdapter(this@DatabaseTableRecordsActivity)
        binding.girdTableData.adapter = tableDataAdapter
        binding.girdTableData.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                tableDataAdapter!!.setSelectedRow(position)
                val titles = tableDataAdapter!!.tableTitles
                val dataRow = tableDataAdapter!!.getRowData(position)
                if (titles != null && dataRow != null) {
                    val intent = Intent(
                        this@DatabaseTableRecordsActivity,
                        DatabaseTableRecordDetailActivity::class.java
                    )
                    intent.putExtra(DatabaseTableRecordDetailActivity.EXTRA_TABLE_TITLES, titles)
                    intent.putExtra(DatabaseTableRecordDetailActivity.EXTRA_ROW_DATA, dataRow)
                    startActivity(intent)
                }
            }
        LoadTableRecordTask(tableName).execute()
    }

    internal inner class LoadTableRecordTask(private val tableName: String?) :
        AsyncTask<String?, Int?, List<Array<String?>>?>() {
        override fun onPreExecute() {
        }

        protected override fun doInBackground(vararg params: String?): List<Array<String?>>? {
            val result: MutableList<Array<String?>> = ArrayList()
            var db: SupportSQLiteDatabase? = null
            var cursor: Cursor? = null
            if (TextUtils.equals(dbName, AppDatabase.DATABASE_NAME)) {
                db = WriteDbHelper.getInstance().sqliteDatabase
            } else if (TextUtils.equals(dbName, WriteDbHelper.DATABASE_NAME)) {
                db = WriteDbHelper.getInstance().sqliteDatabase
            }
            if (db != null) {
                cursor = db.query("select * from $tableName", null)
                val ColumnNames = cursor.columnNames
                result.add(ColumnNames)
                val columnsCount = ColumnNames.size
                while (cursor.moveToNext()) {
                    val columnValues = arrayOfNulls<String>(columnsCount)
                    for (i in 0 until columnsCount) {
                        columnValues[i] = cursor.getString(i)
                    }
                    result.add(columnValues)
                }
                cursor.close()
            }
            return result
        }

        override fun onPostExecute(result: List<Array<String?>>?) {
            if (!result.isNullOrEmpty()) {
                val columnCount = result[0].size
                binding.girdTableData.numColumns = columnCount
                val gridLayoutParams = binding.girdTableData.layoutParams
                gridLayoutParams.width =
                    columnCount * UIUtil.dip2px(this@DatabaseTableRecordsActivity, 96.0)
                binding.girdTableData.layoutParams = gridLayoutParams
                tableDataAdapter!!.setDataset(result)
            } else {
                ToastUtil.showToast(this@DatabaseTableRecordsActivity, "加载数据表记录失败!!!")
            }
        }
    }

    internal inner class LoadTableDataTask(private val tableName: String?) :
        AsyncTask<Int?, Int?, List<Array<String?>>?>() {
        override fun onPreExecute() {
        }

        protected override fun doInBackground(vararg params: Int?): List<Array<String?>>? {
            val result: MutableList<Array<String?>> = ArrayList()
            val offset = params[0]
            val count = params[1]
            val database = WriteDbHelper.getInstance().sqliteDatabase
            val cursor = database.query(
                "select * from " + tableName + "limit ?,?",
                arrayOf(offset.toString(), count.toString())
            )

            // 读取表头
            if (offset == 0) {
                val ColumnNames = cursor.columnNames
                result.add(ColumnNames)
            }

            // 读取行数据
            val columnsSize = cursor.columnCount
            while (cursor.moveToNext()) {
                val columnValues = arrayOfNulls<String>(columnsSize)
                for (i in 0 until columnsSize) {
                    columnValues[i] = cursor.getString(i)
                }
                result.add(columnValues)
            }
            cursor.close()
            return result
        }

        override fun onPostExecute(result: List<Array<String?>>?) {}
    }

    companion object {
        const val EXTRA_TABLE_NAME = "TableName"
        const val EXTRA_DB_NAME = "DbName"
    }
}