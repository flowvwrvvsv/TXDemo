package com.tianxie.demo.ui.face;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.aries.library.fast.manager.RxJavaManager;
import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.SizeUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.ui.view.tab.utils.FragmentChangeManager;
import com.aries.ui.view.title.TitleBarView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tianx.demo.R;
import com.tianxie.demo.adapter.FaceResultWidgetAdapter;
import com.tianxie.demo.decoration.SearchFindDecoration;
import com.tianxie.demo.entity.FaceResultWidgetEntity;
import com.tianxie.demo.entity.FaceSearchEntity;
import com.tianxie.demo.entity.FaceSearchResult;
import com.tianxie.demo.entity.FaceUserEntitiy;
import com.tianxie.demo.ui.BaseUI.TitleActivity;
import com.tianxie.demo.ui.face.fragment.FaceAddSanfeiFrag;
import com.tianxie.demo.ui.face.fragment.FaceDetailFrag;
import com.tianxie.demo.ui.face.fragment.FaceHomeFrag;
import com.tianxie.demo.util.ViewColorUtil;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.tianxie.demo.ui.face.fragment.FaceDetailFrag.FACE_DETAIL;

public class FaceCompareResultActivity extends TitleActivity
{
    public static final String FACE_COMPARE = "FACE_COMPARE";

    private FragmentChangeManager mFragmentChangeManager;

    private ArrayList<Fragment> fragments;

    private FaceSearchResult faceSearchResult;


    @Override
    public int getContentLayout()
    {
        return R.layout.activity_face_compare_result;
    }

    @Override
    public void initView(Bundle savedInstanceState)
    {
        faceSearchResult = (FaceSearchResult) getIntent().getSerializableExtra(FACE_COMPARE);
        if (faceSearchResult != null)
        {
            fragments = new ArrayList<>();
            FaceHomeFrag faceHomeFrag = new FaceHomeFrag();
            faceHomeFrag.setOnItemChildClickListener(mOnItemChildClickListener);
            Bundle bundle = new Bundle();
            bundle.putSerializable(FACE_COMPARE, faceSearchResult);
            faceHomeFrag.setArguments(bundle);
            fragments.add(faceHomeFrag);

            FaceDetailFrag faceDetailFrag = new FaceDetailFrag();
            fragments.add(faceDetailFrag);

            FaceAddSanfeiFrag faceAddSanfeiFrag = new FaceAddSanfeiFrag();
            fragments.add(faceAddSanfeiFrag);

            mFragmentChangeManager = new FragmentChangeManager(getSupportFragmentManager(), R.id.frag_container, fragments);
            mFragmentChangeManager.setFragments(0);
        }
    }

    @Override
    public void setTitleBar(TitleBarView titleBar)
    {
        titleBar.setLeftTextDrawable(ViewColorUtil.zoomDrawable(getDrawable(R.mipmap.img_back), SizeUtil.dp2px(48), SizeUtil.dp2px(48)))
                .setBgColor(ActivityCompat.getColor(this, R.color.color_search_find_title_bkg))
                .setTitleMainTextColor(Color.WHITE)
                .setTitleMainText(R.string.search_face_compare_result);
    }


    @OnClick({R.id.face_home, R.id.face_to_face})
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.face_to_face:
                FastUtil.startActivity(FaceCompareResultActivity.this, TxFaceDetectActivity.class);
                finish();
                break;
            case R.id.face_home:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        if (mFragmentChangeManager != null && mFragmentChangeManager.getCurrentTab() != 0)
        {
            mFragmentChangeManager.setFragments(0);
        } else
            super.onBackPressed();
    }

    private BaseQuickAdapter.OnItemChildClickListener mOnItemChildClickListener = new BaseQuickAdapter.OnItemChildClickListener()
    {
        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, android.view.View view, int position)
        {
            switch (view.getId())
            {
                case R.id.item_all:
                    FaceResultWidgetEntity faceResultWidgetEntity = (FaceResultWidgetEntity) adapter.getItem(position);
                    if (faceResultWidgetEntity == null)
                        return;

                    float score = faceResultWidgetEntity.getFaceUserEntitiy().getScore();
                    if (score >=50)
                    {
                        FaceDetailFrag faceDetailFrag = (FaceDetailFrag) fragments.get(1);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(FACE_DETAIL, faceResultWidgetEntity);
                        faceDetailFrag.setArguments(bundle);
                        mFragmentChangeManager.setFragments(1);
                    } else
                    {
                        FaceAddSanfeiFrag faceAddSanfeiFrag = (FaceAddSanfeiFrag) fragments.get(2);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(FaceAddSanfeiFrag.ADD_SANFEI, faceSearchResult);
                        faceAddSanfeiFrag.setArguments(bundle);
                        mFragmentChangeManager.setFragments(2);
                    }
                break;
            }
        }
    };
}
