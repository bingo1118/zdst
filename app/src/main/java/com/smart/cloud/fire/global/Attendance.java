package com.smart.cloud.fire.global;

/**
 * Created by Rain on 2017/8/2.
 */
public class Attendance {
    String lable;//人员名称
    String value;//人员出勤率

    public Attendance(String lable, String value) {
        this.lable = lable;
        this.value = value;
    }

    public String getLable() {

        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
