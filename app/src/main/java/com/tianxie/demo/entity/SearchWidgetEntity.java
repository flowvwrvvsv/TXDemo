package com.tianxie.demo.entity;

import java.io.Serializable;

public class SearchWidgetEntity implements Serializable
{
    private String imageUrl;
    private String name;
    private String sex;
    private String nation;
    private String religion;
    private String myanmarID;
    private String passID;
    private String documentID;
    private String limtDate;


    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getNation()
    {
        return nation;
    }

    public void setNation(String nation)
    {
        this.nation = nation;
    }

    public String getReligion()
    {
        return religion;
    }

    public void setReligion(String religion)
    {
        this.religion = religion;
    }

    public String getMyanmarID()
    {
        return myanmarID;
    }

    public void setMyanmarID(String myanmarID)
    {
        this.myanmarID = myanmarID;
    }

    public String getPassID()
    {
        return passID;
    }

    public void setPassID(String passID)
    {
        this.passID = passID;
    }

    public String getDocumentID()
    {
        return documentID;
    }

    public void setDocumentID(String documentID)
    {
        this.documentID = documentID;
    }

    public String getLimtDate()
    {
        return limtDate;
    }

    public void setLimtDate(String limtDate)
    {
        this.limtDate = limtDate;
    }
}
