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
import com.tianxie.demo.entity.WorkInfo;
import com.tianxie.demo.ui.scan.ScanResultActivity;

public class WorkInfoFrag extends BasisFragment
{
    @Override
    public int getContentLayout()
    {
        return R.layout.layout_labor_info;
    }

    @Override
    public void initView(Bundle savedInstanceState)
    {
    }

    WorkInfo workInfo;

    @Override
    public Object getObject()
    {
        return workInfo;
    }


    public void loadData()
    {
        Bundle args = getArguments();
        if (args != null)
        {
            if (workInfo == null)
                workInfo = (WorkInfo) args.getSerializable(ScanResultActivity.FRAG_TAG);
            if (workInfo != null)
            {
                ((TextView) mContentView.findViewById(R.id.text_employingUnit)).setText(workInfo.getEmployingUnit());
                ((TextView) mContentView.findViewById(R.id.text_employingAddress)).setText(workInfo.getEmployingAddress());
                ((TextView) mContentView.findViewById(R.id.text_employeeResponsiblePersonName)).setText(workInfo.getEmployeeResponsiblePersonName());
                ((TextView) mContentView.findViewById(R.id.text_employeeResponsiblePersonIdcardNo)).setText(workInfo.getEmployeeResponsiblePersonIdcardNo());
                ((TextView) mContentView.findViewById(R.id.text_employeeResponsiblePersonPhone)).setText(workInfo.getEmployeeResponsiblePersonPhone());
                ((TextView) mContentView.findViewById(R.id.text_businessLicence)).setText(workInfo.getBusinessLicence());
                setViewWidth(mContentView.findViewById(R.id.cardFront));
                setViewWidth(mContentView.findViewById(R.id.cardBack));
                GlideManager.loadImg(Common.PIC_URL + workInfo.getEmployeeResponsiblePersonIdcardFrontUrl(), (ImageView) mContentView.findViewById(R.id.cardFront), R.drawable.shape_scan_base_error_image);
                GlideManager.loadImg(Common.PIC_URL + workInfo.getEmployeeResponsiblePersonIdcardIdcardBackUrl(), (ImageView) mContentView.findViewById(R.id.cardBack), R.drawable.shape_scan_base_error_image);
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
}
