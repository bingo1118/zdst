package com.smart.cloud.fire.global;

/**
 * Created by Rain on 2019/11/5.
 */
public class Repeater {

    private int hoststate;
    private String deviceType;
    private String repeaterTime;
    private String hostStates;
    private int netstate;
    private String repeaterMac;
    private String netStates;
    private String address;

    public void setHoststate(int hoststate) {
        this.hoststate = hoststate;
    }
    public int getHoststate() {
        return hoststate;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
    public String getDeviceType() {
        return deviceType;
    }

    public void setRepeaterTime(String repeaterTime) {
        this.repeaterTime = repeaterTime;
    }
    public String getRepeaterTime() {
        return repeaterTime;
    }

    public void setHostStates(String hostStates) {
        this.hostStates = hostStates;
    }
    public String getHostStates() {
        return hostStates;
    }

    public void setNetstate(int netstate) {
        this.netstate = netstate;
    }
    public int getNetstate() {
        return netstate;
    }

    public void setRepeaterMac(String repeaterMac) {
        this.repeaterMac = repeaterMac;
    }
    public String getRepeaterMac() {
        return repeaterMac;
    }

    public void setNetStates(String netStates) {
        this.netStates = netStates;
    }
    public String getNetStates() {
        return netStates;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
