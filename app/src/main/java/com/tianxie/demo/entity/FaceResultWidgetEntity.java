package com.tianxie.demo.entity;

import java.io.Serializable;

public class FaceResultWidgetEntity implements Serializable
{
    private String nativeUrl;
    private String netUrl;
    private float persent;
    private String name;
    private String sex;
    private String documentID;

    private FaceUserEntitiy mFaceUserEntitiy;

    private boolean bBack;

    public boolean isbBack()
    {
        return bBack;
    }

    public void setbBack(boolean bBack)
    {
        this.bBack = bBack;
    }


    public FaceUserEntitiy getFaceUserEntitiy()
    {
        return mFaceUserEntitiy;
    }

    public void setFaceUserEntitiy(FaceUserEntitiy faceUserEntitiy)
    {
        mFaceUserEntitiy = faceUserEntitiy;
    }

    public String getNativeUrl()
    {
        return nativeUrl;
    }

    public void setNativeUrl(String nativeUrl)
    {
        this.nativeUrl = nativeUrl;
    }

    public String getNetUrl()
    {
        return netUrl;
    }

    public void setNetUrl(String netUrl)
    {
        this.netUrl = netUrl;
    }

    public float getPersent()
    {
        return persent;
    }

    public void setPersent(float persent)
    {
        this.persent = persent;
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

    public String getDocumentID()
    {
        return documentID;
    }

    public void setDocumentID(String documentID)
    {
        this.documentID = documentID;
    }
}
