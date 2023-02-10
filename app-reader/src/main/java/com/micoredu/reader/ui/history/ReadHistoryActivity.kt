package com.micoredu.reader.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import com.alibaba.android.arouter.facade.annotation.Route
import com.liuzhenli.common.constant.ARouterConstants
import com.micoredu.reader.BaseActivity
import com.microedu.lib.reader.databinding.ActReadhistoryBinding

/**
 * Description:read history page
 *
 * @author liuzhenli 2021/10/12
 * Email: 848808263@qq.com
 */
@Route(path = ARouterConstants.ACT_READ_HISTORY)
class ReadHistoryActivity : BaseActivity<ActReadhistoryBinding>() {


    override fun inflateView(inflater: LayoutInflater?): ActReadhistoryBinding {
        return ActReadhistoryBinding.inflate(inflater!!)
    }


    override fun init(savedInstanceState: Bundle?) {

    }

}