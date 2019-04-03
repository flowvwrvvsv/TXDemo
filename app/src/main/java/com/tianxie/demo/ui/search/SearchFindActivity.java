package com.tianxie.demo.ui.search;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.aries.library.fast.manager.RxJavaManager;
import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.SizeUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.ui.view.title.TitleBarView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tianx.demo.R;
import com.tianxie.demo.adapter.SearchWidgeAdapter;
import com.tianxie.demo.decoration.SearchFindDecoration;
import com.tianxie.demo.entity.SearchWidgetEntity;
import com.tianxie.demo.ui.BaseUI.TitleActivity;
import com.tianxie.demo.util.ViewColorUtil;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class SearchFindActivity extends TitleActivity
{
    @BindView(R.id.search_find_edit)
    EditText mEditText;
    @BindView(R.id.search_find_list)
    RecyclerView mRecyclerView;

    private BaseQuickAdapter mQuickAdapter;

    @Override
    public int getContentLayout()
    {
        return R.layout.activity_search_find;
    }

    @Override
    public void initView(Bundle savedInstanceState)
    {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mQuickAdapter = new SearchWidgeAdapter();
        mRecyclerView.setAdapter(mQuickAdapter);
        mRecyclerView.addItemDecoration(new SearchFindDecoration());
        SmartRefreshLayout layout = findViewById(R.id.search_refresh_container);
        layout.setEnableRefresh(false);
        layout.setEnableLoadMore(false);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar)
    {
        titleBar.setLeftTextDrawable(ViewColorUtil.zoomDrawable(getDrawable(R.mipmap.img_back), SizeUtil.dp2px(48), SizeUtil.dp2px(48)))
                .setBgColor(ActivityCompat.getColor(this, R.color.color_search_find_title_bkg))
                .setTitleMainTextColor(Color.WHITE)
                .setTitleMainText(R.string.search_find_title);
    }

    @OnClick({R.id.search_find_btn,R.id.search_btn_home,R.id.search_btn_advanced})
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.search_find_btn:
                String searchStr = mEditText.getText().toString();
                if (searchStr == null || searchStr.isEmpty())
                {
                    ToastUtil.show("请输入查询内容！");
                    break;
                }

                List<SearchWidgetEntity> list = new ArrayList<>();
                list.add(null);
                list.add(null);
                RxJavaManager.getInstance().getDelayObservable(list, 2, TimeUnit.MILLISECONDS)
                        .compose(bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(new FastLoadingObserver<List<SearchWidgetEntity>>("正在加载")
                        {
                            @Override
                            public void _onNext(List<SearchWidgetEntity> entity)
                            {
                                findViewById(R.id.container_find).setVisibility(View.GONE);
                                findViewById(R.id.search_refresh_container).setVisibility(View.VISIBLE);
                                mQuickAdapter.setNewData(entity);
                            }
                        });
                break;
            case R.id.search_btn_home:
                onBackPressed();
                break;
            case R.id.search_btn_advanced:
                ToastUtil.show("功能正在开发中！");
                break;
        }
    }
}
