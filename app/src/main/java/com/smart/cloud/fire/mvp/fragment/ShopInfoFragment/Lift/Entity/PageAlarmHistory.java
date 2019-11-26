package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Entity;

import java.util.List;

/**
 * Created by Rain on 2019/11/22.
 */
public class PageAlarmHistory  {

    private int totalCount;
    private int pageSize;
    private int totalPage;
    private int currPage;
    private List<AlarmHistoryEntity> list;
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    public int getTotalCount() {
        return totalCount;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getPageSize() {
        return pageSize;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }
    public int getCurrPage() {
        return currPage;
    }

    public void setList(List<AlarmHistoryEntity> list) {
        this.list = list;
    }
    public List<AlarmHistoryEntity> getList() {
        return list;
    }

}
