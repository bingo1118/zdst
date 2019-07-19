package com.smart.cloud.fire.mvp.fragment.MapFragment;

import com.smart.cloud.fire.global.ElectricInfo;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.mvp.ElectrTimerTask.TimerTaskEntity;
import com.smart.cloud.fire.mvp.electric_change_history.HistoryBean;
import com.smart.cloud.fire.mvp.fragment.CollectFragment.AlarmMessageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */
public class HttpError<T> {

    /**
     * error : 获取烟感成功）
     * errorCode : 0
     */

    private String error;
    private int errorCode;
    private  String state;
    private List<Smoke> smoke;
    private List<Camera> camera;
    private ArrayList<ShopType> placeType;
    private List<AlarmMessageModel> Alarm;
    private List<ElectricInfo> Electric;
    private List<HistoryBean> eleList;//@@8.28

    private List<String> safetyItems;//@@7.25

    private ArrayList<TimerTaskEntity> tasks;//@@定时任务

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<Smoke> getSmoke() {
        return smoke;
    }

    public void setSmoke(List<Smoke> smoke) {
        this.smoke = smoke;
    }

    public List<Camera> getCamera() {
        return camera;
    }

    public void setCamera(List<Camera> camera) {
        this.camera = camera;
    }

    public ArrayList<ShopType> getPlaceType() {
        return placeType;
    }

    public void setPlaceType(ArrayList<ShopType> placeType) {
        this.placeType = placeType;
    }

    public List<AlarmMessageModel> getAlarm() {
        return Alarm;
    }

    public void setAlarm(List<AlarmMessageModel> alarm) {
        Alarm = alarm;
    }

    public List<ElectricInfo> getElectric() {
        return Electric;
    }

    public void setElectric(List<ElectricInfo> electric) {
        Electric = electric;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<String> getSafetyItems() {
        return safetyItems;
    }

    public void setSafetyItems(List<String> safetyItems) {
        this.safetyItems = safetyItems;
    }

    public List<HistoryBean> getEleList() {
        return eleList;
    }

    public void setEleList(List<HistoryBean> eleList) {
        this.eleList = eleList;
    }

    public ArrayList<TimerTaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<TimerTaskEntity> tasks) {
        this.tasks = tasks;
    }
}
