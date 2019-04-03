package com.tianxie.demo.entity;

import java.io.Serializable;

public class FaceLocation implements Serializable
{
    private double left;
    private double top;
    private double width;
    private double height;
    private int rotation;


    public double getLeft()
    {
        return left;
    }

    public void setLeft(double left)
    {
        this.left = left;
    }

    public double getTop()
    {
        return top;
    }

    public void setTop(double top)
    {
        this.top = top;
    }

    public double getWidth()
    {
        return width;
    }

    public void setWidth(double width)
    {
        this.width = width;
    }

    public double getHeight()
    {
        return height;
    }

    public void setHeight(double height)
    {
        this.height = height;
    }

    public int getRotation()
    {
        return rotation;
    }

    public void setRotation(int rotation)
    {
        this.rotation = rotation;
    }
}
