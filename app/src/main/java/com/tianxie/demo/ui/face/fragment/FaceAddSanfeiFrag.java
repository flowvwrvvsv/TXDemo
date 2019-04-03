package com.tianxie.demo.ui.face.fragment;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aries.library.fast.basis.BasisFragment;
import com.aries.library.fast.manager.GlideManager;
import com.aries.library.fast.util.SizeUtil;
import com.tianx.demo.R;
import com.tianxie.demo.entity.FaceSearchResult;



public class FaceAddSanfeiFrag extends BasisFragment
{
    public static final String ADD_SANFEI = "ADD_SANFEI";

    private FaceSearchResult mFaceSearchResult;

    @Override
    public int getContentLayout()
    {
        return R.layout.layout_add_sanfei_frag;
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
            if (mFaceSearchResult == null)
                mFaceSearchResult = (FaceSearchResult) getArguments().getSerializable(ADD_SANFEI);

            if (mFaceSearchResult != null)
            {
                final ImageView imageView = mContentView.findViewById(R.id.image_sanfei);
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                params.width = (int) (SizeUtil.getScreenWidth() * 0.8);
                params.height = (int) (SizeUtil.getScreenWidth() * 0.8);
                imageView.setLayoutParams(params);

                GlideManager.loadImgFit(mFaceSearchResult.getNativeUrl(), imageView, R.drawable.shape_scan_base_error_image);
            }
        }
    }
}
