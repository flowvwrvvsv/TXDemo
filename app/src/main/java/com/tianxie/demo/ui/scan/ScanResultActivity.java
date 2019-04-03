package com.tianxie.demo.ui.scan;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.aries.library.fast.basis.BasisFragment;
import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.SizeUtil;
import com.aries.ui.view.tab.utils.FragmentChangeManager;
import com.aries.ui.view.title.TitleBarView;

import com.aries.library.fast.util.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tianx.demo.R;
import com.tianxie.demo.entity.ResideInfo;
import com.tianxie.demo.entity.ScanResult;
import com.tianxie.demo.retrofit.repository.ApiRepository;
import com.tianxie.demo.ui.BaseUI.TitleActivity;
import com.tianxie.demo.ui.scan.fragment.HomeBaseInfoFrag;
import com.tianxie.demo.ui.scan.fragment.ImmigrationInfoFrag;
import com.tianxie.demo.ui.scan.fragment.MyanmarIdentityInfoFrag;
import com.tianxie.demo.ui.scan.fragment.ResideInfoFrag;
import com.tianxie.demo.ui.scan.fragment.TrafficInfoFrag;
import com.tianxie.demo.ui.scan.fragment.WorkInfoFrag;
import com.tianxie.demo.util.ViewColorUtil;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;

public class ScanResultActivity extends TitleActivity
{
    public static final String SCAN_POST = "SCAN_POST";
    public static final String FRAG_TAG = "FRAG_TAG";
    public static final int HEIGHT_PADDING = 16;

    private boolean isLoadWeb = false;

    private WebView webView;
    private ProgressBar progressBar;
    private ScanResult scanPost;

    private FragmentChangeManager mFragmentChangeManager;

    private ArrayList<Fragment> fragments;

    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient webViewClient;

    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient webChromeClient;

    @Override
    public int getContentLayout()
    {
        scanPost = (ScanResult) getIntent().getSerializableExtra(SCAN_POST);
        int layoutID = -1;
        if (scanPost.getToken() != null)
        {
            layoutID = R.layout.activity_new_scan_result;
            isLoadWeb = false;
        } else
        {
            layoutID = R.layout.activity_scan_result;
            isLoadWeb = true;
        }
        isLoadWeb = false;
        layoutID = R.layout.activity_new_scan_result;
        return layoutID;
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void initView(Bundle savedInstanceState)
    {
        if (scanPost == null)
            return;

        if (isLoadWeb)
        {
            webViewClient = new WebViewClient()
            {
                @Override
                public void onPageFinished(WebView view, String url)
                {//页面加载完成
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon)
                {//页面开始加载
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url)
                {
                    if (url.equals("http://www.google.com/"))
                    {
                        ToastUtil.show("国内不能访问google,拦截该url");
                        return true;//表示我已经处理过了
                    }
                    return super.shouldOverrideUrlLoading(view, url);
                }
            };

            webChromeClient = new WebChromeClient()
            {
                //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
                @Override
                public boolean onJsAlert(WebView webView, String url, String message, JsResult result)
                {
                    AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
                    localBuilder.setMessage(message).setPositiveButton("确定", null);
                    localBuilder.setCancelable(false);
                    localBuilder.create().show();

                    //注意:
                    //必须要这一句代码:result.confirm()表示:
                    //处理结果为确定状态同时唤醒WebCore线程
                    //否则不能继续点击按钮
                    result.confirm();
                    return true;
                }

                //获取网页标题
                @Override
                public void onReceivedTitle(WebView view, String title)
                {
                    super.onReceivedTitle(view, title);
                }

                //加载进度回调
                @Override
                public void onProgressChanged(WebView view, int newProgress)
                {
                    progressBar.setProgress(newProgress);
                }
            };


            webView = findViewById(R.id.webview);
            progressBar = findViewById(R.id.progressbar);
            webView.addJavascriptInterface(this, "android");//添加js监听 这样html就能调用客户端
            webView.setWebChromeClient(webChromeClient);
            webView.setWebViewClient(webViewClient);

            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);//允许使用js

            /**
             * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
             * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
             * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
             * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
             */
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.

            //支持屏幕缩放
            webSettings.setSupportZoom(true);
            webSettings.setBuiltInZoomControls(true);

            ScanResult scanPost = (ScanResult) getIntent().getSerializableExtra(SCAN_POST);
            if (webView != null && scanPost != null)
                webView.loadUrl(scanPost.getUrl());
        } else
        {
            if (scanPost.getIdNumber() == null)
                return;

            View.OnClickListener listener = (View view) ->
            {
                switch (view.getId())
                {
                    case R.id.scan_result_btn_home:
                        onBackPressed();
                        break;
                    case R.id.scan_result_btn_law:
                        ToastUtil.show("功能正在开发中！！");
                        break;
                    case R.id.scan_result_btn_trajectory:
                        ToastUtil.show("功能正在开发中！！");
                        break;
                }
            };

            findViewById(R.id.scan_result_btn_home).setOnClickListener(listener);
            findViewById(R.id.scan_result_btn_law).setOnClickListener(listener);
            findViewById(R.id.scan_result_btn_trajectory).setOnClickListener(listener);


            fragments = new ArrayList<>();
            HomeBaseInfoFrag mHomeBaseInfoFrag = new HomeBaseInfoFrag();
            mHomeBaseInfoFrag.setOnItemChildClickListener(mOnItemChildClickListener);
            Bundle bundle = new Bundle();
            bundle.putString(HomeBaseInfoFrag.HOME_BASE_ID, scanPost.getIdNumber());
            mHomeBaseInfoFrag.setArguments(bundle);
            fragments.add(mHomeBaseInfoFrag);
            fragments.add(new MyanmarIdentityInfoFrag());
            fragments.add(new ImmigrationInfoFrag());
            fragments.add(new WorkInfoFrag());
            fragments.add(new ResideInfoFrag());
            fragments.add(new TrafficInfoFrag());
            mFragmentChangeManager = new FragmentChangeManager(getSupportFragmentManager(), R.id.frag_container, fragments);
            mFragmentChangeManager.setFragments(0);
        }
    }

