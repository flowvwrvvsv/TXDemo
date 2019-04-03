package com.tianxie.demo.entity;

import com.tianxie.demo.base.BaseEntity;

public class VerificationEntitiy extends BaseEntity
{
    private VerificationVo data;

    public VerificationVo getVerificationVo()
    {
        return data;
    }

    public void setVerificationVo(VerificationVo verificationVo)
    {
        data = verificationVo;
    }
}
