package com.tianxie.demo.entity;

import android.app.Activity;

/**
 * @Author: AriesHoo on 2018/11/19 14:18
 * @E-Mail: AriesHoo@126.com
 * @Function: 描述性条目实体
 * @Description:
 */
public class WidgetEntity {

    public String title;
    public int imageID;
    public int backID;
    public Class<? extends Activity> activity;

    public WidgetEntity() {
    }

    public WidgetEntity(String title, int imageID, Class<? extends Activity> activity) {
        this.title = title;
        this.imageID = imageID;
        this.activity = activity;
    }

    public WidgetEntity(String title, int imageID) {
        this.title = title;
        this.imageID = imageID;
    }
}
