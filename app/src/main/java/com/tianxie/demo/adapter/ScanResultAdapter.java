package com.tianxie.demo.adapter;

import com.tianx.demo.R;
import com.tianxie.demo.base.BaseItemTouchQuickAdapter;
import com.tianxie.demo.base.BaseItemTouchViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ScanResultAdapter extends BaseItemTouchQuickAdapter<String, BaseItemTouchViewHolder>
{
    public ScanResultAdapter()
    {
        super(R.layout.layout_scan_result_item, new ArrayList<>());
    }

    @Override
    protected void convert(BaseItemTouchViewHolder helper, String item)
    {
        if (item != null)
            helper.setText(R.id.item_text, item);


        if (helper.getLayoutPosition() != 0)
            helper.addOnClickListener(R.id.scan_result_item);
    }
}
