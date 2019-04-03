package com.tianxie.demo.adapter;

import com.aries.library.fast.manager.GlideManager;
import com.tianx.demo.R;
import com.tianxie.demo.base.BaseItemTouchQuickAdapter;
import com.tianxie.demo.base.BaseItemTouchViewHolder;
import com.tianxie.demo.entity.SearchWidgetEntity;

import java.util.ArrayList;


public class SearchWidgeAdapter extends BaseItemTouchQuickAdapter<SearchWidgetEntity, BaseItemTouchViewHolder>
{
    public SearchWidgeAdapter()
    {
        super(R.layout.layout_search_find_item, new ArrayList<>());
    }

    @Override
    protected void convert(BaseItemTouchViewHolder helper, SearchWidgetEntity item)
    {
        if (item != null)
        {
            GlideManager.loadImg(item.getImageUrl(), helper.getView(R.id.search_find_item_image));
            helper.setText(R.id.item_name, item.getName())
                    .setText(R.id.item_sex, item.getSex())
                    .setText(R.id.item_nation, item.getNation())
                    .setText(R.id.item_religion, item.getReligion())
                    .setText(R.id.item_myanmar_id, item.getMyanmarID())
                    .setText(R.id.item_pass_id, item.getPassID())
                    .setText(R.id.item_document_id, item.getDocumentID())
                    .setText(R.id.item_document_period, item.getLimtDate());
        }
    }
}
