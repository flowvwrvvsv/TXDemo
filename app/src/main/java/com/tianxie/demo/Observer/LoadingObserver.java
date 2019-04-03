package com.tianxie.demo.Observer;

import android.app.Activity;

import com.aries.library.fast.FastManager;
import com.aries.library.fast.i.IHttpRequestControl;
import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.FastStackUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.library.fast.widget.FastLoadDialog;
import com.tianxie.demo.base.BaseEntity;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

public abstract class LoadingObserver<T extends BaseEntity> extends FastLoadingObserver<T>
{
    /**
     * 用于全局配置
     *
     * @param activity
     */
    public LoadingObserver(@Nullable Activity activity, IHttpRequestControl httpRequestControl, @StringRes int resId) {
        this(FastManager.getInstance().getLoadingDialog().createLoadingDialog(activity).setMessage(resId), httpRequestControl);
    }

    public LoadingObserver(IHttpRequestControl httpRequestControl, @StringRes int resId) {
        this(FastStackUtil.getInstance().getCurrent(), httpRequestControl, resId);
    }

    public LoadingObserver(@Nullable Activity activity, IHttpRequestControl httpRequestControl, CharSequence msg) {
        this(FastManager.getInstance().getLoadingDialog().createLoadingDialog(activity).setMessage(msg), httpRequestControl);
    }

    public LoadingObserver(IHttpRequestControl httpRequestControl, CharSequence msg) {
        this(FastStackUtil.getInstance().getCurrent(), httpRequestControl, msg);
    }

    public LoadingObserver(@Nullable Activity activity, @StringRes int resId) {
        this(FastManager.getInstance().getLoadingDialog().createLoadingDialog(activity).setMessage(resId));
    }

    public LoadingObserver(@StringRes int resId) {
        this(FastStackUtil.getInstance().getCurrent(), resId);
    }

    public LoadingObserver(@Nullable Activity activity, CharSequence msg) {
        this(FastManager.getInstance().getLoadingDialog().createLoadingDialog(activity).setMessage(msg));
    }

    public LoadingObserver(CharSequence msg) {
        this(FastStackUtil.getInstance().getCurrent(), msg);
    }

    public LoadingObserver(@Nullable Activity activity, IHttpRequestControl httpRequestControl) {
        this(FastManager.getInstance().getLoadingDialog().createLoadingDialog(activity), httpRequestControl);
    }

    public LoadingObserver(IHttpRequestControl httpRequestControl) {
        this(FastStackUtil.getInstance().getCurrent(), httpRequestControl);
    }

    public LoadingObserver(@Nullable Activity activity) {
        this(FastManager.getInstance().getLoadingDialog().createLoadingDialog(activity));
    }

    public LoadingObserver() {
        this(FastStackUtil.getInstance().getCurrent());
    }

    public LoadingObserver(FastLoadDialog dialog) {
        this(dialog, null);
    }

    public LoadingObserver(FastLoadDialog dialog, IHttpRequestControl httpRequestControl) {
        super(httpRequestControl);
        this.mDialog = dialog;
    }

    @Override
    public void _onNext(T entity)
    {
        if (entity.getCode() == 200)
            onSuccess(entity);
        else
            ToastUtil.show(entity.getMsg());
    }

    @Override
    public void onError(Throwable e)
    {
        super.onError(e);
    }

    protected abstract void onSuccess(T entity);
}
