package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift;

import java.util.List;

/**
 * Created by Rain on 2019/11/6.
 */
public interface LiftView {
    void getDataSuccess(List<?> smokeList, boolean research, int totalCount);

    void getDataFail(String msg);

    void showLoading();

    void hideLoading();

    void onLoadingMore(List<?> smokeList);
}
