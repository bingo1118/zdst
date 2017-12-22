package com.smart.cloud.fire.global;

import java.io.Serializable;

/**
 * Created by Rain on 2017/9/12.
 */
public class InspectionSiteBean  implements Serializable {
    String deptname;
    String deptid;
    String siteid;
    String sitename;
    String lng;
    String lat;
    String begintime;
    String endtime;
    String happentime;
    String tourdataid;

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public String getSiteid() {
        return siteid;
    }

    public void setSiteid(String siteid) {
        this.siteid = siteid;
    }

    public String getSitename() {
        return sitename;
    }

    public void setSitename(String sitename) {
        this.sitename = sitename;
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

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getHappentime() {
        return happentime;
    }

    public void setHappentime(String happentime) {
        this.happentime = happentime;
    }

    public String getTourdataid() {
        return tourdataid;
    }

    public void setTourdataid(String tourdataid) {
        this.tourdataid = tourdataid;
    }

    public InspectionSiteBean(String deptname, String deptid, String siteid, String sitename, String lng, String lat, String begintime, String endtime, String happentime, String tourdataid) {

        this.deptname = deptname;
        this.deptid = deptid;
        this.siteid = siteid;
        this.sitename = sitename;
        this.lng = lng;
        this.lat = lat;
        this.begintime = begintime;
        this.endtime = endtime;
        this.happentime = happentime;
        this.tourdataid = tourdataid;
    }
}
