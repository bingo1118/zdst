package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Devices;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Entity.TransmissionDevice;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Entity.YCLogin;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Entity.getAllTransmissionDevice;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;
import com.smart.cloud.fire.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rain on 2019/10/30.
 */
public class YCDevicesPresenter extends BasePresenter<YCDevicesView>{

    public YCDevicesPresenter(YCDevicesView electricView){
        attachView(electricView);
    }

    public void getAllTransmissionDevice(final String yc_mac, final List<TransmissionDevice> list, final String page, final int type, final boolean refresh){
            if(!refresh){
            mvpView.showLoading();
        }
//        Observable mObservable = apiStores4.getAllTransmissionDevice("20",yc_mac,"",page);
        String userId = SharedPreferencesManager.getInstance().getData(MyApp.app, SharedPreferencesManager.SP_FILE_GWELL, SharedPreferencesManager.KEY_RECENTNAME);
        String userPwd = SharedPreferencesManager.getInstance().getData(MyApp.app, SharedPreferencesManager.SP_FILE_GWELL, SharedPreferencesManager.KEY_RECENTPASS);
        Observable mObservable_login = apiStores4.login(userId,userPwd);
//        Observable mObservable_login = apiStores4.login("admin","123456");
        twoSubscription(mObservable_login,new Func1<YCLogin,Observable>() {
            @Override
            public Observable call(YCLogin loginModel) {
                if(loginModel.getCode()==0){
                    Observable mObservable = apiStores4.getAllTransmissionDevice("20",yc_mac,loginModel.getJsessionid(),page,type==1?"1":null,type==2?"1":null);
                    return mObservable;//登陆内部服务器。。
                }else {
                    Observable<YCLogin> observable = Observable.just(loginModel);
                    return observable;
                }
            }
        },new SubscriberCallBack<>(new ApiCallback<getAllTransmissionDevice>() {
            @Override
            public void onSuccess(getAllTransmissionDevice model) {
                int resultCode = model.getCode();
                if (resultCode == 0) {
                    List<TransmissionDevice> smokeList = model.getPage().getList();
                    if (list == null || list.size() == 0) {
                        mvpView.getDataSuccess(smokeList, false,model.getPage().getTotalCount());
                    } else if (list != null && list.size() >= 20) {
                        mvpView.onLoadingMore(smokeList);
                    }

                } else {
                    List<TransmissionDevice> mSmokeList = new ArrayList<>();
                    mvpView.getDataSuccess(mSmokeList, false, model.getPage().getTotalCount());
                    mvpView.getDataFail("无数据");
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.getDataFail("网络错误，请检查网络");
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
//        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<getAllTransmissionDevice>() {
//            @Override
//            public void onSuccess(getAllTransmissionDevice model) {
//                int resultCode = model.getCode();
//                if(resultCode==0){
//                    List<TransmissionDevice> electricList = model.getPage().getList();
//                    if(electricList!=null){
//                        mvpView.getDataSuccess(electricList,refresh);
//                    }else{
//                        mvpView.getDataFail("无数据");
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(int code, String msg) {
//                mvpView.getDataFail("网络错误，请检查网络");
//            }
//
//            @Override
//            public void onCompleted() {
//                mvpView.hideLoading();
//            }
//        }));
    }
}
