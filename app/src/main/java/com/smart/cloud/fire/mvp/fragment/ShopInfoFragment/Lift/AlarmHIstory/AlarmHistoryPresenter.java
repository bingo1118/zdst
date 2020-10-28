package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.AlarmHIstory;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Entity.AlarmHistoryEntity;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Entity.YCLogin;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Entity.getAlarmHistoryEntity;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;
import com.smart.cloud.fire.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rain on 2019/11/22.
 */
public class AlarmHistoryPresenter extends BasePresenter<AlarmHistoryView> {
    public AlarmHistoryPresenter(AlarmHistoryView electricView){
        attachView(electricView);
    }

    public void getAllAlarmHistory(final String yc_mac, final List<AlarmHistoryEntity> list, final String page, final int type, final boolean refresh){
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
                    Observable mObservable = null;
                    switch (type){
                        case 0:
                            mObservable = apiStores4.getAlarmHistoryEntity("20",yc_mac,loginModel.getJsessionid(),page);
                            break;
                        case 1:
                            mObservable = apiStores4.getAlarmHistoryEntityByConsident("20",yc_mac,loginModel.getJsessionid(),page,"1","");
                            break;
                        case 2:
                            mObservable = apiStores4.getAlarmHistoryEntityByConsident("20",yc_mac,loginModel.getJsessionid(),page,"","1");
                            break;
                    }
                    return mObservable;//登陆内部服务器。。
                }else {
                    Observable<YCLogin> observable = Observable.just(loginModel);
                    return observable;
                }
            }
        },new SubscriberCallBack<>(new ApiCallback<getAlarmHistoryEntity>() {
            @Override
            public void onSuccess(getAlarmHistoryEntity model) {
                int resultCode = model.getCode();
                if (resultCode == 0) {
                    List<AlarmHistoryEntity> smokeList = model.getPage().getList();
                    if (list == null || list.size() == 0) {
                        mvpView.getDataSuccess(smokeList, false,model.getPage().getTotalCount());
                    } else if (list != null && list.size() >= 20) {
                        mvpView.onLoadingMore(smokeList);
                    }

                } else {
                    List<AlarmHistoryEntity> mSmokeList = new ArrayList<>();
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

    }
}
