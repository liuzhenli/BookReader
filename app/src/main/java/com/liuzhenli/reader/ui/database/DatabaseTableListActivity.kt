package com.liuzhenli.reader.ui.database

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import com.liuzhenli.common.BaseActivity
import com.liuzhenli.reader.bean.DatabaseTable
import com.liuzhenli.reader.ui.adapter.DatabaseTableListAdapter
import com.liuzhenli.reader.ui.contract.DatabaseTableListContract
import com.micoredu.reader.databinding.ActDatabasetablelistBinding
import java.util.*

/**
 * 数据库中包含的数据表
 *
 * @author liuzhenli
 * @date 2021.02.18
 */
class DatabaseTableListActivity :
    BaseActivity<ActDatabasetablelistBinding>(),
    DatabaseTableListContract.View {

    private var tablesAdapter: DatabaseTableListAdapter? = null
    private val tableList: MutableList<DatabaseTable> = ArrayList()

    override fun init(savedInstanceState: Bundle?) {
        tablesAdapter = DatabaseTableListAdapter(this)
        binding?.databaseTablesList!!.adapter = tablesAdapter
        binding?.databaseTablesList!!.onItemClickListener =
            AdapterView.OnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                val tableName = tableList[position]
                if (!TextUtils.isEmpty(tableName.tableName)) {
                    val intent = Intent(
                        this@DatabaseTableListActivity,
                        DatabaseTableRecordsActivity::class.java
                    )
                    intent.putExtra(
                        DatabaseTableRecordsActivity.EXTRA_TABLE_NAME,
                        tableName.tableName
                    )
                    intent.putExtra(DatabaseTableRecordsActivity.EXTRA_DB_NAME, tableName.dbName)
                    startActivity(intent)
                }
            }
        //mPresenter!!.loadDatabase()
    }

    override fun showDatabase(data: List<DatabaseTable>) {
        tableList.addAll(data)
        tablesAdapter!!.setDataset(tableList)
    }

    override fun showError(e: Exception) {}
    override fun complete() {}

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, DatabaseTableListActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun inflateView(inflater: LayoutInflater?): ActDatabasetablelistBinding {
        return ActDatabasetablelistBinding.inflate(inflater!!)
    }
}