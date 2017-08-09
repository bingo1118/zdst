package com.smart.cloud.fire.global;

/**
 * Created by Rain on 2017/8/1.
 */
public class Department {
    String departName;
    String departId;

    public String getDepartParentId() {
        return departParentId;
    }

    public void setDepartParentId(String departParentId) {
        this.departParentId = departParentId;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }

    String departParentId;
    public Department(String departName,String departId,String departParentId){
        this.departName=departName;
        this.departId=departId;
        this.departParentId=departParentId;
    }
}
