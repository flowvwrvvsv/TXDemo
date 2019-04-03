/**
 * Copyright (C) 2017 Baidu Inc. All rights reserved.
 */
package com.tianxie.demo.ui.face;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.FaceDetector;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.manager.RxJavaManager;
import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.SPUtil;
import com.aries.library.fast.util.SizeUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.ui.view.title.TitleBarView;
import com.baidu.aip.face.stat.Ast;
import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.FaceStatusEnum;
import com.baidu.idl.face.platform.IDetectStrategy;
import com.baidu.idl.face.platform.IDetectStrategyCallback;
import com.baidu.idl.face.platform.ui.FaceSDKResSettings;
import com.baidu.idl.face.platform.ui.utils.CameraUtils;
import com.baidu.idl.face.platform.ui.utils.VolumeUtils;
import com.baidu.idl.face.platform.ui.widget.FaceDetectRoundView;
import com.baidu.idl.face.platform.utils.APIUtils;
import com.baidu.idl.face.platform.utils.Base64Utils;
import com.baidu.idl.face.platform.utils.CameraPreviewUtils;
import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.tianx.demo.R;
import com.tianxie.demo.base.Common;
import com.tianxie.demo.database.FaceModel;
import com.tianxie.demo.database.FaceModel_Table;
import com.tianxie.demo.entity.BaiduAccessToken;
import com.tianxie.demo.entity.FaceSearchResult;
import com.tianxie.demo.entity.FaceUserEntitiy;
import com.tianxie.demo.entity.RegistFaceResult;
import com.tianxie.demo.retrofit.repository.ApiRepository;
import com.tianxie.demo.ui.BaseUI.TitleActivity;
import com.tianxie.demo.util.ViewColorUtil;
import com.tianxie.demo.widget.ResizeAbleSurfaceView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import androidx.core.app.ActivityCompat;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * 人脸采集接口
 */
