package com.liuzhenli.reader.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.liuzhenli.common.base.BaseActivity
import com.liuzhenli.common.base.BaseContract.BasePresenter
import com.liuzhenli.common.AppComponent
import com.liuzhenli.reader.ui.adapter.DatabaseTableRecordDetailAdapter
import com.microedu.reader.databinding.ActivityDiagnoseDatabaseTableRecordDetailBinding

/**
 * 数据表，行数据详情 (由于内容长度过长，需要查看完整内容)
 *
 * @author liuzhenli
 * @date 2021.02.18
 */
class DatabaseTableRecordDetailActivity :
    BaseActivity<BasePresenter<*>, ActivityDiagnoseDatabaseTableRecordDetailBinding>() {


    private var recordDetailAdapter: DatabaseTableRecordDetailAdapter? = null
    private var tableTitles: Array<String>? = null
    private var rowData: Array<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tableTitles = intent.getStringArrayExtra(EXTRA_TABLE_TITLES)
        rowData = intent.getStringArrayExtra(EXTRA_ROW_DATA)
        recordDetailAdapter = DatabaseTableRecordDetailAdapter(this, tableTitles, rowData)
        binding!!.listTableRecordDetail.adapter = recordDetailAdapter
    }

    override fun setupActivityComponent(appComponent: AppComponent) {}
    override fun initToolBar() {
        mTvTitle.text = "数据详情"
    }

    override fun initData() {}
    override fun configViews() {
    }

    companion object {
        const val EXTRA_TABLE_TITLES = "TableTitles"
        const val EXTRA_ROW_DATA = "RowData"
    }

    override fun inflateView(inflater: LayoutInflater?): ActivityDiagnoseDatabaseTableRecordDetailBinding {
        return ActivityDiagnoseDatabaseTableRecordDetailBinding.inflate(inflater!!)
    }
}