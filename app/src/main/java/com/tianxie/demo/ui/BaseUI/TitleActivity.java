package com.tianxie.demo.ui.BaseUI;

import android.os.Bundle;

import com.aries.library.fast.basis.BasisActivity;
import com.aries.library.fast.delegate.FastTitleDelegate;
import com.aries.library.fast.i.IFastTitleView;
import com.aries.library.fast.util.SPUtil;
import com.aries.ui.view.title.TitleBarView;
import com.google.gson.Gson;
import com.tianxie.demo.base.Common;
import com.tianxie.demo.entity.LoginEntity;

/**
 * @Author: AriesHoo on 2018/7/23 10:35
 * @E-Mail: AriesHoo@126.com
 * Function: 包含TitleBarView的Activity
 * Description:
 */
public abstract class TitleActivity extends BasisActivity implements IFastTitleView
{

    protected FastTitleDelegate mFastTitleDelegate;
    protected TitleBarView mTitleBar;

    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {

    }

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
        mFastTitleDelegate = new FastTitleDelegate(mContentView, this, this.getClass());
        mTitleBar = mFastTitleDelegate.mTitleBar;
    }


    public LoginEntity getUseEntitiy()
    {
        LoginEntity entity = null;
        String spString = (String) SPUtil.get(this, Common.SP_USER_KEY, "");
        if (spString != null && !spString.isEmpty())
             entity = new Gson().fromJson(spString, LoginEntity.class);

        return entity;
    }
}
