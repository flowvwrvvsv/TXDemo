package com.tianxie.demo.entity.scan;


import com.tianxie.demo.base.BaseEntity;
import com.tianxie.demo.entity.ScanEntitiyVo;

import java.io.Serializable;
import java.util.Date;

public class ScanEntity extends BaseEntity
{
    private ScanEntitiyVo data;

    public ScanEntitiyVo getData()
    {
        return data;
    }

    public void setData(ScanEntitiyVo data)
    {
        this.data = data;
    }
}
