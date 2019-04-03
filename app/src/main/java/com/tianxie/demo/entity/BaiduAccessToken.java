package com.tianxie.demo.entity;

import java.io.Serializable;

public class BaiduAccessToken implements Serializable
{
    public static final String BAIDU_ACCESS_TOKEN = "BaiduAccessToken";

    private String refresh_token;
    private int expires_in;
    private String scope;
    private String session_key;
    private String access_token;
    private String session_secret;
    private long update_in;


    public int getExpires_in()
    {
        return expires_in;
    }

    public void setExpires_in(int expires_in)
    {
        this.expires_in = expires_in;
    }

    public long getUpdate_in()
    {
        return update_in;
    }

    public void setUpdate_in(long update_in)
    {
        this.update_in = update_in;
    }

    public String getRefresh_token()
    {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token)
    {
        this.refresh_token = refresh_token;
    }


    public String getScope()
    {
        return scope;
    }

    public void setScope(String scope)
    {
        this.scope = scope;
    }

    public String getSession_key()
    {
        return session_key;
    }

    public void setSession_key(String session_key)
    {
        this.session_key = session_key;
    }

    public String getAccess_token()
    {
        return access_token;
    }

    public void setAccess_token(String access_token)
    {
        this.access_token = access_token;
    }

    public String getSession_secret()
    {
        return session_secret;
    }

    public void setSession_secret(String session_secret)
    {
        this.session_secret = session_secret;
    }
}
