package com.micoredu.reader.ui.read

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import com.airbnb.mvrx.asMavericksArgs
import com.liuzhenli.common.utils.AppConfig
import com.micoredu.reader.BaseActivity
import com.micoredu.reader.model.ReadBook
import com.micoredu.reader.utils.dialogs.alert
import com.microedu.lib.reader.R
import com.microedu.lib.reader.databinding.ActivityReaderBinding

/**
 * Description:reader page
 */
class ReaderActivity : BaseActivity<ActivityReaderBinding>() {
    override fun inflateView(inflater: LayoutInflater?): ActivityReaderBinding {
        return ActivityReaderBinding.inflate(inflater!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.frameLayout.getFragment<ReaderFragment>().arguments =
            intent.extras?.asMavericksArgs()
        setContentView(binding.root)
    }

    override fun init(savedInstanceState: Bundle?) {
    }

    override fun finish() {
        val fragment = binding.frameLayout.getFragment<ReaderFragment>()

        ReadBook.book?.let {
            if (!ReadBook.inBookshelf) {
                if (!AppConfig.showAddToShelfAlert) {
                    fragment.mViewMode.removeFromBookshelf()
                    super.finish()
                } else {
                    alert(title = getString(R.string.add_to_shelf)) {
                        setMessage(getString(R.string.check_add_bookshelf, it.name))
                        okButton {
                            ReadBook.inBookshelf = true
                            setResult(Activity.RESULT_OK)
                            super.finish()
                        }
                        noButton {
                            fragment.mViewMode.removeFromBookshelf()
                            super.finish()
                        }
                    }
                }
            } else {
                super.finish()
            }
        } ?: super.finish()
    }
}