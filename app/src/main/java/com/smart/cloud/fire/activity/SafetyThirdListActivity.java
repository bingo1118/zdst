package com.smart.cloud.fire.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.smart.cloud.fire.adapter.SafetyStudyAdapter;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Camera;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpError;
import com.smart.cloud.fire.retrofit.ApiStores;
import com.smart.cloud.fire.retrofit.AppClient;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class SafetyThirdListActivity extends Activity {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.safety_progressbar)
    ProgressBar safety_progressbar;
    @Bind(R.id.title_tv)
    TextView title_tv;
    Context mContext;
    List<String> list=null;
    SafetyStudyAdapter safetyStudyAdapter;
    private LinearLayoutManager linearLayoutManager;

    String firstDir;
    String secondDir;
    String secondDirName;

    int studyType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_third_list);

        ButterKnife.bind(this);
        mContext=this;
        firstDir=getIntent().getStringExtra("firstDir");
        secondDir=getIntent().getStringExtra("secondDir");
        secondDirName=getIntent().getStringExtra("secondDirName");
        studyType=getIntent().getIntExtra("StudyType",1);
        title_tv.setText(secondDirName);
        init();
    }

    private void init() {
        safety_progressbar.setVisibility(View.VISIBLE);
        ApiStores apiStores1 = AppClient.retrofit(ConstantValues.SERVER_IP_NEW).create(ApiStores.class);
        Observable mObservable=null;
        if(studyType==1){
            mObservable=apiStores1.getContentlist(firstDir,secondDir);
        }else{
            mObservable=apiStores1.getRuleContentlist(firstDir);
        }
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                int resule = model.getErrorCode();
                if(resule==0){
                    final List<String> list = model.getSafetyItems();
                    if(list!=null||list.size()!=0){
                                linearLayoutManager=new LinearLayoutManager(mContext);
                                linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                safetyStudyAdapter=new SafetyStudyAdapter(mContext,list);
                                safetyStudyAdapter.setOnItemClickListener(new SafetyStudyAdapter.OnRecyclerViewItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int data) {
                                        Intent intent=new Intent(mContext,TestSafetyActivity.class);
                                        intent.putExtra("firstDir",firstDir);
                                        intent.putExtra("secondDir",secondDir);
                                        intent.putExtra("ThirdDir",list.get(data));
                                        intent.putExtra("StudyType",studyType);
                                        startActivity(intent);
                                    }
                                });
                                recyclerView.setAdapter(safetyStudyAdapter);
                    }
                }else{
                    List<Camera> cameraList = new ArrayList<>();
                }
                safety_progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int code, String msg) {
                safety_progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onCompleted() {
                safety_progressbar.setVisibility(View.GONE);
            }
        }));

//        //http://127.0.0.1:8080/fireSystem/getContentlist?firstDir=电气培训&secondDir=宣传教育
//        String url= ConstantValues.SERVER_IP_NEW+"getContentlist?firstDir="+firstDir+"&secondDir="+secondDir;
//        RequestQueue mQueue = Volley.newRequestQueue(this);
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            int errorCode=response.getInt("errorCode");
//                            if(errorCode==0){
//                                String[] jsonArray= (String[]) response.get("safetyItems");
//                                list=new ArrayList<String>();
//                                for(int i=0;i<jsonArray.length;i++){
//                                    list.add(jsonArray[i]);
//                                }
//                                linearLayoutManager=new LinearLayoutManager(mContext);
//                                linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
//                                recyclerView.setLayoutManager(linearLayoutManager);
//                                safetyStudyAdapter=new SafetyStudyAdapter(mContext,list);
//                                safetyStudyAdapter.setOnItemClickListener(new SafetyStudyAdapter.OnRecyclerViewItemClickListener() {
//                                    @Override
//                                    public void onItemClick(View view, int data) {
//                                        Intent intent=new Intent(mContext,SafetySecondListActivity.class);
//                                        startActivity(intent);
//                                    }
//                                });
//                                recyclerView.setAdapter(safetyStudyAdapter);
//                            }else{
//                                Toast.makeText(mContext,response.getString("error"),Toast.LENGTH_SHORT).show();
//                            }
//                            safety_progressbar.setVisibility(View.GONE);
//                        } catch (JSONException e) {
//                            safety_progressbar.setVisibility(View.GONE);
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                safety_progressbar.setVisibility(View.GONE);
//                Toast.makeText(mContext,"获取服务器数据失败",Toast.LENGTH_SHORT).show();
//            }
//        });
//        mQueue.add(jsonObjectRequest);
    }
    private CompositeSubscription mCompositeSubscription;//管理subseription
    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }
}
