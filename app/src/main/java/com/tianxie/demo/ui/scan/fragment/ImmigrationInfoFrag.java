package com.tianxie.demo.ui.scan.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aries.library.fast.basis.BasisFragment;
import com.aries.library.fast.manager.GlideManager;
import com.aries.library.fast.util.SizeUtil;
import com.tianx.demo.R;
import com.tianxie.demo.base.Common;
import com.tianxie.demo.entity.EntryInfo;
import com.tianxie.demo.ui.scan.ScanResultActivity;

public class ImmigrationInfoFrag extends BasisFragment
{
    @Override
    public int getContentLayout()
    {
        return R.layout.layout_immigration_info;
    }

    @Override
    public void initView(Bundle savedInstanceState)
    {

    }

    public void loadData()
    {
        Bundle args = getArguments();
        if (args != null)
        {

            if (entryInfo == null)
                entryInfo = (EntryInfo) args.getSerializable(ScanResultActivity.FRAG_TAG);

            if (entryInfo != null)
            {
                ((TextView) mContentView.findViewById(R.id.text_passNumber)).setText(entryInfo.getPassNumber());
                ((TextView) mContentView.findViewById(R.id.text_reason)).setText(entryInfo.getReason());
                ((TextView) mContentView.findViewById(R.id.text_destination)).setText(entryInfo.getDestination());
                ((TextView) mContentView.findViewById(R.id.text_entryPort)).setText(entryInfo.getEntryPort());
                ((TextView) mContentView.findViewById(R.id.text_exitPort)).setText(entryInfo.getExitPort());
                ((TextView) mContentView.findViewById(R.id.text_entryDate)).setText(entryInfo.getEntryDate());
                ((TextView) mContentView.findViewById(R.id.text_cardValidDate)).setText(entryInfo.getCardValidDate());
                setViewWidth(mContentView.findViewById(R.id.cardFront));
                setViewWidth(mContentView.findViewById(R.id.cardBack));
                GlideManager.loadImg(Common.PIC_URL + entryInfo.getBorderPassFrontUrl(), (ImageView) mContentView.findViewById(R.id.cardFront), R.drawable.shape_scan_base_error_image);
                GlideManager.loadImg(Common.PIC_URL + entryInfo.getBorderPassBackUrl(), (ImageView) mContentView.findViewById(R.id.cardBack), R.drawable.shape_scan_base_error_image);
            }
        }
    }

    public void setViewWidth(View v)
    {
        ViewGroup.LayoutParams params =  mContentView.findViewById(R.id.cardFront).getLayoutParams();
        params.height = (int) ((SizeUtil.getScreenWidth() - 2 * SizeUtil.dp2px(ScanResultActivity.HEIGHT_PADDING)) * 0.6);
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        v.setLayoutParams(params);
    }


    EntryInfo entryInfo;

    @Override
    public Object getObject()
    {
        return entryInfo;
    }
}
