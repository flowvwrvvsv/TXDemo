package com.tianxie.demo.entity;

import com.tianxie.demo.base.BaseEntity;

import java.io.Serializable;
import java.util.Date;

import androidx.annotation.Nullable;

/**
 * Created with IDEA
 * author:csy
 * Date:2019/3/4
 * Time:10:36
 */
public class LoginEntity extends BaseEntity
{
    private static final long serialVersionUID = 8044807164912487127L;

    private String userName;
    private String password;
    private String data;


    public static long getSerialVersionUID()
    {
        return serialVersionUID;
    }


    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    @Override
    public boolean equals(@Nullable Object obj)
    {
        if (obj instanceof LoginEntity || obj == null)
        {
            return false;
        }
        LoginEntity other = (LoginEntity) obj;
        return this.userName.equals(other.getUserName()) && this.password.equals(other.getPassword());
    }
}
