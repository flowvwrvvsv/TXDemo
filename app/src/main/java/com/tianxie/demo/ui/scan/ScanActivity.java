package com.tianxie.demo.ui.scan;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.manager.RxJavaManager;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.SizeUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.ui.view.title.TitleBarView;
import com.tianx.demo.R;
import com.tianxie.demo.entity.LoginEntity;
import com.tianxie.demo.entity.ScanResult;
import com.tianxie.demo.ui.BaseUI.TitleActivity;
import com.tianxie.demo.util.ViewColorUtil;
import com.tianxie.demo.widget.CodeView;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.yanzhenjie.zbar.camera.CameraPreview;
import com.yanzhenjie.zbar.camera.ScanCallback;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import androidx.core.app.ActivityCompat;
import butterknife.BindView;

public class ScanActivity extends TitleActivity
{
    @BindView(R.id.capture_crop_view)
    RelativeLayout mScanCropView;
    @BindView(R.id.capture_scan_line)
    ImageView mScanLine;
    @BindView(R.id.capture_preview)
    CameraPreview mPreviewView;
    @BindView(R.id.codeView)
    CodeView mCodeView;

    /**
     * Accept scan result.
     */
    private ScanCallback resultCallback = (String content) ->
    {
        if (content != null && !content.isEmpty())
        {
            String startWith = null;
            boolean isUrl = false;
            do
            {
                if (content.startsWith("http://www.baoboka.com"))
                {
                    startWith = "http://www.baoboka.com";
                    isUrl = true;
                    break;
                }

                if (content.startsWith("https://www.baoboka.com"))
                {
                    startWith = "https://www.baoboka.com";
                    isUrl = true;
                    break;
                }

                if (content.startsWith("MM860301"))
                {
                    startWith = "MM860301";
                    break;
                }

            } while (false);

            String idNumber = null;
            if (startWith != null)
            {
                if (isUrl)
                {
                    try
                    {
                        idNumber = content.split("\\?")[1].split("=")[1];
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                } else
                {
                    idNumber = content;
                }
            }

            if (idNumber != null)
            {
                ScanResult scanPost = new ScanResult();
                scanPost.setIdNumber(idNumber);
//                scanPost.setUrl(content);
//                LoginEntity entity = getUseEntitiy();
//                if (isCanRequest == true && entity != null)
//                    scanPost.setToken(getUseEntitiy().getData());
                Intent intent = new Intent(mContext, ScanResultActivity.class);
                intent.putExtra(ScanResultActivity.SCAN_POST, scanPost);
                startActivity(intent);
                finish();
            } else
                ToastUtil.show("扫描无效");
        }

        if (mPreviewView != null)
            mPreviewView.reStartScan();
    };

    @Override
    public int getContentLayout()
    {
        return R.layout.activity_scan;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mPreviewView.start(SizeUtil.dp2px(250), SizeUtil.dp2px(250));
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mPreviewView.stop();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    public void initView(Bundle savedInstanceState)
    {
        mPreviewView.setScanCallback(resultCallback);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar)
    {
        titleBar.setLeftTextDrawable(ViewColorUtil.zoomDrawable(getDrawable(R.mipmap.img_back), SizeUtil.dp2px(48), SizeUtil.dp2px(48)))
                .setBgColor(ActivityCompat.getColor(this, R.color.black))
                .setTitleMainTextColor(Color.WHITE)
                .setTitleMainText(R.string.scan_title);
    }
}
