package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.AlarmHIstory;

import java.util.List;

/**
 * Created by Rain on 2019/11/22.
 */
public interface AlarmHistoryView {
    void getDataSuccess(List<?> smokeList, boolean research, int totalCount);

    void getDataFail(String msg);

    void showLoading();

    void hideLoading();

    void onLoadingMore(List<?> smokeList);
}
