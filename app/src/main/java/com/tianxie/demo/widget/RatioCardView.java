package com.tianxie.demo.widget;

import android.content.Context;
import android.util.AttributeSet;

import net.soulwolf.widget.ratiolayout.RatioDatumMode;
import net.soulwolf.widget.ratiolayout.RatioLayoutDelegate;
import net.soulwolf.widget.ratiolayout.RatioMeasureDelegate;

import androidx.cardview.widget.CardView;

/**
 * Copyright ©2017 by ruzhan
 */

public class RatioCardView extends CardView implements RatioMeasureDelegate
{

    private RatioLayoutDelegate mRatioLayoutDelegate;

    public RatioCardView(Context context) {
        super(context);
    }

    public RatioCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRatioLayoutDelegate = RatioLayoutDelegate.obtain(this, attrs);
    }

    public RatioCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRatioLayoutDelegate = RatioLayoutDelegate.obtain(this, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mRatioLayoutDelegate != null) {
            mRatioLayoutDelegate.onMeasure(widthMeasureSpec, heightMeasureSpec);
            widthMeasureSpec = mRatioLayoutDelegate.getWidthMeasureSpec();
            heightMeasureSpec = mRatioLayoutDelegate.getHeightMeasureSpec();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setDelegateMeasuredDimension(int measuredWidth, int measuredHeight) {
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    public void setRatio(RatioDatumMode mode, float datumWidth, float datumHeight) {
        if (mRatioLayoutDelegate != null) {
            mRatioLayoutDelegate.setRatio(mode, datumWidth, datumHeight);
        }
    }
}
