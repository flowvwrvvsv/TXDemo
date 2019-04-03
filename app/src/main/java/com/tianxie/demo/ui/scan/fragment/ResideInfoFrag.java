package com.tianxie.demo.ui.scan.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.aries.library.fast.basis.BasisFragment;
import com.tianx.demo.R;
import com.tianxie.demo.entity.ResideInfo;
import com.tianxie.demo.ui.scan.ScanResultActivity;

public class ResideInfoFrag extends BasisFragment
{
    @Override
    public int getContentLayout()
    {
        return R.layout.layout_residence_info;
    }

    @Override
    public void initView(Bundle savedInstanceState)
    {

    }

    ResideInfo resideInfo;

    @Override
    public Object getObject()
    {
        return resideInfo;
    }


    public void loadData()
    {
        Bundle args = getArguments();
        if (args != null)
        {
            if (resideInfo == null)
                resideInfo = (ResideInfo) args.getSerializable(ScanResultActivity.FRAG_TAG);

            if (resideInfo != null)
            {
                ((TextView) mContentView.findViewById(R.id.text_police)).setText(resideInfo.getPolice());
                ((TextView) mContentView.findViewById(R.id.text_resideAddress)).setText(resideInfo.getResideAddress());
                ((TextView) mContentView.findViewById(R.id.text_leasePersonName)).setText(resideInfo.getLeasePersonName());
                ((TextView) mContentView.findViewById(R.id.text_leasePersonIdcardNo)).setText(resideInfo.getLeasePersonIdcardNo());
                ((TextView) mContentView.findViewById(R.id.text_leasePersonPhone)).setText(resideInfo.getLeasePersonPhone());
                ((TextView) mContentView.findViewById(R.id.text_introductionLetter)).setText(resideInfo.getIntroductionLetter());
            }
        }
    }
}
