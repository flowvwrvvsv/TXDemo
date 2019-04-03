package com.tianxie.demo;


import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;

import com.aries.library.fast.BuildConfig;
import com.aries.library.fast.FastManager;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.retrofit.FastRetrofit;
import com.aries.library.fast.util.SizeUtil;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.tianxie.demo.base.Common;
import com.tianxie.demo.base.Config;
import com.tianxie.demo.impl.ActivityControlImpl;
import com.tianxie.demo.impl.AppImpl;
import com.tianxie.demo.impl.HttpRequestControlImpl;
import com.tianxie.demo.impl.SwipeBackControlImpl;
import com.tianxie.demo.util.CrashHandler;

import java.lang.reflect.Method;

public class App extends Application
{
    private static Context mContext;
    private static String TAG = "FastLib";
    private static int imageHeight = 0;
    private long start;

    @Override
    public void onCreate()
    {
        super.onCreate();

        //初始化Logger日志打印
        LoggerManager.init(TAG, BuildConfig.DEBUG);
        start = System.currentTimeMillis();
        LoggerManager.i(TAG, "start:" + start);
        mContext = this;

        FlowManager.init(this);
        FlowLog.setMinimumLoggingLevel(FlowLog.Level.V); // set to verbose logging


        //最简单UI配置模式-必须进行初始化
        FaceSDKManager.getInstance().initialize(this, Config.licenseID, Config.licenseFileName);
        FastManager.init(this);
        AppImpl impl = new AppImpl(mContext);
        ActivityControlImpl activityControl = new ActivityControlImpl();
        FastManager.getInstance()
                //设置Adapter加载更多视图--默认设置了FastLoadMoreView
                .setLoadMoreFoot(impl)
                //全局设置RecyclerView
                .setFastRecyclerViewControl(impl)
                //设置RecyclerView加载过程多布局属性
                .setMultiStatusView(impl)
                //设置全局网络请求等待Loading提示框如登录等待loading--观察者必须为FastLoadingObserver及其子类
                .setLoadingDialog(impl)
                //设置SmartRefreshLayout刷新头-自定加载使用BaseRecyclerViewAdapterHelper
                .setDefaultRefreshHeader(impl)
                //设置全局TitleBarView相关配置
                .setTitleBarViewControl(impl)
                //设置Activity滑动返回控制-默认开启滑动返回功能不需要设置透明主题
                .setSwipeBackControl(new SwipeBackControlImpl())
                //设置Activity/Fragment相关配置(横竖屏+背景+虚拟导航栏+状态栏+生命周期)
                .setActivityFragmentControl(activityControl)
                //设置BasisActivity 子类按键监听
                .setActivityKeyEventControl(activityControl)
                //配置BasisActivity 子类事件派发相关
                .setActivityDispatchEventControl(activityControl)
                //设置http请求结果全局控制
                .setHttpRequestControl(new HttpRequestControlImpl())
                //设置主页返回键控制-默认效果为2000 毫秒时延退出程序
                .setQuitAppControl(impl)
                //设置ToastUtil全局控制
                .setToastControl(impl);
        //初始化Retrofit配置
        FastRetrofit.getInstance()
                //配置全局网络请求BaseUrl
                .setBaseUrl(Common.BASE_URL)
                //信任所有证书--也可设置setCertificates(单/双向验证)
                .setCertificates()
                //设置统一请求头
//                .addHeader(header)
//                .addHeader(key,value)
                //设置请求全局log-可设置tag及Level类型
                .setLogEnable(true)
//                .setLogEnable(BuildConfig.DEBUG, TAG, HttpLoggingInterceptor.Level.BODY)
                //设置统一超时--也可单独调用read/write/connect超时(可以设置时间单位TimeUnit)
                //默认20 s
                .setTimeout(30);


//        FastRetrofit.getInstance()
//                .putBaseUrl(Common.BASE_BAIDU_KEY, Common.BASE_BAIDU_URL);

        FastRetrofit.getInstance()
                //设置Header模式优先-默认Method方式优先
                .setHeaderPriorityEnable(true)
                .putHeaderBaseUrl(Common.BASE_BAIDU_KEY, Common.BASE_BAIDU_URL);

        if (Common.DEBUG)
        {
            final CrashHandler myCrashHandler = CrashHandler.getInstance();
            myCrashHandler.init(getApplicationContext());
            Thread.currentThread().setUncaughtExceptionHandler(myCrashHandler);
        }
    }

    /**
     * 获取banner及个人中心设置ImageView宽高
     *
     * @return
     */
    public static int getImageHeight()
    {
        imageHeight = (int) (SizeUtil.getScreenWidth() * 0.55);
        return imageHeight;
    }

    public static boolean isSupportElevation()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * 是否控制底部导航栏---目前发现小米8上检查是否有导航栏出现问题
     *
     * @return
     */
    public static boolean isControlNavigation(Context context)
    {
        LoggerManager.i(TAG, "mode:" + Build.MODEL);
        boolean ret = false;
        if (isCheckedNavigation == -1)
        {
            boolean hasNavigationBar = false;
            Resources rs = context.getResources();
            int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
            if (id > 0)
            {
                hasNavigationBar = rs.getBoolean(id);
            }
            try
            {
                Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
                Method m = systemPropertiesClass.getMethod("get", String.class);
                String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
                if ("1".equals(navBarOverride))
                {
                    hasNavigationBar = false;
                } else if ("0".equals(navBarOverride))
                {
                    hasNavigationBar = true;
                }
            } catch (Exception e)
            {
                hasNavigationBar = false;
            }


            isCheckedNavigation = hasNavigationBar ? 1 : 0;
        }

        isCheckedNavigation = 0;
        return isCheckedNavigation == 1 ? true : false;
    }

    private static int isCheckedNavigation = -1;

    public static Context getContext()
    {
        return mContext;
    }
}
