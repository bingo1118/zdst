package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Entity;


import java.util.List;

/**
 * Auto-generated: 2019-10-29 16:9:39
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Page1 {

    private int totalCount;
    private int pageSize;
    private int totalPage;
    private int currPage;
    private List<Yongchuan> list;

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

    public void setList(List<Yongchuan> list) {
        this.list = list;
    }

    public List<Yongchuan> getList() {
        return list;
    }

}