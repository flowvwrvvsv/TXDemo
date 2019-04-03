package com.tianxie.demo.ui.scan.fragment;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.aries.library.fast.basis.BasisFragment;
import com.aries.library.fast.manager.GlideManager;
import com.tianx.demo.R;
import com.tianxie.demo.base.Common;
import com.tianxie.demo.entity.EntryInfo;
import com.tianxie.demo.entity.TrafficInfo;
import com.tianxie.demo.ui.scan.ScanResultActivity;

public class TrafficInfoFrag extends BasisFragment
{
    @Override
    public int getContentLayout()
    {
        return R.layout.layout_traffic_info;
    }

    @Override
    public void initView(Bundle savedInstanceState)
    {

    }

    TrafficInfo trafficInfo;

    @Override
    public Object getObject()
    {
        return trafficInfo;
    }


    public void loadData()
    {
        Bundle args = getArguments();
        if (args != null)
        {
            if (trafficInfo == null)
                trafficInfo = (TrafficInfo) args.getSerializable(ScanResultActivity.FRAG_TAG);

            if (trafficInfo != null)
            {
                ((TextView) mContentView.findViewById(R.id.text_electrombileNo)).setText(trafficInfo.getElectrombileNo());
                ((TextView) mContentView.findViewById(R.id.text_motorcycleNo)).setText(trafficInfo.getElectrombileNo());
                ((TextView) mContentView.findViewById(R.id.text_carNo)).setText(trafficInfo.getElectrombileNo());
                ((TextView) mContentView.findViewById(R.id.text_drvingLicence)).setText(trafficInfo.getElectrombileNo());
                GlideManager.loadImg(Common.PIC_URL + trafficInfo.getVehiclePhoto(), (ImageView) mContentView.findViewById(R.id.vehiclePhoto));
            }
        }
    }
}
