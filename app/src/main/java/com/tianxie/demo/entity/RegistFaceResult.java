package com.tianxie.demo.entity;

import java.io.Serializable;

public class RegistFaceResult implements Serializable
{
    private int error_code;
    private String error_msg;
    private long log_id;
    private int timestamp;
    private int cached;
    private RegistFaceEntity result;


    public int getError_code()
    {
        return error_code;
    }

    public void setError_code(int error_code)
    {
        this.error_code = error_code;
    }

    public String getError_msg()
    {
        return error_msg;
    }

    public void setError_msg(String error_msg)
    {
        this.error_msg = error_msg;
    }

    public long getLog_id()
    {
        return log_id;
    }

    public void setLog_id(long log_id)
    {
        this.log_id = log_id;
    }

    public int getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(int timestamp)
    {
        this.timestamp = timestamp;
    }

    public int getCached()
    {
        return cached;
    }

    public void setCached(int cached)
    {
        this.cached = cached;
    }

    public RegistFaceEntity getResult()
    {
        return result;
    }

    public void setResult(RegistFaceEntity result)
    {
        this.result = result;
    }
}
