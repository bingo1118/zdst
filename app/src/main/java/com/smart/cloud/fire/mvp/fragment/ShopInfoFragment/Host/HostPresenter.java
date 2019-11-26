package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Host;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.Repeater;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpError;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by Rain on 2019/11/5.
 */
public class HostPresenter extends BasePresenter<HostView> {

    public HostPresenter(HostView view) {
        attachView(view);
    }

    //@@获取主机列表信息
    public void getHost(String userId, String privilege, String page, final List<Repeater> list, final int type, boolean refresh) {
        if (!refresh) {
            mvpView.showLoading();
        }
        Observable mObservable = apiStores1.getRepeaterInfo(userId, privilege, page, "");
        addSubscription(mObservable, new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                int result = model.getErrorCode();
                if (result == 0) {
                    List<Repeater> smokeList = model.getRepeater();
                    if (type == 1) {
                        if (list == null || list.size() == 0) {
                            mvpView.getDataSuccess(smokeList, false);
                        } else if (list != null && list.size() >= 20) {
                            mvpView.onLoadingMore(smokeList);
                        }
                    }
                } else {
                    List<Smoke> mSmokeList = new ArrayList<>();
                    mvpView.getDataSuccess(mSmokeList, false);
                    mvpView.getDataFail("无数据");
                }
            }


            @Override
            public void onFailure(int code, String msg) {
                if (type != 1) {
                    List<Smoke> mSmokeList = new ArrayList<>();
                    mvpView.getDataSuccess(mSmokeList, false);
                }
                mvpView.getDataFail("网络错误");
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }

}
