package com.tianxie.demo.entity;

import java.io.Serializable;

public class TrafficInfo implements Serializable
{
    private String  carNo;
    private String  drvingLicence;
    private String  electrombileNo;
    private String  id;
    private String  motorcycleNo;
    private String vehiclePhoto;


    public String getVehiclePhoto()
    {
        return vehiclePhoto;
    }

    public void setVehiclePhoto(String vehiclePhoto)
    {
        this.vehiclePhoto = vehiclePhoto;
    }

    public String getCarNo()
    {
        return carNo;
    }

    public void setCarNo(String carNo)
    {
        this.carNo = carNo;
    }

    public String getDrvingLicence()
    {
        return drvingLicence;
    }

    public void setDrvingLicence(String drvingLicence)
    {
        this.drvingLicence = drvingLicence;
    }

    public String getElectrombileNo()
    {
        return electrombileNo;
    }

    public void setElectrombileNo(String electrombileNo)
    {
        this.electrombileNo = electrombileNo;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getMotorcycleNo()
    {
        return motorcycleNo;
    }

    public void setMotorcycleNo(String motorcycleNo)
    {
        this.motorcycleNo = motorcycleNo;
    }
}