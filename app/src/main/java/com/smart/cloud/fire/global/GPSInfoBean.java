package com.smart.cloud.fire.global;

import java.io.Serializable;

/**
 * Created by Rain on 2017/8/9.
 */
public class GPSInfoBean implements Serializable {
    private String devMac;
    private String lon;
    private String lat;
    private String speed;
    private String dataTime;


    public String getDevMac() {
        return devMac;
    }
    public void setDevMac(String devMac) {
        this.devMac = devMac;
    }
    public String getLon() {
        return lon;
    }
    public void setLon(String lon) {
        this.lon = lon;
    }
    public String getLat() {
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getSpeed() {
        return speed;
    }
    public void setSpeed(String speed) {
        this.speed = speed;
    }
    public String getDataTime() {
        return dataTime;
    }
    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public GPSInfoBean(String devMac, String lon, String lat, String speed, String dataTime) {
        this.devMac = devMac;
        this.lon = lon;
        this.lat = lat;
        this.speed = speed;
        this.dataTime = dataTime;
    }
}
