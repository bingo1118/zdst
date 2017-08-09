package com.smart.cloud.fire.global;

/**
 * Created by Rain on 2017/8/2.
 */
public class InspectionAlarmInfo {
    String alarmtype;
    String deviceno;
    String alarmtime;

    public InspectionAlarmInfo(String alarmtype, String deviceno, String alarmtime) {
        this.alarmtype = alarmtype;
        this.deviceno = deviceno;
        this.alarmtime = alarmtime;
    }

    public String getAlarmtype() {

        return alarmtype;
    }

    public void setAlarmtype(String alarmtype) {
        this.alarmtype = alarmtype;
    }

    public String getDeviceno() {
        return deviceno;
    }

    public void setDeviceno(String deviceno) {
        this.deviceno = deviceno;
    }

    public String getAlarmtime() {
        return alarmtime;
    }

    public void setAlarmtime(String alarmtime) {
        this.alarmtime = alarmtime;
    }
}
