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
import com.tianxie.demo.entity.MyanmarIdentityInfo;
import com.tianxie.demo.ui.scan.ScanResultActivity;

public class MyanmarIdentityInfoFrag extends BasisFragment
{
    @Override
    public int getContentLayout()
    {
        return R.layout.layout_myanmar_identity_info;
    }

    @Override
    public void initView(Bundle savedInstanceState)
    {

    }

    MyanmarIdentityInfo myanmarIdentityInfo;

    @Override
    public Object getObject()
    {
        return myanmarIdentityInfo;
    }


    public void loadData()
    {
        Bundle args = getArguments();
        if (args != null)
        {
            if (myanmarIdentityInfo == null)
                myanmarIdentityInfo = (MyanmarIdentityInfo) args.getSerializable(ScanResultActivity.FRAG_TAG);

            if (myanmarIdentityInfo != null)
            {
                ((TextView) mContentView.findViewById(R.id.text_identity_id)).setText(myanmarIdentityInfo.getIdCardNo());
                ((TextView) mContentView.findViewById(R.id.text_issuance_date)).setText(myanmarIdentityInfo.getCardDate());
                ((TextView) mContentView.findViewById(R.id.text_chinese_name)).setText(myanmarIdentityInfo.getChineseName());
                ((TextView) mContentView.findViewById(R.id.text_foreign_name)).setText(myanmarIdentityInfo.getForeignName());
                ((TextView) mContentView.findViewById(R.id.text_parent_name)).setText(myanmarIdentityInfo.getFatherName());
                ((TextView) mContentView.findViewById(R.id.text_sex)).setText(myanmarIdentityInfo.getGender());
                ((TextView) mContentView.findViewById(R.id.text_national)).setText(myanmarIdentityInfo.getNation());
                ((TextView) mContentView.findViewById(R.id.text_religion)).setText(myanmarIdentityInfo.getReligion());
                ((TextView) mContentView.findViewById(R.id.text_profession)).setText(myanmarIdentityInfo.getJob());
                ((TextView) mContentView.findViewById(R.id.text_height)).setText(myanmarIdentityInfo.getHeight());
                ((TextView) mContentView.findViewById(R.id.text_feature)).setText(myanmarIdentityInfo.getFeature());
                ((TextView) mContentView.findViewById(R.id.text_address)).setText(myanmarIdentityInfo.getAddress());
                setViewWidth(mContentView.findViewById(R.id.cardFront));
                setViewWidth(mContentView.findViewById(R.id.cardBack));
                GlideManager.loadImg(Common.PIC_URL + myanmarIdentityInfo.getCardFrontUrl(),mContentView.findViewById(R.id.cardFront), R.drawable.shape_scan_base_error_image);
                GlideManager.loadImg(Common.PIC_URL + myanmarIdentityInfo.getCardFrontUrl(),mContentView.findViewById(R.id.cardBack), R.drawable.shape_scan_base_error_image);
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
