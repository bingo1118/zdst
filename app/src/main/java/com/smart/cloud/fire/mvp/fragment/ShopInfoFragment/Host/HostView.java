package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Host;

import java.util.List;

/**
 * Created by Rain on 2019/11/5.
 */
public interface HostView {
    void getDataSuccess(List<?> smokeList, boolean research);

    void getDataFail(String msg);

    void showLoading();

    void hideLoading();

    void onLoadingMore(List<?> smokeList);
}
