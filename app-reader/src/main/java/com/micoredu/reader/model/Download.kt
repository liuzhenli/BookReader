package com.micoredu.reader.model

import android.content.Context
import com.micoredu.reader.constant.IntentAction
import com.micoredu.reader.service.DownloadService
import com.liuzhenli.common.utils.startService
object Download {


    fun start(context: Context, url: String, fileName: String) {
        context.startService<DownloadService> {
            action = IntentAction.start
            putExtra("url", url)
            putExtra("fileName", fileName)
        }
    }

}