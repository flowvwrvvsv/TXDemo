package com.tianxie.demo.entity;

import java.io.Serializable;

public class WorkInfo implements Serializable
{
    private String businessLicence;
    private String employeeResponsiblePersonIdcardFrontUrl;
    private String employeeResponsiblePersonIdcardIdcardBackUrl;
    private String employeeResponsiblePersonIdcardNo;
    private String employeeResponsiblePersonName;
    private String employeeResponsiblePersonPhone;
    private String employingAddress;
    private String employingUnit;


    public String getBusinessLicence()
    {
        return businessLicence;
    }

    public void setBusinessLicence(String businessLicence)
    {
        this.businessLicence = businessLicence;
    }

    public String getEmployeeResponsiblePersonIdcardFrontUrl()
    {
        return employeeResponsiblePersonIdcardFrontUrl;
    }

    public void setEmployeeResponsiblePersonIdcardFrontUrl(String employeeResponsiblePersonIdcardFrontUrl)
    {
        this.employeeResponsiblePersonIdcardFrontUrl = employeeResponsiblePersonIdcardFrontUrl;
    }

    public String getEmployeeResponsiblePersonIdcardIdcardBackUrl()
    {
        return employeeResponsiblePersonIdcardIdcardBackUrl;
    }

    public void setEmployeeResponsiblePersonIdcardIdcardBackUrl(String employeeResponsiblePersonIdcardIdcardBackUrl)
    {
        this.employeeResponsiblePersonIdcardIdcardBackUrl = employeeResponsiblePersonIdcardIdcardBackUrl;
    }

    public String getEmployeeResponsiblePersonIdcardNo()
    {
        return employeeResponsiblePersonIdcardNo;
    }

    public void setEmployeeResponsiblePersonIdcardNo(String employeeResponsiblePersonIdcardNo)
    {
        this.employeeResponsiblePersonIdcardNo = employeeResponsiblePersonIdcardNo;
    }

    public String getEmployeeResponsiblePersonName()
    {
        return employeeResponsiblePersonName;
    }

    public void setEmployeeResponsiblePersonName(String employeeResponsiblePersonName)
    {
        this.employeeResponsiblePersonName = employeeResponsiblePersonName;
    }

    public String getEmployeeResponsiblePersonPhone()
    {
        return employeeResponsiblePersonPhone;
    }

    public void setEmployeeResponsiblePersonPhone(String employeeResponsiblePersonPhone)
    {
        this.employeeResponsiblePersonPhone = employeeResponsiblePersonPhone;
    }

    public String getEmployingAddress()
    {
        return employingAddress;
    }

    public void setEmployingAddress(String employingAddress)
    {
        this.employingAddress = employingAddress;
    }

    public String getEmployingUnit()
    {
        return employingUnit;
    }

    public void setEmployingUnit(String employingUnit)
    {
        this.employingUnit = employingUnit;
    }
}
