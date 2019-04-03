package com.tianxie.demo.ui.login;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.FastStackUtil;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.SPUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.ui.view.title.TitleBarView;
import com.google.gson.Gson;
import com.tianx.demo.R;
import com.tianxie.demo.Observer.LoadingObserver;
import com.tianxie.demo.base.Common;
import com.tianxie.demo.entity.LoginEntity;
import com.tianxie.demo.entity.VerificationEntitiy;
import com.tianxie.demo.retrofit.repository.ApiRepository;
import com.tianxie.demo.ui.BaseUI.TitleActivity;
import com.tianxie.demo.ui.MainActivity;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends TitleActivity
{
    @BindView(R.id.edit_userName)
    EditText mEUserName;
    @BindView(R.id.image_userName)
    ImageView mIUserName;
//    @BindView(R.id.et_password)
//    EditText mEPassword;
//    @BindView(R.id.iv_swich_passwrod)
//    ImageView mIPassword;
    @BindView(R.id.et_verification)
    EditText mEVerification;

    private boolean mVisPassword = false;

    @Override
    public int getContentLayout()
    {
        return R.layout.activity_login_new;
    }

    @Override
    public void beforeSetContentView()
    {
        LoggerManager.i(TAG, "isTaskRoot:" + isTaskRoot() + ";getCurrent:" + FastStackUtil.getInstance().getCurrent());
        //防止应用后台后点击桌面图标造成重启的假象---MIUI及Flyme上发现过(原生未发现)
        if (!isTaskRoot())
        {
            finish();
            return;
        }
        super.beforeSetContentView();
    }

    @Override
    public void initView(Bundle savedInstanceState)
    {
        LoginEntity entity = getUseEntitiy();
        if (entity != null)
        {
            mEUserName.setText(entity.getUserName());
            //mEPassword.setText(entity.getPassword());
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        AndPermission.with(this)
                .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                .onGranted(new Action()
                {
                    @Override
                    public void onAction(List<String> permissions)
                    {
                    }
                }).start();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar)
    {
        if (titleBar != null)
        {
            titleBar.setStatusBarLightMode(false)
                    .setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.image_userName, R.id.button_login, R.id.button_verification})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.image_userName:
                mEUserName.setText("");
                break;
            case R.id.button_verification:
                Map<String, String> map = new HashMap<>();
                map.put("userName", mEUserName.getText().toString());
                map.put("smsType", mEUserName.getText().toString());
                ApiRepository.getInstance().doGetVerification(map)
                        .compose(bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(new FastLoadingObserver<VerificationEntitiy>()
                        {
                            @Override
                            public void _onNext(VerificationEntitiy entity)
                            {
                                if (entity != null)
                                    mEVerification.setText(entity.getVerificationVo().getSmsCode());
                            }

                            @Override
                            public void onError(Throwable e)
                            {
                                super.onError(e);
                                LoggerManager.e(e.toString());
                            }
                        });
                break;
//            case R.id.iv_swich_passwrod:
//                if (!mVisPassword)
////                {
////                    //密码可见
////                    //在xml中定义id后,使用binding可以直接拿到这个view的引用,不再需要findViewById去找控件了
////                    mIPassword.setImageResource(R.mipmap.show_psw_press);
////                    mEPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
////                } else
////                {
////                    //密码不可见
////                    mIPassword.setImageResource(R.mipmap.show_psw);
////                    mEPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
////                }
//                break;
            case R.id.button_login:
                if (!Common.DEBUG)
                {
                    String userName = mEUserName.getText().toString();
                    if (userName == null || userName.isEmpty())
                    {
                        ToastUtil.show("请输入用户名！");
                        break;
                    }
//                    String password = mEPassword.getText().toString();
//                    if (password == null || password.isEmpty())
//                    {
//                        ToastUtil.show("请输入密码！");
//                        break;
//                    }
                    String verficationStr = mEVerification.getText().toString();
                    if (verficationStr == null || verficationStr.isEmpty())
                    {
                        ToastUtil.show("请输入验证码！");
                        break;
                    }

                    Map<String, Object> params = new HashMap<>();
                    params.put("userName", userName);
                    //params.put("password", password);
                    params.put("smsCode", verficationStr);
                    ApiRepository.getInstance().doLogin(params).
                            compose(bindUntilEvent(ActivityEvent.DESTROY))
                            .subscribe(new LoadingObserver<LoginEntity>()
                            {
                                @Override
                                protected void onSuccess(LoginEntity entity)
                                {
                                    entity.setUserName(userName);
                                    //entity.setPassword(password);
                                    boolean isSp = false;
                                    LoginEntity oldEntity = getUseEntitiy();
                                    if (oldEntity != null)
                                    {
                                        if (!oldEntity.equals(entity))
                                            isSp = true;
                                    } else
                                        isSp = true;
                                    if (isSp)
                                    {
                                        SPUtil.put(LoginActivity.this, Common.SP_USER_KEY, new Gson().toJson(entity));
                                    }
                                    FastUtil.startActivity(mContext, MainActivity.class);
                                    finish();
                                }
                            });
                } else
                {
                    FastUtil.startActivity(mContext, MainActivity.class);
                    finish();
                }
                break;
        }
    }
}
