package com.tianxie.demo.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aries.library.fast.util.FastUtil;

/**
 * @Author: AriesHoo on 2018/11/2 13:27
 * @E-Mail: AriesHoo@126.com
 * @Function: 标题栏背景颜色变更帮助类
 * @Description:
 */
public class ViewColorUtil {

    private static volatile ViewColorUtil sInstance;

    private ViewColorUtil() {
    }

    public static ViewColorUtil getInstance() {
        if (sInstance == null) {
            synchronized (ViewColorUtil.class) {
                if (sInstance == null) {
                    sInstance = new ViewColorUtil();
                }
            }
        }
        return sInstance;
    }

    public void changeColor(View rootView, int alpha, boolean mIsLight, boolean showText) {
        if (rootView != null) {
            //滚动视图
            if (rootView instanceof TextView) {
                TextView textView = (TextView) rootView;
                Drawable[] drawables = textView.getCompoundDrawables();
                for (Drawable item : drawables) {
                    if (item != null) {
                        //使用该方法避免同一Drawable被全局修改
                        item = item.mutate();
                        FastUtil.getTintDrawable(item, Color.argb(mIsLight ? alpha : 255 - alpha, mIsLight ? 0 : 255, mIsLight ? 0 : 255, mIsLight ? 0 : 255));
                    }
                }
                if (!showText) {
                    textView.setTextColor(Color.argb(alpha, mIsLight ? 0 : 255, mIsLight ? 0 : 255, mIsLight ? 0 : 255));
                } else {
                    textView.setTextColor(Color.argb(mIsLight ? alpha : 255 - alpha, mIsLight ? 0 : 255, mIsLight ? 0 : 255, mIsLight ? 0 : 255));
                }
            } else if (rootView instanceof ImageView) {
                //使用该方法避免同一Drawable被全局修改
                Drawable drawable = ((ImageView) rootView).getDrawable().mutate();
                FastUtil.getTintDrawable(drawable, Color.argb(mIsLight ? alpha : 255 - alpha, mIsLight ? 0 : 255, mIsLight ? 0 : 255, mIsLight ? 0 : 255));
            } else if (rootView instanceof ViewGroup) {
                ViewGroup contentView = (ViewGroup) rootView;
                int size = contentView.getChildCount();
                //循环遍历所有子View
                for (int i = 0; i < size; i++) {
                    View childView = contentView.getChildAt(i);
                    //递归查找
                    changeColor(childView, alpha, mIsLight, showText);
                }
            }
        }
    }

    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        oldbmp.recycle();
        return new BitmapDrawable(null, newbmp);
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }
}
