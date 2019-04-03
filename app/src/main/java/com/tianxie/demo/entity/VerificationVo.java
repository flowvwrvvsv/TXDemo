package com.tianxie.demo.entity;

import java.io.Serializable;

public class VerificationVo implements Serializable
{
    private String smsCode;

    public String getSmsCode()
    {
        return smsCode;
    }

    public void setSmsCode(String smsCode)
    {
        this.smsCode = smsCode;
    }
}
