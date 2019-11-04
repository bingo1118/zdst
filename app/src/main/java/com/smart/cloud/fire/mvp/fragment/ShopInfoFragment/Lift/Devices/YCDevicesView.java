package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Devices;

import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Entity.TransmissionDevice;

import java.util.List;

/**
 * Created by Rain on 2019/10/30.
 */
public interface YCDevicesView {
    void getDataSuccess(List<?> smokeList,boolean research);

    void getDataFail(String msg);

    void showLoading();

    void hideLoading();

    void onLoadingMore(List<?> smokeList);
}
