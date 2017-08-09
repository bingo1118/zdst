package com.smart.cloud.fire.global;

/**
 * Created by Rain on 2017/8/2.
 */
public class LostHint {
    String deptid;
    String deptname;
    String lostdate;
    String lostrate;

    public LostHint(String deptid, String deptname, String lostdate, String lostrate) {
        this.deptid = deptid;
        this.deptname = deptname;
        this.lostdate = lostdate;
        this.lostrate = lostrate;
    }

    public String getDeptid() {

        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public String getLostdate() {
        return lostdate;
    }

    public void setLostdate(String lostdate) {
        this.lostdate = lostdate;
    }

    public String getLostrate() {
        return lostrate;
    }

    public void setLostrate(String lostrate) {
        this.lostrate = lostrate;
    }
}
