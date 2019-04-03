package com.tianxie.demo.entity;

import java.io.Serializable;

public class BaseInfo implements Serializable
{
    private String barCodeUrl;
    private String birthDay;
    private String chineseName;
    private String faceUrl;
    private String gender;
    private String id;
    private String idNumber;
    private String issueDate;
    private String nation;
    private String qrCodeUrl;
    private String religion;


    public String getBarCodeUrl()
    {
        return barCodeUrl;
    }

    public void setBarCodeUrl(String barCodeUrl)
    {
        this.barCodeUrl = barCodeUrl;
    }

    public String getBirthDay()
    {
        return birthDay;
    }

    public void setBirthDay(String birthDay)
    {
        this.birthDay = birthDay;
    }

    public String getChineseName()
    {
        return chineseName;
    }

    public void setChineseName(String chineseName)
    {
        this.chineseName = chineseName;
    }

    public String getFaceUrl()
    {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl)
    {
        this.faceUrl = faceUrl;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getIdNumber()
    {
        return idNumber;
    }

    public void setIdNumber(String idNumber)
    {
        this.idNumber = idNumber;
    }

    public String getIssueDate()
    {
        return issueDate;
    }

    public void setIssueDate(String issueDate)
    {
        this.issueDate = issueDate;
    }

    public String getNation()
    {
        return nation;
    }

    public void setNation(String nation)
    {
        this.nation = nation;
    }

    public String getQrCodeUrl()
    {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl)
    {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getReligion()
    {
        return religion;
    }

    public void setReligion(String religion)
    {
        this.religion = religion;
    }
}
