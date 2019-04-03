package com.tianxie.demo.decoration;

import android.graphics.Rect;
import android.view.View;

import com.aries.library.fast.util.SizeUtil;

import androidx.recyclerview.widget.RecyclerView;

public class ScanResultDecoration extends RecyclerView.ItemDecoration
{
    private int width = 10;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        int position = parent.getChildAdapterPosition(view);

        if (position != 0)
        {
            outRect.top = SizeUtil.dp2px(width);
            outRect.left = SizeUtil.dp2px(4);
        }
    }
}
