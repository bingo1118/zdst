package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Entity;

/**
 * Created by Rain on 2019/11/22.
 */
public class getAlarmHistoryEntity  {

    private String msg;
    private int code;
    private PageAlarmHistory page;
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

    public void setPage(PageAlarmHistory page) {
        this.page = page;
    }
    public PageAlarmHistory getPage() {
        return page;
    }

}
