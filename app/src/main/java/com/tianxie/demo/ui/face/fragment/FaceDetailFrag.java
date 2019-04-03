package com.tianxie.demo.ui.face.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aries.library.fast.basis.BasisFragment;
import com.aries.library.fast.manager.GlideManager;
import com.aries.library.fast.util.SizeUtil;
import com.tianx.demo.R;
import com.tianxie.demo.entity.FaceResultWidgetEntity;

public class FaceDetailFrag extends BasisFragment
{
    public static final String FACE_DETAIL = "FACE_DETAIL";
    FaceResultWidgetEntity faceSearchResult;

    @Override
    public int getContentLayout()
    {
        return R.layout.activity_face_detatil_info;
    }

    @Override
    public void initView(Bundle savedInstanceState)
    {

    }

    @Override
    public void loadData()
    {
        Bundle args = getArguments();
        if (args != null)
        {
            if (faceSearchResult == null)
                faceSearchResult = (FaceResultWidgetEntity) args.getSerializable(FACE_DETAIL);

            if (faceSearchResult != null)
            {
                View v = mContentView.findViewById(R.id.image_person);
                ViewGroup.LayoutParams params = v.getLayoutParams();
                params.width = (int) (SizeUtil.getScreenWidth() * 0.5);
                params.height = (int) (SizeUtil.getScreenWidth() * 0.8);
                v.setLayoutParams(params);
                GlideManager.loadImg(faceSearchResult.getFaceUserEntitiy().getNetUrl(), (ImageView) v, R.drawable.shape_scan_base_error_image);
            }
        }
    }
}
