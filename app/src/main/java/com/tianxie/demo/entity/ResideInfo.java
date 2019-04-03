package com.tianxie.demo.entity;

import java.io.Serializable;

public class ResideInfo implements Serializable
{
    private String id;
    private String introductionLetter;
    private String leasePersonIdcardNo;
    private String leasePersonName;
    private String leasePersonPhone;
    private String police;
    private String resideAddress;

    public String getResideAddress()
    {
        return resideAddress;
    }

    public void setResideAddress(String resideAddress)
    {
        this.resideAddress = resideAddress;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getIntroductionLetter()
    {
        return introductionLetter;
    }

    public void setIntroductionLetter(String introductionLetter)
    {
        this.introductionLetter = introductionLetter;
    }

    public String getLeasePersonIdcardNo()
    {
        return leasePersonIdcardNo;
    }

    public void setLeasePersonIdcardNo(String leasePersonIdcardNo)
    {
        this.leasePersonIdcardNo = leasePersonIdcardNo;
    }

    public String getLeasePersonName()
    {
        return leasePersonName;
    }

    public void setLeasePersonName(String leasePersonName)
    {
        this.leasePersonName = leasePersonName;
    }

    public String getLeasePersonPhone()
    {
        return leasePersonPhone;
    }

    public void setLeasePersonPhone(String leasePersonPhone)
    {
        this.leasePersonPhone = leasePersonPhone;
    }

    public String getPolice()
    {
        return police;
    }

    public void setPolice(String police)
    {
        this.police = police;
    }
}
