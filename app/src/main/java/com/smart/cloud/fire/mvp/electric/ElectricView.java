package com.smart.cloud.fire.mvp.electric;


import com.smart.cloud.fire.global.ElectricDXDetailEntity;
import com.smart.cloud.fire.global.ElectricValue;

import java.util.List;

/**
 * Created by Administrator on 2016/11/2.
 */
public interface ElectricView {
    void getDataSuccess(List<ElectricValue.ElectricValueBean> smokeList);

    void getDataDXSuccess(ElectricDXDetailEntity entity);

    void getDataFail(String msg);

    void showLoading();

    void hideLoading();

    void getDataDXyuzhiSuccess(ElectricDXDetailEntity model);

}
