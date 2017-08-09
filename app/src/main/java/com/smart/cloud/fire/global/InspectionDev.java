package com.smart.cloud.fire.global;

import java.io.Serializable;

/**
 * Created by Rain on 2017/8/1.
 */
public class InspectionDev implements Serializable{
    String deptname;
    String readercode;
    String lng;
    String lat;
    String lasttime;

    public InspectionDev(String deptname, String readercode, String lng, String lat, String lasttime) {
        this.deptname = deptname;
        this.readercode = readercode;
        this.lng = lng;
        this.lat = lat;
        this.lasttime = lasttime;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public String getReadercode() {
        return readercode;
    }

    public void setReadercode(String readercode) {
        this.readercode = readercode;
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

    public String getLasttime() {
        return lasttime;
    }

    public void setLasttime(String lasttime) {
        this.lasttime = lasttime;
    }


}
