package com.smart.cloud.fire.retrofit;

import com.smart.cloud.fire.global.Electric;
import com.smart.cloud.fire.global.ElectricDXDetailEntity;
import com.smart.cloud.fire.global.ElectricInfo;
import com.smart.cloud.fire.global.ElectricValue;
import com.smart.cloud.fire.global.ProofGasHistoryEntity;
import com.smart.cloud.fire.global.SmokeSummary;
import com.smart.cloud.fire.global.TemperatureTime;
import com.smart.cloud.fire.mvp.fragment.ConfireFireFragment.ConfireFireModel;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpAreaResult;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpError;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Entity.YCLogin;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Entity.getAlarmHistoryEntity;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Entity.getAllTransmissionDevice;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Entity.getAllYongchuan;
import com.smart.cloud.fire.mvp.login.model.LoginModel;
import com.smart.cloud.fire.mvp.register.model.RegisterModel;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiStores {
    //登录技威服务器
    @FormUrlEncoded
    @POST("Users/LoginCheck.ashx")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<LoginModel>
    loginYooSee(@Field("User") String User, @Field("Pwd") String Pwd,
                                       @Field("VersionFlag") String VersionFlag, @Field("AppOS") String AppOS,
                                       @Field("AppVersion") String AppVersion);
    //登录本地服务器
    @GET("login")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<LoginModel> login(@Query("userId") String userId);

    //登录本地服务器2，登陆新接口2017.5.16
    @GET("login")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<LoginModel> login2(@Query("userId") String userId,@Query("pwd") String pwd
            ,@Query("cid") String cid,@Query("appId") String appId,@Query("ifregister") String ifregister);

    //获取短信验证码
    @FormUrlEncoded
    @POST("Users/PhoneCheckCode.ashx")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<RegisterModel> getMesageCode(@Field("CountryCode") String countryCode, @Field("PhoneNO") String phoneNO
            , @Field("AppVersion") String appVersion);

    //检查短信验证码
    @FormUrlEncoded
    @POST("Users/PhoneVerifyCodeCheck.ashx")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<RegisterModel> verifyPhoneCode(@Field("CountryCode") String countryCode,@Field("PhoneNO") String phoneNO
            ,@Field("VerifyCode") String verifyCode);

    //注册
    @FormUrlEncoded
    @POST("Users/RegisterCheck.ashx")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<RegisterModel> register(@Field("VersionFlag") String versionFlag,@Field("Email") String email
            ,@Field("CountryCode") String countryCode,@Field("PhoneNO") String phoneNO
            ,@Field("Pwd") String pwd,@Field("RePwd") String rePwd
            ,@Field("VerifyCode") String verifyCode,@Field("IgnoreSafeWarning") String ignoreSafeWarning);

    //获取用户所有的烟感
    @GET("getAllSmoke")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> getAllSmoke(@Query("userId") String userId, @Query("privilege") String privilege,@Query("page") String page);

    //获取用传
    @GET("scy/scyinfotransferinfo/listIncludeDeviceNum")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<getAllYongchuan> getAllYongcuan(@Query("jsessionid") String jsessionid, @Query("page") String page);

    //获取用传下的设备
    @GET("scy/scydeviceinfo/list")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<getAllTransmissionDevice> getAllTransmissionDevice(@Query("limit") String limit,@Query("deviceInfotransferId") String yc_mac
            ,@Query("jsessionid") String jsessionid, @Query("page") String page
            ,@Query("fireDevice") String fireDevice, @Query("troubleDevice") String troubleDevice);

    //获取用传下的报警历史记录
    @GET("scy/scydeviceloginfo/deviceLogList")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<getAlarmHistoryEntity> getAlarmHistoryEntity(@Query("limit") String limit, @Query("infotransferId") String yc_mac
            ,@Query("jsessionid") String jsessionid, @Query("page") String page);

    //获取用传下的报警历史记录
    @GET("scy/scydeviceloginfo/deviceLogList")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<getAlarmHistoryEntity> getAlarmHistoryEntityByConsident(@Query("limit") String limit, @Query("infotransferId") String yc_mac
            ,@Query("jsessionid") String jsessionid, @Query("page") String page
            , @Query("fireAlarm") String fireAlarm, @Query("troubleAlarm") String troubleAlarm);

    @GET("getOneElectricInfo")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<ElectricDXDetailEntity> getOneElectricDXInfo(@Query("userId") String userId, @Query("privilege") String privilege,
                                                            @Query("smokeMac") String smokeMac, @Query("devType") String devType);


    @GET("getElectrDXThreshold")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<ElectricDXDetailEntity> getElectrDXThreshold(@Query("mac") String smokeMac);

    @GET("getElectrDXThreshold2")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<ElectricDXDetailEntity> getElectrDXThreshold2(@Query("mac") String smokeMac);


    //用传登陆
    @GET("sys/login")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<YCLogin> login(@Query("username") String username, @Query("password") String password);

    //@@5.15获取用户安防设备列表
    @GET("getSecurityInfo")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> getSecurityInfo(@Query("userId") String userId, @Query("privilege") String privilege,@Query("page") String page);

    //@@7.28获取环境探测器列表
    @GET("getNeedEnviDev")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> getNeedEnviDev(@Query("userId") String userId, @Query("privilege") String privilege,@Query("page") String page,@Query("areaId") String areaId,
                                         @Query("placeTypeId") String placeTypeId);

//    @FormUrlEncoded
//    @POST("getAllSmoke")
//    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
//    Observable<HttpError> getAllSmoke(@Field("userId") String userId, @Field("privilege") String privilege,@Field("page") String page);

    //获取用户所有的摄像头
    @GET("getAllCamera")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> getAllCamera(@Query("userId") String userId, @Query("privilege") String privilege,
                                       @Query("page") String page,@Query("everypagenum") int everypagenum);

    //获取所有的店铺类型
    @GET("getPlaceTypeId")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> getPlaceTypeId(@Query("userId") String userId, @Query("privilege") String privilege,@Query("page") String page);

    //获取所有的区域类型
    @GET("getAreaId")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpAreaResult> getAreaId(@Query("userId") String userId, @Query("privilege") String privilege, @Query("page") String page);

    //根据条件查询用户烟感
    @GET("getNeedSmoke")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> getNeedSmoke(@Query("userId") String userId, @Query("privilege") String privilege,
                                            @Query("areaId") String areaId,@Query("page") String page,
                                            @Query("placeTypeId") String placeTypeId);
    //@@根据条件查询用户安防设备列表
    @GET("getNeedSecurity")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> getNeedSecurity(@Query("userId") String userId, @Query("privilege") String privilege,
                                        @Query("page") String page,@Query("areaId") String areaId,
                                       @Query("placeTypeId") String placeTypeId);

    //根据查询内容查询用户烟感
    @GET("getSmokeBySearch")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> getSearchSmoke(@Query("userId") String userId, @Query("privilege") String privilege,
                                       @Query("search") String search);
    //处理报警消息
    @GET("dealAlarm")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> dealAlarm(@Query("userId") String userId, @Query("smokeMac") String smokeMac);

    //根据条件查询用户所有设备（设备类型<11）
    @GET("getNeedDevice")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> getNeedDevice(@Query("userId") String userId, @Query("privilege") String privilege,
                                        @Query("areaId") String areaId,@Query("page") String page,
                                        @Query("placeTypeId") String placeTypeId);

    //获取单个烟感信息
    @GET("getOneSmoke")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<ConfireFireModel> getOneSmoke(@Query("userId") String userId, @Query("smokeMac") String smokeMac,
                                             @Query("privilege") String privilege);

    //添加烟感
    @FormUrlEncoded
    @POST("addSmoke")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<ConfireFireModel> addSmoke(@Field("userId") String userId, @Field("smokeName") String smokeName,
                                          @Field("privilege") String privilege, @Field("smokeMac") String smokeMac,
                                          @Field("address") String address, @Field("longitude") String longitude,
                                          @Field("latitude") String latitude, @Field("placeAddress") String placeAddress,
                                          @Field("placeTypeId") String placeTypeId, @Field("principal1") String principal1,
                                          @Field("principal1Phone") String principal1Phone, @Field("principal2") String principal2,
                                          @Field("principal2Phone") String principal2Phone, @Field("areaId") String areaId,
                                          @Field("repeater") String repeater,@Field("camera") String camera,@Field("deviceType") String deviceType,
                                          @Field("electrState") String electrState);

//    @FormUrlEncoded
//    @POST("addSmoke")
//    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
//    Observable<ConfireFireModel> addSmoke(@Field("userId") String userId, @Field("smokeName") String smokeName,
//                                          @Field("privilege") String privilege, @Field("smokeMac") String smokeMac,
//                                          @Field("address") String address, @Field("longitude") String longitude,
//                                          @Field("latitude") String latitude, @Field("placeAddress") String placeAddress,
//                                          @Field("placeTypeId") String placeTypeId, @Field("principal1") String principal1,
//                                          @Field("principal1Phone") String principal1Phone, @Field("principal2") String principal2,
//                                          @Field("principal2Phone") String principal2Phone, @Field("areaId") String areaId,
//                                          @Field("repeater") String repeater,@Field("camera") String camera,@Field("deviceType") String deviceType);

    //获取用户报警消息
    @GET("getAllAlarm")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> getAllAlarm(@Query("userId") String userId, @Query("privilege") String privilege,@Query("page") String page);

    //条件查询获取用户报警消息
    @GET("getNeedAlarm")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> getNeedAlarm(@Query("userId") String userId, @Query("privilege") String privilege
            ,@Query("startTime") String startTime,@Query("endTime") String endTime
            ,@Query("areaId") String areaId,@Query("placeTypeId") String placeTypeId
            ,@Query("page") String page);

    //添加摄像头
    @GET("addCamera")
    Observable<HttpError> addCamera(@Query("cameraId") String cameraId, @Query("cameraName") String cameraName,
                                          @Query("cameraPwd") String cameraPwd, @Query("cameraAddress") String cameraAddress,
                                          @Query("longitude") String longitude, @Query("latitude") String latitude,
                                          @Query("principal1") String principal1, @Query("principal1Phone") String principal1Phone,
                                          @Query("principal2") String principal2, @Query("principal2Phone") String principal2Phone,
                                          @Query("areaId") String areaId, @Query("placeTypeId") String placeTypeId);

    //绑定烟感与摄像头
    @GET("bindCameraSmoke")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> bindCameraSmoke(@Query("cameraId") String cameraId, @Query("smoke") String smoke);


    @GET("getCid")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> bindAlias(@Query("alias") String alias, @Query("cid") String cid,@Query("projectName") String projectName);

    //一键报警
    @GET("textAlarm")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> textAlarm(@Query("userId") String userId, @Query("privilege") String privilege,
                                    @Query("smokeMac") String smokeMac,@Query("info") String info);

    //一键报警确认回复
    @GET("textAlarmAck")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> textAlarmAck(@Query("userId") String userId, @Query("alarmSerialNumber") String alarmSerialNumber);


    //@@5.25添加appId字段，1或null为智能云消防，2为安全监控
    @GET("getNeedLossSmoke")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> getNeedLossSmoke(@Query("userId") String userId, @Query("privilege") String privilege,
                                           @Query("areaId") String areaId,@Query("page") String page,
                                           @Query("placeTypeId") String placeTypeId,@Query("appId") String appId);

    //@@5.25添加appId字段，1或null为智能云消防，2为安全监控
    @GET("getSmokeSummary")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<SmokeSummary> getSmokeSummary(@Query("userId") String userId, @Query("privilege") String privilege,
                                             @Query("areaId") String areaId,@Query("appId") String appId,
                                             @Query("placeTypeId") String placeTypeId,@Query("devType") String devType);

    //根据条件查询用户设备
    @GET("getNeedDev")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> getNeedDev(@Query("userId") String userId, @Query("privilege") String privilege,
                                     @Query("areaId") String areaId,@Query("page") String page,
                                     @Query("placeTypeId") String placeTypeId,@Query("devType") String devType);

    @GET("getAllElectricInfo")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<ElectricInfo<Electric>> getAllElectricInfo(@Query("userId") String userId, @Query("privilege") String privilege,
                                                          @Query("page") String page);
//    getOneElectricInfo?userId=13428282520&privilege=2&smokeMac=32110533
    @GET("getOneElectricInfo")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<ElectricInfo<ElectricValue>> getOneElectricInfo(@Query("userId") String userId, @Query("privilege") String privilege,
                                                               @Query("smokeMac") String smokeMac,@Query("devType") String devType);

//    getElectricTypeInfo?userId=13428282520&privilege=2&smokeMac=32110533&electricType=6&electricNum=1&page=
    @GET("getElectricTypeInfo")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<TemperatureTime> getElectricTypeInfo(@Query("userId") String userId, @Query("privilege") String privilege,
                                                @Query("smokeMac") String smokeMac, @Query("electricType") String electricType,
                                                @Query("electricNum") String electricNum, @Query("page") String page, @Query("devType") int devType);
//    getNeedElectricInfo?userId=13622215085&privilege=2&areaId=14&placeTypeId=2&page
    @GET("getNeedElectricInfo")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<ElectricInfo<Electric>> getNeedElectricInfo(@Query("userId") String userId, @Query("privilege") String privilege,
                                                    @Query("areaId") String areaId, @Query("placeTypeId") String placeTypeId,
                                                    @Query("page") String page);

    @FormUrlEncoded
    @POST("changeCameraPwd")
    Observable<HttpError> changeCameraPwd(@Field("cameraId") String cameraId, @Field("cameraPwd") String cameraPwd);

    //获取用户所有的摄像头
    @GET("getContentlist")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> getContentlist(@Query("firstDir") String firstDir, @Query("secondDir") String secondDir);

    //获取用户所有的摄像头
    @GET("getRuleContentlist")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> getRuleContentlist(@Query("firstDir") String firstDir);

    //获取用户所有的烟感
    @GET("getNeedGPSDev")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> getNeedGPSDev(@Query("userId") String userId, @Query("privilege") String privilege,
                                        @Query("areaId") String areaId,@Query("page") String page,
                                        @Query("placeTypeId") String placeTypeId);

    //获取用户所有的主机
    @GET("getRepeaterInfo")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> getRepeaterInfo(@Query("userId") String userId, @Query("privilege") String privilege,
                                        @Query("page") String page, @Query("search") String search);

    //获取电气设备切换设备
    @GET("getEleNeedHis")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> getEleNeedHis(@Query("smokeMac") String smokeMac,@Query("page") String page);

    @GET("getWaterHistoryInfo")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<TemperatureTime> getWaterHistoryInfo(@Query("userId") String userId, @Query("privilege") String privilege,
                                                    @Query("smokeMac") String smokeMac, @Query("page") String page);

    //获取燃气历史数据
    @GET("getGasHistoryInfo")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<ProofGasHistoryEntity> getGasHistoryInfo(@Query("userId") String userId, @Query("privilege") String privilege,
                                                        @Query("smokeMac") String smokeMac, @Query("page") String page);


    @GET("getTHDevInfoHistoryInfo")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<TemperatureTime> getTHDevInfoHistoryInfo(@Query("mac") String smokeMac, @Query("page") String page, @Query("type") String tepe);

    //获取电气设备定时任务
    @GET("getAllTimerTask")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> getAllTimerTask(@Query("mac") String smokeMac);


    //删除定时任务
    @GET("removeElectrTimer")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> removeElectrTimer(@Query("id") String id);
}
