package com.tianxie.demo.ui.face.fragment;

import android.os.Bundle;

import com.aries.library.fast.basis.BasisFragment;
import com.aries.library.fast.manager.RxJavaManager;
import com.aries.library.fast.retrofit.FastObserver;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tianx.demo.R;
import com.tianxie.demo.adapter.FaceResultWidgetAdapter;
import com.tianxie.demo.decoration.SearchFindDecoration;
import com.tianxie.demo.entity.FaceResultWidgetEntity;
import com.tianxie.demo.entity.FaceSearchResult;
import com.tianxie.demo.entity.FaceUserEntitiy;
import com.trello.rxlifecycle3.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.tianxie.demo.ui.face.FaceCompareResultActivity.FACE_COMPARE;

public class FaceHomeFrag extends BasisFragment
{
    private FaceSearchResult mFaceSearchResult;

    @BindView(R.id.search_find_list)
    RecyclerView mRecyclerView;

    private BaseQuickAdapter mQuickAdapter;

    @Override
    public int getContentLayout()
    {
        return R.layout.layout_face_result_frag;
    }

    @Override
    public void initView(Bundle savedInstanceState)
    {
        ((SmartRefreshLayout) mContentView.findViewById(R.id.face_result_refresh)).setEnableLoadMore(false);
        ((SmartRefreshLayout) mContentView.findViewById(R.id.face_result_refresh)).setEnableRefresh(false);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mQuickAdapter = new FaceResultWidgetAdapter();
        mRecyclerView.addItemDecoration(new SearchFindDecoration());
        mRecyclerView.setAdapter(mQuickAdapter);
        mQuickAdapter.setOnItemChildClickListener(mOnItemChildClickListener);
        mQuickAdapter.setOnLoadMoreListener(null, mRecyclerView);
    }

    BaseQuickAdapter.OnItemChildClickListener mOnItemChildClickListener;

    public void setOnItemChildClickListener(BaseQuickAdapter.OnItemChildClickListener onItemChildClickListener)
    {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    public void loadData()
    {
        Bundle args = getArguments();
        if (args != null)
        {
            if (mFaceSearchResult == null)
                mFaceSearchResult = (FaceSearchResult) getArguments().getSerializable(FACE_COMPARE);

            if (mFaceSearchResult != null)
            {
                List<FaceUserEntitiy> faceSearchEntityList = mFaceSearchResult.getResult().getUser_list();
                List<FaceResultWidgetEntity> list = new ArrayList<>();
                FaceResultWidgetEntity entity = null;

                for (int x = 0; x < faceSearchEntityList.size(); x++)
                {
                    entity = new FaceResultWidgetEntity();
                    entity.setFaceUserEntitiy(faceSearchEntityList.get(0));
                    entity.setbBack(mFaceSearchResult.isbBack());
                    entity.setNativeUrl(mFaceSearchResult.getNativeUrl());
                    list.add(entity);
                }

                RxJavaManager.getInstance().getDelayObservable(list, 2, TimeUnit.MILLISECONDS)
                        .compose(bindUntilEvent(FragmentEvent.DESTROY))
                        .subscribe(new FastObserver<List<FaceResultWidgetEntity>>(null)
                        {
                            @Override
                            public void _onNext(List<FaceResultWidgetEntity> entity)
                            {
                                mQuickAdapter.setNewData(entity);
                                mQuickAdapter.setOnItemChildClickListener(mOnItemChildClickListener);
                            }
                        });
            }
        }
    }
}
