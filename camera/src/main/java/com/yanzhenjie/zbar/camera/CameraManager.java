/*
 * Copyright © Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.zbar.camera;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * <p>Camera manager.</p>
 * Created by Yan Zhenjie on 2017/5/10.
 */
public final class CameraManager
{
    private static final int MIN_PREVIEW_PIXELS = 640 * 480;
    private static final int MAX_PREVIEW_PIXELS = 1280 * 720;


    private final CameraConfiguration mConfiguration;

    private Camera mCamera;

    public CameraManager(Context context)
    {
        this.mConfiguration = new CameraConfiguration(context);
    }

    /**
     * Opens the mCamera driver and initializes the hardware parameters.
     *
     * @throws Exception ICamera open failed, occupied or abnormal.
     */
    public synchronized void openDriver(int width, int height) throws Exception
    {
        if (mCamera != null) return;

        mCamera = Camera.open();
        if (mCamera == null) throw new IOException("The camera is occupied.");

        mConfiguration.initFromCameraParameters(mCamera);

        Camera.Parameters parameters = mCamera.getParameters();
        String parametersFlattened = parameters == null ? null : parameters.flatten();
        try
        {
            mConfiguration.setDesiredCameraParameters(mCamera, false);
        } catch (RuntimeException re)
        {
            if (parametersFlattened != null)
            {
                parameters = mCamera.getParameters();
                parameters.unflatten(parametersFlattened);
                try
                {
                    mCamera.setParameters(parameters);
                    mConfiguration.setDesiredCameraParameters(mCamera, true);
                } catch (RuntimeException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Closes the camera driver if still in use.
     */
    public synchronized void closeDriver()
    {
        if (mCamera != null)
        {
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * Camera is opened.
     *
     * @return true, other wise false.
     */
    public boolean isOpen()
    {
        return mCamera != null;
    }

    /**
     * Get camera configuration.
     *
     * @return {@link CameraConfiguration}.
     */
    public CameraConfiguration getConfiguration()
    {
        return mConfiguration;
    }

    /**
     * Camera start preview.
     *
     * @param holder          {@link SurfaceHolder}.
     * @param previewCallback {@link Camera.PreviewCallback}.
     * @throws IOException if the method fails (for example, if the surface is unavailable or unsuitable).
     */
    public void startPreview(SurfaceHolder holder, Camera.PreviewCallback previewCallback) throws IOException
    {
        if (mCamera != null)
        {
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(holder);
            mCamera.setPreviewCallback(previewCallback);
            mCamera.startPreview();
        }
    }

    /**
     * Camera stop preview.
     */
    public void stopPreview()
    {
        if (mCamera != null)
        {
            try
            {
                mCamera.stopPreview();
            } catch (Exception ignored)
            {
                // nothing.
            }
            try
            {
                mCamera.setPreviewDisplay(null);
            } catch (IOException ignored)
            {
                // nothing.
            }
        }
    }

    /**
     * Focus on, make a scan action.
     *
     * @param callback {@link Camera.AutoFocusCallback}.
     */
    public void autoFocus(Camera.AutoFocusCallback callback)
    {
        if (mCamera != null)
            try
            {
                mCamera.autoFocus(callback);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
    }
}
