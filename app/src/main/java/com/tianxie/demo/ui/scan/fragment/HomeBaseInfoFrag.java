package com.tianxie.demo.ui.scan.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aries.library.fast.basis.BasisFragment;
import com.aries.library.fast.manager.GlideManager;
import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.SizeUtil;
import com.aries.library.fast.util.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tianx.demo.R;
import com.tianxie.demo.adapter.ScanResultAdapter;
import com.tianxie.demo.base.Common;
import com.tianxie.demo.decoration.ScanResultDecoration;
import com.tianxie.demo.entity.BaseInfo;
import com.tianxie.demo.retrofit.repository.ApiRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeBaseInfoFrag extends BasisFragment
{
    private final int SIZE_QR_WIDTH = 66;
    public static final String HOME_BASE_ID = "HOME_BASE_ID";
    public static final String HOME_ON_ITEM_CLICK = "HOME_ON_ITEM_CLICK";
    public String idNumber;

    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mQuickAdapter;

    private BaseInfo mBaseInfo;

    @Override
    public int getContentLayout()
    {
        return R.layout.layout_scan_result_base;
    }

    @Override
    public void initView(Bundle savedInstanceState)
    {
        Bundle bundle = getArguments();
        if (bundle == null)
            return;

        idNumber = bundle.getString(HOME_BASE_ID);

        if (idNumber == null || idNumber.isEmpty())
            return;

        SmartRefreshLayout mRefreshLayout = (SmartRefreshLayout) mContentView;
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMore(false);
        mRecyclerView = mContentView.findViewById(R.id.scan_result_list);
        mRecyclerView.addItemDecoration(new ScanResultDecoration());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mQuickAdapter = new ScanResultAdapter();
        mRecyclerView.setAdapter(mQuickAdapter);

        mQuickAdapter.setOnLoadMoreListener(null, mRecyclerView);
        String[] titles = getResources().getStringArray(R.array.arrays_scan_result);
        List<String> strings = new ArrayList<>();
        for (String str : titles)
            strings.add(str);
        mQuickAdapter.setNewData(strings);

        View v = View.inflate(mContext, R.layout.layout_scan_result_header, null);
        mQuickAdapter.addHeaderView(v);
//        ViewGroup.LayoutParams params = v.getLayoutParams();
//        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        v.setLayoutParams(params);

        mQuickAdapter.setOnItemChildClickListener(mOnItemChildClickListener);

        ApiRepository.getInstance().doGetBaseInfo(idNumber).subscribe(new FastLoadingObserver<List<BaseInfo>>()
        {
            @Override
            public void _onNext(List<BaseInfo> entity)
            {
                if (entity == null || entity.isEmpty())
                {
                    ToastUtil.show("获取数据为空。。。");
                    return;
                }

                BaseInfo baseInfo = entity.get(0);
                if (baseInfo != null)
                {
                    mBaseInfo = baseInfo;

                    View container = mRecyclerView.findViewHolderForLayoutPosition(0).itemView;
                    if (container != null)
                    {
                        ((TextView) container.findViewById(R.id.text_name)).setText(mBaseInfo.getChineseName());
                        ((TextView) container.findViewById(R.id.text_gender)).setText(mBaseInfo.getGender());
                        ((TextView) container.findViewById(R.id.text_national)).setText(mBaseInfo.getNation());
                        ((TextView) container.findViewById(R.id.text_birth)).setText(mBaseInfo.getBirthDay());
                        ((TextView) container.findViewById(R.id.text_religion)).setText(mBaseInfo.getReligion());
                        ((TextView) container.findViewById(R.id.text_insurance_date)).setText(mBaseInfo.getIssueDate());
                        ((TextView) container.findViewById(R.id.text_idNumber)).setText(mBaseInfo.getIdNumber());


                        View textContainer = container.findViewById(R.id.text_container);
                        Rect rect = new Rect();
                        textContainer.getGlobalVisibleRect(rect);
                        int headerHeight = (rect.bottom - rect.top);


                        View headerContainer = container.findViewById(R.id.header_container);
                        headerContainer.getGlobalVisibleRect(rect);

                        int headerWidth = rect.right - rect.left;
                        View v = container.findViewById(R.id.image_header);
                        ViewGroup.LayoutParams params = v.getLayoutParams();
                        params.width = (int) (headerWidth * 0.75);
                        params.height = (int) (headerHeight * 0.95);
                        v.setLayoutParams(params);
                        GlideManager.loadImgFit(Common.PIC_URL + baseInfo.getFaceUrl(), container.findViewById(R.id.image_header), R.drawable.shape_scan_base_error_image);

                        v = container.findViewById(R.id.image_bar_code);
                        params = v.getLayoutParams();
                        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        params.height = SizeUtil.dp2px(70);
                        v.setLayoutParams(params);
                        GlideManager.loadImgFit(Common.PIC_URL + baseInfo.getBarCodeUrl(), container.findViewById(R.id.image_bar_code), R.drawable.shape_scan_base_error_image);


//                        container.getGlobalVisibleRect(rect);
//                        int qrWidth = (rect.bottom - rect.top) - SizeUtil.dp2px(10) - headerHeight - 3;
                        v = container.findViewById(R.id.image_qr_code);
                        params = v.getLayoutParams();
                        params.width = SizeUtil.dp2px(SIZE_QR_WIDTH);
                        params.height = SizeUtil.dp2px(SIZE_QR_WIDTH);
                        v.setLayoutParams(params);
                        GlideManager.loadImgFit(Common.PIC_URL + baseInfo.getQrCodeUrl(), container.findViewById(R.id.image_qr_code), R.drawable.shape_scan_base_error_image);
                    }
                }
            }
        });
    }

    BaseQuickAdapter.OnItemChildClickListener mOnItemChildClickListener;

    public void setOnItemChildClickListener(BaseQuickAdapter.OnItemChildClickListener onItemChildClickListener)
    {
        mOnItemChildClickListener = onItemChildClickListener;
    }
}
