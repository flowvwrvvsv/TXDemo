package com.tianxie.demo.entity;

import java.io.Serializable;

public class EntryInfo implements Serializable
{
    private String borderPassBackUrl;
    private String borderPassFrontUrl;
    private String cardValidDate;
    private String destination;
    private String entryDate;
    private String entryPort;
    private String exitPort;
    private String id;
    private String passNumber;
    private String reason;


    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getBorderPassBackUrl()
    {
        return borderPassBackUrl;
    }

    public void setBorderPassBackUrl(String borderPassBackUrl)
    {
        this.borderPassBackUrl = borderPassBackUrl;
    }

    public String getBorderPassFrontUrl()
    {
        return borderPassFrontUrl;
    }

    public void setBorderPassFrontUrl(String borderPassFrontUrl)
    {
        this.borderPassFrontUrl = borderPassFrontUrl;
    }

    public String getCardValidDate()
    {
        return cardValidDate;
    }

    public void setCardValidDate(String cardValidDate)
    {
        this.cardValidDate = cardValidDate;
    }

    public String getDestination()
    {
        return destination;
    }

    public void setDestination(String destination)
    {
        this.destination = destination;
    }

    public String getEntryDate()
    {
        return entryDate;
    }

    public void setEntryDate(String entryDate)
    {
        this.entryDate = entryDate;
    }

    public String getEntryPort()
    {
        return entryPort;
    }

    public void setEntryPort(String entryPort)
    {
        this.entryPort = entryPort;
    }

    public String getExitPort()
    {
        return exitPort;
    }

    public void setExitPort(String exitPort)
    {
        this.exitPort = exitPort;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getPassNumber()
    {
        return passNumber;
    }

    public void setPassNumber(String passNumber)
    {
        this.passNumber = passNumber;
    }
}

