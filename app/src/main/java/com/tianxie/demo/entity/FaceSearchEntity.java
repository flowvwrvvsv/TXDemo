package com.tianxie.demo.entity;

import java.io.Serializable;
import java.util.List;

public class FaceSearchEntity implements Serializable
{
    private String face_token;
    private List<FaceUserEntitiy> user_list;

    public String getFace_token()
    {
        return face_token;
    }

    public void setFace_token(String face_token)
    {
        this.face_token = face_token;
    }

    public List<FaceUserEntitiy> getUser_list()
    {
        return user_list;
    }

    public void setUser_list(List<FaceUserEntitiy> user_list)
    {
        this.user_list = user_list;
    }
}
