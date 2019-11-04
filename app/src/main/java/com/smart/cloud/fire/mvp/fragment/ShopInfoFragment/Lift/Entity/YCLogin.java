package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Entity;

/**
 * Created by Rain on 2019/10/30.
 */
public class YCLogin {

    private String msg;
    private int code;
    private String jsessionid;
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setJsessionid(String jsessionid) {
        this.jsessionid = jsessionid;
    }
    public String getJsessionid() {
        return jsessionid;
    }

}
