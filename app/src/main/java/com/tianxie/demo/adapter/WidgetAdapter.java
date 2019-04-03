package com.tianxie.demo.adapter;

import android.view.ViewGroup;

import com.aries.library.fast.util.SizeUtil;
import com.scwang.smartrefresh.header.util.ColorUtils;
import com.tianx.demo.R;
import com.tianxie.demo.base.BaseItemTouchQuickAdapter;
import com.tianxie.demo.base.BaseItemTouchViewHolder;
import com.tianxie.demo.entity.WidgetEntity;
import com.tianxie.demo.widget.RatioCardView;

import java.util.ArrayList;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;


/**
 * @Author: AriesHoo on 2018/8/10 9:51
 * @E-Mail: AriesHoo@126.com
 * Function: 描述性条目适配器
 * Description:
 */
public class WidgetAdapter extends BaseItemTouchQuickAdapter<WidgetEntity, BaseItemTouchViewHolder>
{

    private int width = -1;
    public static final int HOME_ITEM_SPACE = SizeUtil.dp2px(6);

    public WidgetAdapter()
    {
        super(R.layout.layout_main_item, new ArrayList<>());
    }

    @Override
    protected void convert(BaseItemTouchViewHolder helper, WidgetEntity item)
    {
        helper.setImageResource(R.id.main_item_image, item.imageID)
                .setText(R.id.main_item_text, item.title);
        ((RatioCardView) helper.itemView).setCardBackgroundColor(ActivityCompat.getColor(getRecyclerView().getContext(), item.backID));


        if (width == -1)
        {
            RecyclerView recyclerView = getRecyclerView();
            width = recyclerView.getWidth() / 2;
        }

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) helper.itemView.getLayoutParams();
        layoutParams.width = width - HOME_ITEM_SPACE;
        layoutParams.height = width - HOME_ITEM_SPACE;
    }

}
