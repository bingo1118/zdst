package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Entity.YCLogin;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Entity.Yongchuan;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Entity.getAllYongchuan;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rain on 2019/11/6.
 */
public class LiftPresenter extends BasePresenter<LiftView> {

    public LiftPresenter(LiftView electricView){
        attachView(electricView);
    }

    public void getAllYongchuan(final String page, final List<Yongchuan> list, boolean refresh) {
        if (!refresh) {
            mvpView.showLoading();
        }
//        Observable mObservable = apiStores4.getAllYongcuan("447f1d6f-17af-4b2e-927d-037b57804bf1", page);
        Observable mObservable_login = apiStores4.login("admin","123456");
        twoSubscription(mObservable_login,new Func1<YCLogin,Observable>() {
            @Override
            public Observable call(YCLogin loginModel) {
                if(loginModel.getCode()==0){
                    Observable mObservable = apiStores4.getAllYongcuan(loginModel.getJsessionid(), page);
                    return mObservable;//登陆内部服务器。。
                }else {
                    Observable<YCLogin> observable = Observable.just(loginModel);
                    return observable;
                }
            }
        },new SubscriberCallBack<>(new ApiCallback<getAllYongchuan>() {
            @Override
            public void onSuccess(getAllYongchuan model) {
                int result = model.getCode();
                if (result == 0) {
                    List<Yongchuan> smokeList = model.getPage().getList();
                    if (list == null || list.size() == 0) {
                        mvpView.getDataSuccess(smokeList, false,model.getPage().getTotalCount());
                    } else if (list != null && list.size() >= 20) {
                        mvpView.onLoadingMore(smokeList);
                    }

                } else {
                    List<Smoke> mSmokeList = new ArrayList<>();
                    mvpView.getDataSuccess(mSmokeList, false, model.getPage().getTotalCount());
                    mvpView.getDataFail("无数据");
                }
            }


            @Override
            public void onFailure(int code, String msg) {
                mvpView.getDataFail("网络错误");
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }
}
