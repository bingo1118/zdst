package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Entity;

/**
 * Created by Rain on 2019/11/22.
 */
public class AlarmHistoryEntity  {

    private String deviceName;
    private String deviceNum;
    private String deviceAddress;
    private int troubleAlarm;
    private String eventTime;
    private int logid;
    private String infotransferArea;
    private int fireAlarm;
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceNum(String deviceNum) {
        this.deviceNum = deviceNum;
    }
    public String getDeviceNum() {
        return deviceNum;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }
    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setTroubleAlarm(int troubleAlarm) {
        this.troubleAlarm = troubleAlarm;
    }
    public int getTroubleAlarm() {
        return troubleAlarm;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
    public String getEventTime() {
        return eventTime;
    }

    public void setLogid(int logid) {
        this.logid = logid;
    }
    public int getLogid() {
        return logid;
    }

    public void setInfotransferArea(String infotransferArea) {
        this.infotransferArea = infotransferArea;
    }
    public String getInfotransferArea() {
        return infotransferArea;
    }

    public void setFireAlarm(int fireAlarm) {
        this.fireAlarm = fireAlarm;
    }
    public int getFireAlarm() {
        return fireAlarm;
    }

}
