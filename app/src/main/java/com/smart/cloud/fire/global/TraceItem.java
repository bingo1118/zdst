package com.smart.cloud.fire.global;

/**
 * Created by Rain on 2017/8/4.
 */
public class TraceItem {
    String time;
    String lng;
    String lat;

    public TraceItem(String time, String lng, String lat) {
        this.time = time;
        this.lng = lng;
        this.lat = lat;
    }

    public String getTime() {

        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
