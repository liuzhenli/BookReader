package com.liuzhenli.reader.ui.about

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import com.liuzhenli.common.BaseActivity
import com.liuzhenli.common.BaseApplication
import com.liuzhenli.common.BitIntentDataManager
import com.liuzhenli.common.constant.AppConstant
import com.liuzhenli.common.utils.AppConfigManager.getNewVersion
import com.liuzhenli.common.utils.AppConfigManager.getNewVersionIntro
import com.liuzhenli.common.utils.AppConfigManager.isShowDonate
import com.liuzhenli.common.utils.ClickUtils
import com.liuzhenli.common.utils.Constant
import com.liuzhenli.reader.ReaderApplication
import com.liuzhenli.reader.ui.activity.ContentActivity
import com.liuzhenli.reader.utils.SayingsManager
import com.micoredu.reader.R
import com.micoredu.reader.databinding.ActivityAboutBinding
import org.jetbrains.anko.toast

/**
 * Description:about Page
 */
class AboutActivity : BaseActivity<ActivityAboutBinding>() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AboutActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun inflateView(inflater: LayoutInflater?): ActivityAboutBinding {
        return ActivityAboutBinding.inflate(layoutInflater)
    }

    override fun init(savedInstanceState: Bundle?) {

        val appName = resources.getString(R.string.app_name)
        val versionName = ReaderApplication.getInstance().mVersionName
        val versionCode = ReaderApplication.getInstance().mVersionCode
        val channel = ReaderApplication.getInstance().mVersionChannel
        binding.tvVersionInfo.text =
            String.format("%s %s Build#%s %s ", appName, versionName, versionCode, channel)
        ClickUtils.click(binding.tvVersionCheckUpdate) {
            //版本有更新
            if (getNewVersion() != null && getNewVersion()!! > BaseApplication.instance!!.mVersionCode) {
                toast(getNewVersionIntro() ?: "")
            } else {
                toast("已经是最新版本")
            }
        }
        ClickUtils.click(binding.tvAboutContact) {
            //发送邮件
            openIntent(Intent.ACTION_SENDTO, "mailto:hpuzhenli@163.com")
        }
        ClickUtils.click(binding.tvDisclaimer) {
            //免责声明
            val key = System.currentTimeMillis().toString() + ""
            BitIntentDataManager.getInstance()
                .putData(key, resources.getString(R.string.disclaimer_content))
            ContentActivity.start(this@AboutActivity, key, "免责声明")
        }
        configQQGroup()

        //版本有更新
        if (getNewVersion() != null && getNewVersion()!! > BaseApplication.instance!!.mVersionCode) {
            binding.ivNewVersionIcon.visibility = View.VISIBLE
            binding.tvNewVersionInfo.visibility = View.VISIBLE
        } else {
            binding.ivNewVersionIcon.visibility = View.GONE
            binding.tvNewVersionInfo.visibility = View.GONE
        }
        val sayings = SayingsManager.instance.getSayings()
        binding.mTvSaying.text = sayings.saying
        val aboutDes = resources.getString(R.string.about_donate)
        val spannableString = SpannableString(aboutDes)
        val donate = resources.getString(R.string.donate)
        spannableString.setSpan(
            object : ClickableSpan() {
                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = resources.getColor(R.color.text_color_66)
                    ds.isUnderlineText = false
                }

                override fun onClick(widget: View) {}
            },
            aboutDes.indexOf(donate),
            aboutDes.indexOf(donate) + donate.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        //设置点击后的颜色为透明
        binding.mTvAbout.highlightColor = Color.TRANSPARENT
        binding.mTvAbout.movementMethod = LinkMovementMethod.getInstance()
        binding.mTvAbout.text = spannableString
        ClickUtils.click(binding.mViewDonateAliPay) { o: Any? ->
            val uri = Uri.parse(AppConstant.DonateUrl.ali)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        ClickUtils.click(binding.mViewZFB) {
            val uri = Uri.parse(AppConstant.DonateUrl.zfbCode)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        ClickUtils.click(binding.mViewQQ) {
            val uri = Uri.parse(AppConstant.DonateUrl.wxCode)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        ClickUtils.click(binding.mViewWX) {
            val uri = Uri.parse(AppConstant.DonateUrl.qqCode)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        configDonate(isShowDonate())
    }

    private fun configDonate(showDonate: Boolean) {
        if (!showDonate) {
            binding.tvDonateTitle.visibility = View.GONE
            binding.mTvAbout.visibility = View.GONE
            binding.mViewDonateAliPay.visibility = View.GONE
            binding.mViewQQ.visibility = View.GONE
            binding.mViewWX.visibility = View.GONE
            binding.mViewZFB.visibility = View.GONE
        }
    }

    private fun configQQGroup() {
        ClickUtils.click(binding.tvQQGroup0) { o: Any? -> joinQQGroup(Constant.QQGroup.QQ_272343970) }
        ClickUtils.click(binding.tvQQGroup1) { o: Any? -> joinQQGroup(Constant.QQGroup.QQ_1140723995) }
    }

    /****************
     *
     * 发起添加群流程。群号：阅读①(1140723995) 的 key 为： py5-vU4j3y7mobTS3IkZMKKJAFbiKRgl
     * 调用 joinQQGroup(py5-vU4j3y7mobTS3IkZMKKJAFbiKRgl) 即可发起手Q客户端申请加群 阅读①(1140723995)
     *
     * @param key 由官网生成的key
     */
    fun joinQQGroup(key: String) {
        val intent = Intent()
        intent.data =
            Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D$key")
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent)
        } catch (e: Exception) {
            // 未安装手Q或安装的版本不支持
        }
    }

    private fun openIntent(intentName: String?, address: String?) {
        try {
            val intent = Intent(intentName)
            intent.data = Uri.parse(address)
            startActivity(intent)
        } catch (e: Exception) {
            toast(resources.getString(R.string.can_not_open))
        }
    }
}