    final int TYPE_MYANMAR_INFO = 1;
    final int TYPE_IMMIGRATION = TYPE_MYANMAR_INFO + 1;
    final int TYPE_WORK = TYPE_IMMIGRATION + 1;
    final int TYPE_RESIDE = TYPE_WORK + 1;
    final int TYPE_TRAFFIC = TYPE_RESIDE + 1;

    private BaseQuickAdapter.OnItemChildClickListener mOnItemChildClickListener = new BaseQuickAdapter.OnItemChildClickListener()
    {
        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, android.view.View view, int position)
        {
            BasisFragment fragment = (BasisFragment) fragments.get(position + 1);
            if (fragment.getObject() == null)
            {
                Observable observable = null;
                switch (position + 1)
                {
                    case TYPE_MYANMAR_INFO:
                        observable = ApiRepository.getInstance().doGetIdCardInfo(scanPost.getIdNumber());
                        break;
                    case TYPE_IMMIGRATION:
                        observable = ApiRepository.getInstance().doGetImmigrationInfo(scanPost.getIdNumber());
                        break;
                    case TYPE_WORK:
                        observable = ApiRepository.getInstance().doGetLaborInfo(scanPost.getIdNumber());
                        break;
                    case TYPE_RESIDE:
                        observable = ApiRepository.getInstance().doGetResideInfo(scanPost.getIdNumber());
                        break;
                    case TYPE_TRAFFIC:
                        observable = ApiRepository.getInstance().doGetTrafficInfo(scanPost.getIdNumber());
                        break;
                }

                if (observable != null)
                {
                    observable.subscribe(new FastLoadingObserver<Object>()
                    {
                        @Override
                        public void _onNext(Object entity)
                        {
                            ArrayList<Serializable> list = (ArrayList) entity;
                            if (list == null || list.isEmpty())
                            {
                                ToastUtil.show("获取数据为空！！");
                                return;
                            }

                            Serializable serializable = list.get(0);
                            if (serializable != null)
                            {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(FRAG_TAG, serializable);
                                fragment.setArguments(bundle);
                                mFragmentChangeManager.setFragments(position + 1);
                            }
                        }
                    });
                }
            } else
                mFragmentChangeManager.setFragments(position + 1);
        }
    };

    @Override
    public void setTitleBar(TitleBarView titleBar)
    {
        titleBar.setLeftTextDrawable(ViewColorUtil.zoomDrawable(getDrawable(R.mipmap.img_back), SizeUtil.dp2px(48), SizeUtil.dp2px(48)))
                .setBgColor(ActivityCompat.getColor(this, R.color.color_search_find_title_bkg))
                .setTitleMainTextColor(Color.WHITE)
                .setTitleMainText(R.string.scan_result_title);
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
}