public class TxFaceDetectActivity extends TitleActivity implements
        SurfaceHolder.Callback,
        Camera.PreviewCallback,
        Camera.ErrorCallback,
        VolumeUtils.VolumeCallback,
        IDetectStrategyCallback
{

    public static final String TAG = TxFaceDetectActivity.class.getSimpleName();
    public static final String DETECT_CONFIG = "FaceOptions";

    // View
    protected View mRootView;
    protected FrameLayout mFrameLayout;
    protected ResizeAbleSurfaceView mSurfaceView;
    protected SurfaceHolder mSurfaceHolder;
    protected ImageView mSoundView;
    protected ImageView mSuccessView;
    protected TextView mTipsTopView;
    protected TextView mTipsBottomView;
    protected FaceDetectRoundView mFaceDetectRoundView;
    protected View mSuccessContainer;
    protected ImageView mSuccessImage;
    // 人脸信息
    protected FaceConfig mFaceConfig;
    protected IDetectStrategy mIDetectStrategy;
    // 显示Size
    private Rect mPreviewRect = new Rect();
    protected int mDisplayWidth = 0;
    protected int mDisplayHeight = 0;
    protected int mSurfaceWidth = 0;
    protected int mSurfaceHeight = 0;
    protected Drawable mTipsIcon;
    // 状态标识
    protected volatile boolean mIsEnableSound = true;
    protected HashMap<String, String> mBase64ImageMap = new HashMap<String, String>();
    protected boolean mIsCreateSurface = false;
    protected volatile boolean mIsCompletion = false;
    // 相机
    protected Camera mCamera;
    protected Camera.Parameters mCameraParam;
    protected int mCameraId;
    protected int mPreviewWidth;
    protected int mPreviewHight;
    protected int mPreviewDegree;
    private boolean bBack = true;
    // 监听系统音量广播
    protected BroadcastReceiver mVolumeReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = this.getWindowManager().getDefaultDisplay();
        display.getMetrics(dm);
        mDisplayWidth = dm.widthPixels;
        mDisplayHeight = dm.heightPixels;

        FaceSDKResSettings.initializeResId();
        mFaceConfig = FaceSDKManager.getInstance().getFaceConfig();

        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int vol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        mIsEnableSound = vol > 0 ? mFaceConfig.isSound : false;

        mRootView = this.findViewById(R.id.detect_root_layout);
        mSuccessContainer = findViewById(R.id.success_container);
        mSuccessImage = findViewById(R.id.success_imageview);
        mFrameLayout = mRootView.findViewById(R.id.detect_surface_layout);

        mSurfaceView = new ResizeAbleSurfaceView(this);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setSizeFromLayout();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

//        int w = mDisplayWidth;
//        int h = mDisplayHeight;
//
//        FrameLayout.LayoutParams cameraFL = new FrameLayout.LayoutParams(
//                (int) (w * FaceDetectRoundView.SURFACE_RATIO), (int) (h * FaceDetectRoundView.SURFACE_RATIO),
//                Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        FrameLayout.LayoutParams cameraFL = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        mSurfaceView.setLayoutParams(cameraFL);
        mFrameLayout.addView(mSurfaceView);

        findViewById(R.id.detect_close).setOnClickListener(view -> {
            onBackPressed();
        });

        mFaceDetectRoundView = mRootView.findViewById(R.id.detect_face_round);
        mSoundView = findViewById(R.id.detect_sound);
        mSoundView.setImageResource(mIsEnableSound ? R.mipmap.ic_enable_sound_ext : R.mipmap.ic_disable_sound_ext);
//        findViewById(R.id.detect_sound_container).setOnClickListener((View view) ->
//        {
//            mIsEnableSound = !mIsEnableSound;
//            mSoundView.setImageResource(mIsEnableSound ? R.mipmap.ic_enable_sound_ext : R.mipmap.ic_disable_sound_ext);
//            if (mIDetectStrategy != null)
//                mIDetectStrategy.setDetectStrategySoundEnable(mIsEnableSound);
//        });

//        findViewById(R.id.detect_sound_container).setOnClickListener(view -> {
//            changeFace();
//        });
        mTipsTopView = findViewById(R.id.detect_top_tips);
        mTipsBottomView = findViewById(R.id.detect_bottom_tips);
        mSuccessView = findViewById(R.id.detect_success_image);

        if (mBase64ImageMap != null)
            mBase64ImageMap.clear();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (mVolumeReceiver != null)
            VolumeUtils.unRegisterVolumeReceiver(this, mVolumeReceiver);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mVolumeReceiver = VolumeUtils.registerVolumeReceiver(this, this);
        if (mTipsTopView != null)
            mTipsTopView.setText(R.string.detect_face_in);

        startPreview();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        stopPreview();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (mVolumeReceiver != null)
            VolumeUtils.unRegisterVolumeReceiver(this, mVolumeReceiver);
        mVolumeReceiver = null;
        if (mIDetectStrategy != null)
        {
            mIDetectStrategy.reset();
        }
        stopPreview();
    }

    @Override
    public void finish()
    {
        if (mVolumeReceiver != null)
            VolumeUtils.unRegisterVolumeReceiver(this, mVolumeReceiver);
        super.finish();
    }

    @Override
    public void volumeChanged()
    {
        try
        {
            AudioManager am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            if (am != null)
            {
                int cv = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                mIsEnableSound = cv > 0;
                mSoundView.setImageResource(mIsEnableSound
                        ? R.mipmap.ic_enable_sound_ext : R.mipmap.ic_disable_sound_ext);
                if (mIDetectStrategy != null)
                {
                    mIDetectStrategy.setDetectStrategySoundEnable(mIsEnableSound);
                }
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void changeFace()
    {
        bBack = !bBack;
        mIsCompletion = false;

        mSuccessContainer.setVisibility(View.GONE);
        mSuccessImage.setImageBitmap(null);

        stopPreview();
        if (mIDetectStrategy != null)
            mIDetectStrategy.reset();

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mVolumeReceiver = VolumeUtils.registerVolumeReceiver(this, this);
        if (mTipsTopView != null)
            mTipsTopView.setText(R.string.detect_face_in);

        startPreview();
    }

    private Camera open()
    {
        Camera camera;
        int numCameras = Camera.getNumberOfCameras();
        if (numCameras == 0)
        {
            return null;
        }


        int indexCamera = bBack ? Camera.CameraInfo.CAMERA_FACING_BACK : Camera.CameraInfo.CAMERA_FACING_FRONT;
        int index = 0;
        while (index < numCameras)
        {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(index, cameraInfo);
            if (cameraInfo.facing == indexCamera)
            {
                break;
            }
            index++;
        }

        if (index < numCameras)
        {
            camera = Camera.open(index);
            mCameraId = index;
        } else
        {
            camera = Camera.open(0);
            mCameraId = 0;
        }
        return camera;
    }

    protected void startPreview()
    {
        if (mCamera == null)
        {
            try
            {
                mCamera = open();
            } catch (RuntimeException e)
            {
                e.printStackTrace();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        if (mCamera == null)
        {
            return;
        }
        if (mCameraParam == null)
        {
            mCameraParam = mCamera.getParameters();
        }

        mCameraParam.setPictureFormat(PixelFormat.JPEG);
        int degree = displayOrientation(this);
        mCamera.setDisplayOrientation(degree);
        // 设置后无效，camera.setDisplayOrientation方法有效
        mCameraParam.set("rotation", degree);
        mPreviewDegree = degree;
        if (mIDetectStrategy != null)
            mIDetectStrategy.setPreviewDegree(degree);

//        Point point = CameraPreviewUtils.getBestPreview(mCameraParam,
//                new Point(mDisplayWidth, (int) (mDisplayHeight * 0.9)));
        Point point = CameraPreviewUtils.findBestPreviewSizeValue(mCameraParam,
                new Point(mDisplayHeight >= mDisplayWidth ? mDisplayHeight : mDisplayWidth, mDisplayHeight <= mDisplayWidth ? mDisplayHeight : mDisplayWidth));
        mPreviewWidth = point.x;
        mPreviewHight = point.y;

        mPreviewRect.set(0, 0, mPreviewHight, mPreviewWidth);
        try
        {
            mCameraParam.setPreviewSize(mPreviewWidth, mPreviewHight);
            mCamera.setParameters(mCameraParam);

            Camera.Parameters afterParameters = mCamera.getParameters();
            Camera.Size afterSize = afterParameters.getPreviewSize();
            if (afterSize != null && (mPreviewWidth != afterSize.width || mPreviewHight != afterSize.height))
            {
                mPreviewWidth = afterSize.width;
                mPreviewHight = afterSize.height;
            }

        } catch (Exception e)
        {
            //非常罕见的情况
            //个别机型在SupportPreviewSizes里汇报了支持某种预览尺寸，但实际是不支持的，设置进去就会抛出RuntimeException.
            e.printStackTrace();
            try
            {
                //遇到上面所说的情况，只能设置一个最小的预览尺寸
                mCameraParam.setPreviewSize(720, 480);
                mCamera.setParameters(mCameraParam);
                mPreviewWidth = 720;
                mPreviewHight = 480;
            } catch (Exception e1)
            {
                //到这里还有问题，就是拍照尺寸的锅了，同样只能设置一个最小的拍照尺寸
                e1.printStackTrace();
                try
                {
                    mCameraParam.setPreviewSize(720, 480);
                    mCameraParam.setPictureSize(720, 480);
                    mCamera.setParameters(mCameraParam);
                    mPreviewWidth = 720;
                    mPreviewHight = 480;
                } catch (Exception ignored)
                {
                }
            }
        }

        try
        {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.stopPreview();
            mCamera.setErrorCallback(this);
            mCamera.setPreviewCallback(this);
            mCamera.startPreview();
        } catch (RuntimeException e)
        {
            e.printStackTrace();
            CameraUtils.releaseCamera(mCamera);
            mCamera = null;
        } catch (Exception e)
        {
            e.printStackTrace();
            CameraUtils.releaseCamera(mCamera);
            mCamera = null;
        }
    }

    protected void stopPreview()
    {
        if (mCamera != null)
        {
            try
            {
                mCameraParam = null;
                mCamera.setErrorCallback(null);
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
            } catch (RuntimeException e)
            {
                e.printStackTrace();
            } catch (Exception e)
            {
                e.printStackTrace();
            } finally
            {
                CameraUtils.releaseCamera(mCamera);
                mCamera = null;
            }
        }
        if (mSurfaceHolder != null)
        {
            mSurfaceHolder.removeCallback(this);
        }
        if (mIDetectStrategy != null)
        {
            mIDetectStrategy = null;
        }
    }

    private int displayOrientation(Context context)
    {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int rotation = windowManager.getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation)
        {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
            default:
                degrees = 0;
                break;
        }
        int result = (0 - degrees + 360) % 360;
        if (APIUtils.hasGingerbread())
        {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(mCameraId, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
            {
                result = (info.orientation + degrees) % 360;
                result = (360 - result) % 360;
            } else
            {
                result = (info.orientation - degrees + 360) % 360;
            }
        }
        return result;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        mIsCreateSurface = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder,
                               int format,
                               int width,
                               int height)
    {
        mSurfaceWidth = width;
        mSurfaceHeight = height;
        if (holder.getSurface() == null)
        {
            return;
        }
        startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        mIsCreateSurface = false;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera)
    {
        if (mIsCompletion)
        {
            return;
        }

        if (mIDetectStrategy == null && mFaceDetectRoundView != null && mFaceDetectRoundView.getRound() > 0)
        {
            mIDetectStrategy = FaceSDKManager.getInstance().getDetectStrategyModule();
            mIDetectStrategy.setPreviewDegree(bBack ? mPreviewDegree + 180 : mPreviewDegree);
            mIDetectStrategy.setDetectStrategySoundEnable(mIsEnableSound);

            Rect detectRect = FaceDetectRoundView.getPreviewDetectRect(mDisplayWidth, mPreviewHight, mPreviewWidth);
            mIDetectStrategy.setDetectStrategyConfig(mPreviewRect, detectRect, this);
        }
        if (mIDetectStrategy != null)
        {
            mIDetectStrategy.detectStrategy(data);
        }
    }

    @Override
    public void onError(int error, Camera camera)
    {
    }

    @Override
    public void onDetectCompletion(FaceStatusEnum status, String message,
                                   HashMap<String, String> base64ImageMap)
    {
        if (mIsCompletion)
        {
            return;
        }

        onRefreshView(status, message);

        if (status == FaceStatusEnum.OK)
        {
            mIsCompletion = true;
            saveImage(base64ImageMap);
        } else if (status == FaceStatusEnum.Error_DetectTimeout ||
                status == FaceStatusEnum.Error_LivenessTimeout ||
                status == FaceStatusEnum.Error_Timeout)
        {
            ToastUtil.show("人脸图像采集超时 ！");
        }
        Ast.getInstance().faceHit("detect");
    }


    private void onRefreshView(FaceStatusEnum status, String message)
    {
        switch (status)
        {
            case OK:
                onRefreshTipsView(false, message);
                mTipsBottomView.setText("");
                mFaceDetectRoundView.processDrawState(false);
                onRefreshSuccessView(true);
                break;
            case Detect_PitchOutOfUpMaxRange:
            case Detect_PitchOutOfDownMaxRange:
            case Detect_PitchOutOfLeftMaxRange:
            case Detect_PitchOutOfRightMaxRange:
                onRefreshTipsView(true, message);
                mTipsBottomView.setText(message);
                mFaceDetectRoundView.processDrawState(true);
                onRefreshSuccessView(false);
                break;
            default:
                onRefreshTipsView(false, message);
                mTipsBottomView.setText("");
                mFaceDetectRoundView.processDrawState(true);
                onRefreshSuccessView(false);
        }
    }

    private void onRefreshTipsView(boolean isAlert, String message)
    {
        if (isAlert)
        {
            if (mTipsIcon == null)
            {
                mTipsIcon = getResources().getDrawable(R.mipmap.ic_warning);
                mTipsIcon.setBounds(0, 0, (int) (mTipsIcon.getMinimumWidth() * 0.7f),
                        (int) (mTipsIcon.getMinimumHeight() * 0.7f));
                mTipsTopView.setCompoundDrawablePadding(15);
            }
            mTipsTopView.setBackgroundResource(R.drawable.bg_tips);
            mTipsTopView.setText(R.string.detect_standard);
            mTipsTopView.setCompoundDrawables(mTipsIcon, null, null, null);
        } else
        {
            mTipsTopView.setBackgroundResource(R.drawable.bg_tips_no);
            mTipsTopView.setCompoundDrawables(null, null, null, null);
            if (!TextUtils.isEmpty(message))
            {
                mTipsTopView.setText(message);
            }
        }
    }

    private void onRefreshSuccessView(boolean isShow)
    {
//        if (mSuccessView.getTag() == null)
//        {
//            Rect rect = mFaceDetectRoundView.getFaceRoundRect();
//            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) mSuccessView.getLayoutParams();
//            rlp.setMargins(
//                    rect.centerX() - (mSuccessView.getWidth() / 2),
//                    rect.top - (mSuccessView.getHeight() / 2),
//                    0,
//                    0);
//            mSuccessView.setLayoutParams(rlp);
//            mSuccessView.setTag("setlayout");
//        }
//        mSuccessView.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }


    private String bestImage = null;
    private Bitmap bitmap = null;

    private void saveImage(HashMap<String, String> imageMap)
    {
        if (mIDetectStrategy != null)
        {
            ViewGroup.LayoutParams params = mSuccessContainer.getLayoutParams();
            params.height = mFaceDetectRoundView.getCycleBottom();

            mSuccessContainer.setLayoutParams(params);
            mSuccessContainer.setVisibility(View.VISIBLE);

            ViewGroup.LayoutParams imageLayoutParams = mSuccessImage.getLayoutParams();
            imageLayoutParams.height = mFaceDetectRoundView.getCycleBottom() - SizeUtil.dp2px(48);
            imageLayoutParams.width = imageLayoutParams.height * mPreviewHight / mPreviewWidth;
            mSuccessImage.setLayoutParams(imageLayoutParams);

            bestImage = mIDetectStrategy.getBestFaceImage();
            byte[] imageByte = Base64Utils.decode(bestImage, Base64Utils.NO_WRAP);
            if (imageByte != null && imageByte.length > 0)
            {
                bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
                if (bitmap != null)
                {
                    if (bBack)
                    {
                        Matrix m = new Matrix();
                        m.postScale(-1, 1); // 镜像水平翻转
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                    }
                    mSuccessImage.setImageBitmap(bitmap);
                }
            }

            mSuccessContainer.findViewById(R.id.face_sure_compare).setOnClickListener(view -> {
                stopPreview();
                mFrameLayout.setVisibility(View.GONE);
                mTitleBar.setVisibility(View.GONE);
                findViewById(R.id.root).setBackgroundResource(R.mipmap.img_face_rec_bkg);
                mSuccessContainer.setVisibility(View.GONE);
                mTipsTopView.setVisibility(View.GONE);
                mFaceDetectRoundView.startCompare();


                BaiduAccessToken baiduAccessToken = (BaiduAccessToken) (new Gson().fromJson((String) SPUtil.get(TxFaceDetectActivity.this, BaiduAccessToken.BAIDU_ACCESS_TOKEN, ""), BaiduAccessToken.class));
                Observer<BaiduAccessToken> tokenObservable = null;
                if (baiduAccessToken == null || ((System.currentTimeMillis() / 1000 - (baiduAccessToken.getUpdate_in() + baiduAccessToken.getUpdate_in())) < 60))
                {
                    tokenObservable = new FastLoadingObserver<BaiduAccessToken>()
                    {
                        @Override
                        public void _onNext(BaiduAccessToken entity)
                        {
                            if (entity != null && entity.getAccess_token() != null)
                            {
                                entity.setUpdate_in((int) System.currentTimeMillis());
                                Gson gson = new Gson();
                                SPUtil.put(TxFaceDetectActivity.this, BaiduAccessToken.BAIDU_ACCESS_TOKEN, gson.toJson(entity));

                                faceSearch(entity.getAccess_token(), new FastLoadingObserver<FaceSearchResult>()
                                {
                                    @Override
                                    public void _onNext(FaceSearchResult entity)
                                    {
                                        if (entity != null && entity.getResult() != null && entity.getResult().getUser_list() != null && !entity.getResult().getUser_list().isEmpty())
                                        {
                                            List<FaceUserEntitiy> list = entity.getResult().getUser_list();
                                            for (FaceUserEntitiy faceUserEntitiy : list)
                                            {
                                                List<FaceModel> user2Models = SQLite.select().from(FaceModel.class).where(FaceModel_Table.group_id.eq(faceUserEntitiy.getGroup_id()), FaceModel_Table.user_id.eq(faceUserEntitiy.getUser_id()))
                                                        .queryList();
                                                if (user2Models.size() != 0)
                                                    faceUserEntitiy.setNetUrl(user2Models.get(0).getUrl());
                                            }

                                            String name = "temp_" + GetRandomString(6) + (System.currentTimeMillis() / 1000) + ".jpg";
                                            saveBitmap(Common.FILE_ROOT_FACE_TEMP, name, false);
                                            entity.setbBack(bBack);
                                            entity.setNativeUrl(Common.FILE_ROOT_FACE_TEMP + name);
                                            mFaceDetectRoundView.stopCompare();
                                            Intent t = new Intent(TxFaceDetectActivity.this, FaceCompareResultActivity.class);
                                            t.putExtra(FaceCompareResultActivity.FACE_COMPARE, entity);
                                            startActivity(t);
                                            finish();
                                        } else
                                        {
                                            ToastUtil.show("对比失败");
                                        }
                                    }

                                    @Override
                                    public void _onError(Throwable e)
                                    {
                                        super._onError(e);
                                    }
                                });
                            }
                        }

                        @Override
                        public void _onError(Throwable e)
                        {
                            super._onError(e);
                            ToastUtil.show("对比失败");
                        }
                    };
                    getBaiduToken(tokenObservable);
                } else
                {
                    faceSearch(baiduAccessToken.getAccess_token(), new FastLoadingObserver<FaceSearchResult>()
                    {
                        @Override
                        public void _onNext(FaceSearchResult entity)
                        {
                            LoggerManager.e("当前线程：" + Thread.currentThread().getName());
                            if (entity != null && entity.getResult() != null && entity.getResult().getUser_list() != null && !entity.getResult().getUser_list().isEmpty())
                            {
                                RxJavaManager.getInstance();
                                List<FaceUserEntitiy> list = entity.getResult().getUser_list();
                                for (FaceUserEntitiy faceUserEntitiy : list)
                                {
                                    List<FaceModel> user2Models = SQLite.select().from(FaceModel.class).where(FaceModel_Table.group_id.eq(faceUserEntitiy.getGroup_id()), FaceModel_Table.user_id.eq(faceUserEntitiy.getUser_id()))
                                            .queryList();
                                    if (user2Models.size() != 0)
                                        faceUserEntitiy.setNetUrl(user2Models.get(0).getUrl());
                                }

                                String name = "temp_" + GetRandomString(6) + (System.currentTimeMillis() / 1000) + ".jpg";
                                saveBitmap(Common.FILE_ROOT_FACE_TEMP, name, false);
                                entity.setbBack(bBack);
                                entity.setNativeUrl(Common.FILE_ROOT_FACE_TEMP + name);
                                mFaceDetectRoundView.stopCompare();
                                Intent t = new Intent(TxFaceDetectActivity.this, FaceCompareResultActivity.class);
                                t.putExtra(FaceCompareResultActivity.FACE_COMPARE, entity);
                                startActivity(t);
                                finish();
                            } else
                            {
                                ToastUtil.show("没有匹配到对应的人脸");
                                finish();
                            }
                        }

                        @Override
                        public void _onError(Throwable e)
                        {
                            super._onError(e);
                            ToastUtil.show("对比失败");
                        }
                    });
                }
            });


            mSuccessContainer.findViewById(R.id.face_register).setOnClickListener(view -> {
                BaiduAccessToken baiduAccessToken = (BaiduAccessToken) (new Gson().fromJson((String) SPUtil.get(TxFaceDetectActivity.this, BaiduAccessToken.BAIDU_ACCESS_TOKEN, ""), BaiduAccessToken.class));
                Observer<BaiduAccessToken> tokenObservable = null;
                if (baiduAccessToken == null || ((System.currentTimeMillis() / 1000 - (baiduAccessToken.getUpdate_in() + baiduAccessToken.getUpdate_in())) < 60))
                {
                    tokenObservable = new FastLoadingObserver<BaiduAccessToken>()
                    {
                        @Override
                        public void _onNext(BaiduAccessToken entity)
                        {
                            if (entity != null && entity.getAccess_token() != null)
                            {
                                entity.setUpdate_in((int) System.currentTimeMillis());
                                Gson gson = new Gson();
                                SPUtil.put(TxFaceDetectActivity.this, BaiduAccessToken.BAIDU_ACCESS_TOKEN, gson.toJson(entity));

                                registBaiduFace(entity.getAccess_token(), new FastLoadingObserver<RegistFaceResult>()
                                {
                                    @Override
                                    public void _onNext(RegistFaceResult entity)
                                    {
                                        if (entity != null && entity.getResult() != null && entity.getResult().getFace_token() != null)
                                        {
                                            String name = entity.getResult().getFace_token() + ".jpg";
                                            saveBitmap(Common.FILE_ROOT_FACE, name, true);

                                            FaceModel faceModel = new FaceModel();
                                            faceModel.setGroup_id("rl");
                                            faceModel.setUser_id(user_id);
                                            faceModel.setUrl(Common.FILE_ROOT_FACE + name);
                                            faceModel.insert();

                                            ToastUtil.show("用户添加成功！");
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void _onError(Throwable e)
                                    {
                                        super._onError(e);
                                    }
                                });
                            }
                        }

                        @Override
                        public void _onError(Throwable e)
                        {
                            super._onError(e);
                        }
                    };
                    getBaiduToken(tokenObservable);
                } else
                {
                    registBaiduFace(baiduAccessToken.getAccess_token(), new FastLoadingObserver<RegistFaceResult>()
                    {
                        @Override
                        public void _onNext(RegistFaceResult entity)
                        {
                            if (entity != null && entity.getResult() != null && entity.getResult().getFace_token() != null)
                            {
                                String name = entity.getResult().getFace_token() + ".jpg";
                                saveBitmap(Common.FILE_ROOT_FACE, name, true);

                                FaceModel faceModel = new FaceModel();
                                faceModel.setGroup_id("rl");
                                faceModel.setUser_id(user_id);
                                faceModel.setUrl(Common.FILE_ROOT_FACE + name);
                                faceModel.insert();

                                ToastUtil.show("用户添加成功！");
                                finish();
                            }
                        }

                        @Override
                        public void _onError(Throwable e)
                        {
                            super._onError(e);
                        }
                    });
                }
            });
        }
    }

    private static boolean deleteDir(File dir)
    {
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++)
            {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success)
                    return false;
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public void saveBitmap(String path, String name, boolean isRegister)
    {
        File dir = new File(path);
        if (!isRegister)
            deleteDir(dir);

        if (!dir.exists())
            dir.mkdirs();

        File f = new File(path, name);
        if (f.exists())
            f.delete();
        try
        {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void faceSearch(String token, Observer observer)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("image", bestImage);
        map.put("liveness_control", "NORMAL");
        map.put("group_id_list", "rl");
        map.put("image_type", "BASE64");
        map.put("quality_control", "LOW");
        ApiRepository.getInstance().doFaceSearch(token, map).subscribe(observer);
    }

    String user_id = null;

    public void registBaiduFace(String token, Observer observer)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("image", bestImage);
        map.put("image_type", "BASE64");
        map.put("group_id", "rl");
        map.put("user_id", user_id = GetRandomString(10));
        map.put("user_info", "test测试");
        map.put("liveness_control", "NORMAL");
        map.put("quality_control", "LOW");
        map.put("action_type", "REPLACE");
        ApiRepository.getInstance().doRegistBaiduFace(token, map).subscribe(observer);
    }

    public void getBaiduToken(Observer observer)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("grant_type", "client_credentials");
        map.put("client_id", Common.BAIDU_API_KEY);
        map.put("client_secret", Common.BAIDU_SECRET_KEY);
        ApiRepository.getInstance().doGetBaiduToken(map).subscribe(observer);
    }

    public String GetRandomString(int Len)
    {

        String[] baseString = {"0", "1", "2", "3",
                "4", "5", "6", "7", "8", "9",
                "a", "b", "c", "d", "e",
                "f", "g", "h", "i", "j",
                "k", "l", "m", "n", "o",
                "p", "q", "r", "s", "t",
                "u", "v", "w", "x", "y",
                "z", "A", "B", "C", "D",
                "E", "F", "G", "H", "I",
                "J", "K", "L", "M", "N",
                "O", "P", "Q", "R", "S",
                "T", "U", "V", "W", "X", "Y", "Z"};
        Random random = new Random();
        int length = baseString.length;
        String randomString = "";
        for (int i = 0; i < length; i++)
        {
            randomString += baseString[random.nextInt(length)];
        }
        random = new Random(System.currentTimeMillis());
        String resultStr = "";
        for (int i = 0; i < Len; i++)
        {
            resultStr += randomString.charAt(random.nextInt(randomString.length() - 1));
        }
        return resultStr;
    }

    @Override
    public int getContentLayout()
    {
        return R.layout.activity_tx_face_detect;
    }

    @Override
    public void initView(Bundle savedInstanceState)
    {

    }

    @Override
    public void setTitleBar(TitleBarView titleBar)
    {
        titleBar.setLeftTextDrawable(ViewColorUtil.zoomDrawable(getDrawable(R.mipmap.ic_close_ext), SizeUtil.dp2px(48), SizeUtil.dp2px(48)))
                .setRightTextDrawable(ViewColorUtil.zoomDrawable(getDrawable(R.mipmap.img_camera_rotate), SizeUtil.dp2px(48), SizeUtil.dp2px(48)))
                .setOnRightTextClickListener(v -> {
                    changeFace();
                })
                .setBgColor(ActivityCompat.getColor(this, R.color.black))
                .setTitleMainTextColor(Color.WHITE)
                .setTitleMainText(R.string.face_reg);
    }
}
