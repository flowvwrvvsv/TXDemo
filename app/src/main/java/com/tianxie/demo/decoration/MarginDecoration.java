package com.tianxie.demo.decoration;

import android.graphics.Rect;
import android.view.View;

import com.aries.library.fast.util.SizeUtil;
import com.tianxie.demo.adapter.WidgetAdapter;

import androidx.recyclerview.widget.RecyclerView;

public class MarginDecoration extends RecyclerView.ItemDecoration
{
    private int width = -1;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {

        if (width == -1)
            width = parent.getWidth() / 2;

        int position = parent.getChildAdapterPosition(view);
        if (position > 0 && position % 2 == 0)
            outRect.left = WidgetAdapter.HOME_ITEM_SPACE;


        if (position == 0)
            outRect.top = SizeUtil.dp2px(74);
        else
            outRect.top = 2 * WidgetAdapter.HOME_ITEM_SPACE;

    }
}