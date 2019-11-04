package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Entity;

import java.util.List;


public class Page {

    private int totalCount;
    private int pageSize;
    private int totalPage;
    private int currPage;
    private List<TransmissionDevice> list;
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

    public void setList(List<TransmissionDevice> list) {
        this.list = list;
    }
    public List<TransmissionDevice> getList() {
        return list;
    }

}
