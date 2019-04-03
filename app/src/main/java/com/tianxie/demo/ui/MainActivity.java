package com.tianxie.demo.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.aries.library.fast.FastManager;
import com.aries.library.fast.i.IFastRefreshLoadView;
import com.aries.library.fast.i.IHttpRequestControl;
import com.aries.library.fast.manager.RxJavaManager;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.ui.view.title.TitleBarView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tianx.demo.R;
import com.tianxie.demo.adapter.WidgetAdapter;
import com.tianxie.demo.entity.WidgetEntity;
import com.tianxie.demo.ui.BaseUI.TitleActivity;
import com.tianxie.demo.ui.scan.ScanActivity;
import com.tianxie.demo.ui.face.TxFaceDetectActivity;
import com.tianxie.demo.ui.search.SearchFindActivity;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.tianxie.demo.decoration.MarginDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

public class MainActivity extends TitleActivity implements IFastRefreshLoadView<WidgetEntity>
{
    @BindView(R.id.rv_contentFastLib)
    RecyclerView mRecyclerView;

    private BaseQuickAdapter mQuickAdapter;

    @Override
    public int getContentLayout()
    {
        return R.layout.activity_main_new;
    }

    @Override
    public void initView(Bundle savedInstanceState)
    {
        mQuickAdapter = new WidgetAdapter();
        mQuickAdapter.setOnLoadMoreListener(null, mRecyclerView);
        List<WidgetEntity> list = new ArrayList<>();
        String[] titles = getResources().getStringArray(R.array.arrays_main_item);
        Class[] listActivity = {ScanActivity.class, SearchFindActivity.class, null, null, null, null};
        int[] images = {R.mipmap.img_scan, R.mipmap.img_search, R.mipmap.img_add_sanfei, R.mipmap.img_register, R.mipmap.img_notification, R.mipmap.img_personal,};
        int[] background = {R.color.main_header_bkg, R.color.main_color_search, R.color.main_color_add_sanfei, R.color.main_color_personal, R.color.main_color_notification, R.color.main_color_personal,};
        for (int i = 0; i < titles.length; i++)
        {
            WidgetEntity entity = new WidgetEntity();
            entity.title = titles[i];
            entity.activity = listActivity[i];
            entity.imageID = images[i];
            entity.backID = background[i];
            list.add(entity);
        }

        RxJavaManager.getInstance().getDelayObservable(list, 2, TimeUnit.MILLISECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<List<WidgetEntity>>()
                {
                    @Override
                    public void _onNext(List<WidgetEntity> entity)
                    {
                        IHttpRequestControl control = getIHttpRequestControl();
                        View v = View.inflate(mContext, R.layout.layout_main_header, null);
                        v.setOnClickListener(view -> {
                            FastUtil.startActivity(mContext, TxFaceDetectActivity.class);
                        });
                        mQuickAdapter.addHeaderView(v);
                        ViewGroup.LayoutParams params = v.getLayoutParams();
                        params.height = 300;
                        mRecyclerView.setLayoutManager(getLayoutManager());
                        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
                        mRecyclerView.addItemDecoration(new MarginDecoration());
                        mRecyclerView.setAdapter(mQuickAdapter);
                        if (isItemClickEnable())
                        {
                            mQuickAdapter.setOnItemClickListener((adapter, view, position) -> {
                                onItemClicked(adapter, view, position);
                            });
                        }
                        control.getRefreshLayout().setEnableRefresh(isRefreshEnable());
                        control.getRefreshLayout().setEnableLoadMore(isLoadMoreEnable());
                        FastManager.getInstance().getHttpRequestControl().httpRequestSuccess(control, entity, null);
                    }
                });
    }

    @Override
    public void setTitleBar(TitleBarView titleBar)
    {
        titleBar.setVisibility(View.GONE);
    }

    @Override
    public BaseQuickAdapter<WidgetEntity, BaseViewHolder> getAdapter()
    {
        return null;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager()
    {
        return new GridLayoutManager(this, 2);
    }

    @Override
    public RefreshHeader getRefreshHeader()
    {
        return null;
    }

    @Override
    public LoadMoreView getLoadMoreView()
    {
        return null;
    }

    @Override
    public void loadData(int page)
    {

    }

    @Override
    public boolean isLoadMoreEnable()
    {
        return false;
    }

    @Override
    public boolean isRefreshEnable()
    {
        return false;
    }

    @Override
    public boolean isItemClickEnable()
    {
        return true;
    }

    @Override
    public void onItemClicked(BaseQuickAdapter<WidgetEntity, BaseViewHolder> adapter, View view, int position)
    {
        WidgetEntity entity = adapter.getItem(position);
        if (entity.activity != null)
            FastUtil.startActivity(mContext, entity.activity);
        else
            ToastUtil.show("功能正在开发中~！！");
    }

    @Override
    public IHttpRequestControl getIHttpRequestControl()
    {
        return new IHttpRequestControl()
        {
            @Override
            public SmartRefreshLayout getRefreshLayout()
            {
                return findViewById(R.id.smartLayout_rootFastLib);
            }

            @Override
            public BaseQuickAdapter getRecyclerAdapter()
            {
                return mQuickAdapter;
            }

            @Override
            public StatusLayoutManager getStatusLayoutManager()
            {
                return null;
            }

            @Override
            public int getCurrentPage()
            {
                return 1;
            }

            @Override
            public int getPageSize()
            {
                return 1;
            }

            @Override
            public Class<?> getRequestClass()
            {
                return this.getClass();
            }
        };
    }

    @Override
    public View getMultiStatusContentView()
    {
        return null;
    }

    @Override
    public void setMultiStatusView(StatusLayoutManager.Builder statusView)
    {

    }

    @Override
    public View.OnClickListener getEmptyClickListener()
    {
        return null;
    }

    @Override
    public View.OnClickListener getErrorClickListener()
    {
        return null;
    }

    @Override
    public View.OnClickListener getCustomerClickListener()
    {
        return null;
    }

    @Override
    public void onLoadMoreRequested()
    {

    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout)
    {

    }
}
