package com.tianxie.demo.entity;

import java.io.Serializable;

public class RegistFaceEntity implements Serializable
{

    private String face_token;
    private FaceLocation location;

    public String getFace_token()
    {
        return face_token;
    }

    public void setFace_token(String face_token)
    {
        this.face_token = face_token;
    }

    public FaceLocation getLocation()
    {
        return location;
    }

    public void setLocation(FaceLocation location)
    {
        this.location = location;
    }
}
