package com.liuzhenli.reader.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.base.BaseActivity;
import com.liuzhenli.common.constant.ARouterConstants;
import com.liuzhenli.common.observer.MyObserver;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.common.utils.FileUtils;
import com.liuzhenli.common.utils.PermissionUtil;
import com.liuzhenli.common.utils.picker.FilePicker;
import com.microedu.reader.R;
import com.microedu.reader.databinding.ActQrcodeBinding;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Description:
 *
 * @author liuzhenli 2021/5/6
 * Email: 848808263@qq.com
 */
@Route(path = ARouterConstants.ACT_QRCODE)
public class QrCodeActivity extends BaseActivity implements QRCodeView.Delegate {
    public static final int REQUEST_MAGE = 10010;
    private ActQrcodeBinding binding;

    @Override
    protected View bindContentView() {
        binding = ActQrcodeBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initToolBar() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void configViews() {
        binding.zxingview.setDelegate(this);
        //打开后置摄像头开始预览，但是并未开始识别
        PermissionUtil.requestPermission(this, new MyObserver<Boolean>() {
            @Override
            public void onNext(@NonNull Boolean aBoolean) {
                binding.zxingview.startCamera();
                binding.zxingview.showScanRect();
                binding.zxingview.startSpot();
            }
        }, Manifest.permission.CAMERA);

        binding.zxingview.setOnClickListener(v -> binding.zxingview.startSpot());

        ClickUtils.click(binding.ivPickImage, o -> {
            PermissionUtil.requestPermission(this, new MyObserver<Boolean>() {
                @Override
                public void onNext(@NonNull Boolean aBoolean) {
                    chooseFromGallery();
                }
            }, Manifest.permission.CAMERA);
        });
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Intent intent = new Intent();
        intent.putExtra("result", result);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }

    private void chooseFromGallery() {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_MAGE);
        } catch (Exception ignored) {
            FilePicker picker = new FilePicker(this, FilePicker.FILE);
            picker.setBackgroundColor(getResources().getColor(R.color.background));
            picker.setTopBackgroundColor(getResources().getColor(R.color.background));
            picker.setItemHeight(30);
            picker.setOnFilePickListener(currentPath -> binding.zxingview.decodeQRCode(currentPath));
            picker.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 显示扫描框，并开始识别
        binding.zxingview.startSpotAndShowRect();
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_MAGE) {
            final String picturePath = FileUtils.getPath(this, data.getData());
            binding.zxingview.decodeQRCode(picturePath);
        }
    }
}
