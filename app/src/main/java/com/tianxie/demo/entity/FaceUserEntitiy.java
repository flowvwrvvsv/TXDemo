package com.tianxie.demo.entity;

import java.io.Serializable;

public class FaceUserEntitiy implements Serializable
{
    private String group_id;
    private String user_id;
    private String user_info;
    private float score;

    private String netUrl;

    public String getNetUrl()
    {
        return netUrl;
    }

    public void setNetUrl(String netUrl)
    {
        this.netUrl = netUrl;
    }

    public String getGroup_id()
    {
        return group_id;
    }

    public void setGroup_id(String group_id)
    {
        this.group_id = group_id;
    }

    public String getUser_id()
    {
        return user_id;
    }

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public String getUser_info()
    {
        return user_info;
    }

    public void setUser_info(String user_info)
    {
        this.user_info = user_info;
    }

    public float getScore()
    {
        return score;
    }

    public void setScore(float score)
    {
        this.score = score;
    }
}
