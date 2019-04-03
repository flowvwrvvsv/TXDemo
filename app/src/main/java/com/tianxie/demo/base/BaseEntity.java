package com.tianxie.demo.base;

import java.io.Serializable;

/**
 * Created: AriesHoo on 2017/6/29 16:39
 * Function:
 * Desc:
 */
public class BaseEntity implements Serializable
{
    private int code;
    private String msg;

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }
}
