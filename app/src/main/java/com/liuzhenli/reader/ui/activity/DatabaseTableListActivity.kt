package com.liuzhenli.reader.ui.activity

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import com.liuzhenli.common.base.BaseActivity
import com.liuzhenli.common.AppComponent
import com.liuzhenli.reader.DaggerReadBookComponent
import com.liuzhenli.reader.ui.adapter.DatabaseTableListAdapter
import com.liuzhenli.reader.ui.contract.DatabaseTableListContract
import com.liuzhenli.reader.ui.presenter.DatabaseTableListPresenter
import com.microedu.reader.databinding.ActDatabasetablelistBinding
import nl.siegmann.epublib.util.StringUtil
import java.util.*

/**
 * 数据库中包含的数据表
 *
 * @author liuzhenli
 * @date 2021.02.18
 */
class DatabaseTableListActivity : BaseActivity<DatabaseTableListPresenter>(), DatabaseTableListContract.View {

    private var tablesAdapter: DatabaseTableListAdapter? = null
    private val tableList: MutableList<String> = ArrayList()

    private var binding: ActDatabasetablelistBinding? = null

    override fun bindContentView(): View {
        binding = ActDatabasetablelistBinding.inflate(layoutInflater)
        return binding!!.root;
    }

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerReadBookComponent.builder().build().inject(this)
    }

    override fun initToolBar() {
        mTvTitle.text = "数据表"
    }

    override fun initData() {}

    override fun configViews() {
        tablesAdapter = DatabaseTableListAdapter(this)
        binding?.databaseTablesList!!.adapter = tablesAdapter
        binding?.databaseTablesList!!.onItemClickListener = AdapterView.OnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            val tableName = tableList[position]
            if (StringUtil.isNotBlank(tableName)) {
                val intent = Intent(this@DatabaseTableListActivity, DatabaseTableRecordsActivity::class.java)
                intent.putExtra(DatabaseTableRecordsActivity.EXTRA_TABLE_NAME, tableName)
                startActivity(intent)
            }
        }
        mPresenter!!.loadDatabase()
    }

    override fun showDatabase(data: List<String>) {
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
}