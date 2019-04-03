package com.tianxie.demo.decoration;

import android.graphics.Rect;
import android.view.View;

import com.aries.library.fast.util.SizeUtil;

import androidx.recyclerview.widget.RecyclerView;

public class SearchFindDecoration extends RecyclerView.ItemDecoration
{
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        int position = parent.getChildAdapterPosition(view);

        if (position != (parent.getAdapter().getItemCount() - 1))
            outRect.bottom = SizeUtil.dp2px(15);

    }
}
